package org.andromda.core.metadecorators.uml14;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple list of mapping rules for decorators. Features adding of rules
 * and searching for decorators.
 * 
 * @since 31.01.2004
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
public class DecoratorMappingRuleList
{
    private ArrayList theList = new ArrayList();

    /**
     * Adds another mapping rule to the list.
     * 
     * @param rule the rule
     */
    public void addRule(DecoratorMappingRule rule)
    {
        theList.add(rule);
    }

    /**
     * Finds a decorator, using the context name and the stereotype name
     * of the model element.
     * 
     * @param actualContextName
     * @param actualStereotypeName
     * @return
     */
    public String findDecorator(
        String actualContextName,
        String actualStereotypeName)
    {
        for (Iterator iter = theList.iterator(); iter.hasNext();)
        {
            DecoratorMappingRule rule =
                (DecoratorMappingRule) iter.next();
            if (rule.match(actualContextName, actualStereotypeName))
            {
                return rule.getDecoratorTypeName();
            }
        }
        return null;
    }
}
