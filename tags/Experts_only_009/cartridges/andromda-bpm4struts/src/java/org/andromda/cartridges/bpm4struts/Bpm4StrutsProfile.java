package org.andromda.cartridges.bpm4struts;

public class Bpm4StrutsProfile
{
    /* ----------------- Stereotypes -------------------- */

    public static final String STEREOTYPE_VIEW = "FrontEndView";
    public static final String STEREOTYPE_EVENT = "FrontEndEvent";
    public static final String STEREOTYPE_USER = "FrontEndUser";
    public static final String STEREOTYPE_CONTROLLER = "FrontEndController";
    public static final String STEREOTYPE_EXCEPTION = "FrontEndException";

    public static final String STEREOTYPE_VIEW_TEXTFIELD = "FrontEndViewTextField";
    public static final String STEREOTYPE_VIEW_TEXTAREA = "FrontEndViewTextArea";
    public static final String STEREOTYPE_VIEW_CHECKBOX = "FrontEndViewTextCheckBox";
    public static final String STEREOTYPE_VIEW_RADIOBUTTON = "FrontEndViewRadioButton";
    public static final String STEREOTYPE_VIEW_COMBOBOX = "FrontEndViewComboBox";
    public static final String STEREOTYPE_VIEW_LIST = "FrontEndViewList";
    public static final String STEREOTYPE_VIEW_MULTIBOX = "FrontEndViewMultiBox";
    public static final String STEREOTYPE_VIEW_PASSWORD = "FrontEndViewPassword";

    /* ----------------- Tagged Values -------------------- */

    public static final String TAGGED_VALUE_ACTION_TYPE = "@andromda.struts.action.type";
    public static final String TAGGED_VALUE_ACTION_SUCCES_MESSAGE = "@andromda.struts.action.succes.message";
    public static final String TAGGED_VALUE_INPUT_REQUIRED = "@andromda.struts.view.field.required";
    public static final String TAGGED_VALUE_INPUT_SIZE = "@andromda.struts.view.field.size";
    public static final String TAGGED_VALUE_INPUT_ROWS = "@andromda.struts.view.field.rows";
    public static final String TAGGED_VALUE_INPUT_COLS = "@andromda.struts.view.field.cols";
    public static final String TAGGED_VALUE_INPUT_FORMAT = "@andromda.struts.view.field.format";
    public static final String TAGGED_VALUE_INPUT_TYPE = "@andromda.struts.view.field.type";
    public static final String TAGGED_VALUE_INPUT_VALIDWHEN = "@andromda.struts.view.field.validwhen";
    public static final String TAGGED_VALUE_EXCEPTION_TYPE = "@andromda.struts.exception.type";
    public static final String TAGGED_VALUE_EXCEPTION_PATH = "@andromda.struts.exception.path";
    public static final String TAGGED_VALUE_EXCEPTION_KEY = "@andromda.struts.exception.key";

    /* ----------------- Default Values ------------------- */
    public static final String TAGGED_VALUE_INPUT_DEFAULT_REQUIRED = "true";

    public static final String TAGGED_VALUE_ACTION_TYPE_HYPERLINK = "hyperlink";
    public static final String TAGGED_VALUE_ACTION_TYPE_FORM = "form";
    public static final String TAGGED_VALUE_EXCEPTION_DEFAULT_TYPE = "java.lang.Exception";

    private Bpm4StrutsProfile()
    {
    }
}
