package org.andromda.samples.animalquiz.decisiontree;

import java.util.Collection;

import javax.ejb.EJBException;

import net.sf.hibernate.HibernateException;

public class DecisionServiceBeanImpl
    extends DecisionServiceBean
    implements javax.ejb.SessionBean {
    // concrete business methods that were declared
    // abstract in class DecisionServiceBean ...

    protected org
        .andromda
        .samples
        .animalquiz
        .decisiontree
        .VODecisionItem handleGetFirstQuestion(net.sf.hibernate.Session sess)
        throws DecisionException {
        try {
            Collection dItems = DecisionItemFactory.findRoot(sess);
            DecisionItem dItem;
            if (dItems.isEmpty()) {
                dItem = AnimalFactory.create("elephant", true);
                sess.save(dItem);
            }
            else {
                dItem = (DecisionItem) dItems.iterator().next();
            }
            return dItem.getVO();
        }
        catch (HibernateException e) {
            throw new EJBException(e);
        }
    }

    protected org
        .andromda
        .samples
        .animalquiz
        .decisiontree
        .VODecisionItem handleGetNextQuestion(
            net.sf.hibernate.Session sess,
            java.lang.String itemId)
        throws DecisionException {
        // TODO: put your implementation here.

        // Dummy return value, just that the file compiles
        return null;
    }

    protected java.lang.String handleAddNewAnimalWithQuestion(
        net.sf.hibernate.Session sess,
        java.lang.String animalName,
        java.lang.String promptForYes,
        java.lang.String idOfLastNoDecision)
        throws DecisionException {
        // TODO: put your implementation here.

        // Dummy return value, just that the file compiles
        return null;
    }

    // ---------- the usual session bean stuff... ------------

    public void setSessionContext(javax.ejb.SessionContext ctx) {
        //Log.trace("DecisionServiceBean.setSessionContext...");
        super.setSessionContext(ctx);
    }

    public void ejbRemove() {
        //Log.trace(
        //    "DecisionServiceBean.ejbRemove...");
    }

    public void ejbPassivate() {
        //Log.trace("DecisionServiceBean.ejbPassivate...");
    }

    public void ejbActivate() {
        //Log.trace("DecisionServiceBean.ejbActivate...");
    }
}
