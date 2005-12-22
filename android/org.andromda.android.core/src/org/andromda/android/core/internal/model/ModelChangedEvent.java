package org.andromda.android.core.internal.model;

import org.andromda.android.core.model.IModelChangeProvider;
import org.andromda.android.core.model.IModelChangedEvent;

/**
 * @see IModelChangedEvent
 * 
 * @since 2.0
 */
public class ModelChangedEvent
        implements IModelChangedEvent
{
    private int type;

    private IModelChangeProvider provider;

    private Object[] changedObjects;

    private Object oldValue, newValue;

    private String changedProperty;

    /**
     * The constructor of the event.
     * 
     * @param provider the change provider
     * @param type the event type
     * @param objects the changed objects
     * @param changedProperty or <samp>null </samp> if not applicable
     */
    public ModelChangedEvent(IModelChangeProvider provider,
        int type,
        Object[] objects,
        String changedProperty)
    {
        this.type = type;
        this.provider = provider;
        this.changedObjects = objects;
        this.changedProperty = changedProperty;
    }

    /**
     * A costructor that should be used for changes of object properties.
     * 
     * @param provider the event provider
     * @param object affected object
     * @param changedProperty changed property of the affected object
     * @param oldValue the value before the change
     * @param newValue the value after the change
     */
    public ModelChangedEvent(IModelChangeProvider provider,
        Object object,
        String changedProperty,
        Object oldValue,
        Object newValue)
    {
        this.type = CHANGE;
        this.provider = provider;
        this.changedObjects = new Object[] { object };
        this.changedProperty = changedProperty;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * @see IModelChangedEvent#getChangeProvider
     */
    public IModelChangeProvider getChangeProvider()
    {
        return provider;
    }

    /**
     * @see IModelChangedEvent#getChangedObjects
     */
    public Object[] getChangedObjects()
    {
        return changedObjects;
    }

    /**
     * @see IModelChangedEvent#getChangedProperty
     */
    public String getChangedProperty()
    {
        return changedProperty;
    }

    /**
     * Returns the old property value.
     * 
     * @return the value before the change
     */
    public Object getOldValue()
    {
        return oldValue;
    }

    /**
     * Returns the new property value.
     * 
     * @return the value after the change
     */
    public Object getNewValue()
    {
        return newValue;
    }

    /**
     * Returns the event change type
     * 
     * @return the event change type
     * 
     * @see IModelChangedEvent#getChangeType
     */
    public int getChangeType()
    {
        return type;
    }
}
