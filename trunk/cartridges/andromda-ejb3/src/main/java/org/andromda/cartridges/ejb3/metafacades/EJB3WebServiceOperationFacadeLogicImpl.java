package org.andromda.cartridges.ejb3.metafacades;

import java.util.Iterator;

import org.andromda.cartridges.ejb3.EJB3Profile;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3WebServiceOperationFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3WebServiceOperationFacade
 */
public class EJB3WebServiceOperationFacadeLogicImpl
    extends EJB3WebServiceOperationFacadeLogic
{

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(EJB3WebServiceOperationFacadeLogicImpl.class);

    public EJB3WebServiceOperationFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3WebServiceOperationFacade#isExposed()
     */
    protected boolean handleIsExposed()
    {
        return this.getOwner().hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE) ||
            this.hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE_OPERATION);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3WebServiceOperationFacadeLogic#handleIsOneway()
     */
    protected boolean handleIsOneway()
    {
        return BooleanUtils.toBoolean(
                (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_WEBSERVICE_OPERATION_ONEWAY));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3WebServiceOperationFacadeLogic#getAnnotatedSignature()
     */
    protected String handleGetAnnotatedSignature()
    {
        final StringBuffer signature = new StringBuffer(this.getName());
        signature.append("(");
        signature.append(this.getAnnotatedTypedArgumentList(true, null));
        signature.append(")");
        return signature.toString();
    }

    /**
     * @param withArgumentNames
     * @param modifier
     * @return
     */
    private String getAnnotatedTypedArgumentList(boolean withArgumentNames, String modifier)
    {
        final StringBuilder buffer = new StringBuilder();
        final Iterator parameterIterator = this.getArguments().iterator();

        boolean commaNeeded = false;
        while (parameterIterator.hasNext())
        {
            ParameterFacade paramter = (ParameterFacade)parameterIterator.next();
            String type = null;
            if (paramter.getType() == null)
            {
                this.logger.error(
                        "ERROR! No type specified for parameter --> '" + paramter.getName() +
                        "' on operation --> '" +
                        this.getName() +
                        "', please check your model");
            }
            else
            {
                type = paramter.getType().getFullyQualifiedName();
            }

            if (commaNeeded)
            {
                buffer.append(",");
            }
            buffer.append('\n');

            // Add WebParam annotation
            if (withArgumentNames)
            {
                buffer.append("        @javax.jws.WebParam(name = \"");
                buffer.append(StringUtils.capitalize(paramter.getName())).append("\")");
                buffer.append(" ");
            }
            if (StringUtils.isNotBlank(modifier))
            {
                buffer.append(modifier);
                buffer.append(" ");
            }
            buffer.append(type);
            if (withArgumentNames)
            {
                buffer.append(" ");
                buffer.append(paramter.getName());
            }
            commaNeeded = true;
        }
        buffer.append('\n');
        if (commaNeeded)
        {
            buffer.append("    ");
        }
        return buffer.toString();
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3WebServiceOperationFacadeLogic#handleGetMethodName()
     */
    protected String handleGetMethodName()
    {
        String methodName = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_WEBSERVICE_OPERATION_NAME);
        if (StringUtils.isBlank(methodName))
        {
            methodName = StringUtils.capitalize(this.getName());
        }
        return methodName;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3WebServiceOperationFacadeLogic#handleGetResultName()
     */
    protected String handleGetResultName()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_WEBSERVICE_OPERATION_RESULT_NAME);
    }

}