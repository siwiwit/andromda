package org.andromda.core.metadecorators.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;
import org.omg.uml.foundation.datatypes.ScopeKindEnum;

/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Operation
 *
 *
 */
public class OperationDecoratorImpl extends OperationDecorator
{
    // ---------------- constructor -------------------------------

    public OperationDecoratorImpl(
        org.omg.uml.foundation.core.Operation metaObject)
    {
        super(metaObject);
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.OperationDecorator#getSignature()
     */
    public String getSignature()
    {
        Iterator it = metaObject.getParameter().iterator();
        if (!it.hasNext())
        {
            return metaObject.getName() + "()";
        }

        StringBuffer sb = new StringBuffer();
        sb.append(metaObject.getName());
        sb.append("(");
        sb.append(getTypedParameterList());
        sb.append(")");

        return sb.toString();
    }

    public String getTypedParameterList()
    {
        StringBuffer sb = new StringBuffer();
        Iterator it = metaObject.getParameter().iterator();

        boolean commaNeeded = false;
        while (it.hasNext())
        {
            Parameter p = (Parameter) it.next();

            if (!ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind()))
            {
                String type = null;
                if (p.getType() == null)
                {
                    this.logger.error("ERROR! No type specified for parameter --> '" 
                    	+ p.getName() + "' on operation --> '" 
						+ this.getName() + "', please chek your model");
                }
                else
                {
                    type =
                        ((ClassifierDecorator) this.decoratedElement(p.getType()))
                            .getFullyQualifiedName();
                }

                if (commaNeeded)
                {
                    sb.append(", ");
                }
                sb.append(type);
                sb.append(" ");
                sb.append(p.getName());
                commaNeeded = true;
            }
        }
        return sb.toString();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.OperationDecorator#getCall()
     */
    public String getCall()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(metaObject.getName());
        sb.append("(");
        sb.append(getParameterNames());
        sb.append(")");

        return sb.toString();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.OperationDecorator#getParameterNames()
     */
    public String getParameterNames()
    {
        StringBuffer sb = new StringBuffer();

        Iterator it = metaObject.getParameter().iterator();

        boolean commaNeeded = false;
        while (it.hasNext())
        {
            Parameter p = (Parameter) it.next();

            if (!ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind()))
            {
                if (commaNeeded)
                {
                    sb.append(", ");
                }
                sb.append(p.getName());
                commaNeeded = true;
            }
        }
        return sb.toString();
    }
    
    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.OperationDecorator#getParameterTypeNames()
     */
    public String getParameterTypeNames()
	{
    	StringBuffer sb = new StringBuffer();
    	
    	Iterator it = this.getParameters().iterator();
    	
    	boolean commaNeeded = false;
    	while (it.hasNext())
    	{
    		ParameterDecorator p = (ParameterDecorator) it.next();
    		
    		if (!ParameterDirectionKindEnum.PDK_RETURN.equals(((Parameter)p.getMetaObject()).getKind()))
    		{
    			if (commaNeeded)
    			{
    				sb.append(", ");
    			}
    			sb.append(((ClassifierDecorator)p.getType()).getFullyQualifiedName());
    			commaNeeded = true;
    		}
    	}
    	return sb.toString();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.OperationDecorator#handleGetType()
     */
    protected ModelElement handleGetType()
    {
        Collection parms = metaObject.getParameter();
        for (Iterator i = parms.iterator(); i.hasNext();)
        {
            Parameter p = (Parameter) i.next();
            if (ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind()))
            {
                return p.getType();
            }
        }

        return null;
    }
   
    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.OperationDecorator#handleGetOwner()
     */
    public ModelElement handleGetOwner() 
	{
    	return this.metaObject.getOwner();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.OperationDecorator#handleGetParameters()
     */
    protected Collection handleGetParameters()
    {
        ArrayList ret = new ArrayList();

        for (Iterator i = metaObject.getParameter().iterator();
            i.hasNext();
            )
        {
            Parameter p = (Parameter) i.next();
            if (!ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind()))
            {
                ret.add(p);
            }
        }

        return ret;
    }
    
    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.OperationDecorator#findTaggedValue(java.lang.String, boolean)
     */
    public String findTaggedValue(String name, boolean follow) 
	{
    	name = StringUtils.trimToEmpty(name);
    	String value = findTaggedValue(name);
    	if (follow) {
	    	ClassifierDecorator type = this.getType();
	    	while (value == null && type != null) {
	    		value = type.findTaggedValue(name);
	    		type = type.getSuperclass();
	    	}
    	}
    	return value;
    }
    
    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AttributeDecorator#isStatic()
     */
    public boolean isStatic() 
	{
    	return ScopeKindEnum.SK_CLASSIFIER.equals(this.metaObject.getOwnerScope());
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.OperationDecorator#isAbstract()
     */
    public boolean isAbstract()
    {
        return metaObject.isAbstract();
    }

    // ------------- relations ------------------

}
