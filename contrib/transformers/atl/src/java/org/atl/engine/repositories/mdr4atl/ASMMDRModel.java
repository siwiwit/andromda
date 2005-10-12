package org.atl.engine.repositories.mdr4atl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.jmi.reflect.InvalidCallException;
import javax.jmi.reflect.RefAssociation;
import javax.jmi.reflect.RefClass;
import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.XmiReader;
import javax.jmi.xmi.XmiWriter;

import org.andromda.repositories.mdr.MDRXmiReferenceResolver;
import org.atl.engine.vm.ModelLoader;
import org.atl.engine.vm.nativelib.ASMCollection;
import org.atl.engine.vm.nativelib.ASMModel;
import org.atl.engine.vm.nativelib.ASMModelElement;
import org.atl.engine.vm.nativelib.ASMString;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIReaderFactory;
import org.openide.util.Lookup;

/**
 * This class must be used instead of the default one provided by ATL because it
 * allows the module search path to be specified (so we can use modules).
 * Another issue is the fact that ATL loads models through the input stream to
 * MDR and therefore MDR can not reference modules because it doesn't have a
 * relative point from which to compare the path, this class takes a uri path
 * instead of the input stream.
 * 
 * This class must be on the classpath before the default one provided by ATL.
 * 
 * @author Fr�d�ric Jouault
 * @author Chad Brandon
 */
public class ASMMDRModel extends ASMModel {
	private static int verboseLevel = 1;

	private static boolean persist = false;

	private static MDRepository rep = null;

	private static XmiReader reader;

	private static XmiWriter writer;

	static {
		initMDR();
	}

	private ASMMDRModel(String name, RefPackage pack, ASMModel metamodel,
			boolean isTarget, ModelLoader ml) {
		super(name, metamodel, isTarget, ml);
		this.pack = pack;
	}

	private Map modelElements = new HashMap();

	// only for metamodels...
	public ASMModelElement findModelElement(String name) {
		ASMModelElement ret = (ASMModelElement) modelElements.get(name);

		if (ret == null) {
			RefObject retro = null;
			RefClass cl = pack.refClass("Classifier");
			for (Iterator i = cl.refAllOfType().iterator(); i.hasNext();) {
				RefObject ro = (RefObject) i.next();
				try {
					if (ro.refGetValue("name").equals(name)) {
						retro = ro;
						break;
					}
				} catch (Exception e) {
					retro = null;
				}
			}

			if (retro != null) {
				ret = ASMMDRModelElement.getASMModelElement(this, retro);
				modelElements.put(name, ret);
			}
		}

		return ret;
	}

	public Set getElementsByType(ASMModelElement ame) {
		Set ret = new HashSet();
		RefObject o = ((ASMMDRModelElement) ame).getObject();

		for (Iterator i = findRefClass(pack, o).refAllOfType().iterator(); i
				.hasNext();) {
			ret.add(ASMMDRModelElement.getASMModelElement(this, (RefObject) i
					.next()));
		}

		return ret;
	}

	public ASMModelElement newModelElement(ASMModelElement type) {
		ASMModelElement ret = null;
		RefObject refObject = ((ASMMDRModelElement) type).getObject();
		RefClass refClass = findRefClass(pack, refObject);
        if (refClass == null)
        {
            throw new ASMMDRModelException("The type '" + type 
                + "' could not be found, if this type is from a metamodel referenced from an external HREF module, "
                + "it's possible you'll need to reference the external module's model as clustered");
        }
		ret = ASMMDRModelElement.getASMModelElement(this, refClass
				.refCreateInstance(null));

		return ret;
	}

	private RefClass findRefClass(RefPackage pack, RefObject object) {
		RefClass ret = null;

		try {
			ret = pack.refClass(object);
		} catch (InvalidCallException ice) {
		}

		if (ret == null) {
			for (Iterator i = pack.refAllPackages().iterator(); i.hasNext() && (ret == null);) {
                final Object modelPackage = i.next();
                System.out.println("searching package: " + modelPackage);
				ret = findRefClass((RefPackage)modelPackage, object);
			}
		}

		return ret;
	}

	protected RefAssociation findRefAssociation(RefObject object) {
		return findRefAssociation(pack, object);
	}

	private RefAssociation findRefAssociation(RefPackage pack, RefObject object) {
		RefAssociation ret = null;

		try {
			ret = pack.refAssociation(object);
		} catch (InvalidCallException ice) {
		}

		if (ret == null) {
			for (Iterator i = pack.refAllPackages().iterator(); i.hasNext()
					&& (ret == null);) {
				ret = findRefAssociation((RefPackage) i.next(), object);
			}
		}

		return ret;
	}

