package org.andromda.core.common;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class represents properties which are used to 
 * configure Namespace objects which are made available
 * to configure Plugin instances.
 * 
 * @see org.andromda.core.common.Namespace
 * @see org.andromda.core.common.Namespaces
 * 
 * @author Chad Brandon
 */
public class Property {
	
    private String name;
    private String value;
    
    /**
     * Returns the name.
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the value.
     * @return String
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the name.
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = StringUtils.trimToEmpty(name);
    }

    /**
     * Sets the value.
     * @param value The value to set
     */
    public void setValue(String value) {
        this.value = StringUtils.trimToEmpty(value);
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
    	return ToStringBuilder.reflectionToString(this);
    }
}
