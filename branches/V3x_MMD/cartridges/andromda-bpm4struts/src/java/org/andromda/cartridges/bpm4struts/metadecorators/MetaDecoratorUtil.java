package org.andromda.cartridges.bpm4struts.metadecorators;

import org.omg.uml.UmlPackage;
import org.omg.uml.behavioralelements.statemachines.StateVertex;
import org.omg.uml.foundation.core.ModelElement;

public final class MetaDecoratorUtil
{

    public static String toJavaClassName(String string)
    {
        return upperCaseFirstLetter(toJavaMethodName(string));
    }

    public static String toJavaMethodName(String string)
    {
        String[] parts = splitAtNonWordCharacters(string);
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
        else
            if (string.length() == 1)
            {
                upperCasedString = string.toUpperCase();
            }
            else
            {
                upperCasedString = string.substring(0, 1).toUpperCase() + string.substring(1);
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
        else
            if (string.length() == 1)
            {
                lowerCasedString = string.toLowerCase();
            }
            else
            {
                lowerCasedString = string.substring(0, 1).toLowerCase() + string.substring(1);
            }

        return lowerCasedString;
    }

    public static String toWebFileName(String string)
    {
        return toLowercaseSeparatedName(string, "-");
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

        String[] parts1 = splitAtNonWordCharacters(name.toLowerCase());
        StringBuffer conversionBuffer = new StringBuffer();

        for (int i = 0; i < parts1.length - 1; i++)
        {
            conversionBuffer.append(parts1[i]).append(separator);
        }
        conversionBuffer.append(parts1[parts1.length - 1]);

        return conversionBuffer.toString();
    }

    /**
     * Splits at each sequence of non-word characters.
     */
    private static String[] splitAtNonWordCharacters(String s)
    {
        return s.split("[\\W+]");
    }

    public static UmlPackage getModel(ModelElement modelElement)
    {
        if (modelElement instanceof UmlPackage)
            return (UmlPackage) modelElement;

        if (modelElement instanceof StateVertex)
            return getModel(((StateVertex) modelElement).getContainer().getStateMachine());

        return getModel(modelElement.getNamespace());
    }
}
