package org.andromda.core.common;

import java.util.Collection;

/**
 * <p>
 * An interface for objects responsible for being a model that has
 * been loaded into a metadata repository.
 * </p>
 * 
 * <p>
 * Models can be instances of any metamodel. The most common models
 * will be UML models. This interface is an abstraction. Any model
 * that implements this interface can be used with AndroMDA. 
 * </p>
 * 
 * <p>
 * Design goal: This class should only contain the <b>minimum amount
 * of methods</b> that will be needed such that the AndroMDA core can
 * deal with it. All other stuff should be done in cartridge-specific
 * classes!!! So, please don't make this class grow!
 * </p>
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
public interface ModelFacade
{
    /**
     * Returns an object that represents the entire model.
     * Data type is defined by the implementor of this interface.
     * 
     * @return Object
     */
    public Object getModel();

    /**
     * Returs all elements of the model.
     * @return Collection of model elements. Data type is defined by the implementor of this interface.
     */
    public Collection getModelElements();
    
    /**
     * Returns the name of a model element (whatever that means for a concrete model).
     * @param modelElement the model element
     * @return String containing the name
     */
    public String getName(Object modelElement);
    
    /**
     * Returns the package name of a model element (whatever that means for a concrete model).
     * @param modelElement the model element
     * @return String containing the name
     */
    public String getPackageName(Object modelElement);

    /**
     * Returns a collection of stereotype names for a model element
     * (whatever that means for a concrete model).
     * 
     * @param modelElement the model element
     * @return Collection of Strings with stereotype names
     */
    public Collection getStereotypeNames(Object modelElement);


}
