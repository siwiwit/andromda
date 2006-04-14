package org.andromda.metafacades.uml2;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.AssociationFacade.
 *
 * @see org.andromda.metafacades.uml.AssociationFacade
 */
public class AssociationFacadeLogicImpl
    extends AssociationFacadeLogic
{

    public AssociationFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#getRelationName()
     */
    protected java.lang.String handleGetRelationName()
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#isMany2Many()
     */
    protected boolean handleIsMany2Many()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#isAssociationClass()
     */
    protected boolean handleIsAssociationClass()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#getAssociationEnds()
     */
    protected java.util.List handleGetAssociationEnds()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#isAbstract()
     */
    protected boolean handleIsAbstract()
    {
        // TODO: add your implementation here!
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#isLeaf
     */
    protected boolean handleIsLeaf()
    {
        // TODO: add your implementation here!
        return false;
    }

    protected Object handleGetAssociationEndA()
    {
        // TODO Auto-generated method stub
        return null;
    }

    protected Object handleGetAssociationEndB()
    {
        // TODO Auto-generated method stub
        return null;
    }
}