	private void getAllAcquaintances() {
		boolean debug = false;
		if (getMetamodel().equals(getMOF())) {
			ASMMDRModelElement assoType = ((ASMMDRModelElement) getMOF()
					.findModelElement("Association"));
			for (Iterator i = getElementsByType(assoType).iterator(); i
					.hasNext();) {
				ASMMDRModelElement asso = (ASMMDRModelElement) i.next();
				if (debug) {
					System.out.println(asso);
				}

				ASMMDRModelElement type1 = null;
				String name1 = null;
				ASMModelElement ae1 = null;

				ASMMDRModelElement type2 = null;
				String name2 = null;
				ASMModelElement ae2 = null;
				for (Iterator j = ((ASMCollection) asso.get(null, "contents"))
						.iterator(); j.hasNext();) {
					ASMModelElement ae = (ASMModelElement) j.next();
					if (ae.getMetaobject().get(null, "name").equals(
							new ASMString("AssociationEnd"))) {
						ASMMDRModelElement type = (ASMMDRModelElement) ae.get(
								null, "type");
						if (type1 == null) {
							type1 = type;
							name1 = ((ASMString) ae.get(null, "name"))
									.getSymbol();
							ae1 = ae;
						} else {
							type2 = type;
							name2 = ((ASMString) ae.get(null, "name"))
									.getSymbol();
							ae2 = ae;
						}
					}
				}

				// if(!((Boolean)ae1.refGetValue("isNavigable")).booleanValue())
				// {
				if (debug) {
					System.out.println("\tAdding acquaintance \"" + name1
							+ "\" to " + type2);
				}
				type2.addAcquaintance(name1, asso, ae1, true);

				// }
				// if(!((Boolean)ae2.refGetValue("isNavigable")).booleanValue())
				// {
				if (debug) {
					System.out.println("\tAdding acquaintance \"" + name2
							+ "\" to " + type1);
				}
				type1.addAcquaintance(name2, asso, ae2, false);

				// }
			}
		}
	}

	public static ASMMDRModel newASMMDRModel(String name,
			ASMMDRModel metamodel, ModelLoader ml) throws Exception {
		RefPackage mextent = null;
		String modifiedName = name;
		int id = 0;

		while (rep.getExtent(modifiedName) != null) {
			modifiedName = name + "_" + id++;
		}

		if (metamodel.getName().equals("MOF")) {
			mextent = rep.createExtent(modifiedName);
		} else {
			RefPackage mmextent = metamodel.pack;
			RefObject pack = null;
			for (Iterator it = mmextent.refClass("Package").refAllOfClass()
					.iterator(); it.hasNext();) {
				pack = (RefObject) it.next();
				if (pack.refGetValue("name").equals(metamodel.getName())) {
					break;
				}
			} // mp now contains a package with the same name as the extent

			// or the last package
			mextent = rep.createExtent(modifiedName, pack);
		}

		return new ASMMDRModel(name, mextent, metamodel, true, ml);
	}

	public static ASMMDRModel loadASMMDRModel(String name,
			ASMMDRModel metamodel, String url, ModelLoader ml) throws Exception {
		return loadASMMDRModel(name, metamodel, new File(url).toURL(), ml, null);
	}

	public static ASMMDRModel loadASMMDRModel(String name,
			ASMMDRModel metamodel, URL url, ModelLoader ml,
			String[] moduleSearchPaths) throws Exception {
		return loadASMMDRModel(name, metamodel, url.toString(), ml,
				moduleSearchPaths);
	}

	public static ASMMDRModel loadASMMDRModel(String name,
			ASMMDRModel metamodel, String uri, ModelLoader ml,
			String[] moduleSearchPaths) throws Exception {
		ASMMDRModel ret = newASMMDRModel(name, metamodel, ml);

		try {
			final RefPackage model = ret.pack;
			reader = XMIReaderFactory.getDefault().createXMIReader(
					new MDRXmiReferenceResolver(new RefPackage[] { model },
							moduleSearchPaths));
			reader.read(uri, model);
		} catch (Exception e) {
			System.out.println("Error while reading " + name + ":");

			// e.printStackTrace(System.out);
		}
		ret.setIsTarget(false);
		ret.getAllAcquaintances();

		return ret;
	}

	public static ASMMDRModel createMOF(ModelLoader ml) {
		ASMMDRModel ret = null;

		try {
			ret = new ASMMDRModel("MOF", rep.getExtent("MOF"), null, false, ml);
			mofmm = ret;
		} catch (org.netbeans.mdr.util.DebugException de) {
			de.printStackTrace(System.out);
		}

		return ret;
	}

	public void save(String url) throws IOException {
		OutputStream out = new FileOutputStream(url);
		save(out);
	}

	public void save(OutputStream out) throws IOException {
		writer.write(out, pack, null);
	}

	public void save(String url, String xmiVersion) throws IOException {
		OutputStream out = new FileOutputStream(url);
		save(out, xmiVersion);
	}

	public void save(OutputStream out, String xmiVersion) throws IOException {
		writer.write(out, pack, xmiVersion);
	}

	private static void initMDR() {
		if (rep != null) {
			return;
		}

		if (verboseLevel < 1) {
			System.setProperty("org.netbeans.lib.jmi.Logger.fileName", "");
		}
		if (!persist) {
			System
					.setProperty(
							"org.netbeans.mdr.storagemodel.StorageFactoryClassName",
							"org.netbeans.mdr.persistence.memoryimpl.StorageFactoryImpl");
		}
		System.setProperty("org.openide.util.Lookup",
				"org.openide.util.lookup.ATLLookup");

		//
		// otherwise MDR does not find ATLLookup
		Thread.currentThread().setContextClassLoader(
				org.openide.util.lookup.ATLLookup.class.getClassLoader());

		rep = MDRManager.getDefault().getDefaultRepository();

		writer = (XmiWriter) Lookup.getDefault().lookup(XmiWriter.class);
		rep.getExtent("MOF");
	}

	public RefPackage getPackage() {
		return pack;
	}

	public static ASMModel getMOF() {
		return mofmm;
	}

	private RefPackage pack;

	private static ASMMDRModel mofmm;
}