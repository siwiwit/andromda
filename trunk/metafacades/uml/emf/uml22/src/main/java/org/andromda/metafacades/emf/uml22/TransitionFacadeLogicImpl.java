package org.andromda.metafacades.emf.uml22;

import java.util.Collection;
import java.util.Iterator;
import org.andromda.metafacades.uml.ActionStateFacade;
import org.andromda.metafacades.uml.FinalStateFacade;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.eclipse.uml2.uml.Action;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.ConnectableElement;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Vertex;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.TransitionFacade.
 *
 * @see org.andromda.metafacades.uml.TransitionFacade
 */
public class TransitionFacadeLogicImpl
    extends TransitionFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public TransitionFacadeLogicImpl(
        final Transition metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.TransitionFacadeLogic#handleGetEffect()
     */
    @Override
    protected Action handleGetEffect()
    {
        // Effect is mapped to action, not activity
        // We return the first action encountered in the activity
        Action effectAction = null;
        Behavior effect = this.metaObject.getEffect();
        if (effect != null)
        {
            Collection<ConnectableElement> nodes = effect.getRoles();
            for (Iterator<ConnectableElement> nodesIt = nodes.iterator(); nodesIt.hasNext() && effectAction == null;)
            {
                Object nextNode = nodesIt.next();
                if (nextNode instanceof Action)
                {
                    effectAction = (Action)nextNode;
                }
            }
        }
        return effectAction;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.TransitionFacadeLogic#handleGetSource()
     */
    @Override
    protected Vertex handleGetSource()
    {
        return this.metaObject.getSource();
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.TransitionFacadeLogic#handleGetTarget()
     */
    @Override
    protected Vertex handleGetTarget()
    {
        return this.metaObject.getTarget();
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.TransitionFacadeLogic#handleGetTrigger()
     */
    @Override
    protected Behavior handleGetTrigger()
    {
        // We use the effect instead of trigger. It's the same "trick" as
        // using entry instead of deferrable events
        return this.metaObject.getEffect();
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.TransitionFacadeLogic#handleGetGuard()
     */
    @Override
    protected Constraint handleGetGuard()
    {
        return this.metaObject.getGuard();
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.TransitionFacadeLogic#handleIsTriggerPresent()
     */
    @Override
    protected boolean handleIsTriggerPresent()
    {
        return this.metaObject.getEffect() != null;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.TransitionFacadeLogic#handleIsExitingDecisionPoint()
     */
    @Override
    protected boolean handleIsExitingDecisionPoint()
    {
        final StateVertexFacade sourceVertex = this.getSource();
        return sourceVertex instanceof PseudostateFacade && ((PseudostateFacade)sourceVertex).isDecisionPoint();
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.TransitionFacadeLogic#handleIsEnteringDecisionPoint()
     */
    @Override
    protected boolean handleIsEnteringDecisionPoint()
    {
        final StateVertexFacade target = this.getTarget();
        return target instanceof PseudostateFacade && ((PseudostateFacade)target).isDecisionPoint();
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.TransitionFacadeLogic#handleIsExitingActionState()
     */
    /**
     * @see org.andromda.metafacades.emf.uml22.TransitionFacadeLogic#handleIsExitingActionState()
     */
    @Override
    protected boolean handleIsExitingActionState()
    {
        return this.getSource() instanceof ActionStateFacade;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.TransitionFacadeLogic#handleIsEnteringActionState()
     */
    @Override
    protected boolean handleIsEnteringActionState()
    {
        return this.getTarget() instanceof ActionStateFacade;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.TransitionFacadeLogic#handleIsExitingInitialState()
     */
    @Override
    protected boolean handleIsExitingInitialState()
    {
        StateVertexFacade sourceVertex = this.getSource();
        return sourceVertex instanceof PseudostateFacade && ((PseudostateFacade)sourceVertex).isInitialState();
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.TransitionFacadeLogic#handleIsEnteringFinalState()
     */
    @Override
    protected boolean handleIsEnteringFinalState()
    {
        return this.getTarget() instanceof FinalStateFacade;
    }

    /**
     * @return this.getTarget().getStateMachine()
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object handleGetValidationOwner()
    {
        return this.getTarget().getStateMachine();
    }
}
