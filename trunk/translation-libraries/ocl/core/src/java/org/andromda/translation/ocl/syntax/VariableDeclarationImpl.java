package org.andromda.translation.ocl.syntax;

import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * An implementation of the ocl VariableDeclaration.
 *
 * @author Chad Brandon
 * @see org.andromda.translation.ocl.syntax.VariableDeclaration
 */
public class VariableDeclarationImpl
        implements VariableDeclaration
{

    private String name;
    private String type;
    private String value;

    /**
     * Constructs a new VariableDeclarationImpl
     *
     * @param name the name of the VariableDeclaratiom
     * @param type the type of the VariableDeclaration
     */
    public VariableDeclarationImpl(String name, String type, String value)
    {
        String methodName = "VariableDeclarationImpl";
        ExceptionUtils.checkNull(methodName, "name", name);
        this.name = StringUtils.trimToEmpty(name);
        this.type = StringUtils.trimToEmpty(type);
        this.value = StringUtils.trimToEmpty(value);
    }

    /**
     * @see org.andromda.translation.ocl.syntax.VariableDeclaration#getName()
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * ==
     *
     * @see org.andromda.translation.ocl.syntax.VariableDeclaration#getType()
     */
    public String getType()
    {
        return this.type;
    }

    /**
     * @see org.andromda.translation.ocl.syntax.VariableDeclaration#getValue()
     */
    public String getValue()
    {
        return this.value;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        StringBuffer toString = new StringBuffer(this.getName());
        if (StringUtils.isNotEmpty(this.getType()))
        {
            toString.append(":");
            toString.append(this.getType());
        }
        return toString.toString();
    }

}