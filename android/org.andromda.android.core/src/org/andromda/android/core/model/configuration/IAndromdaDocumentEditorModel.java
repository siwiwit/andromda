package org.andromda.android.core.model.configuration;

import org.andromda.android.core.internal.model.configuration.AndromdaDocumentEditorModel;
import org.andromda.android.core.model.IEditorModel;
import org.andromda.core.configuration.AndromdaDocument;

/**
 * Model wrapper interface for the AndroMDA configuration model.
 * 
 * @author Peter Friese
 * @since 15.12.2005
 */
public interface IAndromdaDocumentEditorModel
        extends IEditorModel
{

    /**
     * @return The AndroMDA configuration document.
     */
    AndromdaDocument getAndromdaDocument();

    /**
     * Factory for {@link IAndromdaDocumentEditorModel} instances.
     */
    public static final class Factory
    {
        /**
         * Creates a new {@link IAndromdaDocumentEditorModel} instance.
         * 
         * @param andromdaDocument The document wrapped in the model.
         * @return A new model instance.
         */
        public static IAndromdaDocumentEditorModel newInstance(AndromdaDocument andromdaDocument)
        {
            return new AndromdaDocumentEditorModel(andromdaDocument);
        }
    }

}
