package org.andromda.metafacades.emf.uml22;

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
import org.andromda.metafacades.uml.ManageableEntityAssociationEnd;
import org.andromda.metafacades.uml.ManageableEntityAttribute;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.ManageableEntity.
 *
 * @see org.andromda.metafacades.uml.ManageableEntity
 */
public class ManageableEntityLogicImpl
    extends ManageableEntityLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public ManageableEntityLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @return the configured property denoting the character sequence to use
     *         for the separation of namespaces
     */
    private String getNamespaceSeparator()
    {
        return (String)this.getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR);
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntity#getManageablePackageName()
     */
    @Override
    protected String handleGetManageablePackageName()
    {
        String manageablePackageName = "";

        final String parentPackage = super.getPackageName();
        if (StringUtils.isNotBlank(parentPackage))
        {
            manageablePackageName = parentPackage;
        }

        final Object suffix = this.getConfiguredProperty(UMLMetafacadeProperties.MANAGEABLE_PACKAGE_NAME_SUFFIX);
        if (suffix != null && StringUtils.isNotBlank(suffix.toString()))
        {
            manageablePackageName += this.getNamespaceSeparator() + suffix;
        }

        return manageablePackageName;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ManageableEntityLogic#handleGetManageablePackagePath()
     */
    @Override
    protected String handleGetManageablePackagePath()
    {
        return StringUtils.replace(
            this.getManageablePackageName(),
            this.getNamespaceSeparator(),
            "/");
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ManageableEntityLogic#handleGetManageableAssociationEnds()
     */
    @Override
    protected List<AssociationEndFacade> handleGetManageableAssociationEnds()
    {
        final Set<AssociationEndFacade> manageableAssociationEnds = new LinkedHashSet<AssociationEndFacade>(); // linked

        // hashset
        // to
        // guarantee
        // ordering
        // wo/
        // duplicates
        collectAssociationEnds(
            manageableAssociationEnds,
            this);

        return new ArrayList<AssociationEndFacade>(manageableAssociationEnds);
    }

    /**
     * This method recursively collects all association ends to which a
     * manageable entity would need to navigate, adds to manageableAssociationEnds
     *
     * @param manageableAssociationEnds
     *            the collection in which to collect the association ends
     * @param entity
     *            the entity from which to recursively gather the association
     *            ends
     */
    private static void collectAssociationEnds(
        final Collection<AssociationEndFacade> manageableAssociationEnds,
        final ManageableEntity entity)
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
                collectAssociationEnds(
                    manageableAssociationEnds,
                    (ManageableEntity)parentEntityObject);
            }
        }
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntity#isCreate()
     */
    @Override
    protected boolean handleIsCreate()
    {
        return !this.isAbstract();
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ManageableEntityLogic#handleGetManageableServiceName()
     */
    @Override
    protected String handleGetManageableServiceName()
    {
        return this.handleGetName() + "ManageableService";
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ManageableEntityLogic#handleGetManageableServiceFullPath()
     */
    @Override
    protected String handleGetManageableServiceFullPath()
    {
        return '/' +
        StringUtils.replace(
            this.getFullyQualifiedManageableServiceName(),
            this.getNamespaceSeparator(),
            "/");
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ManageableEntityLogic#handleGetFullyQualifiedManageableServiceName()
     */
    @Override
    protected String handleGetFullyQualifiedManageableServiceName()
    {
        return this.getManageablePackageName() + this.getNamespaceSeparator() + this.getManageableServiceName();
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ManageableEntityLogic#handleGetManageableServiceAccessorCall()
     */
    @Override
    protected String handleGetManageableServiceAccessorCall()
    {
        final String property = UMLMetafacadeProperties.MANAGEABLE_SERVICE_ACCESSOR_PATTERN;
        final String accessorImplementation =
            this.isConfiguredProperty(property) ? ObjectUtils.toString(this.getConfiguredProperty(property)) 
                : "${application.package}.ManageableServiceLocator.instance().get{1}()";
        return accessorImplementation.replaceAll(
            "\\{0\\}",
            this.getManageablePackageName()).replaceAll(
            "\\{1\\}",
            this.getManageableServiceName());
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ManageableEntityLogic#handleIsRead()
     */
    @Override
    protected boolean handleIsRead()
    {
        return true;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ManageableEntityLogic#handleIsUpdate()
     */
    @Override
    protected boolean handleIsUpdate()
    {
        return this.getManageableIdentifier() != null; // TODO implement isUpdate
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ManageableEntityLogic#handleIsDelete()
     */
    @Override
    protected boolean handleIsDelete()
    {
        return this.getManageableIdentifier() != null; // TODO implement isDelete
    }

    /**)
     * @see org.andromda.metafacades.emf.uml22.ManageableEntityLogic#handleGetManageableAttributes()
     */
    @Override
    protected List<AttributeFacade> handleGetManageableAttributes()
    {
        List<AttributeFacade> attList = new ArrayList<AttributeFacade>(this.getAttributes(true));
        return attList;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ManageableEntityLogic#handleGetManageableIdentifier()
     */
    @Override
    protected Object handleGetManageableIdentifier()
    {
        return this.getIdentifiers(true).iterator().next();
    }

    /**
     * Contains both Attributes and AssociationEnds
     * @see org.andromda.metafacades.emf.uml22.ManageableEntityLogic#handleGetManageableMembers()
     */
    @Override
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

        for (ManageableEntityAttribute attribute : this.getManageableAttributes())
        {
            if (buffer.length() > 0)
            {
                buffer.append(", ");
            }

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

        for (ManageableEntityAssociationEnd associationEnd : this.getManageableAssociationEnds())
        {
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
                for (EntityAttribute identifier : entity.getIdentifiers())
                {
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
     * @see org.andromda.metafacades.emf.uml22.ManageableEntityLogic#handleListManageableMembersWithWrapperTypes()
     */
    @Override
    protected String handleListManageableMembersWithWrapperTypes()
    {
        return createListWithManageableMembers(ListType.WRAPPER);
    }
    
    /**
     * @see org.andromda.metafacades.emf.uml22.ManageableEntityLogic#handleListManageableMembers(boolean)
     */
    @Override
    protected String handleListManageableMembers(final boolean withTypes)
    {
        return createListWithManageableMembers(withTypes? ListType.PRIMITIVE: null);
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ManageableEntityLogic#handleIsManageable()
     */
    @Override
    protected boolean handleIsManageable()
    {
        return Boolean.valueOf((String) this.getConfiguredProperty(UMLMetafacadeProperties.ENABLE_MANAGEABLE_ENTITIES));
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ManageableEntityLogic#handleGetReferencingManageables()
     */
    @Override
    protected List<ClassifierFacade> handleGetReferencingManageables()
    {
        final Set<ClassifierFacade> referencingManageables = new LinkedHashSet();
        final Collection<AssociationEndFacade> associationEnds = this.getAssociationEnds();
        for (final Iterator<AssociationEndFacade> associationEndIterator = associationEnds.iterator(); associationEndIterator.hasNext();)
        {
            final AssociationEndFacade associationEnd = associationEndIterator.next();

            if (associationEnd.isNavigable())
            {
                if (associationEnd.isMany() || (associationEnd.isOne2One() && associationEnd.isChild()))
                {
                    final Object otherEndType = associationEnd.getOtherEnd().getType();
                    if (otherEndType instanceof Entity)
                    {
                        referencingManageables.add((ClassifierFacade) otherEndType);
                    }
                }
            }
        }
        return new ArrayList<ClassifierFacade>(referencingManageables);
    }

    /**
     * Returns the value of the 'andromda_manageable_table_displayname' or the first unique attribute, or the identifier column.
     * @see org.andromda.metafacades.emf.uml22.ManageableEntityLogic#handleGetDisplayAttribute()
     */
    @Override
    protected AttributeFacade handleGetDisplayAttribute()
    {
        AttributeFacade displayAttribute = null;

        final Object taggedValueObject = this.findTaggedValue(UMLProfile.TAGGEDVALUE_MANAGEABLE_DISPLAY_NAME);
        if (taggedValueObject != null)
        {
            displayAttribute = this.findAttribute(StringUtils.trimToEmpty(taggedValueObject.toString()));
        }

        final Collection<AttributeFacade> attributes = this.getAttributes(true);
        for (final Iterator<AttributeFacade> attributeIterator = attributes.iterator();
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
            if (!this.getIdentifiers().isEmpty())
            {
                displayAttribute = this.getIdentifiers().iterator().next();
            }
            else if (!attributes.isEmpty())
            {
                displayAttribute = attributes.iterator().next();
            }
        }

        return displayAttribute;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ManageableEntityLogic#handleGetUsers()
     */
    @Override
    protected List<ActorFacade> handleGetUsers()
    {
        final Set<ActorFacade> users = new LinkedHashSet();

        final Collection<DependencyFacade> dependencies = this.getTargetDependencies();
        for (final Iterator<DependencyFacade> dependencyIterator = dependencies.iterator(); dependencyIterator.hasNext();)
        {
            final DependencyFacade dependency = dependencyIterator.next();
            final Object dependencyObject = dependency.getSourceElement();

            if (!users.contains(dependencyObject) && dependencyObject instanceof ActorFacade)
            {
                this.collectActors(
                    (ActorFacade)dependencyObject,
                    users);
            }
        }

        return new ArrayList<ActorFacade>(users);
    }

    private void collectActors(
        final ActorFacade actor,
        final Collection<ActorFacade> actors)
    {
        if (!actors.contains(actor))
        {
            actors.add(actor);

            final Collection<ActorFacade> childActors = actor.getGeneralizedByActors();
            for (final Iterator<ActorFacade> iterator = childActors.iterator(); iterator.hasNext();)
            {
                final ActorFacade childActor = iterator.next();
                this.collectActors(
                    childActor,
                    actors);
            }
        }
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ManageableEntityLogic#handleGetMaximumListSize()
     */
    @Override
    protected int handleGetMaximumListSize()
    {
        int maximumListSize;

        final Object taggedValueObject = this.findTaggedValue(UMLProfile.TAGGEDVALUE_MANAGEABLE_MAXIMUM_LIST_SIZE);
        if (taggedValueObject != null)
        {
            try
            {
                maximumListSize = Integer.parseInt(taggedValueObject.toString());
            }
            catch (NumberFormatException e)
            {
                maximumListSize = this.internalDefaultMaximumListSize();
            }
        }
        else
        {
            maximumListSize = this.internalDefaultMaximumListSize();
        }

        return maximumListSize;
    }

    private int internalDefaultMaximumListSize()
    {
        int maximumListSize;

        try
        {
            maximumListSize =
                Integer.parseInt(
                    (String)this.getConfiguredProperty(UMLMetafacadeProperties.PROPERTY_DEFAULT_MAX_LIST_SIZE));
        }
        catch (NumberFormatException e1)
        {
            maximumListSize = -1;
        }

        return maximumListSize;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ManageableEntityLogic#handleGetPageSize()
     */
    @Override
    protected int handleGetPageSize()
    {
        int pageSize;

        final Object taggedValueObject = this.findTaggedValue(UMLProfile.TAGGEDVALUE_MANAGEABLE_PAGE_SIZE);
        if (taggedValueObject != null)
        {
            try
            {
                pageSize = Integer.parseInt(taggedValueObject.toString());
            }
            catch (NumberFormatException e)
            {
                pageSize = this.internalDefaultPageSize();
            }
        }
        else
        {
            pageSize = this.internalDefaultPageSize();
        }

        return pageSize;
    }

    private int internalDefaultPageSize()
    {
        int pageSize;

        try
        {
            pageSize =
                Integer.parseInt(
                    (String)this.getConfiguredProperty(UMLMetafacadeProperties.PROPERTY_DEFAULT_PAGE_SIZE));
        }
        catch (NumberFormatException e1)
        {
            pageSize = 20;
        }

        return pageSize;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ManageableEntityLogic#handleIsResolveable()
     */
    @Override
    protected boolean handleIsResolveable()
    {
        boolean resolveable;

        final Object taggedValueObject = this.findTaggedValue(UMLProfile.TAGGEDVALUE_MANAGEABLE_RESOLVEABLE);
        if (taggedValueObject != null)
        {
            try
            {
                resolveable = Boolean.valueOf(taggedValueObject.toString()).booleanValue();
            }
            catch (NumberFormatException e)
            {
                resolveable = this.internalDefaultResolveable();
            }
        }
        else
        {
            resolveable = this.internalDefaultResolveable();
        }

        return resolveable;
    }

    private boolean internalDefaultResolveable()
    {
        boolean resolveable;

        try
        {
            resolveable =
                    Boolean.valueOf(
                            (String) this.getConfiguredProperty(UMLMetafacadeProperties.PROPERTY_DEFAULT_RESOLVEABLE)).booleanValue();
        }
        catch (NumberFormatException ex)
        {
            resolveable = true;
        }

        return resolveable;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ManageableEntityLogic#handleGetAllManageables()
     */
    @Override
    protected List<ManageableEntity> handleGetAllManageables()
    {
        final Set<ManageableEntity> allManageableEntities = new TreeSet<ManageableEntity>(new ManageableComparator());

        final Collection<ClassifierFacade> allClasses = this.getModel().getAllClasses();
        for (final Iterator<ClassifierFacade> classIterator = allClasses.iterator(); classIterator.hasNext();)
        {
            final Object classObject = classIterator.next();
            if (classObject instanceof ManageableEntity)
            {
                allManageableEntities.add((ManageableEntity)classObject);
            }
        }
        return new ArrayList<ManageableEntity>(allManageableEntities);
    }

    /**
     */
    final static class ManageableComparator
        implements Comparator
    {
        /**
         * @see java.util.Comparator#compare(Object, Object)
         */
        public int compare(
            final Object left,
            final Object right)
        {
            final ModelElementFacade leftEntity = (ModelElementFacade)left;
            final ModelElementFacade rightEntity = (ModelElementFacade)right;

            return leftEntity.getName().compareTo(rightEntity.getName());
        }
    }
}
