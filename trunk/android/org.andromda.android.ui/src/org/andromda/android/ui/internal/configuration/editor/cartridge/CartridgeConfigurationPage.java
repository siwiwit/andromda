package org.andromda.android.ui.internal.configuration.editor.cartridge;

import org.andromda.android.ui.internal.configuration.editor.AbstractAndromdaModelFormPage;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * This form editor page contains a master/detail GUI that lets the user edit the cartridge configuration.
 *
 * @author Peter Friese
 * @since 08.11.2005
 */
public class CartridgeConfigurationPage
        extends AbstractAndromdaModelFormPage
{

    /** The ID of the page. */
    public static final String PAGE_ID = "cartridges";

    /** The master block of the master/details GUI- */
    private CartridgeConfigurationMasterDetailsBlock cartridgeConfigurationMasterDetailsBlock;

    /**
     * Creates a new cartridge configuration form page.
     *
     * @param editor The editor hosting this page.
     * @param id The page ID.
     * @param title The title of this page.
     */
    public CartridgeConfigurationPage(FormEditor editor,
        String id,
        String title)
    {
        super(editor, id, title);
        cartridgeConfigurationMasterDetailsBlock = new CartridgeConfigurationMasterDetailsBlock(this);
    }

    /**
     * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
     */
    protected void createFormContent(IManagedForm managedForm)
    {
        FormToolkit toolkit = managedForm.getToolkit();
        ScrolledForm form = managedForm.getForm();
        form.setText("Cartridge Configuration");
        Composite body = form.getBody();
        final GridLayout gridLayout = new GridLayout();
        body.setLayout(gridLayout);
        toolkit.paintBordersFor(body);

        cartridgeConfigurationMasterDetailsBlock.createContent(managedForm);
    }

    /**
     * @see org.andromda.android.ui.editor.AbstractFormEditorPage#doUpdatePage(org.eclipse.jface.text.IDocument)
     */
    protected void doUpdatePage(IDocument document)
    {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.andromda.android.ui.editor.AbstractFormEditorPage#doUpdateDocument(org.eclipse.jface.text.IDocument)
     */
    protected void doUpdateDocument(IDocument document)
    {
        // TODO Auto-generated method stub

    }

}
