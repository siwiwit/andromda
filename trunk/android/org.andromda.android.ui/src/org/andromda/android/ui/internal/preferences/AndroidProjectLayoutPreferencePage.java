/**
 *
 */
package org.andromda.android.ui.internal.preferences;

import org.andromda.android.ui.AndroidUIPlugin;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This preference page allows the user to define the project layout for Android projects.
 * 
 * @author Peter Friese
 * @since 05.10.2005
 */
public class AndroidProjectLayoutPreferencePage
        extends PreferencePage
        implements IWorkbenchPreferencePage
{

    /** The composite containing the input fields. */
    private AndroidProjectLayoutComposite projectPropertiesComposite;
    
    public Control createContents(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new FillLayout());

        projectPropertiesComposite = new AndroidProjectLayoutComposite(container, SWT.NONE);
        //
        setupData();
        return container;
    }

    /**
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench)
    {
        setPreferenceStore(AndroidUIPlugin.getDefault().getPreferenceStore());
    }
    
    /**
     * Initialize the GUI controls with data read from the preference store.
     */
    private void setupData()
    {
        String andromdaxmlLocation = getPreferenceStore().getString(IPreferenceConstants.RELATIVE_LOCATION_ANDROMDA_XML);

        projectPropertiesComposite.getConfigurationText().setText(andromdaxmlLocation);
    }
    
    /**
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    public boolean performOk()
    {
        getPreferenceStore().setValue(IPreferenceConstants.RELATIVE_LOCATION_ANDROMDA_XML, projectPropertiesComposite.getConfigurationText().getText());
        return super.performOk();
    }

}
