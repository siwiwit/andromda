package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.HashSet;

import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.EntityFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.RoleFacade;
import org.andromda.metafacades.uml.ServiceFacade;
import org.andromda.metafacades.uml.ServiceOperationFacade;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

/**
 * Metaclass facade implementation.
 */
public class ServiceFacadeLogicImpl
    extends ServiceFacadeLogic
{
    // ---------------- constructor -------------------------------

    public ServiceFacadeLogicImpl(
        java.lang.Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ServiceFacade#getEntityReferences()
     */
    public Collection handleGetEntityReferences()
    {
        return new FilteredCollection(this.getSourceDependencies())
        {
            public boolean evaluate(Object object)
            {
                ModelElementFacade targetElement = ((DependencyFacade)object)
                    .getTargetElement();
                return targetElement != null
                    && EntityFacade.class.isAssignableFrom(targetElement
                        .getClass());
            }
        };
    }

    /**
     * @see org.andromda.metafacades.uml.ServiceFacade#getServiceReferences()
     */
    public Collection handleGetServiceReferences()
    {
        return new FilteredCollection(this.getSourceDependencies())
        {
            public boolean evaluate(Object object)
            {
                ModelElementFacade targetElement = ((DependencyFacade)object)
                    .getTargetElement();
                return targetElement != null
                    && ServiceFacade.class.isAssignableFrom(targetElement
                        .getClass());
            }
        };
    }

    /**
     * @see org.andromda.metafacades.uml.ServiceFacade#getRoles()
     */
    protected Collection handleGetRoles()
    {
        Collection roles = this.getTargetDependencies();
        CollectionUtils.filter(roles, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                DependencyFacade dependency = (DependencyFacade)object;
                return dependency != null
                    && dependency.getSourceElement() instanceof RoleFacade;
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
                allRoles.addAll(((RoleFacade)object).getSpecializations());
            }
        });
        return allRoles;
    }

    /**
     * @see org.andromda.metafacades.uml.ServiceFacade#getAllRoles()
     */
    protected Collection handleGetAllRoles()
    {
        final Collection roles = new HashSet(this.getRoles());
        CollectionUtils.forAllDo(this.getOperations(), new Closure()
        {
            public void execute(Object object)
            {
                if (object instanceof ServiceOperationFacade)
                {
                    roles.addAll(((ServiceOperationFacade)object).getRoles());
                }
            }
        });
        return roles;
    }
}