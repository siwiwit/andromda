package org.andromda.cartridges.jbpm;

import org.andromda.core.common.Profile;

public class JBpmProfile
{
    /**
     * The Profile instance from which we retrieve the mapped profile names.
     */
    private static final Profile profile = Profile.instance();

    public static final String STEREOTYPE_BUSINESS_PROCESS = profile.get("BUSINESS_PROCESS");

    public static final String STEREOTYPE_BEFORE_SIGNAL = profile.get("BEFORE_SIGNAL");
    public static final String STEREOTYPE_AFTER_SIGNAL = profile.get("AFTER_SIGNAL");
    public static final String STEREOTYPE_NODE_ENTER = profile.get("NODE_ENTER");
    public static final String STEREOTYPE_NODE_LEAVE = profile.get("NODE_LEAVE");

    public static final String STEREOTYPE_TASK = profile.get("TASK");

    public static final String TAGGED_VALUE_ASSIGNMENT_EXPRESSION = profile.get("ASSIGNMENT_EXPRESSION");
}
