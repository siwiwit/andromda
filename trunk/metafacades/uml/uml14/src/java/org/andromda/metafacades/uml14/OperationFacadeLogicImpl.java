package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;
import org.omg.uml.foundation.datatypes.ScopeKindEnum;

/**
 * Metaclass facade implementation.
 */
public class OperationFacadeLogicImpl
    extends OperationFacadeLogic
    implements org.andromda.metafacades.uml.OperationFacade
{
    // ---------------- constructor -------------------------------

    public OperationFacadeLogicImpl(
        org.omg.uml.foundation.core.Operation metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getSignature()
     */
    public String handleGetSignature()
    {
        return this.getSignature(true);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getSignature(boolean)
     */
    public String handleGetSignature(boolean withArgumentNames)
    {
        return this.getSignature(withArgumentNames, null);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getTypedArgumentList()
     */
    public String handleGetTypedArgumentList()
    {
        return this.getTypedArgumentList(true);
    }

    private String getTypedArgumentList(boolean withArgumentNames)
    {
        return this.getTypedArgumentList(withArgumentNames, null);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#handleGetCall()
     */
    public String handleGetCall()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(metaObject.getName());
        sb.append("(");
        sb.append(getArgumentNames());
        sb.append(")");

        return sb.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacadeLogic#getArgumentNames()
     */
    public String handleGetArgumentNames()
    {
        StringBuffer sb = new StringBuffer();

        Iterator it = metaObject.getParameter().iterator();

        boolean commaNeeded = false;
        while (it.hasNext())
        {
            Parameter p = (Parameter)it.next();

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

    /**
     * @see org.andromda.metafacades.uml.OperationFacadeLogic#getArgumentTypeNames()
     */
    public String handleGetArgumentTypeNames()
    {
        StringBuffer sb = new StringBuffer();

        Iterator it = metaObject.getParameter().iterator();

        boolean commaNeeded = false;
        while (it.hasNext())
        {
            Parameter p = (Parameter)it.next();

            if (!ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind()))
            {
                if (commaNeeded)
                {
                    sb.append(", ");
                }
                ParameterFacade facade = (ParameterFacade)shieldedElement(p);
                sb.append(facade.getType().getFullyQualifiedName());
                commaNeeded = true;
            }
        }
        return sb.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getReturnType()
     */
    protected Object handleGetReturnType()
    {
        Object type = null;
        Collection parms = metaObject.getParameter();
        for (Iterator i = parms.iterator(); i.hasNext();)
        {
            Parameter p = (Parameter)i.next();
            if (ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind()))
            {
                type = p.getType();
                break;
            }
        }

        return type;
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacadeLogic#getArguments()
     */
    public Collection handleGetArguments()
    {
        Collection arguments = new ArrayList(metaObject.getParameter());

        CollectionUtils.filter(arguments, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                return !ParameterDirectionKindEnum.PDK_RETURN
                    .equals(((Parameter)object).getKind());
            }
        });
        return this.shieldedElements(arguments);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacadeLogic#getOwner()
     */
    public Object handleGetOwner()
    {
        return this.metaObject.getOwner();
    }

    /**
     * 
     * @see org.andromda.metafacades.uml.OperationFacadeLogic#getParameters()
     */
    protected Collection handleGetParameters()
    {
        return metaObject.getParameter();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacadeLogic#findTaggedValue(java.lang.String, boolean)
     */
    public Object handleFindTaggedValue(String name, boolean follow)
    {
        name = StringUtils.trimToEmpty(name);
        Object value = findTaggedValue(name);
        if (follow)
        {
            ClassifierFacade type = this.getReturnType();
            while (value == null && type != null)
            {
                value = type.findTaggedValue(name);
                type = (ClassifierFacade)type.getGeneralization();
            }
        }
        return value;
    }

    /**
     * @see org.andromda.metafacades.uml14.OperationFacade#isStatic()
     */
    public boolean handleIsStatic()
    {
        return ScopeKindEnum.SK_CLASSIFIER.equals(this.metaObject
            .getOwnerScope());
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#isAbstract()
     */
    public boolean handleIsAbstract()
    {
        return metaObject.isAbstract();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#hasExceptions()
     */
    public boolean handleIsExceptionsPresent()
    {
        return !this.getExceptions().isEmpty();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getExceptions()
     */
    public Collection handleGetExceptions()
    {
        Collection exceptions = new HashSet();

        // finds both exceptions and exception references
        final class ExceptionFilter
            implements Predicate
        {
            public boolean evaluate(Object object)
            {
                DependencyFacade dependency = (DependencyFacade)object;
                // first check for exception references
                boolean hasException = dependency
                    .hasStereotype(UMLProfile.STEREOTYPE_EXCEPTION_REF);

                // if there wasn't any exception reference
                // now check for actual exceptions
                if (!hasException)
                {
                    ModelElementFacade targetElement = dependency
                        .getTargetElement();
                    hasException = targetElement != null
                        && targetElement
                            .hasStereotype(UMLProfile.STEREOTYPE_EXCEPTION);
                }
                return hasException;
            }
        }

        // first get any dependencies on this operation's
        // owner (because these will represent the default exception(s))
        Collection ownerDependencies = this.getOwner().getTargetDependencies();
        if (ownerDependencies != null && !ownerDependencies.isEmpty())
        {
            CollectionUtils.filter(ownerDependencies, new ExceptionFilter());
            exceptions.addAll(ownerDependencies);
        }

        Collection operationDependencies = this.getTargetDependencies();
        // now get any exceptions directly on the operation
        if (operationDependencies != null && !operationDependencies.isEmpty())
        {
            CollectionUtils
                .filter(operationDependencies, new ExceptionFilter());
            exceptions.addAll(operationDependencies);
        }

        // now transform the dependency(s) to the actual exception(s)
        CollectionUtils.transform(exceptions, new Transformer()
        {
            public Object transform(Object object)
            {
                return ((DependencyFacade)object).getTargetElement();
            }
        });
        return exceptions;
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getExceptionList()
     */
    public String handleGetExceptionList()
    {
        return this.getExceptionList(null);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#hasReturnType()
     */
    public boolean handleIsReturnTypePresent()
    {
        boolean hasReturnType = true;
        if (this.getReturnType() != null)
        {
            hasReturnType = !StringUtils.trimToEmpty(
                this.getReturnType().getFullyQualifiedName()).equals("void");
        }
        return hasReturnType;
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getExceptionList(java.lang.String)
     */
    public String handleGetExceptionList(String initialExceptions)
    {
        initialExceptions = StringUtils.trimToEmpty(initialExceptions);
        StringBuffer exceptionList = new StringBuffer(initialExceptions);
        Collection exceptions = this.getExceptions();
        if (exceptions != null && !exceptions.isEmpty())
        {
            if (StringUtils.isNotEmpty(initialExceptions))
            {
                exceptionList.append(", ");
            }
            Iterator exceptionIt = exceptions.iterator();
            while (exceptionIt.hasNext())
            {
                ModelElementFacade exception = (ModelElementFacade)exceptionIt
                    .next();
                exceptionList.append(exception.getFullyQualifiedName());
                if (exceptionIt.hasNext())
                {
                    exceptionList.append(", ");
                }
            }
        }

        return exceptionList.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getTypedArgumentList(java.lang.String)
     */
    public String handleGetTypedArgumentList(String modifier)
    {
        return this.getTypedArgumentList(true, modifier);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getSignature(java.lang.String)
     */
    public String handleGetSignature(String argumentModifier)
    {
        return this.getSignature(true, argumentModifier);
    }
    
    private String getSignature(boolean withArgumentNames, String argumentModifier)
    {
        StringBuffer signature = new StringBuffer(this.getName());
        signature.append("(");
        signature.append(this.getTypedArgumentList(withArgumentNames, argumentModifier));
        signature.append(")");
        return signature.toString();
    }
    
    private String getTypedArgumentList(boolean withArgumentNames, String modifier)
    {
        StringBuffer sb = new StringBuffer();
        Iterator it = metaObject.getParameter().iterator();

        boolean commaNeeded = false;
        while (it.hasNext())
        {
            Parameter p = (Parameter)it.next();

            if (!ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind()))
            {
                String type = null;
                if (p.getType() == null)
                {
                    this.logger
                        .error("ERROR! No type specified for parameter --> '"
                            + p.getName() + "' on operation --> '"
                            + this.getName() + "', please check your model");
                }
                else
                {
                    type = ((ClassifierFacade)this.shieldedElement(p.getType()))
                        .getFullyQualifiedName();
                }

                if (commaNeeded)
                {
                    sb.append(", ");
                }
                if (StringUtils.isNotBlank(modifier))
                {
                    sb.append(modifier);
                    sb.append(" ");
                }
                sb.append(type);
                if (withArgumentNames)
                {
                    sb.append(" ");
                    sb.append(p.getName());
                }
                commaNeeded = true;
            }
        }
        return sb.toString();
    }
}