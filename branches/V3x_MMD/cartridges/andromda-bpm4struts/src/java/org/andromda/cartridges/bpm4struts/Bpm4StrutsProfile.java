package org.andromda.cartridges.bpm4struts;

public class Bpm4StrutsProfile
{
    /* ----------------- Stereotypes -------------------- */

    public static final String STEREOTYPE_MODEL = "FrontEndModel";
    public static final String STEREOTYPE_CONTROLLER = "FrontEndController";
    public static final String STEREOTYPE_VIEW = "FrontEndView";
    public static final String STEREOTYPE_INPUTFIELD = "FrontEndViewInputField";
    public static final String STEREOTYPE_WORKFLOW = "FrontEndWorkflow";
    public static final String STEREOTYPE_USECASE = "FrontEndUseCase";

    /* ----------------- Tagged Values -------------------- */

    public static final String TAGGED_VALUE_FORM_BEAN_NAME = "@org.andromda.struts.form.name";
    public static final String TAGGED_VALUE_ACTION_PATH = "@org.andromda.struts.action.path";
    public static final String TAGGED_VALUE_ACTION_STATE = "@org.andromda.struts.action.state";
    public static final String TAGGED_VALUE_JSP_FILENAME = "@org.andromda.struts.jsp.filename";

    private Bpm4StrutsProfile()
    {
    }
}
