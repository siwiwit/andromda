package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;

import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.RoleFacade;
import org.andromda.metafacades.uml.ServiceFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.ServiceOperationFacade.
 * 
 * @see org.andromda.metafacades.uml.ServiceOperationFacade
 */
public class ServiceOperationFacadeLogicImpl
    extends ServiceOperationFacadeLogic
    implements org.andromda.metafacades.uml.ServiceOperationFacade
{
    // ---------------- constructor -------------------------------

    public ServiceOperationFacadeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ServiceOperationFacade#getRoles()
     */
    public java.util.Collection handleGetRoles()
    {
        Collection roles = new ArrayList();
        if (this.getOwner() != null
            && ServiceFacade.class.isAssignableFrom(this.getOwner().getClass()))
        {
            roles.addAll(((ServiceFacade)this.getOwner()).getRoles());
        }
        Collection operationRoles = this.getSourceDependencies();
        CollectionUtils.filter(operationRoles, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                DependencyFacade dependency = (DependencyFacade)object;
                return dependency != null
                    && dependency.getSourceElement() != null
                    && RoleFacade.class.isAssignableFrom(dependency
                        .getSourceElement().getClass());
            }
        });
        CollectionUtils.transform(operationRoles, new Transformer()
        {
            public Object transform(Object object)
            {
                return ((DependencyFacade)object).getSourceElement();
            }
        });
        roles.addAll(operationRoles);
        return roles;
    }

}
