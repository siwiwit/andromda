package org.andromda.cartridges.bpm4struts;

import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState;
import org.omg.uml.behavioralelements.statemachines.FinalState;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;
import org.omg.uml.foundation.datatypes.PseudostateKindEnum;

/**
 * This class is a script helper designed for the AndroMDA Struts cartridge that works with
 * the dynamic part of a UML model such as the Activity Graphs.
 *
 * @author <a href="mailto:draftdog@users.sourceforge.net">Wouter Zoons</a>
 */
public final class StrutsScriptHelper
{
    public boolean isActionState(Object object)
    {
        return (object instanceof ActionState);
    }

    public boolean isFinalState(Object object)
    {
        return (object instanceof FinalState);
    }

    public boolean isObjectFlowState(Object object)
    {
        return (object instanceof ObjectFlowState);
    }

    public boolean isDecisionPoint(Object object)
    {
        return (object instanceof Pseudostate) &&
            ( (PseudostateKindEnum.PK_CHOICE.equals(((Pseudostate)(object)).getKind())) ||
            (PseudostateKindEnum.PK_JUNCTION.equals(((Pseudostate)(object)).getKind())) );
    }
}





















