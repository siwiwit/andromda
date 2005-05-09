package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.Role;
import org.andromda.metafacades.uml.Service;
import org.andromda.metafacades.uml.ServiceOperation;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import java.util.Collection;
import java.util.HashSet;

/**
 * Metaclass facade implementation.
 */
public class ServiceLogicImpl
        extends ServiceLogic
{
    // ---------------- constructor -------------------------------

    public ServiceLogicImpl(java.lang.Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.Service#getEntityReferences()
     */
    public Collection handleGetEntityReferences()
    {
        return new FilteredCollection(this.getSourceDependencies())
        {
            public boolean evaluate(Object object)
            {
                ModelElementFacade targetElement = ((DependencyFacade)object).getTargetElement();
                return targetElement != null && Entity.class.isAssignableFrom(targetElement.getClass());
            }
        };
    }

    /**
     * @see org.andromda.metafacades.uml.Service#getServiceReferences()
     */
    public Collection handleGetServiceReferences()
    {
        return new FilteredCollection(this.getSourceDependencies())
        {
            public boolean evaluate(Object object)
            {
                ModelElementFacade targetElement = ((DependencyFacade)object).getTargetElement();
                return targetElement != null && Service.class.isAssignableFrom(targetElement.getClass());
            }
        };
    }

    /**
     * @see org.andromda.metafacades.uml.Service#getRoles()
     */
    protected Collection handleGetRoles()
    {
        Collection roles = this.getTargetDependencies();
        CollectionUtils.filter(roles, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                DependencyFacade dependency = (DependencyFacade)object;
                return dependency != null && dependency.getSourceElement() instanceof Role;
            }
        });
        CollectionUtils.transform(roles, new Transformer()
        {
            public Object transform(Object object)
            {
                return ((DependencyFacade)object).getSourceElement();
            }
        });
        final Collection allRoles = new HashSet(roles);
        // add all roles which are specializations of this one
        CollectionUtils.forAllDo(roles, new Closure()
        {
            public void execute(Object object)
            {
                allRoles.addAll(((Role)object).getSpecializations());
            }
        });
        return allRoles;
    }

    /**
     * @see org.andromda.metafacades.uml.Service#getAllRoles()
     */
    protected Collection handleGetAllRoles()
    {
        final Collection roles = new HashSet(this.getRoles());
        CollectionUtils.forAllDo(this.getOperations(), new Closure()
        {
            public void execute(Object object)
            {
                if (object instanceof ServiceOperation)
                {
                    roles.addAll(((ServiceOperation)object).getRoles());
                }
            }
        });
        return roles;
    }
}