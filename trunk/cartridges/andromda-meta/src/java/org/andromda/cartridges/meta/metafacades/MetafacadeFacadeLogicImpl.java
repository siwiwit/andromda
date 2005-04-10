package org.andromda.cartridges.meta.metafacades;

import org.andromda.cartridges.meta.MetaProfile;
import org.andromda.core.metafacade.MetafacadeException;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Metaclass facade implementation.
 *
 * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade
 */
public class MetafacadeFacadeLogicImpl extends MetafacadeFacadeLogic
{
    // ---------------- constructor -------------------------------

    public MetafacadeFacadeLogicImpl(java.lang.Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * Returns the class tagged with &lt;&lt;metaclass&gt;&gt;&gt; that is connected to the metaobject via a dependency.
     * If no metaclass is directly connected, the method walks up the supertype hierarchy.
     *
     * @return the metaclass object
     */
    protected Object handleGetMetaclass()
    {
        // delegate to recursive method
        return getMetaclass(this);
    }

    /**
     * Returns the class tagged with &lt;&lt;metaclass&gt;&gt; that is connected to cl via a dependency.
     *
     * @param cl the source classifier
     * @return the metaclass object
     */
    private ClassifierFacade getMetaclass(ClassifierFacade classifier)
    {
        for (Iterator iter = classifier.getSourceDependencies().iterator(); iter.hasNext();)
        {
            DependencyFacade dep = (DependencyFacade) iter.next();
            ClassifierFacade target = (ClassifierFacade) dep.getTargetElement();
            Collection stereotypes = target.getStereotypeNames();
            if (stereotypes != null && stereotypes.size() > 0)
            {
                String stereotypeName = (String) stereotypes.iterator().next();
                if (stereotypeName.equals(MetaProfile.STEREOTYPE_METACLASS))
                {
                    return target;
                }
            }
        }

        ClassifierFacade superclass = (ClassifierFacade) classifier.getGeneralization();
        return (superclass != null) ? getMetaclass(superclass) : null;
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade#isMetaclassDirectDependency()
     */
    protected boolean handleIsMetaclassDirectDependency()
    {
        boolean isMetaClassDirectDependency = false;
        Collection dependencies = this.getSourceDependencies();
        if (dependencies != null && !dependencies.isEmpty())
        {
            // there should be only one.
            DependencyFacade dependency = (DependencyFacade) dependencies.iterator().next();
            if (dependency != null)
            {
                ModelElementFacade targetElement = dependency.getTargetElement();
                if (targetElement != null)
                {
                    isMetaClassDirectDependency = targetElement.hasStereotype(MetaProfile.STEREOTYPE_METACLASS);
                }
            }
        }
        return isMetaClassDirectDependency;
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade#getLogicName()
     */
    protected String handleGetLogicName()
    {
        return this.getName() + "Logic";
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade#getLogicImplName()
     */
    protected String handleGetLogicImplName()
    {
        return this.getName() + "LogicImpl";
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade#getFullyQualifiedLogicImplName()
     */
    protected String handleGetFullyQualifiedLogicImplName()
    {
        return this.getMetafacadeSupportClassName(this.getLogicImplName());
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade#getFullyQualifiedLogicName()
     */
    protected String handleGetFullyQualifiedLogicName()
    {
        return this.getMetafacadeSupportClassName(this.getLogicName());
    }

    /**
     * This defines the metamodel version package name (i.e. org.andromda.metafacades.uml14,
     * org.andromda.metafacades.um20, etc) used by this cartridge to create the generated impl package name, if left
     * empty then the impl package will be the same as the metafacade package (therefore we default to an empty name)
     */
    private static final String METAMODEL_VERSION_PACKAGE = "metamodelVersionPackage";

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade#getLogicFile(java.lang.String)
     */
    protected String handleGetLogicFile()
    {
        return this.getFullyQualifiedLogicName().replace('.', '/') + ".java";
    }

    /**
     * Gets the metamodel version package name (i.e. org.andromda.metafacades.uml14, org.andromda.metafacades.um20, etc)
     * used by this cartridge to create the generated impl package name, if left empty then the impl package will be the
     * same as the metafacade package (therefore we default to an empty name)
     */
    private String getMetaModelVersionPackage()
    {
        return String.valueOf(this.getConfiguredProperty(METAMODEL_VERSION_PACKAGE));
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade#getLogicPackageName(java.lang.String)
     */
    protected String handleGetLogicPackageName()
    {
        String packageName = this.getMetaModelVersionPackage();
        if (StringUtils.isEmpty(packageName))
        {
            packageName = this.getPackageName();
        }
        return packageName;
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade#getLogicImplFile(java.lang.String)
     */
    protected String handleGetLogicImplFile()
    {
        return this.getFullyQualifiedLogicImplName().replace('.', '/') + ".java";
    }

    /**
     * Creates a metafacade support class name from the given <code>metamodelVersionPackage</code> (i.e. the package for
     * the specific meta model version). Support classes are the 'Logic' classes.
     *
     * @param metamodelVersionPackage the version of the meta model
     * @param the                     name of the class to append to the package.
     * @return the new metafacade support class name.
     */
    private String getMetafacadeSupportClassName(String name)
    {
        StringBuffer fullyQualifiedName = new StringBuffer(this.getLogicPackageName());
        if (StringUtils.isNotBlank(fullyQualifiedName.toString()))
        {
            fullyQualifiedName.append(".");
            fullyQualifiedName.append(name);
        }
        return fullyQualifiedName.toString();
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade#getMethodDataForPSM(boolean)
     */
    protected Collection handleGetMethodDataForPSM(boolean includeSuperclasses)
    {
        ArrayList result = null;
        HashMap map = new HashMap();
        internalGetMethodDataForPSM(map, this);
        if (includeSuperclasses)
        {
            for (ClassifierFacade cd = (ClassifierFacade) getGeneralization(); cd != null; cd = (ClassifierFacade) cd.getGeneralization())
            {
                internalGetMethodDataForPSM(map, (MetafacadeFacade) cd);
            }
        }
        result = new ArrayList(map.values());
        Collections.sort(result);
        return result;
    }

    private void internalGetMethodDataForPSM(HashMap map, MetafacadeFacade facade)
    {
        final String methodName = "MetafacadeFacadeLogicImpl.internalGetMethodDataForPSM";
        try
        {
            final String fullyQualifiedName = facade.getFullyQualifiedName();

            // translate UML attributes to getter methods
            for (Iterator attributeIterator = facade.getAttributes().iterator(); attributeIterator.hasNext();)
            {
                AttributeFacade attribute = (AttributeFacade) attributeIterator.next();
                final MethodData methodData = new MethodData(fullyQualifiedName, "public", false,
                        attribute.getType().getFullyQualifiedName(), attribute.getGetterName(),
                        attribute.getDocumentation("    * "));
                map.put(methodData.buildCharacteristicKey(), methodData);
            }
            // translate UML operations to methods
            for (Iterator iter = facade.getOperations().iterator(); iter.hasNext();)
            {
                OperationFacade op = (OperationFacade) iter.next();
                final UMLOperationData md = new UMLOperationData(fullyQualifiedName, op);
                map.put(md.buildCharacteristicKey(), md);
            }

            // translate UML associations to getter methods
            for (Iterator endIterator = facade.getAssociationEnds().iterator(); endIterator.hasNext();)
            {
                AssociationEndFacade end = (AssociationEndFacade) endIterator.next();
                AssociationEndFacade otherEnd = end.getOtherEnd();
                if (otherEnd.isNavigable())
                {
                    final MethodData md = new MethodData(fullyQualifiedName, "public", false,
                            otherEnd.getGetterSetterTypeName(), otherEnd.getGetterName(),
                            otherEnd.getDocumentation("    * "));
                    map.put(md.buildCharacteristicKey(), md);
                }
            }
        }
        catch (Throwable th)
        {
            String errMsg = "Error performing " + methodName;
            logger.error(errMsg, th);
            throw new MetafacadeException(errMsg, th);
        }
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade#isRequiresInheritanceDelegatation()
     */
    protected boolean handleIsRequiresInheritanceDelegatation()
    {
        boolean requiresInheritanceDelegation = false;
        ModelElementFacade superMetafacade = this.getGeneralization();
        if (superMetafacade != null)
        {
            requiresInheritanceDelegation = !superMetafacade.getPackageName().equals(this.getPackageName()) || this.getGeneralizations()
                    .size() >
                    1;
        }
        return requiresInheritanceDelegation;
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade#isConstructorRequiresMetaclassCast()
     */
    protected boolean handleIsConstructorRequiresMetaclassCast()
    {
        boolean requiresCast = false;
        MetafacadeFacade superMetafacade = (MetafacadeFacade) this.getGeneralization();
        if (superMetafacade != null)
        {
            requiresCast = superMetafacade.isMetaclassDirectDependency() && !this.isRequiresInheritanceDelegatation();

        }
        return requiresCast;
    }

}