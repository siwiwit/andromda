package org.andromda.cartridges.ejb.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.andromda.cartridges.ejb.EJBProfile;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.OperationFacade;
import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * Represents an entity EJB.
 * </p>
 * Metaclass facade implementation.
 */
public class EJBEntityFacadeLogicImpl
    extends EJBEntityFacadeLogic
    implements org.andromda.cartridges.ejb.metafacades.EJBEntityFacade
{
    // ---------------- constructor -------------------------------

    public EJBEntityFacadeLogicImpl(
        java.lang.Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    public Collection handleGetIdentifiers()
    {

        Collection identifiers = new ArrayList();
        Iterator iter = this.getDependencies().iterator();
        while (iter.hasNext())
        {
            DependencyFacade dep = (DependencyFacade)iter.next();
            if (dep.hasStereotype(EJBProfile.STEREOTYPE_IDENTIFIER))
            {
                identifiers = ((ClassifierFacade)dep.getTargetElement())
                    .getInstanceAttributes();
                MetafacadeUtils.filterByStereotype(
                    identifiers,
                    EJBProfile.STEREOTYPE_IDENTIFIER);
                return identifiers;
            }
        }

        // No PK dependency found - try a PK attribute
        if (super.getIdentifiers() != null && !super.getIdentifiers().isEmpty())
        {
            AttributeFacade attr = (AttributeFacade)super.getIdentifiers()
                .iterator().next();
            identifiers.add(attr);
            return identifiers;
        }

        // Still nothing found - recurse up the inheritance tree
        EJBEntityFacade decorator = (EJBEntityFacade)this.getGeneralization();
        return decorator.getIdentifiers();
    }

    public java.util.Collection handleGetAllEntityRelations()
    {

        // Only concrete entities may have EJB relations. Return
        // an empty collection for everything else
        if (this.isAbstract())
        {
            return Collections.EMPTY_LIST;
        }

        Collection result = new ArrayList();
        result.addAll(getEntityRelations());

        ClassifierFacade classifier = (ClassifierFacade)this
            .getGeneralization();
        while (classifier != null && classifier instanceof EJBEntityFacade
            && classifier.isAbstract())
        {

            EJBEntityFacade entity = (EJBEntityFacade)classifier;
            result.add(entity.getEntityRelations());
            classifier = (ClassifierFacade)this.getGeneralization();
        }
        return result;
    }

    public String handleGetViewType()
    {
        return EJBMetafacadeUtils.getViewType(this);
    }

    public java.util.Collection handleGetEntityRelations()
    {

        Collection result = new ArrayList();

        Iterator i = this.getAssociationEnds().iterator();
        while (i.hasNext())
        {
            EJBAssociationEndFacade assoc = (EJBAssociationEndFacade)i.next();
            ClassifierFacade target = assoc.getOtherEnd().getType();
            if (target instanceof EJBEntityFacade
                && assoc.getOtherEnd().isNavigable())
            {
                // Check the integrity constraint
                Object value = assoc.getOtherEnd().getAssociation()
                    .findTaggedValue(EJBProfile.TAGGEDVALUE_GENERATE_CMR);
                String generateCmr = value == null ? null : value.toString();
                if (target.isAbstract()
                    && !"false".equalsIgnoreCase(generateCmr))
                {
                    throw new IllegalStateException("Relation '"
                        + assoc.getAssociation().getName()
                        + "' has the abstract target '" + target.getName()
                        + "'. Abstract targets are not allowed in EJB.");
                }

                result.add(assoc);
            }
        }
        return result;
    }

    public List handleGetAllInstanceAttributes()
    {
        return EJBMetafacadeUtils.getAllInstanceAttributes(this);
    }

    public List handleGetInheritedInstanceAttributes()
    {
        return EJBMetafacadeUtils.getInheritedInstanceAttributes(this);
    }

    public Collection handleGetCreateMethods(boolean follow)
    {
        return EJBMetafacadeUtils.getCreateMethods(this, follow);
    }

    public Collection handleGetSelectMethods(boolean follow)
    {
        Collection retval = new ArrayList();
        EJBEntityFacade entity = null;
        do
        {
            Collection ops = this.getOperations();
            for (Iterator i = ops.iterator(); i.hasNext();)
            {
                OperationFacade op = (OperationFacade)i.next();
                if (op.hasStereotype(EJBProfile.STEREOTYPE_SELECT_METHOD))
                {
                    retval.add(op);
                }
            }
            if (follow)
            {
                entity = (EJBEntityFacade)this.getGeneralization();
            }
            else
            {
                break;
            }
        }
        while (entity != null);
        return retval;
    }

    public String handleGetHomeInterfaceName()
    {
        return EJBMetafacadeUtils.getHomeInterfaceName(this);
    }

    public Collection handleGetEnvironmentEntries(boolean follow)
    {
        return EJBMetafacadeUtils.getEnvironmentEntries(this, follow);
    }

    public Collection handleGetConstants(boolean follow)
    {
        return EJBMetafacadeUtils.getConstants(this, follow);
    }
    
    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntity#getJndiName()
     */
    public java.lang.String handleGetJndiName()
    {
        StringBuffer jndiName = new StringBuffer();
        String jndiNamePrefix = StringUtils.trimToEmpty(this.getJndiNamePrefix());
        if (StringUtils.isNotEmpty(jndiNamePrefix))
        {
            jndiName.append(jndiNamePrefix);
            jndiName.append("/");
        }
        jndiName.append("ejb/");
        jndiName.append(this.getFullyQualifiedName());
        return jndiName.toString();
    }

    /**
     * Gets the <code>jndiNamePrefix</code> for this EJB.
     * 
     * @return the EJB Jndi name prefix.
     */
    protected String getJndiNamePrefix()
    {
        return (String)this.getConfiguredProperty(EJBGlobals.JNDI_NAME_PREFIX);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacade#allowSyntheticCreateMethod()
     */
    public boolean handleAllowSyntheticCreateMethod()
    {
        return EJBMetafacadeUtils.allowSyntheticCreateMethod(this);
    }   
}