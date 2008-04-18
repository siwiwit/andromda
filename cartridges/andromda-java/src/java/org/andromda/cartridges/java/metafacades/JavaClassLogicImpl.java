package org.andromda.cartridges.java.metafacades;

import java.util.Iterator;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.java.metafacades.JavaClass.
 *
 * @see org.andromda.cartridges.java.metafacades.JavaClass
 */
public class JavaClassLogicImpl
    extends JavaClassLogic
{
    public JavaClassLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.java.metafacades.JavaClass#getInterfaceImplementationName()
     */
    protected java.lang.String handleGetInterfaceImplementationName()
    {
        return this.getInterfaceImplementionName().replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * Gets the value of the {@link JavaGlobals#INTERFACE_IMPLEMENTATION_NAME_PATTERN}.
     *
     * @return the interface implementation name..
     */
    private String getInterfaceImplementionName()
    {
        return String.valueOf(this.getConfiguredProperty(JavaGlobals.INTERFACE_IMPLEMENTATION_NAME_PATTERN));
    }

    /**
     * @see org.andromda.cartridges.java.metafacades.JavaClass#getFullyQualifiedInterfaceImplementationName()
     */
    protected String handleGetFullyQualifiedInterfaceImplementationName()
    {
        final StringBuffer fullName = new StringBuffer();
        final String packageName = this.getPackageName();
        if (StringUtils.isNotBlank(packageName))
        {
            fullName.append(packageName).append('.');
        }
        return fullName.append(this.getInterfaceImplementationName()).toString();
    }

    /**
     * @see org.andromda.cartridges.java.metafacades.JavaClass#isAbstractInterfaceImplementation()
     */
    protected boolean handleIsAbstractInterfaceImplementation()
    {
        boolean abstractImplementation = !this.getOperations().isEmpty();
        if (!abstractImplementation)
        {
            for (final Iterator iterator = this.getAllGeneralizations().iterator(); iterator.hasNext();)
            {
                final ClassifierFacade classifier = (ClassifierFacade)iterator.next();
                abstractImplementation = !classifier.getOperations().isEmpty();
                if (abstractImplementation)
                {
                    break;
                }
            }
        }
        return abstractImplementation;
    }
}