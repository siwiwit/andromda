package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.metadecorators.MetaDecoratorUtil;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.metadecorators.uml14.DecoratorValidationException;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.statemachines.Pseudostate
 *
 *
 */
public class StrutsPseudostateDecoratorImpl extends StrutsPseudostateDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsPseudostateDecoratorImpl (org.omg.uml.behavioralelements.statemachines.Pseudostate metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsPseudostateDecorator ...

    public String getImplementationMethodName()
    {
        return MetaDecoratorUtil.toJavaMethodName(getName()) + Bpm4StrutsProfile.DEFAULT_IMPLEMENTATION_METHOD_SUFFIX;
    }

    public String getAbstractMethodName()
    {
        return MetaDecoratorUtil.toJavaMethodName(getName()) + Bpm4StrutsProfile.DEFAULT_ABSTRACT_METHOD_SUFFIX;
    }
    // ------------- relations ------------------

    public void validate() throws DecoratorValidationException
    {
        // the name must not be empty
        final String name = getName();
        if ( (name==null) || (name.trim().length()==0) )
            throw new DecoratorValidationException(this, "Name may not be empty or only contain whitespace");

        // the name must be unique for pseudo states in the use-case
    }

}
