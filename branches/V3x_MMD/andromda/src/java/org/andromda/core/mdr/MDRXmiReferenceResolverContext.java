package org.andromda.core.mdr;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import javax.jmi.reflect.RefPackage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.netbeans.api.xmi.XMIInputConfig;
import org.netbeans.lib.jmi.xmi.XmiContext;

/**
 * This class supports the expansion of XML HREF references to other modules within
 * a model.  The result of the resolver should be a valid URL.  This is 
 * necessary for Magic Draw as it doesn't have the entire model referenced
 * but just the archived model.
 * 
 * @author Matthias Bohlen
 * @author Chad Brandon  
 */
public class MDRXmiReferenceResolverContext extends XmiContext {
	
	private static Log logger = LogFactory.getLog(MDRXmiReferenceResolverContext.class);
	
	private HashMap urlMap = new HashMap();

	/**
	 * Constructs an instance of this class.
	 * 
	 * @param extents
	 * @param config
	 */
	public MDRXmiReferenceResolverContext(
		RefPackage[] extents,
		XMIInputConfig config) {
		super(extents, config);
	}

    /**
     * @see org.netbeans.lib.jmi.xmi.XmiContext#toURL(java.lang.String)
     */
	public URL toURL(String systemId) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("attempting to resolve Xmi Href --> '" + systemId + "'");
		}
		// Several tries to construct a URL that really exists.

		// If this is a valid URL, simply return it.
		try {
			URL url = new URL(systemId);
			InputStream stream = url.openStream();
			urlMap.put(getSuffix(systemId), url);
			return url;
		} catch (Exception e) {
			// do nothing, try once more with URL modification!
		}

		// Find URL in map. If found, return it.
		String suffix = getSuffix(systemId);
		URL mappedUrl = (URL) urlMap.get(suffix);
		if (mappedUrl != null) {
			return mappedUrl;
		}

		// If ZIP inside ZIP, try to cut one ZIP.
		int mid = systemId.indexOf(".xml.zip!/");
		if (mid > 0 && systemId.endsWith(".zip")) {
			String prefix = systemId.substring(0, mid - 1);
			int lastSlash = prefix.lastIndexOf("/");
			prefix = prefix.substring(0, lastSlash + 1);
			String suffixZipped = systemId.substring(mid + 10);
			String suffixWithoutZip =
				suffixZipped.substring(0, suffixZipped.length() - 4);
			String completeURL =
				prefix + suffixZipped + "!/" + suffixWithoutZip;
			return super.toURL(completeURL);
		}

		// If still ends with .zip, find it in map without the '.zip'.
		if (systemId.endsWith(".zip")) {
			String urlWithoutZip = systemId.substring(0, systemId.length() - 4);
			mappedUrl = (URL) urlMap.get(urlWithoutZip);
			if (mappedUrl != null) {
				return mappedUrl;
			}
		}
		
		// Give up and let superclass deal with it.
		return super.toURL(systemId);
	}

	/**
	 *  Gets the suffix of the <code>systemId</code>
	 * @param systemId the system identifier.
	 * @return the suffix as a String.
	 */
	private static String getSuffix(String systemId) {
		int lastSlash = systemId.lastIndexOf("/");
		if (lastSlash > 0) {
			String suffix = systemId.substring(lastSlash + 1);
			return suffix;
		}
		return systemId;
	}

}
