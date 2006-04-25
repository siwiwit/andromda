package org.andromda.metafacades.emf.uml2;

import java.util.Collection;

import org.andromda.core.metafacade.MetafacadeException;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.Enumeration;
import org.eclipse.uml2.NamedElement;
import org.eclipse.uml2.Type;
import org.eclipse.uml2.UML2Factory;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.EnumerationFacade.
 *
 * @see org.andromda.metafacades.uml.EnumerationFacade
 */
public class EnumerationFacadeLogicImpl
    extends EnumerationFacadeLogic
{
    public EnumerationFacadeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * Overridden to provide name masking.
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    protected String handleGetName()
    {
        final String nameMask =
            String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ENUMERATION_NAME_MASK));
        return NameMasker.mask(
            super.handleGetName(),
            nameMask);
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getLiterals()
     */
    protected java.util.Collection handleGetLiterals()
    {
        if (metaObject instanceof Enumeration)
        {
            return this.shieldedElements(((Enumeration)metaObject).getOwnedLiterals());
        }
        return this.getAttributes();
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getFromOperationSignature()
     */
    protected String handleGetFromOperationSignature()
    {
        final StringBuffer signature = new StringBuffer(this.getFromOperationName());
        final ClassifierFacade type = this.getLiteralType();
        if (type != null)
        {
            signature.append('(');
            signature.append(type.getFullyQualifiedName());
            signature.append(" value)");
        }
        return signature.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getFromOperationName()
     */
    protected String handleGetFromOperationName()
    {
        final StringBuffer name = new StringBuffer("from");
        final ClassifierFacade type = this.getLiteralType();
        if (type != null)
        {
            name.append(StringUtils.capitalize(type.getName()));
        }
        return name.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getLiteralType()
     */
    protected Object handleGetLiteralType()
    {
        Object type = null;
        final Collection literals = this.getLiterals();
        if (literals != null && !literals.isEmpty())
        {
            ModelElementFacade literal = (ModelElementFacade)literals.iterator().next();
            if (literal instanceof AttributeFacade)
            {
                type = ((AttributeFacade)literals.iterator().next()).getType();
            }
            else
            {
                NamedElement enumeration = (NamedElement)metaObject;

                //fake a primitive type called string to return. This should work...
                //if not we will need to pass in a type qName as a parameter and search for it
                Type syntheticType = UML2Factory.eINSTANCE.createPrimitiveType();
                syntheticType.setName("string");

                //for example:
//                final String syntheticTypeName = "MODEL-TYPES::string";
//                final Object syntheticType =
//                    UmlUtilities.findByName(
//                        enumeration.eResource().getResourceSet(),
//                        syntheticTypeName);
                if (syntheticType instanceof Type)
                {
                    type = syntheticType;
                    if (((Type)type).eIsProxy())
                    {
                        EcoreUtil.resolve(
                            (Type)type,
                            enumeration.eResource().getResourceSet());
                    }
                }
                else
                {
                    throw new MetafacadeException("Note that real Literals are not supported yet.");
                }
            }
        }
        return type;
    }
}