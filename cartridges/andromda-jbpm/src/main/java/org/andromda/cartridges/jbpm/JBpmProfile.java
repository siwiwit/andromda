package org.andromda.cartridges.jbpm;

import org.andromda.core.profile.Profile;

/**
 * The jBPM profile. Contains the profile information (tagged values, and stereotypes) for the jBPM cartridge.
 *
 * @author Wouter Zoons
 */
public class JBpmProfile
{
    /**
     * The Profile instance from which we retrieve the mapped profile names.
     */
    private static final Profile profile = Profile.instance();
    
    // Stereotypes
    /**
     * profile.get("BUSINESS_PROCESS")
     */
    public static final String STEREOTYPE_BUSINESS_PROCESS = profile.get("BUSINESS_PROCESS");
    /**
     * profile.get("BEFORE_SIGNAL")
     */
    public static final String STEREOTYPE_BEFORE_SIGNAL = profile.get("BEFORE_SIGNAL");
    /**
     * profile.get("AFTER_SIGNAL")
     */
    public static final String STEREOTYPE_AFTER_SIGNAL = profile.get("AFTER_SIGNAL");
    /**
     * profile.get("NODE_ENTER")
     */
    public static final String STEREOTYPE_NODE_ENTER = profile.get("NODE_ENTER");
    /**
     * profile.get("NODE_LEAVE")
     */
    public static final String STEREOTYPE_NODE_LEAVE = profile.get("NODE_LEAVE");
    /**
     * profile.get("TASK")
     */
    public static final String STEREOTYPE_TASK = profile.get("TASK");
    /**
     * profile.get("TIMER")
     */
    public static final String STEREOTYPE_TIMER = profile.get("TIMER");

    // Tagged Values
    /**
     * profile.get("ASSIGNMENT_EXPRESSION")
     */
    public static final String TAGGEDVALUE_ASSIGNMENT_EXPRESSION = profile.get("ASSIGNMENT_EXPRESSION");
    /**
     * profile.get("TIMER_REPEAT")
     */
    public static final String TAGGEDVALUE_TIMER_REPEAT = profile.get("TIMER_REPEAT");
    /**
     * profile.get("TIMER_DUEDATE")
     */
    public static final String TAGGEDVALUE_TIMER_DUEDATE = profile.get("TIMER_DUEDATE");
    /**
     * profile.get("TIMER_TRANSITION")
     */
    public static final String TAGGEDVALUE_TIMER_TRANSITION = profile.get("TIMER_TRANSITION");
    /**
     * profile.get("TASK_BLOCKING")
     */
    public static final String TAGGEDVALUE_TASK_BLOCKING = profile.get("TASK_BLOCKING");
}