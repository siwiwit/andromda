/*
 * JETeam, Java Enterprise TeamWork
 *
 * Distributable under the GPL license.
 * See terms of license at http://www.gnu.org
 *
 * $Source: /root/temp/andromda/cartridges/andromda-bpm4struts/src/decorators/manual/org/andromda/cartridges/bpm4struts/Util.java,v $
 * $Date: 2004-01-14 22:45:16 $
 * $Author: draftdog $
 * $Revision: 1.1.2.1 $
 */
package org.andromda.cartridges.bpm4struts;

import org.andromda.core.common.CollectionFilter;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.FinalState;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;
import org.omg.uml.behavioralelements.statemachines.State;
import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.behavioralelements.statemachines.Transition;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.foundation.datatypes.PseudostateKind;
import org.omg.uml.foundation.datatypes.PseudostateKindEnum;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Collections;

public final class Util
{
    public static final String TAG_FORM_BEAN_NAME = "@org.andromda.struts.form.name";
    public static final String TAG_ACTION_PATH = "@org.andromda.struts.action.path";


    /**
     * A filter used to keep only decision points.
     *
     * @see #isDecisionPoint(Object object)
     */
    public static final CollectionFilter DECISIONPOINT_FILTER =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isDecisionPoint(object);
            }
        };

    /**
     * A filter used to keep only merge points.
     *
     * @see #isMergePoint(Object object)
     */
    public static final CollectionFilter MERGEPOINT_FILTER =
        new CollectionFilter() {
            public boolean accept(Object object)
            {
                return isMergePoint(object);
            }
        };

    /**
     * A filter used to keep only ObjectFlowState instances.
     */
    public static final CollectionFilter OBJECTFLOWSTATE_FILTER =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isObjectFlowState(object);
            }
        };

    /**
     * A filter used to keep only Pseudostates of kind 'initial'.
     */
    public static final CollectionFilter INITIALSTATE_FILTER =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isInitialState(object);
            }
        };

    /**
     * A filter used to keep only Pseudostates of kind 'choice'.
     */
    public static final CollectionFilter CHOICEPSEUDOSTATE_FILTER =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isChoice(object);
            }
        };

    /**
     * A filter used to keep only ActionStates.
     */
    public static final CollectionFilter ACTIONSTATE_FILTER =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isActionState(object);
            }
        };

    /**
     * A filter used to keep only States.
     */
    public static final CollectionFilter STATE_FILTER =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isState(object);
            }
        };

    /**
     * A filter used to keep only States.
     */
    public static final CollectionFilter PSEUDOSTATE_FILTER =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isPseudostate(object);
            }
        };

    /**
     * A filter used to keep only FinalStates.
     */
    public static final CollectionFilter FINALSTATE_FILTER =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isFinalState(object);
            }
        };

    /**
     * A filter used to keep only Transitions.
     */
    public static final CollectionFilter TRANSITION_FILTER =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isTransition(object);
            }
        };

    /**
     * A filter used to keep only StateMachines.
     */
    public static final CollectionFilter STATEMACHINE_FILTER =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isStateMachine(object);
            }
        };

    /**
     * A filter used to keep only transitions with a guard.
     */
    public static final CollectionFilter GUARDEDTRANSITION_FILTER =
        new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return isGuardedTransition(object);
            }
        };

    public static boolean isGuardedTransition(Object object)
    {
        return ( (object instanceof Transition) && (((Transition)object).getGuard() != null) );
    }
    /**
     * Returns <code>true</code> if the argument is a StateMachine,
     * <code>false</code> in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a StateMachine,
     *    <code>false</code> in any other case.
     */
    public static boolean isStateMachine(Object object)
    {
        return (object instanceof StateMachine);
    }

    /**
     * Returns <code>true</code> if the argument is a Pseudostate instance
     * <code>false</code> in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a Pseudostate instance
     *    <code>false</code> in any other case.
     */
    public static boolean isPseudostate(Object object)
    {
        return (object instanceof Pseudostate);
    }
    /**
     * Returns <code>true</code> if the argument is a Pseudostate instance
     * of kind 'initial', <code>false</code> in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a Pseudostate instance
     *    of kind 'choice', <code>false</code> in any other case.
     */
    public static boolean isInitialState(Object object)
    {
        return PseudostateKindEnum.PK_INITIAL.equals(getPseudostateKind(object));
    }

    /**
     * Returns <code>true</code> if the argument is a Pseudostate instance
     * of kind 'join', <code>false</code> in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a Pseudostate instance
     *    of kind 'join', <code>false</code> in any other case.
     */
    public static boolean isJoin(Object object)
    {
        return PseudostateKindEnum.PK_JOIN.equals(getPseudostateKind(object));
    }

    /**
     * Returns <code>true</code> if the argument is a Pseudostate instance
     * of kind 'fork', <code>false</code> in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a Pseudostate instance
     *    of kind 'fork', <code>false</code> in any other case.
     */
    public static boolean isFork(Object object)
    {
        return PseudostateKindEnum.PK_FORK.equals(getPseudostateKind(object));
    }
    /**
     * Returns <code>true</code> if the argument is a Transition instance, <code>false</code>
     * in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a Transition instance, <code>false</code>
     *    in any other case.
     */
    public static boolean isTransition(Object object)
    {
        return (object instanceof Transition);
    }

    /**
     * Returns <code>true</code> if the argument is an ActivityGraph instance, <code>false</code>
     * in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is an ActivityGraph instance, <code>false</code>
     *    in any other case.
     */
    public static boolean isActivityGraph(Object object)
    {
        return (object instanceof ActivityGraph);
    }

    /**
     * Returns <code>true</code> if the argument is an ActionState instance, <code>false</code>
     * in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is an ActionState instance, <code>false</code>
     *    in any other case.
     */
    public static boolean isActionState(Object object)
    {
        return (object instanceof ActionState);
    }

    /**
     * Returns <code>true</code> if the argument is a State instance, <code>false</code>
     * in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a State instance, <code>false</code>
     *    in any other case.
     */
    public static boolean isState(Object object)
    {
        return (object instanceof State);
    }

    /**
     * Returns <code>true</code> if the argument is a FinalState instance, <code>false</code>
     * in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a FinalState instance, <code>false</code>
     *    in any other case.
     */
    public static boolean isFinalState(Object object)
    {
        return (object instanceof FinalState);
    }

    /**
     * Returns <code>true</code> if the argument is a ObjectFlowState instance, <code>false</code>
     * in any other case.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a ObjectFlowState instance, <code>false</code>
     *    in any other case.
     */
    public static boolean isObjectFlowState(Object object)
    {
        return (object instanceof ObjectFlowState);
    }

    /**
     * Returns <code>true</code> if the argument is a Pseudostate instance
     * of kind 'choice', <code>false</code> in any other case.
     * <p>
     * Please note that as well decision points as merges are represented using
     * a choice pseudostate. Their difference lies in the number of incoming and
     * outgoing transitions.
     *
     * @param object an argument to test
     * @return <code>true</code> if the argument is a Pseudostate instance
     *    of kind 'choice', <code>false</code> in any other case.
     */
    public static boolean isChoice(Object object)
    {
        return PseudostateKindEnum.PK_CHOICE.equals(getPseudostateKind(object));
    }

    /**
     * Returns <code>true</code> if the argument state vertex is a pseudostate of kind 'choice', it has
     * multiple incoming transitions, but only a single outgoing transition.
     * <p>
     * Such a pseudostate would be used as a merge state in a UML diagram.
     * <p>
     * Currently this method will also return <code>true</code> when the object is
     * is a Pseudostate.JOIN model element instead of the Pseudostate.CHOICE. All other
     * conditions remain. It is recommended you use the former model element.
     *
     * @param object a choice pseudostate
     * @return <code>true</code> if there is more than 1 incoming transition, <code>false</code> otherwise
     * @see #isChoice(Object object)
     */
    public static boolean isMergePoint(Object object)
    {
        boolean isMergePoint = false;

        if (isChoice(object) || isJoin(object))
        {
            Pseudostate pseudostate = (Pseudostate)object;
            isMergePoint = true;
            isMergePoint = isMergePoint && (pseudostate.getIncoming().size() > 1);
            isMergePoint = isMergePoint && (pseudostate.getOutgoing().size() == 1);
        }

        return isMergePoint;
    }

    /**
     * Returns <code>true</code> if the argument state vertex is a pseudostate of kind 'choice', it has
     * multiple outgoing transition, but only a single incoming transition.
     * <p>
     * Such a pseudostate would be used as a decision point in a UML diagram.
     * <p>
     * Currently this method will also return <code>true</code> when the object is
     * is a Pseudostate.FORK modelelement instead of the Pseudostate.CHOICE. All other
     * conditions remain. It is recommended you use the former modelelement.
     *
     * @param object a choice pseudostate
     * @return <code>true</code> if there is more than 1 outgoing transition, <code>false</code> otherwise
     * @see #isChoice(Object object)
     */
    public static boolean isDecisionPoint(Object object)
    {
        boolean isDecisionPoint = false;

        if (isChoice(object) || isFork(object))
        {
            Pseudostate pseudostate = (Pseudostate)object;
            isDecisionPoint = true;
            isDecisionPoint = isDecisionPoint && (pseudostate.getIncoming().size() == 1);
            isDecisionPoint = isDecisionPoint && (pseudostate.getOutgoing().size() > 1);
        }

        return isDecisionPoint;
    }

    /**
     * Checks whether the argument pseudostate has outgoing transitions with a guard, only if all of them
     * have a guard and the argument is a decision point this method will return <code>true</code>.
     *
     * @param pseudostate a decision point
     * @return <code>true</code> if the argument is indeed a decision point with all outgoing transitions guarded,
     *  in any other case this method returns <code>false</code>.
     * @see #isDecisionPoint(Object object)
     * @see #isGuardedTransition(Object object)
     */
    public static boolean isGuardedDecisionPoint(Pseudostate pseudostate)
    {
        boolean isGuardedDecisionPoint = isDecisionPoint(pseudostate);

        Collection transitions = pseudostate.getOutgoing();
        for (Iterator iterator = transitions.iterator(); iterator.hasNext() && isGuardedDecisionPoint;)
        {
            Transition transition = (Transition) iterator.next();
            isGuardedDecisionPoint = isGuardedDecisionPoint && isGuardedTransition(transition);
        }

        return isGuardedDecisionPoint;
    }

    /**
     * Returns the kind of Pseudostate the argument is, if the argument is no
     * Pseudostate instance this method will return <code>null</code>.
     * <p>
     * In short, possible return values are
     * <ul>
     *    <li>PseudostateKindEnum.PK_CHOICE
     *    <li>PseudostateKindEnum.PK_DEEP_HISTORY
     *    <li>PseudostateKindEnum.PK_FORK
     *    <li>PseudostateKindEnum.PK_INITIAL
     *    <li>PseudostateKindEnum.PK_JOIN
     *    <li>PseudostateKindEnum.PK_JUNCTION
     *    <li>PseudostateKindEnum.PK_SHALLOW_HISTORY
     *    <li><code>null</code>
     * </ul>
     * @param object an argument to test, may be <code>null</code>
     * @return the pseudostate kind, or <code>null</code>
     */
    public static PseudostateKind getPseudostateKind(Object object)
    {
        return isPseudostate(object) ? ((Pseudostate) object).getKind() : null;
    }




    /**
     * Returns the collection of taggedValues for a given modelElement
     *
     * @param object model element
     * @return Collection of org.omg.uml.foundation.core.TaggedValue
     *
     */
    public static Collection getTaggedValues(Object object) {
        if ((object == null) || !(object instanceof ModelElement)) {
            return Collections.EMPTY_LIST;
        }

        ModelElement modelElement = (ModelElement) object;

        return modelElement.getTaggedValue();
    }

    /**
     * Searches a collection of tag values for one with a particular
     * name
     *
     * @param taggedValues of taggedValues
     * @param tagName name of tag for which to search
     *
     * @return value of tag, null if tag not found
     */
    public static String findTagValue(Collection taggedValues, String tagName) {
        for (Iterator i = taggedValues.iterator(); i.hasNext();) {
            TaggedValue taggedValue = (TaggedValue) i.next();
            String tgvName = getTaggedValueName(taggedValue);

            if (tagName.equals(tgvName)) {
                Iterator it = taggedValue.getDataValue().iterator();
                if (it.hasNext()) {
                    return it.next().toString();
                }
                return null;
            }
        }

        return null;
    }

    /**
     * Searches for and returns the value of a given tag on
     * the specified model element.
     *
     * @param modelElement model element
     * @param tagName  name of the tag
     * @return String value of tag, <b>null</b> if tag not found
     *
     */
    public static String findTagValue(ModelElement modelElement, String tagName) {
        return findTagValue(getTaggedValues(modelElement), tagName);
    }


    private static String getTaggedValueName(TaggedValue tgv)
    {
        String tgvName = tgv.getName();

        // sometimes the tag name is on the TagDefinition
        if ( (tgvName == null) && (tgv.getType() != null) )
        {
            tgvName = tgv.getType().getName();

            // sometimes it is the TagType
            if (tgvName == null)
            {
                tgvName = tgv.getType().getTagType();
            }
        }

        return tgvName;
    }









    /**
     * Returns a collection of vertices that are contained in the argument
     * StateMachine.
     * <p>
     * The CollectionFilter decides which vertices are being filtered out.
     *
     * @param stateMachine The graph where the look for vertices, may not be <code>null</code>
     * @param collectionFilter the filter that decides which vertices to ignore, may not be <code>null</code>
     * @return A Collection containing only
     *    <code>org.omg.uml.behavioralelements.statemachines.StateVertex</code> instances.
     * @see org.omg.uml.behavioralelements.statemachines.StateVertex
     */
    public static Set getSubvertices(StateMachine stateMachine, CollectionFilter collectionFilter)
    {
        CompositeState compositeState = (CompositeState) stateMachine.getTop();
        return filter(compositeState.getSubvertex(), collectionFilter);
    }

    /**
     * Filters the specified collection using the argument filter.
     *
     * @param collection The collection to filter, may not be <code>null</code>
     * @param collectionFilter The filter to apply, may not be <code>null</code>
     * @return A subset of the argument collection, filtered out as desired
     */
    public static Set filter(Collection collection, CollectionFilter collectionFilter)
    {
        final Set filteredCollection = new LinkedHashSet();
        for (Iterator iterator = collection.iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            if (collectionFilter.accept(object))
            {
                filteredCollection.add(object);
            }
        }
        return filteredCollection;
    }

    /**
     * Converts the argument ModelElement's name to a class name as per Java conventions.
     * <p>
     * Any non-word characters (including whitespace) will be removed.
     *
     * @param modelElement the modelElement which name to convert, the argument will not be modified,
     *  mya not be <code>null</code>
     * @return the argument's name converted into a Java class name
     */
    public static String toJavaClassName(ModelElement modelElement)
    {
        return upperCaseFirstLetter(toJavaMethodName(modelElement));
    }

    /**
     * Converts the argument ModelElement's name to a method name as per Java conventions.
     * <p>
     * Any non-word characters (including whitespace) will be removed.
     *
     * @param modelElement the modelElement which name to convert, the argument will not be modified,
     *  may not be <code>null</code>
     * @return the argument's name converted into a Java method name
     */
    public static String toJavaMethodName(ModelElement modelElement)
    {
        String[] parts = splitAtNonWordCharacters(modelElement.getName());
        StringBuffer conversionBuffer = new StringBuffer();
        for (int i = 0; i < parts.length; i++)
        {
            conversionBuffer.append(upperCaseFirstLetter(parts[i]));
        }
        return lowerCaseFirstLetter(conversionBuffer.toString());
    }

    public static String upperCaseFirstLetter(String string)
    {
        String upperCasedString = null;

        if (string == null)
        {
            upperCasedString = null;
        }
        else if (string.length() == 1)
        {
            upperCasedString = string.toUpperCase();
        }
        else
        {
            upperCasedString = string.substring(0,1).toUpperCase() + string.substring(1);
        }

        return upperCasedString;
    }

    public static String lowerCaseFirstLetter(String string)
    {
        String lowerCasedString = null;

        if (string == null)
        {
            lowerCasedString = null;
        }
        else if (string.length() == 1)
        {
            lowerCasedString = string.toLowerCase();
        }
        else
        {
            lowerCasedString = string.substring(0,1).toLowerCase() + string.substring(1);
        }

        return lowerCasedString;
    }

    /**
     * Converts the argument ModelElement's name to a name suitable for web files.
     * <p>
     * Any non-word characters (including whitespace) will be removed (each sequence will be replaced
     * by a single hyphen '-').
     * <p>
     * The returned name contains no uppercase characters.
     *
     * @param modelElement the modelElement which name to convert, the argument will not be modified,
     *  may not be <code>null</code>
     * @return the argument's name converted into a suitable web file name, there will be no extension added
     */
    public static String toWebFileName(ModelElement modelElement)
    {
        return toLowercaseSeparatedName(modelElement.getName(),"-");
    }

    /**
     * Converts the argument to lowercase, removes all non-word characters, and replaces each of those
     * sequences by a hyphen '-'.
     */
    protected static String toLowercaseSeparatedName(String name, String separator)
    {
        if (name == null)
        {
            return "";
        }

        String[] parts = splitAtNonWordCharacters(name.toLowerCase());
        StringBuffer conversionBuffer = new StringBuffer();

        for (int i = 0; i < parts.length - 1; i++)
        {
            conversionBuffer.append(parts[i]).append(separator);
        }
        conversionBuffer.append(parts[parts.length - 1]);

        return conversionBuffer.toString();
    }

    /**
     * Splits at each sequence of non-word characters.
     */
    protected static String[] splitAtNonWordCharacters(String s)
    {
        return s.split("\\W+");
    }

}
