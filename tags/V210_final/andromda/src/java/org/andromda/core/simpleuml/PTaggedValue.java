package org.andromda.core.simpleuml;

import java.util.Iterator;

import org.andromda.core.uml14.UMLStaticHelper;
import org.omg.uml.foundation.core.TaggedValue;

/**
 * dynamic proxy for a ModelElement: dynamically supports the UMLTaggedValue, 
 * and org.omg.uml.foundation.core.TaggedValue interfaces.
 * 
 * @author <A HREF="http://www.amowers.com">Anthony Mowers</A>
 */
public class PTaggedValue
	extends PModelElement
	implements UMLTaggedValue
{
	
	public static Object newInstance(
		UMLStaticHelper scriptHelper,
		TaggedValue taggedValue)
	{
		Class[] interfaces = {
			UMLTaggedValue.class,
			TaggedValue.class
		};
		
		return java.lang.reflect.Proxy.newProxyInstance(
			taggedValue.getClass().getClassLoader(),
			interfaces,
			new PTaggedValue(taggedValue, scriptHelper));
	}


	
	protected PTaggedValue(
		TaggedValue taggedValue,
		UMLStaticHelper scriptHelper)
	{
		super(taggedValue,scriptHelper);
	}

	public String getTag()
	{
		return scriptHelper.getName(modelElement);
	}
	
	public String getValue()
	{
		TaggedValue tv = (TaggedValue)modelElement;
		
		
		StringBuffer sb = new StringBuffer();
		for (Iterator i = tv.getDataValue().iterator(); i.hasNext(); )
		{
			Object v = i.next();
			sb.append(v.toString());
			
			if (i.hasNext())
			{
				sb.append(" ");
			}
		}
		
		return sb.toString();
	}
}
