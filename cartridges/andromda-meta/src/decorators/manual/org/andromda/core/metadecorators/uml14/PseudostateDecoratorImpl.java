package org.andromda.core.metadecorators.uml14;

import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.foundation.core.ModelElement;
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

    public java.lang.Boolean isChoice()
    {
        return (PseudostateKindEnum.PK_CHOICE.equals(metaObject.getKind())) ? Boolean.TRUE : Boolean.FALSE;
    }

    public java.lang.Boolean isInitialState()
    {
        return (PseudostateKindEnum.PK_INITIAL.equals(metaObject.getKind())) ? Boolean.TRUE : Boolean.FALSE;
    }

    public java.lang.Boolean isJoin()
    {
        return (PseudostateKindEnum.PK_JOIN.equals(metaObject.getKind())) ? Boolean.TRUE : Boolean.FALSE;
    }

    public java.lang.Boolean isDeepHistory()
    {
        return (PseudostateKindEnum.PK_DEEP_HISTORY.equals(metaObject.getKind())) ? Boolean.TRUE : Boolean.FALSE;
    }

    public java.lang.Boolean isFork()
    {
        return (PseudostateKindEnum.PK_FORK.equals(metaObject.getKind())) ? Boolean.TRUE : Boolean.FALSE;
    }

    public java.lang.Boolean isJunction()
    {
        return (PseudostateKindEnum.PK_JUNCTION.equals(metaObject.getKind())) ? Boolean.TRUE : Boolean.FALSE;
    }

    public java.lang.Boolean isShallowHistory()
    {
        return (PseudostateKindEnum.PK_SHALLOW_HISTORY.equals(metaObject.getKind())) ? Boolean.TRUE : Boolean.FALSE;
    }

    public java.lang.Boolean isDecisionPoint()
    {
        boolean isDecisionPoint = false;

        if (isChoice().booleanValue() || isFork().booleanValue())
        {
            isDecisionPoint = true;
            isDecisionPoint = isDecisionPoint && (metaObject.getIncoming().size() == 1);
            isDecisionPoint = isDecisionPoint && (metaObject.getOutgoing().size() > 1);
        }

        return (isDecisionPoint) ? Boolean.TRUE : Boolean.FALSE;
    }

    public java.lang.Boolean isMergePoint()
    {
        boolean isMergePoint = false;

        if (isChoice().booleanValue() || isJoin().booleanValue())
        {
            isMergePoint = true;
            isMergePoint = isMergePoint && (metaObject.getIncoming().size() > 1);
            isMergePoint = isMergePoint && (metaObject.getOutgoing().size() == 1);
        }

        return (isMergePoint) ? Boolean.TRUE : Boolean.FALSE;
    }

    // ------------- relations ------------------

    protected ModelElement handleGetChoiceStateMachine()
    {
        StateMachine stateMachine = null;
        CompositeState compositeState = getContainer();

        if (compositeState != null)
        {
            while (compositeState != null)
            {
                stateMachine = compositeState.getStateMachine();
                compositeState = compositeState.getContainer();
            }
        }
        else
        {
            stateMachine = compositeState.getStateMachine();
        }

        return stateMachine;
    }

    protected ModelElement handleGetInitialStateMachine()
    {
        return handleGetChoiceStateMachine();
    }
}
