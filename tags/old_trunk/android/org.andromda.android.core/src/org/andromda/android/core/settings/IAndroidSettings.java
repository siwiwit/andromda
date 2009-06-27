package org.andromda.android.core.settings;

import org.eclipse.core.resources.IProject;

/**
 * Interface to the Android settings.
 *
 * @author Peter Friese
 * @since 05.12.2005
 */
public interface IAndroidSettings
{

    /** Preference key for the configuration location setting. */
    String CONFIGURATION_LOCATION = "configuration.location";
    
    /** Preference key for te cartridges location setting. */
    String CARTRIDGES_LOCATION = "cartridges.location";
    
    /** Preference key for the profiles location. */
    String PROFILES_LOCATION = "profiles.location";
    
    /** Preference key for the maven location. */
    String MAVEN_LOCATION = "maven.location";
    
    /** Preference key for the AndroMDA version. */
    String ANDROMDA_PREFERRED_VERSION = "andromda.preferred.version";

    /**
     * Check whether the user has provided all settings required to properly run Android.
     *
     * @return <code>true</code> if Android has been set up properly, <code>false</code> if not.
     */
    boolean isConfigurationValid();

    /**
     * Retrieve the location of the AndroMDA configuration XML file (andromda.xml) for the given project. This is a
     * project specific setting.
     *
     * @param project The project to retrieve the configuration for.
     * @return The relative location of the AndroMDA configuration file.
     */
    String getConfigurationLocation(IProject project);

    /**
     * Retrieve the location of the AndroMDA configuration XML file (andromda.xml) as it is configured globally.
     *
     * @return The relative location of the AndroMDA configuration file.
     */
    String getConfigurationLocation();

    /**
     * Sets the location for the AndroMDA configuration file(s) for the given project.
     *
     * @param project The project to set this property for.
     * @param location The location of the configuration file(s).
     */
    void setConfigurationsLocation(IProject project,
        String location);

    /**
     * Sets the location for the AndroMDA configuration file(s). This method operates on a global level, i.e. it sets a
     * preference. Thus, the location MUST be a relative path.
     *
     * @param location The location of the configuration files.
     */
    void setConfigurationLocation(String location);

    /**
     * Retrieves the globally configured location of the AndroMDA cartridges.
     *
     * @return An absolute path to the AndroMDA cartridges directory.
     */
    String getAndroMDACartridgesLocation();

    /**
     * Retrieves the globally configured location of the AndroMDA profiles.
     *
     * @return An absolute path to the AndroMDA profiles directory.
     */
    String getAndroMDAProfilesLocation();

    /**
     * Sets the location of the AndroMDA cartridges directory. This is a global setting.
     *
     * @param location An absolute path to the AndroMDA cartridges directory.
     */
    void setAndroMDACartridgesLocation(String location);

    /**
     * Sets the location of the AndroMDA profiles directory. This is a global setting.
     *
     * @param location An absolute path to the AndroMDA profiles directory.
     */
    void setAndroMDAProfilesLocation(String location);

    /**
     * Retrieves the location of Maven 1.0.x. This is a global setting.
     *
     * @return The Maven home.
     */
    String getMavenLocation();

    /**
     * Sets the Maven location. This is a global setting.
     *
     * @param location An absolute path to the Maven home.
     */
    void setMavenLocation(String location);

    /**
     * Sets the preferred AndroMDA version to be used in Android.
     *
     * @param preferredVersion The preferred version.
     */
    void setAndroMDAPreferredVersion(String preferredVersion);

    /**
     * Retrieves the preferred AndroMDA version to be used in Android.
     *
     * @return The version string.
     */
    String getAndroMDAPreferredVersion();

}
