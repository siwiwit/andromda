package org.andromda.samples.animalquiz.guess;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.rpc.ServiceException;

import org.andromda.samples.animalquiz.decisiontree.client.DecisionService;
import org.andromda.samples.animalquiz.decisiontree.client.DecisionServiceServiceLocator;
import org.andromda.samples.animalquiz.decisiontree.client.VODecisionItem;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.Action;
  
/**
 *
 * This controller class implements all the methods that are called
 * from the activities inside the "Guess" activity graph.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @author Chad Brandon
 */
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
     * Returns the session state object that is stored in the HTTP servlet session.
     * 
     * @param request the last request
     * @return the session state object
     */
    private static GuessSessionState getSessionState(HttpServletRequest request) {
        final String GUESS_STATE = "animal.quiz.guess.state";
        HttpSession session = request.getSession();
        GuessSessionState state = (GuessSessionState) session.getAttribute(GUESS_STATE);
        if (state == null) {
            state = new GuessSessionState();
            session.setAttribute(GUESS_STATE, state);
        }
        return state;
    }

    /**
     * Fetches the first question from the business tier and
     * returns the prompt string in the form.
     * @see GuessControllerInterface#getFirstQuestion(Action, ActionMapping, GuessForm, HttpServletRequest, HttpServletResponse)
     */
    public void getFirstQuestion(
        Action action,
        ActionMapping mapping,
        GuessForm form,
        HttpServletRequest request,
        HttpServletResponse reponse)
        throws Exception {
        DecisionService decisionService = this.getService();
        VODecisionItem vodi = 
            decisionService.getFirstQuestion();

        form.setQuestion(vodi.getPrompt());

        // Keep the decision item in the session so that
        // the next step can process it.
        getSessionState(request).setLastDecisionItem(vodi);
    }

    /**
     * Checks whether a next decision item is available in the
     * decision tree.
     * @see GuessControllerInterface#nextDecisionItemAvailable(Action, ActionMapping, GuessForm, HttpServletRequest, HttpServletResponse)
     * @return String "yes" or "no".
     */
    public java.lang.String nextDecisionItemAvailable(
        Action action,
        ActionMapping mapping,
        GuessForm form,
        HttpServletRequest request,
        HttpServletResponse reponse)
        throws Exception {
        GuessSessionState gss = getSessionState(request);
        VODecisionItem vodi = gss.getLastDecisionItem();

        String idNextItem =
            "yes".equals(gss.getLastAnswerFromUser()) ? vodi.getIdYesItem() : vodi.getIdNoItem();

        if (idNextItem != null) {
            DecisionService decisionService =
                this.getService();
            vodi = decisionService.getNextQuestion(idNextItem);

            form.setQuestion(vodi.getPrompt());

            // Keep the decision item in the session so that
            // the next step can process it.
            gss.setLastDecisionItem(vodi);
            return "yes";
        }
        return "no";
    }

    /**
     * Stores the name of the animal that the user has given. It is stored
     * inside the session state - no call to the business tier.
     * @see GuessControllerInterface#rememberAnimal(Action, ActionMapping, GuessForm, HttpServletRequest, HttpServletResponse)
     */
    public void rememberAnimal(
        Action action,
        ActionMapping mapping,
        GuessForm form,
        HttpServletRequest request,
        HttpServletResponse reponse)
        throws Exception {
        GuessSessionState gss = getSessionState(request);
        gss.setLastAnimalName(form.getAnimal());
    }

    /**
     * Takes the differentiator question that the user has given and creates
     * a new animal in the business tier. If the user answers "yes" to that question
     * during the next run of the game, that animal is presented as a guess.
     * @see GuessControllerInterface#rememberQuestion(Action, ActionMapping, GuessForm, HttpServletRequest, HttpServletResponse)
     */
    public void rememberQuestion(
        Action action,
        ActionMapping mapping,
        GuessForm form,
        HttpServletRequest request,
        HttpServletResponse reponse)
        throws Exception {

        GuessSessionState gss = getSessionState(request);

        DecisionService decisionService =
            this.getService();

        decisionService.addNewAnimalWithQuestion(
            gss.getLastAnimalName(),
            form.getQuestion(),
            gss.getLastDecisionItem().getId());
    }
    
    private DecisionService getService() throws ServiceException 
    {
        DecisionServiceServiceLocator locator =
            new DecisionServiceServiceLocator();
        return locator.getDecisionService(); 
    }

    /**
     * Checks if the last answer from the user was "yes".
     * @see GuessControllerInterface#lastAnswerWasYes(Action, ActionMapping, GuessForm, HttpServletRequest, HttpServletResponse)
     */
    public boolean lastAnswerWasYes(
        Action action,
        ActionMapping mapping,
        GuessForm form,
        HttpServletRequest request,
        HttpServletResponse reponse)
        throws Exception {
        return "yes".equals(getSessionState(request).getLastAnswerFromUser());
    }

    /**
     * Stores the fact that the last answer from the user was positive.
     * @see GuessControllerInterface#rememberPositiveAnswer(Action, ActionMapping, GuessForm, HttpServletRequest, HttpServletResponse)
     */
    public void rememberPositiveAnswer(
        Action action,
        ActionMapping mapping,
        GuessForm form,
        HttpServletRequest request,
        HttpServletResponse reponse)
        throws Exception {
        getSessionState(request).setLastAnswerFromUser("yes");
    }

    /**
     * Stores the fact that the last answer from the user was negative.
     * @see GuessControllerInterface#rememberNegativeAnswer(Action, ActionMapping, GuessForm, HttpServletRequest, HttpServletResponse)
     */
    public void rememberNegativeAnswer(
        Action action,
        ActionMapping mapping,
        GuessForm form,
        HttpServletRequest request,
        HttpServletResponse reponse)
        throws Exception {
        getSessionState(request).setLastAnswerFromUser("no");
    }
}
