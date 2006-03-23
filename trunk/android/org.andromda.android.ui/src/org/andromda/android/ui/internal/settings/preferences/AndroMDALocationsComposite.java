package org.andromda.android.ui.internal.settings.preferences;

import org.andromda.android.ui.internal.util.DialogUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * This composite allows users to configure the location of the AndroMDA binary files. It is intented to be used on
 * preference and property pages.
 *
 * @author Peter Friese
 * @since 27.11.2005
 */
public class AndroMDALocationsComposite
        extends Composite
{
    /** Label for cartridges location. */
    private Label profilesLabel;

    /** Text field for profiles location. */
    private Text profilesText;

    /** Browse button for profiles location. */
    private Button profilesBrowseButton;

    /** Label for cartridges location. */
    private Label cartridgesLabel;

    /** Text field for cartridges location. */
    private Text cartridgesText;

    /** Browse button for cartidges location. */
    private Button cartridgesBrowseButton;

    /** Label for maven home. */
    private Label mavenHomeLabel;

    /** Text field for maven home location. */
    private Text mavenHomeText;

    /** Browse button dor maven home location. */
    private Button mavenHomeBrowseButton;

    /**
     * Create the composite.
     *
     * @param parent The parent for this composite.
     * @param style The SWT style for this composite.
     */
public AndroMDALocationsComposite(final Composite parent,
        final int style)
    {
        super(parent, style);
        setLayout(new GridLayout());
        final Group locationsGroup = new Group(this, SWT.NONE);
        locationsGroup.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        locationsGroup.setText("Locations");
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        locationsGroup.setLayout(gridLayout);

        cartridgesLabel = new Label(locationsGroup, SWT.NONE);
        cartridgesLabel.setText("Cartridges:");

        cartridgesText = new Text(locationsGroup, SWT.BORDER);
        cartridgesText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        cartridgesText.setText("cartridges");

        cartridgesBrowseButton = new Button(locationsGroup, SWT.NONE);
        cartridgesBrowseButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                String directoryName = DialogUtils.selectDirectory(getShell(),
                        "Select location for AndroMDA libraries.",
                        "Please choose the folder that contains the AndroMDA libraries.", cartridgesText.getText());
                if (directoryName != null)
                {
                    cartridgesText.setText(directoryName);
                }
            }
        });
        cartridgesBrowseButton.setText("Browse...");

        profilesLabel = new Label(locationsGroup, SWT.NONE);
        profilesLabel.setText("Profiles:");

        profilesText = new Text(locationsGroup, SWT.BORDER);
        profilesText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        profilesText.setText("profiles");

        profilesBrowseButton = new Button(locationsGroup, SWT.NONE);
        profilesBrowseButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e)
            {
                String directoryName = DialogUtils.selectDirectory(getShell(),
                        "Select location for AndroMDA profile.",
                        "Please choose the folder that contains the AndroMDA profiles.", profilesText.getText());
                if (directoryName != null)
                {
                    profilesText.setText(directoryName);
                }
            }
        });
        profilesBrowseButton.setText("Browse...");

        mavenHomeLabel = new Label(locationsGroup, SWT.NONE);
        mavenHomeLabel.setText("Maven Home:");

        mavenHomeText = new Text(locationsGroup, SWT.BORDER);
        mavenHomeText.setText("maven");
        mavenHomeText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        mavenHomeBrowseButton = new Button(locationsGroup, SWT.NONE);
        mavenHomeBrowseButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e)
            {
                String directoryName = DialogUtils.selectDirectory(getShell(),
                        "Select location for Maven home.",
                        "Please choose the folder that contains Maven.", mavenHomeText.getText());
                if (directoryName != null)
                {
                    mavenHomeText.setText(directoryName);
                }

            }
        });
        mavenHomeBrowseButton.setText("Browse...");
        new Label(locationsGroup, SWT.NONE);
    }

    /**
     * {@inheritDoc}
     */
    public void setEnabled(final boolean enabled)
    {
        super.setEnabled(enabled);
        cartridgesLabel.setEnabled(enabled);
        cartridgesText.setEnabled(enabled);
        cartridgesBrowseButton.setEnabled(enabled);
        profilesLabel.setEnabled(enabled);
        profilesText.setEnabled(enabled);
        profilesBrowseButton.setEnabled(enabled);
        mavenHomeLabel.setEnabled(enabled);
        mavenHomeText.setEnabled(enabled);
        mavenHomeBrowseButton.setEnabled(enabled);
    }

    /**
     * @return Returns the cartridgesText.
     */
    public Text getCartridgesText()
    {
        return cartridgesText;
    }

    /**
     * @return Returns the profilesText.
     */
    public Text getProfilesText()
    {
        return profilesText;
    }

    /**
     * @return Returns the mavenHomeText.
     */
    public Text getMavenHomeText()
    {
        return mavenHomeText;
    }

}
