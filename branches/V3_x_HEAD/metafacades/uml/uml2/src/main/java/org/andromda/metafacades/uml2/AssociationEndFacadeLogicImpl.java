package org.andromda.metafacades.uml2;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.AssociationEndFacade.
 *
 * @see org.andromda.metafacades.uml.AssociationEndFacade
 */
public class AssociationEndFacadeLogicImpl
    extends AssociationEndFacadeLogic
{

    public AssociationEndFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isOne2One()
     */
    protected boolean handleIsOne2One()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isOne2Many()
     */
    protected boolean handleIsOne2Many()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isMany2One()
     */
    protected boolean handleIsMany2One()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isMany2Many()
     */
    protected boolean handleIsMany2Many()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isAggregation()
     */
    protected boolean handleIsAggregation()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isComposition()
     */
    protected boolean handleIsComposition()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isOrdered()
     */
    protected boolean handleIsOrdered()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isReadOnly()
     */
    protected boolean handleIsReadOnly()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isNavigable()
     */
    protected boolean handleIsNavigable()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getGetterName()
     */
    protected java.lang.String handleGetGetterName()
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getSetterName()
     */
    protected java.lang.String handleGetSetterName()
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getGetterSetterTypeName()
     */
    protected java.lang.String handleGetGetterSetterTypeName()
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isMany()
     */
    protected boolean handleIsMany()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isRequired()
     */
    protected boolean handleIsRequired()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isChild()
     */
    protected boolean handleIsChild()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getOtherEnd()
     */
    protected java.lang.Object handleGetOtherEnd()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getAssociation()
     */
    protected java.lang.Object handleGetAssociation()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getType()
     */
    protected java.lang.Object handleGetType()
    {
        // TODO: add your implementation here!
        return null;
    }
    
    /**
     * Get the UML upper multiplicity
     * Not implemented for UML1.4
     */
    protected int handleGetUpper()
    {
        return 0;
    }

    /**
     * Get the UML lower multiplicity
     * Not implemented for UML1.4
     */
    protected int handleGetLower()
    {
        return 0;
    }

}