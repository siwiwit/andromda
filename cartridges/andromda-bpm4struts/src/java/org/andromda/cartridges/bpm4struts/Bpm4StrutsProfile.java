package org.andromda.cartridges.bpm4struts;

public class Bpm4StrutsProfile
{
    /* ----------------- Stereotypes -------------------- */

    public static final String STEREOTYPE_MODEL = "FrontEndModel";
    public static final String STEREOTYPE_CONTROLLER = "FrontEndController";
    public static final String STEREOTYPE_VIEW = "FrontEndView";
    public static final String STEREOTYPE_VIEW_TEXTFIELD = "FrontEndViewTextField";
    public static final String STEREOTYPE_VIEW_TEXTAREA = "FrontEndViewTextArea";
    public static final String STEREOTYPE_VIEW_CHECKBOX = "FrontEndViewTextCheckBox";
    public static final String STEREOTYPE_VIEW_RADIOBUTTON = "FrontEndViewRadioButton";
    public static final String STEREOTYPE_VIEW_COMBOBOX = "FrontEndViewComboBox";
    public static final String STEREOTYPE_VIEW_LIST = "FrontEndViewList";
    public static final String STEREOTYPE_VIEW_MULTIBOX = "FrontEndViewMultiBox";
    public static final String STEREOTYPE_VIEW_PASSWORD = "FrontEndViewPassword";
    public static final String STEREOTYPE_WORKFLOW = "FrontEndWorkflow";
    public static final String STEREOTYPE_USECASE = "FrontEndUseCase";

    /* ----------------- Tagged Values -------------------- */

    public static final String TAGGED_VALUE_FORM_BEAN_NAME = "@andromda.struts.form.name";
    public static final String TAGGED_VALUE_ACTION_PATH = "@andromda.struts.action.path";
    public static final String TAGGED_VALUE_ACTION_STATE = "@andromda.struts.action.state";
    public static final String TAGGED_VALUE_ACTION_PARAMETER = "@andromda.struts.action.parameter";
    public static final String TAGGED_VALUE_JSP_FILENAME = "@andromda.struts.jsp.filename";
    public static final String TAGGED_VALUE_USE_CASE = "@andromda.struts.usecase";
    public static final String TAGGED_VALUE_VIEW_ACTION = "@andromda.struts.view.action";
    public static final String TAGGED_VALUE_INPUT_REQUIRED = "@andromda.struts.view.field.required";
    public static final String TAGGED_VALUE_INPUT_SIZE = "@andromda.struts.view.field.size";
    public static final String TAGGED_VALUE_INPUT_ROWS = "@andromda.struts.view.field.rows";
    public static final String TAGGED_VALUE_INPUT_COLS = "@andromda.struts.view.field.cols";
    public static final String TAGGED_VALUE_INPUT_PATTERN = "@andromda.struts.view.field.pattern";

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
