package org.andromda.core.metadecorators.uml14;

import org.omg.uml.foundation.datatypes.PseudostateKindEnum;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.behavioralelements.statemachines.Pseudostate
 *
 *
 */
public class PseudostateDecoratorImpl extends PseudostateDecorator
{
    // ---------------- constructor -------------------------------

    public PseudostateDecoratorImpl(org.omg.uml.behavioralelements.statemachines.Pseudostate metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class PseudostateDecorator ...

    public boolean isChoice()
    {
        return PseudostateKindEnum.PK_CHOICE.equals(metaObject.getKind());
    }

    public boolean  isInitialState()
    {
        return PseudostateKindEnum.PK_INITIAL.equals(metaObject.getKind());
    }

    public boolean isJoin()
    {
        return PseudostateKindEnum.PK_JOIN.equals(metaObject.getKind());
    }

    public boolean isDeepHistory()
    {
        return PseudostateKindEnum.PK_DEEP_HISTORY.equals(metaObject.getKind());
    }

    public boolean isFork()
    {
        return PseudostateKindEnum.PK_FORK.equals(metaObject.getKind());
    }

    public boolean isJunction()
    {
        return PseudostateKindEnum.PK_JUNCTION.equals(metaObject.getKind());
    }

    public boolean isShallowHistory()
    {
        return PseudostateKindEnum.PK_SHALLOW_HISTORY.equals(metaObject.getKind());
    }

    public boolean isDecisionPoint()
    {
        boolean isDecisionPoint = false;

        if (isChoice() || isJunction())
        {
            isDecisionPoint = true;
            isDecisionPoint = isDecisionPoint && (metaObject.getOutgoing().size() > 1);
        }

        return isDecisionPoint;
    }

    public boolean isMergePoint()
    {
        boolean isMergePoint = false;

        if (isChoice() || isJoin())
        {
            isMergePoint = true;
            isMergePoint = isMergePoint && (metaObject.getIncoming().size() > 1);
            isMergePoint = isMergePoint && (metaObject.getOutgoing().size() == 1);
        }

        return isMergePoint;
    }

    // ------------- relations ------------------
}
