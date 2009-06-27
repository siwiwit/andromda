package org.andromda.android.core.model;

/**
 * Classes that implement this interface are capable of notifying listeners about model changes. Interested parties
 * should implement {@link IModelChangedListener} and add as listeners to be able to receive change notification.
 *
 * @author Eclipse.org
 */
public interface IModelChangeProvider
{
    /**
     * Adds the listener to the list of listeners that will be notified on model changes.
     *
     * @param listener a model change listener to be added
     */
    public void addModelChangedListener(IModelChangedListener listener);

    /**
     * Delivers change event to all the registered listeners.
     *
     * @param event a change event that will be passed to all the listeners
     */
    public void fireModelChanged(IModelChangedEvent event);

    /**
     * Notifies listeners that a property of a model object changed. This is a utility method that will create a model
     * event and fire it.
     *
     * @param object an affected model object
     * @param property name of the property that has changed
     * @param oldValue the old value of the property
     * @param newValue the new value of the property
     */
    public void fireModelObjectChanged(Object object,
        String property,
        Object oldValue,
        Object newValue);

    /**
     * Takes the listener off the list of registered change listeners.
     *
     * @param listener a model change listener to be removed
     */
    public void removeModelChangedListener(IModelChangedListener listener);
}
