package org.andromda.android.ui.internal.configuration.editor.model;

import org.andromda.android.ui.configuration.editor.ConfigurationEditor;
import org.andromda.android.ui.internal.editor.BaseSectionPart;
import org.andromda.core.configuration.AndromdaDocument;
import org.andromda.core.configuration.ServerDocument.Server;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.Section;

/**
 *
 * @author Peter Friese
 * @since 08.12.2005
 */
public class ModelDetailsSection
        extends BaseSectionPart
{

    /** The visual style of the section. */
    private static final int SECTION_STYLE = Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED;

    /** The composite hosted by this section. This composite contains the real edit components. */
    private ModelDetailsComposite modelDetailsComposite;

    /**
     * @param page
     */
    public ModelDetailsSection(FormPage page)
    {
        super(page, SECTION_STYLE);
    }

    /**
     * @param parent
     * @param page
     */
    public ModelDetailsSection(Composite parent,
        FormPage page)
    {
        super(parent, page, SECTION_STYLE);
    }

    public void initialize(IManagedForm form)
    {
        super.initialize(form);
        getSection().setText("Model details");
        getSection().setDescription("Specify the details of this model.");

        // insert model details composite
        modelDetailsComposite = new ModelDetailsComposite(this, SWT.NONE);
        getSection().setClient(modelDetailsComposite);
        getSection().setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
    }

    /**
     * @see org.eclipse.ui.forms.AbstractFormPart#refresh()
     */
    public void refresh()
    {
        super.refresh();
        FormEditor editor = getEditor();
        if (editor instanceof ConfigurationEditor)
        {
            ConfigurationEditor configurationEditor = (ConfigurationEditor)editor;
            AndromdaDocument document = configurationEditor.getDocument();
            Server server = document.getAndromda().getServer();

            boolean lastModifiedCheck = true;

            modelDetailsComposite.setLastModifiedCheck(lastModifiedCheck);
        }
    }

    /**
     * @see org.eclipse.ui.forms.AbstractFormPart#commit(boolean)
     */
    public void commit(boolean onSave)
    {
        FormEditor editor = getEditor();
        if (editor instanceof ConfigurationEditor)
        {
            ConfigurationEditor configurationEditor = (ConfigurationEditor)editor;
            AndromdaDocument document = configurationEditor.getDocument();

            boolean lastModifiedCheck = modelDetailsComposite.isLastModifiedCheck();
        }

        super.commit(onSave);
    }

}

