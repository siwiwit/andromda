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
    public static final String TAGGED_VALUE_ACTION_PARAMETER = "@org.andromda.struts.action.parameter";
    public static final String TAGGED_VALUE_JSP_FILENAME = "@org.andromda.struts.jsp.filename";
    public static final String TAGGED_VALUE_USE_CASE = "@org.andromda.struts.usecase";

    /* ----------------- Default Values ------------------- */
    public static final String TAGGED_VALUE_ACTION_DEFAULT_PARAMETER = "state";
    public static final String DEFAULT_ABSTRACT_CLASS_SUFFIX = "Abstract";
    public static final String DEFAULT_IMPLEMENTATION_CLASS_SUFFIX = "";
    public static final String DEFAULT_ABSTRACT_METHOD_SUFFIX = "";
    public static final String DEFAULT_IMPLEMENTATION_METHOD_SUFFIX = "Impl";

    private Bpm4StrutsProfile()
    {
    }
}
