package org.andromda.samples.animalquiz.guess;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.andromda.samples.animalquiz.decisiontree.DecisionService;
import org.andromda.samples.animalquiz.decisiontree.DecisionServiceHome;
import org.andromda.samples.animalquiz.decisiontree.DecisionServiceUtil;
import org.andromda.samples.animalquiz.decisiontree.VODecisionItem;
import org.apache.struts.action.ActionMapping;

public final class GuessController implements GuessControllerInterface {
    private final static GuessController INSTANCE = new GuessController();

    /**
     * Singleton constructor
     */
    private GuessController() {
    }

    /**
     * Singleton instance accessor
     */
    public static GuessController getInstance() {
        return INSTANCE;
    }

    /**
     * 
     * <p/>
     * This method does not receive any parameters through the form bean.
     */
    public void getFirstQuestion(
        ActionMapping mapping,
        GuessForm form,
        HttpServletRequest request,
        HttpServletResponse reponse)
        throws Exception {
        DecisionServiceHome dsh = DecisionServiceUtil.getHome();
        DecisionService ds = dsh.create();
        VODecisionItem vodi = ds.getFirstQuestion();
        ds.remove();

        form.setQuestion(vodi.getPrompt());

        // Keep the decision item in the session so that
        // the next step can process it.
        HttpSession session = request.getSession();
        session.setAttribute("voLastDecisionItem", vodi);
    }

    /**
     * 
     * <p/>
     * This method does not receive any parameters through the form bean.
     */
    public java.lang.String nextDecisionItemAvailable(
        ActionMapping mapping,
        GuessForm form,
        HttpServletRequest request,
        HttpServletResponse reponse)
        throws Exception {
        HttpSession session = request.getSession();
        VODecisionItem vodi = (VODecisionItem) session.getAttribute("voLastDecisionItem");
        return (vodi.getIdNoItem() != null) ? "yes" : "no";
    }

    /**
     * 
     * <p/>
     * This method does not receive any parameters through the form bean.
     */
    public void rememberAnimal(
        ActionMapping mapping,
        GuessForm form,
        HttpServletRequest request,
        HttpServletResponse reponse)
        throws Exception {

        /*
         * By default this method populates the complete form, it is up to you to replace this
         * by those fields that are required (this cannot be determined here because it might be
         * that case that many action call this controller method, each with their own set of
         * parameters
         */
        populateForm(form);
    }

    /**
     * 
     * <p/>
     * This method does not receive any parameters through the form bean.
     */
    public void rememberQuestion(
        ActionMapping mapping,
        GuessForm form,
        HttpServletRequest request,
        HttpServletResponse reponse)
        throws Exception {

        /*
         * By default this method populates the complete form, it is up to you to replace this
         * by those fields that are required (this cannot be determined here because it might be
         * that case that many action call this controller method, each with their own set of
         * parameters
         */
        populateForm(form);
    }

    /**
     * This method exists solely to make the application work at runtime by populating
     * the complete form with default values.
     * <p/>
     * You may remove this method if you want.
     */
    private void populateForm(GuessForm form) {
        form.setAnimal("animal-test");
        form.setQuestion("question-test");
    }
}
