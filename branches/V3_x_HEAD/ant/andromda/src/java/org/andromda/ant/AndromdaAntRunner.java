package org.andromda.ant;

import org.andromda.andromdapp.AndroMDApp;

/**
 * Execues the AndroMDA application generator from within an Ant task.
 */
public class AndromdaAntRunner
{
    public static void main(String[] args)
    {
        try
        {
            AndroMDApp andromdapp = new AndroMDApp();
            andromdapp.run();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
