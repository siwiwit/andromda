package org.andromda.samples.animalquiz.decisiontree;

                        
public class DecisionServiceBeanImpl
    extends DecisionServiceBean
    implements javax.ejb.SessionBean
{
    // concrete business methods that were declared
    // abstract in class DecisionServiceBean ...



    protected org.andromda.samples.animalquiz.decisiontree.VODecisionItem handleGetFirstQuestion (net.sf.hibernate.Session sess)
        throws DecisionException {
        return new VODecisionItem("Is it an elephant?", "dummy1", "dummy2"); // just for testing whether this string appears on the screen!
    }




    protected org.andromda.samples.animalquiz.decisiontree.VODecisionItem handleGetNextQuestion (net.sf.hibernate.Session sess, java.lang.String itemId)
        throws DecisionException {
        // TODO: put your implementation here.

        // Dummy return value, just that the file compiles
        return null;
    }




    protected java.lang.String handleAddNewAnimalWithQuestion (net.sf.hibernate.Session sess, java.lang.String animalName, java.lang.String promptForYes, java.lang.String idOfLastNoDecision)
        throws DecisionException {
        // TODO: put your implementation here.

        // Dummy return value, just that the file compiles
        return null;
    }



    // ---------- the usual session bean stuff... ------------

    public void setSessionContext(javax.ejb.SessionContext ctx)
    {
        //Log.trace("DecisionServiceBean.setSessionContext...");
        super.setSessionContext (ctx);
    }

    public void ejbRemove()
    {
        //Log.trace(
        //    "DecisionServiceBean.ejbRemove...");
    }

    public void ejbPassivate()
    {
        //Log.trace("DecisionServiceBean.ejbPassivate...");
    }

    public void ejbActivate()
    {
        //Log.trace("DecisionServiceBean.ejbActivate...");
    }
}
