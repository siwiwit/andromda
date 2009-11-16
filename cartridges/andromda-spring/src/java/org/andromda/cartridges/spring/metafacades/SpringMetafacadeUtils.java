package org.andromda.cartridges.spring.metafacades;

import org.andromda.cartridges.spring.SpringProfile;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

/**
 * Contains utilities specific to dealing with the Spring cartridge metafacades.
 *
 * @author Chad Brandon
 * @author Peter Friese
 */
class SpringMetafacadeUtils
{
    /**
     * Creates a fully qualified name from the given <code>packageName</code>, <code>name</code>, and
     * <code>suffix</code>.
     *
     * @param packageName the name of the model element package.
     * @param name        the name of the model element.
     * @param suffix      the suffix to append.
     * @return the new fully qualified name.
     */
    static String getFullyQualifiedName(String packageName, String name, String suffix)
    {
        StringBuilder fullyQualifiedName = new StringBuilder(StringUtils.trimToEmpty(packageName));
        if (StringUtils.isNotBlank(packageName))
        {
            fullyQualifiedName.append(".");
        }
        fullyQualifiedName.append(StringUtils.trimToEmpty(name));
        if (StringUtils.isNotEmpty(suffix))
        {
            fullyQualifiedName.append(StringUtils.trimToEmpty(suffix));
        }
        return fullyQualifiedName.toString();
    }

    /**
     * Creates a fully qualified name from the given <code>packageName</code>, <code>name</code>, and
     * <code>suffix</code>.
     *
     * @param packageName the name of the model element package.
     * @param name        the name of the model element.
     * @return the new fully qualified name.
     */
    static String getFullyQualifiedName(String packageName, String name)
    {
        return getFullyQualifiedName(packageName, name, null);
    }

    /**
     * Gets the remoting type for the passed in <code>classifier</code>. If the remoting type can be retrieved from the
     * <code>classifier</code>, then that is used, otherwise the <code>defaultRemotingType</code> is returned.
     *
     * @return String the remoting type name.
     */
    @SuppressWarnings("null")
    static String getServiceRemotingType(ClassifierFacade classifier, String defaultServiceRemotingType)
    {
        ExceptionUtils.checkNull("classifer", classifier);
        String remotingType = null;
        if (classifier.hasStereotype(UMLProfile.STEREOTYPE_SERVICE))
        {
            String remotingTypeValue = (String)classifier.findTaggedValue(
                    SpringProfile.TAGGEDVALUE_SPRING_SERVICE_REMOTING_TYPE);
            // if the remoting type wasn't found, search all super classes
            if (StringUtils.isBlank(remotingTypeValue))
            {
                remotingType = (String)CollectionUtils.find(classifier.getAllGeneralizations(), new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        return ((ModelElementFacade)object).findTaggedValue(
                                SpringProfile.TAGGEDVALUE_SPRING_SERVICE_REMOTING_TYPE) != null;
                    }
                });
            }
            if (StringUtils.isNotEmpty(remotingTypeValue))
            {
                remotingType = remotingTypeValue;
            }
        }
        if (StringUtils.isBlank(remotingType))
        {
            remotingType = defaultServiceRemotingType;
        }
        return remotingType.toLowerCase().trim();
    }

    /**
     * Get the interceptors for the passed in <code>classifier</code>. If the interceptors can be retrieved from the
     * <code>classifier</code>, then these will be used, otherwise the <code>defaultInterceptors</code> are
     * returned.
     *
     * @param classifier the classifier whose interceptors we are looking for.
     * @param defaultInterceptors a list of interceptors to use if the classifier itself has no explicit interceptors.
     *
     * @return String[] the interceptors.
     */
    static String[] getServiceInterceptors(ClassifierFacade classifier,
        String[] defaultInterceptors)
    {
        ExceptionUtils.checkNull("classifier", classifier);
        String[] interceptors = null;
        if (classifier.hasStereotype(UMLProfile.STEREOTYPE_SERVICE))
        {
            String interceptorsValue = (String)classifier.findTaggedValue(
                    SpringProfile.TAGGEDVALUE_SPRING_SERVICE_INTERCEPTORS);
            // if the interceptors weren't found, search all super classes
            if (StringUtils.isBlank(interceptorsValue))
            {
                interceptorsValue = (String)CollectionUtils.find(classifier.getAllGeneralizations(), new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        return ((ModelElementFacade)object).findTaggedValue(
                                SpringProfile.TAGGEDVALUE_SPRING_SERVICE_INTERCEPTORS) != null;
                    }
                });
            }
            // interceptors are a comma-separated list of strings, go and split the list
            if (StringUtils.isNotEmpty(interceptorsValue))
            {
                interceptors = interceptorsValue.split(",");
            }
        }
        if (interceptors == null || interceptors.length == 0)
        {
            interceptors = defaultInterceptors;
        }
        return interceptors;
    }

    /**
     * Gets the remote service port for the passed in <code>classifier</code>. If the remote service
     * port can be retrieved from the <code>classifier</code>, then that is used, otherwise the
     * <code>defaultRemoteServicePort</code> is returned.
     *
     * @return String the remote service port.
     */
    @SuppressWarnings("null")
    static String getServiceRemotePort(ClassifierFacade classifier, String defaultRemoteServicePort)
    {
        ExceptionUtils.checkNull("classifer", classifier);
        String remoteServicePort = null;
        if (classifier.hasStereotype(UMLProfile.STEREOTYPE_SERVICE))
        {
            String remoteServicePortValue = (String)classifier.findTaggedValue(
                    SpringProfile.TAGGEDVALUE_SPRING_SERVICE_REMOTE_PORT);
            // if the remote service port wasn't found, search all super classes
            if (StringUtils.isBlank(remoteServicePortValue))
            {
                remoteServicePort = (String)CollectionUtils.find(classifier.getAllGeneralizations(), new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        return ((ModelElementFacade)object).findTaggedValue(
                                SpringProfile.TAGGEDVALUE_SPRING_SERVICE_REMOTE_PORT) != null;
                    }
                });
            }
            if (StringUtils.isNotEmpty(remoteServicePortValue))
            {
                remoteServicePort = remoteServicePortValue;
            }
        }
        if (StringUtils.isBlank(remoteServicePort))
        {
            remoteServicePort = defaultRemoteServicePort;
        }
        return remoteServicePort.toLowerCase().trim();
    }

    /**
     * Checks whether the passed in operation is a query and should be using named parameters.
     *
     * @param operation the operation.
     * @param defaultUseNamedParameters the default value.
     * @return whether named parameters should be used.
     */
    static boolean getUseNamedParameters(OperationFacade operation,
        boolean defaultUseNamedParameters)
    {
        ExceptionUtils.checkNull("operation", operation);
        boolean useNamedParameters = defaultUseNamedParameters;
        if (operation.isQuery())
        {
            String useNamedParametersValue = StringUtils.trimToEmpty((String)operation
                    .findTaggedValue(SpringProfile.TAGGEDVALUE_HIBERNATE_USE_NAMED_PARAMETERS));
            if (StringUtils.isNotEmpty(useNamedParametersValue))
            {
                useNamedParameters = Boolean.valueOf(useNamedParametersValue).booleanValue();
            }
        }
        return useNamedParameters;
    }
}