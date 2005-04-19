package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.ManageableEntityAssociationEnd;
import org.andromda.metafacades.uml.ManageableEntityAttribute;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ManageableEntity.
 *
 * @see org.andromda.metafacades.uml.ManageableEntity
 */
public class ManageableEntityLogicImpl
        extends ManageableEntityLogic
{
    // ---------------- constructor -------------------------------

    public ManageableEntityLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntity#getManageablePackageName()
     */
    protected java.lang.String handleGetManageablePackageName()
    {
        String manageablePackageName = "";

        final String parentPackage = super.getPackageName();
        if (parentPackage != null && parentPackage.length() > 0)
        {
            manageablePackageName = parentPackage + ".";
        }

        return manageablePackageName += "manageable";
    }

    protected String handleGetManageablePackagePath()
    {
        return getManageablePackageName().replace('.', '/');
    }

    protected java.util.Collection handleGetManageableAssociationEnds()
    {
        final Collection manageableAssociationEnds = new ArrayList();

        final Collection associationEnds = getAssociationEnds();
        for (Iterator associationEndIterator = associationEnds.iterator(); associationEndIterator.hasNext();)
        {
            final AssociationEndFacade associationEnd = (AssociationEndFacade)associationEndIterator.next();
            if (associationEnd.isMany2One())
            {
                final AssociationEndFacade otherEnd = associationEnd.getOtherEnd();
                final Object otherEndType = otherEnd.getType();
                if (otherEndType instanceof Entity)
                {
                    manageableAssociationEnds.add(otherEnd);
                }
            }
        }

        return manageableAssociationEnds;
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntity#isCreate()
     */
    protected boolean handleIsCreate()
    {
        return true;
    }

    protected String handleGetManageableServiceName()
    {
        return getName() + "ManageableService";
    }

    protected String handleGetManageableServiceFullPath()
    {
        return '/' + getFullyQualifiedManageableServiceName().replace('.', '/');
    }

    protected String handleGetFullyQualifiedManageableServiceName()
    {
        return getManageablePackageName() + '.' + getManageableServiceName();
    }

    protected String handleGetManageableServiceAccessorCall()
    {
        final String accessorImplementation = String.valueOf(
                getConfiguredProperty(UMLMetafacadeProperties.CRUD_SERVICE_ACCESSOR_PATTERN));
        return accessorImplementation.replaceAll("\\{0\\}", getManageablePackageName()).replaceAll("\\{1\\}",
                getManageableServiceName());
    }

    protected boolean handleIsRead()
    {
        return true;
    }

    protected boolean handleIsUpdate()
    {
        return false; // @todo
    }

    protected boolean handleIsDelete()
    {
        return false; // @todo
    }

    protected List handleGetManageableMembers()
    {
        final List criteria = new ArrayList();
        criteria.addAll(getAttributes());
        criteria.addAll(getManageableAssociationEnds());
        return criteria;
    }

    protected String handleListManageableMembers(boolean withTypes, boolean useCollectionTypes)
    {
        final StringBuffer buffer = new StringBuffer();

        final Collection attributes = getAttributes();
        for (Iterator attributeIterator = attributes.iterator(); attributeIterator.hasNext();)
        {
            if (buffer.length() > 0)
            {
                buffer.append(", ");
            }

            final ManageableEntityAttribute attribute = (ManageableEntityAttribute)attributeIterator.next();
            final ClassifierFacade type = attribute.getType();
            if (type != null)
            {
                if (withTypes)
                {
                    if (useCollectionTypes)
                    {
                        buffer.append("java.util.Collection");
                    }
                    else
                    {
                        buffer.append(type.getFullyQualifiedName());
                    }
                    buffer.append(' ');
                }
                buffer.append(attribute.getManageableName());
            }
        }

        final Collection associationEnds = getManageableAssociationEnds();
        for (Iterator associationEndIterator = associationEnds.iterator(); associationEndIterator.hasNext();)
        {
            final ManageableEntityAssociationEnd associationEnd = (ManageableEntityAssociationEnd)associationEndIterator.next();
            final Entity entity = (Entity)associationEnd.getType();

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
                        if (withTypes)
                        {
                            if (useCollectionTypes)
                            {
                                buffer.append("java.util.Collection");
                            }
                            else
                            {
                                buffer.append(type.getFullyQualifiedName());
                            }
                            buffer.append(' ');
                        }
                        buffer.append(associationEnd.getManageableName());
                    }
                }
            }
        }

        return buffer.toString();
    }
}
