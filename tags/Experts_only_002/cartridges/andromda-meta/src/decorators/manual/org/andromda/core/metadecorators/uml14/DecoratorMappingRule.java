package org.andromda.core.metadecorators.uml14;

/**
 * This rule describes a mapping from a context name and a stereotype name
 * to the fully qualified class name of a decorator.
 * 
 * An example for a context is: An entity is a context for its attributes.
 * So, the metaclass "Attribute" would be mapped to "EntityAttributeDecorator"
 * if the Attribute belongs to an entity. If the Attribute belongs to any
 * other Classifier, it would be mapped to a simple "AttributeDecorator".
 * 
 * See also the registration of decorators within the DecoratorFactory class.
 * 
 * @see org.andromda.core.metadecorators.uml14.DecoratorFactory
 * 
 * @since 31.01.2004
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
public class DecoratorMappingRule
{
    private String contextName;
    private String stereotypeName;
    private String decoratorTypeName;

    /**
     * Builds a rule from two criteria and one output.
     * 
     * @param contextName the context name criterium (may be null!)
     * @param stereotypeName the stereotype criterium (may be null!)
     * @param decoratorTypeName the resulting FQCN of the decorator class 
     */
    public DecoratorMappingRule(
        String contextName,
        String stereotypeName,
        String decoratorTypeName)
    {
        this.contextName = contextName;
        this.stereotypeName = stereotypeName;
        this.decoratorTypeName = decoratorTypeName;
    }

    /**
     * @return the FQCN of the decorator
     */
    public String getDecoratorTypeName()
    {
        return decoratorTypeName;
    }

    /**
     * Checks if this rule matches the given actual values.
     * 
     * @param actualContextName the actual context name of a model element (may be null!)
     * @param actualStereotypeName the actual stereotype name of a model element   (may be null!)
     * 
     * @return boolean match or not match
     */
    public boolean match(
        String actualContextName,
        String actualStereotypeName)
    {
        return internalMatch(actualContextName, contextName)
            && internalMatch(actualStereotypeName, stereotypeName);
    }

    private boolean internalMatch(String actual, String criterium)
    {
        if (criterium == null)
        {
            return true;
        }
        
        // Now, criterium is not null, so the rule matches only
        // if the actual value matches the criterium.
        if (actual == null)
        {
            return false;
        }
        
        return actual.equals(criterium);
    }
}
