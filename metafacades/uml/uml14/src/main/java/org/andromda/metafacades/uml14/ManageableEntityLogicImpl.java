package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.andromda.metafacades.uml.ActorFacade;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.EntityAttribute;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.ManageableEntity;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ManageableEntity.
 *
 * @see org.andromda.metafacades.uml.ManageableEntity
 * @author Bob Fields
 */
public class ManageableEntityLogicImpl
    extends ManageableEntityLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public ManageableEntityLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return the configured property denoting the character sequence to use for the separation of namespaces
     */
    private String getNamespaceSeparator()
    {
        return (String)getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR);
    }

    /**
     * @return manageablePackageName
     * @see org.andromda.metafacades.uml.ManageableEntity#getManageablePackageName()
     */
    protected String handleGetManageablePackageName()
    {
        String manageablePackageName = "";

        final String parentPackage = super.getPackageName();
        if (StringUtils.isNotBlank(parentPackage))
        {
            manageablePackageName = parentPackage;
        }

        final Object suffix = getConfiguredProperty(UMLMetafacadeProperties.MANAGEABLE_PACKAGE_NAME_SUFFIX);
        if (suffix != null && StringUtils.isNotBlank(suffix.toString()))
        {
            manageablePackageName += this.getNamespaceSeparator() + suffix;
        }

        return manageablePackageName;
    }

    /**
     * @see org.andromda.metafacades.uml14.ManageableEntityLogic#handleGetManageablePackagePath()
     */
    protected String handleGetManageablePackagePath()
    {
        return StringUtils.replace(this.getManageablePackageName(), this.getNamespaceSeparator(), "/");
    }

    /**
     * @see org.andromda.metafacades.uml14.ManageableEntityLogic#handleGetManageableAssociationEnds()
     */
    protected List handleGetManageableAssociationEnds()
    {
        final Set manageableAssociationEnds = new LinkedHashSet();// linked hashset to guarantee ordering wo/ duplicates
        collectAssociationEnds(manageableAssociationEnds, this);
        return new ArrayList(manageableAssociationEnds);
    }

    /**
     * This method recursively collects all association ends to which a manageable entity would need to navigate
     *
     * @param manageableAssociationEnds the collection in which to collect the association ends
     * @param entity the entity from which to recursively gather the association ends
     */
    private static void collectAssociationEnds(Collection<AssociationEndFacade> manageableAssociationEnds, ManageableEntity entity)
    {
        final Collection<AssociationEndFacade> associationEnds = entity.getAssociationEnds();
        for (final Iterator<AssociationEndFacade> associationEndIterator = associationEnds.iterator(); associationEndIterator.hasNext();)
        {
            final AssociationEndFacade associationEnd = associationEndIterator.next();
            final AssociationEndFacade otherEnd = associationEnd.getOtherEnd();

            if (otherEnd.isNavigable() && otherEnd.getType() instanceof Entity)
            {
                manageableAssociationEnds.add(otherEnd);
            }
        }

        // retrieve all association ends for all parents (recursively)
        final Collection<GeneralizableElementFacade> parentEntities = entity.getAllGeneralizations();
        for (final Iterator<GeneralizableElementFacade> parentEntityIterator = parentEntities.iterator(); parentEntityIterator.hasNext();)
        {
            final Object parentEntityObject = parentEntityIterator.next();
            if (parentEntityObject instanceof ManageableEntity)
            {
                collectAssociationEnds(manageableAssociationEnds, (ManageableEntity)parentEntityObject);
            }
        }
    }

    /**
     * @return !this.isAbstract()
     * @see org.andromda.metafacades.uml.ManageableEntity#isCreate()
     */
    protected boolean handleIsCreate()
    {
        return !this.isAbstract();
    }

    /**
     * @see org.andromda.metafacades.uml14.ManageableEntityLogic#handleGetManageableServiceName()
     */
    protected String handleGetManageableServiceName()
    {
        return getName() + "ManageableService";
    }

    /**
     * @see org.andromda.metafacades.uml14.ManageableEntityLogic#handleGetManageableServiceFullPath()
     */
    protected String handleGetManageableServiceFullPath()
    {
        return '/' + StringUtils.replace(
            getFullyQualifiedManageableServiceName(),
            getNamespaceSeparator(),
            "/");
    }

    /**
     * @see org.andromda.metafacades.uml14.ManageableEntityLogic#handleGetFullyQualifiedManageableServiceName()
     */
    protected String handleGetFullyQualifiedManageableServiceName()
    {
        return this.getManageablePackageName() + this.getNamespaceSeparator() + getManageableServiceName();
    }

    /**
     * @see org.andromda.metafacades.uml14.ManageableEntityLogic#handleGetManageableServiceAccessorCall()
     */
    protected String handleGetManageableServiceAccessorCall()
    {
        final String property = UMLMetafacadeProperties.MANAGEABLE_SERVICE_ACCESSOR_PATTERN;
        final String accessorImplementation =
            this.isConfiguredProperty(property) ? ObjectUtils.toString(this.getConfiguredProperty(property))
                : "${application.package}.ManageableServiceLocator.instance().get{1}()";
        return accessorImplementation.replaceAll(
            "\\{0\\}",
            getManageablePackageName()).replaceAll(
            "\\{1\\}",
            getManageableServiceName());
    }

    /**
     * @see org.andromda.metafacades.uml14.ManageableEntityLogic#handleIsRead()
     */
    protected boolean handleIsRead()
    {
        return true;
    }

    /**
     * @see org.andromda.metafacades.uml14.ManageableEntityLogic#handleIsUpdate()
     */
    protected boolean handleIsUpdate()
    {
        return this.getManageableIdentifier() != null; // TODO Implement handleIsUpdate
    }

    /**
     * @see org.andromda.metafacades.uml14.ManageableEntityLogic#handleIsDelete()
     */
    protected boolean handleIsDelete()
    {
        return this.getManageableIdentifier() != null; // TODO Implement handleIsDelete
    }

    /**
     * @see org.andromda.metafacades.uml14.ManageableEntityLogic#handleGetManageableAttributes()
     */
    protected List handleGetManageableAttributes()
    {
        return new ArrayList(this.getAttributes(true));
    }

    /**
     * @see org.andromda.metafacades.uml14.ManageableEntityLogic#handleGetManageableIdentifier()
     */
    protected Object handleGetManageableIdentifier()
    {
        return this.getIdentifiers(true).iterator().next();
    }

    /**
     * @see org.andromda.metafacades.uml14.ManageableEntityLogic#handleGetManageableMembers()
     */
    protected List handleGetManageableMembers()
    {
        final List criteria = new ArrayList();
        criteria.addAll(this.getManageableAttributes());
        criteria.addAll(this.getManageableAssociationEnds());
        return criteria;
    }

    private enum ListType
    {
        PRIMITIVE,
        WRAPPER;
    }

    private String createListWithManageableMembers(ListType listType)
    {
        final StringBuilder buffer = new StringBuilder();

        final List attributes = this.getManageableAttributes();
        for (int i = 0; i < attributes.size(); i++)
        {
            if (buffer.length() > 0)
            {
                buffer.append(", ");
            }

            final AttributeFacade attribute = (AttributeFacade)attributes.get(i);
            final ClassifierFacade type = attribute.getType();
            if (type != null)
            {
                if(ListType.PRIMITIVE.equals(listType))
                {
                    buffer.append(type.getFullyQualifiedName());
                    buffer.append(' ');
                }
                else if(ListType.WRAPPER.equals(listType))
                {
                    buffer.append(type.isPrimitive()? type.getWrapperName(): type.getFullyQualifiedName());
                    buffer.append(' ');
                }
                buffer.append(attribute.getName());
            }
        }

        final List associationEnds = this.getManageableAssociationEnds();
        for (int i = 0; i < associationEnds.size(); i++)
        {
            final AssociationEndFacade associationEnd = (AssociationEndFacade)associationEnds.get(i);
            final Entity entity = (Entity)associationEnd.getType();

            if(entity.isCompositeIdentifier())
            {
                if (buffer.length() > 0)
                {
                    buffer.append(", ");
                }
                if (listType != null)
                {
                    buffer.append("Object");
                    if (associationEnd.isMany())
                    {
                        buffer.append("[]");
                    }
                    buffer.append(' ');
                }
                buffer.append(associationEnd.getName());
            }
            else
            {
                final Iterator identifierIterator = entity.getIdentifiers().iterator();
                if (identifierIterator.hasNext())
                {
                    final AttributeFacade identifier = (AttributeFacade)identifierIterator.next();
                    if (identifier != null)
                    {
                        if (buffer.length() > 0)
                        {
                            buffer.append(", ");
                        }

                        final ClassifierFacade type = identifier.getType();
                        if (type != null)
                        {
                            if (listType != null)
                            {
                                buffer.append(type.getFullyQualifiedName());
                                if (associationEnd.isMany())
                                {
                                    buffer.append("[]");
                                }
                                buffer.append(' ');
                            }
                            buffer.append(associationEnd.getName());
                        }
                    }
                }
            }
        }

        return buffer.toString();
    }

    /**
     * @see org.andromda.metafacades.uml14.ManageableEntityLogic#handleListManageableMembersWithWrapperTypes()
     */
    protected String handleListManageableMembersWithWrapperTypes()
    {
        return createListWithManageableMembers(ListType.WRAPPER);
    }

    /**
     * @see org.andromda.metafacades.uml14.ManageableEntityLogic#handleListManageableMembers(boolean)
     */
    protected String handleListManageableMembers(boolean withTypes)
    {
        return createListWithManageableMembers(withTypes? ListType.PRIMITIVE: null);
    }

    /**
     * @see org.andromda.metafacades.uml14.ManageableEntityLogic#handleIsManageable()
     */
    protected boolean handleIsManageable()
    {
        return Boolean.valueOf((String)this.getConfiguredProperty(
            UMLMetafacadeProperties.ENABLE_MANAGEABLE_ENTITIES)).booleanValue();
    }

    /**
     * @see org.andromda.metafacades.uml14.ManageableEntityLogic#handleGetReferencingManageables()
     */
    protected List handleGetReferencingManageables()
    {
        final Set referencingManageables = new LinkedHashSet();
        final Collection associationEnds = getAssociationEnds();
        for (final Iterator associationEndIterator = associationEnds.iterator(); associationEndIterator.hasNext();)
        {
            final AssociationEndFacade associationEnd = (AssociationEndFacade)associationEndIterator.next();

            if (associationEnd.isNavigable())
            {
                if (associationEnd.isMany() || (associationEnd.isOne2One() && associationEnd.isChild()))
                {
                    final Object otherEndType = associationEnd.getOtherEnd().getType();
                    if (otherEndType instanceof Entity)
                    {
                        referencingManageables.add(otherEndType);
                    }
                }
            }
        }
        return new ArrayList(referencingManageables);
    }

    /**
     * @see org.andromda.metafacades.uml14.ManageableEntityLogic#handleGetDisplayAttribute()
     */
    protected Object handleGetDisplayAttribute()
    {
        Object displayAttribute = null;

        final Object taggedValueObject = findTaggedValue(UMLProfile.TAGGEDVALUE_MANAGEABLE_DISPLAY_NAME);
        if (taggedValueObject != null)
        {
            displayAttribute = findAttribute(StringUtilsHelper.trimToEmpty(taggedValueObject.toString()));
        }

        final Collection attributes = getAttributes(true);
        for (final Iterator attributeIterator = attributes.iterator();
            attributeIterator.hasNext() && displayAttribute == null;)
        {
            // TODO: UML2 migrated models automatically mark all * attributes as unique. Different display attributes are selected from UML14 and UML2 migrated models.
            // This selects the first attribute that is unique as the display value.
            final Object attribute = attributeIterator.next();
            if(attribute instanceof EntityAttribute)//can get attributes from ancestor classes
            {
                final EntityAttribute entityAttribute = (EntityAttribute)attribute;
                if (entityAttribute.isUnique())
                {
                    displayAttribute = entityAttribute;
                }
            }
        }

        if (displayAttribute == null)
        {
            if (!getIdentifiers().isEmpty())
            {
                displayAttribute = getIdentifiers().iterator().next();
            }
            else if (!attributes.isEmpty())
            {
                displayAttribute = attributes.iterator().next();
            }
        }

        return displayAttribute;
    }

    /**
     * @see org.andromda.metafacades.uml14.ManageableEntityLogic#handleGetUsers()
     */
    protected List handleGetUsers()
    {
        final Set users = new LinkedHashSet();

        final Collection<DependencyFacade> dependencies = getTargetDependencies();
        for (final Iterator<DependencyFacade> dependencyIterator = dependencies.iterator(); dependencyIterator.hasNext();)
        {
            final DependencyFacade dependency = dependencyIterator.next();
            final Object dependencyObject = dependency.getSourceElement();

            if (!users.contains(dependencyObject) && dependencyObject instanceof ActorFacade)
            {
                collectActors((ActorFacade)dependencyObject, users);
            }
        }

        return new ArrayList(users);
    }

    private void collectActors(ActorFacade actor, Collection actors)
    {
        if (!actors.contains(actor))
        {
            actors.add(actor);

            final Collection<ActorFacade> childActors = actor.getGeneralizedByActors();
            for (final Iterator<ActorFacade> iterator = childActors.iterator(); iterator.hasNext();)
            {
                final ActorFacade childActor = iterator.next();
                collectActors(childActor, actors);
            }
        }
    }

    /**
     * @see org.andromda.metafacades.uml14.ManageableEntityLogic#handleGetMaximumListSize()
     */
    protected int handleGetMaximumListSize()
    {
        int maximumListSize;

        final Object taggedValueObject = findTaggedValue(UMLProfile.TAGGEDVALUE_MANAGEABLE_MAXIMUM_LIST_SIZE);
        if (taggedValueObject != null)
        {
            try
            {
                maximumListSize = Integer.parseInt(taggedValueObject.toString());
            }
            catch (NumberFormatException e)
            {
                maximumListSize = internalDefaultMaximumListSize();
            }
        }
        else
        {
            maximumListSize = internalDefaultMaximumListSize();
        }

        return maximumListSize;
    }

    private int internalDefaultMaximumListSize()
    {
        int maximumListSize;

        try
        {
            maximumListSize =
                Integer.parseInt((String)getConfiguredProperty(UMLMetafacadeProperties.PROPERTY_DEFAULT_MAX_LIST_SIZE));
        }
        catch (NumberFormatException e1)
        {
            maximumListSize = -1;
        }

        return maximumListSize;
    }

    /**
     * @see org.andromda.metafacades.uml14.ManageableEntityLogic#handleGetPageSize()
     */
    protected int handleGetPageSize()
    {
        int pageSize;

        final Object taggedValueObject = findTaggedValue(UMLProfile.TAGGEDVALUE_MANAGEABLE_PAGE_SIZE);
        if (taggedValueObject != null)
        {
            try
            {
                pageSize = Integer.parseInt(taggedValueObject.toString());
            }
            catch (NumberFormatException e)
            {
                pageSize = internalDefaultPageSize();
            }
        }
        else
        {
            pageSize = internalDefaultPageSize();
        }

        return pageSize;
    }

    private int internalDefaultPageSize()
    {
        int pageSize;

        try
        {
            pageSize =
                Integer.parseInt((String)getConfiguredProperty(UMLMetafacadeProperties.PROPERTY_DEFAULT_PAGE_SIZE));
        }
        catch (NumberFormatException e1)
        {
            pageSize = 20;
        }

        return pageSize;
    }

    /**
     * @see org.andromda.metafacades.uml14.ManageableEntityLogic#handleIsResolveable()
     */
    protected boolean handleIsResolveable()
    {
        boolean resolveable;

        final Object taggedValueObject = findTaggedValue(UMLProfile.TAGGEDVALUE_MANAGEABLE_RESOLVEABLE);
        if (taggedValueObject != null)
        {
            try
            {
                resolveable = Boolean.valueOf(taggedValueObject.toString()).booleanValue();
            }
            catch (NumberFormatException e)
            {
                resolveable = internalDefaultResolveable();
            }
        }
        else
        {
            resolveable = internalDefaultResolveable();
        }

        return resolveable;
    }

    private boolean internalDefaultResolveable()
    {
        boolean resolveable;

        try
        {
            resolveable =
                Boolean.valueOf((String)getConfiguredProperty(UMLMetafacadeProperties.PROPERTY_DEFAULT_RESOLVEABLE))
                       .booleanValue();
        }
        catch (NumberFormatException ex)
        {
            resolveable = true;
        }

        return resolveable;
    }

    /**
     * @see org.andromda.metafacades.uml14.ManageableEntityLogic#handleGetAllManageables()
     */
    protected List<ClassifierFacade> handleGetAllManageables()
    {
        final Set<ClassifierFacade> allManageableEntities = new TreeSet(new ManageableComparator());

        final Collection<ClassifierFacade> allClasses = getModel().getAllClasses();
        for (final Iterator<ClassifierFacade> classIterator = allClasses.iterator(); classIterator.hasNext();)
        {
            final ClassifierFacade classObject = classIterator.next();
            if (classObject instanceof ManageableEntity)
            {
                allManageableEntities.add(classObject);
            }
        }

        return new ArrayList<ClassifierFacade>(allManageableEntities);
    }

    /**
     *
     */
    final static class ManageableComparator
        implements Comparator
    {
        /**
         * @see java.util.Comparator#compare(Object, Object)
         */
        public int compare(Object left, Object right)
        {
            final ModelElementFacade leftEntity = (ModelElementFacade)left;
            final ModelElementFacade rightEntity = (ModelElementFacade)right;

            return leftEntity.getName().compareTo(rightEntity.getName());
        }
    }
}