package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.core.mapping.Mappings;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.datatypes.AggregationKindEnum;
import org.omg.uml.foundation.datatypes.ChangeableKindEnum;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.foundation.datatypes.OrderingKind;
import org.omg.uml.foundation.datatypes.OrderingKindEnum;


/**
 * 
 *
 * Metaclass facade implementation.
 *
 */
public class AssociationEndFacadeLogicImpl
       extends AssociationEndFacadeLogic
       implements org.andromda.metafacades.uml.AssociationEndFacade
{
    // ---------------- constructor -------------------------------
    
    public AssociationEndFacadeLogicImpl (org.omg.uml.foundation.core.AssociationEnd metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class AssociationEndFacade ...

    public Object handleGetOtherEnd()
    {
        AssociationEnd otherEnd;

        Collection ends = metaObject.getAssociation().getConnection();
        for (Iterator i = ends.iterator(); i.hasNext();)
        {
            AssociationEnd ae = (AssociationEnd) i.next();
            if (!metaObject.equals(ae))
            {
                return ae;
            }
        }

        return null;
    }

    public java.lang.String getRoleName()
    {
        String roleName = metaObject.getName();
        if ((roleName == null) || (roleName.length() == 0))
        {
            roleName = "the" + metaObject.getParticipant().getName();
        }

        return roleName;
    }

    /**
     * @see org.andromda.core.metadecorators.uml14.ModelElementFacade#getName()
     */
    public String getName()
    {
        String name = super.getName();
        //if name is empty, then get the name from the type
        if (StringUtils.isEmpty(name))
        {
            ClassifierFacade type = this.getType();
            name =
                StringUtils.uncapitalize(
                    StringUtils.trimToEmpty(type.getName()));
        }
        return name;
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndFacade#getType()
     */
    public Object handleGetType()
    {
        return metaObject.getParticipant();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndFacade#isOne2Many()
     */
    public boolean isOne2Many()
    {
        return !isMany(metaObject)
            && isMany((AssociationEnd) getOtherEnd().getMetaObject());
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndFacade#isMany2Many()
     */
    public boolean isMany2Many()
    {
        return isMany(metaObject)
            && isMany((AssociationEnd) getOtherEnd().getMetaObject());
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndFacade#isOne2One()
     */
    public boolean isOne2One()
    {
        return !isMany(metaObject)
            && !isMany((AssociationEnd) getOtherEnd().getMetaObject());
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndFacade#isMany2One()
     */
    public boolean isMany2One()
    {
        return isMany(metaObject)
            && !isMany((AssociationEnd) getOtherEnd().getMetaObject());
    }

    static protected boolean isMany(AssociationEnd ae)
    {
        Multiplicity multiplicity = ae.getMultiplicity();
        if (multiplicity == null)
        {
            return false; // no multiplicity means multiplicity "1"
        }

        Collection ranges = multiplicity.getRange();

        for (Iterator i = ranges.iterator(); i.hasNext();)
        {
            MultiplicityRange range = (MultiplicityRange) i.next();
            if (range.getUpper() > 1)
            {
                return true;
            }

            int rangeSize = range.getUpper() - range.getLower();
            if (rangeSize < 0)
            {
                return true;
            }

        }

        return false;
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndFacade#isOrdered()
     */
    public boolean isOrdered()
    {
        boolean ordered = false;

        OrderingKind ordering = metaObject.getOrdering();
        //no ordering is 'unordered'
        if (ordering != null)
        {
            ordered = ordering.equals(OrderingKindEnum.OK_ORDERED);
        }

        return ordered;
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndFacade#isAggregation()
     */
    public boolean isAggregation()
    {
        return AggregationKindEnum.AK_AGGREGATE.equals(
            metaObject.getAggregation());
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndFacade#isComposition()
     */
    public boolean isComposition()
    {
        return AggregationKindEnum.AK_COMPOSITE.equals(
            metaObject.getAggregation());
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndFacade#isReadOnly()
     */
    public boolean isReadOnly()
    {
        return ChangeableKindEnum.CK_FROZEN.equals(
            metaObject.getChangeability());
    }

    /**
     * @see org.andromda.core.metadecorators.uml14.AssociationEndFacade#isNavigable()
     */
    public boolean isNavigable()
    {
        return metaObject.isNavigable();
    }

    /**
     * @see org.andromda.core.metadecorators.uml14.AssociationEndFacade#getGetterName()
     */
    public java.lang.String getGetterName()
    {
        return "get" + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.core.metadecorators.uml14.AssociationEndFacade#getSetterName()
     */
    public java.lang.String getSetterName()
    {
        return "set" + StringUtils.capitalize(this.getName());
    }

    // relations

    /**
     * @see org.andromda.core.metadecorators.uml14.AssociationEndFacade#handleGetAssociation()
     */
    protected Object handleGetAssociation()
    {
        return metaObject.getAssociation();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndFacade#getGetterSetterTypeName()
     */
    public String getGetterSetterTypeName()
    {
        // if many, then list or collection
        if (isOne2Many() || isMany2Many())
        {
            Mappings lm = getLanguageMappings();
            return isOrdered()
                ? lm.getTo("datatype.List")
                : lm.getTo("datatype.Collection");
        }
        
        // if single element, then return the type
        return getOtherEnd().getType().getFullyQualifiedName();
    }
    
    /**
     * Language specific mappings property reference.
     */
    private static final String LANGUAGE_MAPPINGS = "languageMappings";
    
    /**
     * Allows the MetaFacadeFactory to populate 
     * the language mappings for this model element.
     * 
     * @param mappingUri the URI of the language mappings resource.
     */
    public void setLanguageMappings(String mappingUri) {
        try {
            Mappings mappings = Mappings.getInstance(mappingUri);
            // register the mappings with the component container.
            this.registerConfiguredProperty(LANGUAGE_MAPPINGS, mappings);
        } catch (Throwable th) {
            String errMsg = "Error setting '" 
                + LANGUAGE_MAPPINGS + "' --> '" 
                + mappingUri + "'";
            logger.error(errMsg, th);
            //don't throw the exception
        }
    }
    
    /**
     * Gets the language mappings that have been
     * set for this model elemnt.
     * @return the Mappings instance.
     */
    protected Mappings getLanguageMappings() {
        return (Mappings)this.getConfiguredProperty(LANGUAGE_MAPPINGS);
    }

}
