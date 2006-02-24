package org.andromda.repositories.mdr.uml2;

import org.apache.log4j.Logger;

import java.net.URL;

public class TestModel
{
    private static final Logger logger = Logger.getLogger(TestModel.class);

    public static URL getModel()
    {
        try
        {
            String testModel = "/TestModel.xml.zip";
            URL modelUri = TestModel.class.getResource(testModel);
            if (modelUri == null)
            {
                throw new RuntimeException("Could not load '" + testModel + "'");
            }
            if (modelUri != null)
            {
                String jarUrl = "jar:" + modelUri.toString() + "!/TestModel.xml";
                modelUri = new URL(jarUrl);
                if (modelUri == null)
                {
                    throw new RuntimeException("Could not load '" + jarUrl + "'");
                }
            }
            return modelUri;
        }
        catch (Throwable th)
        {
            String errMsg = "Error performing TestModel.getModel()";
            logger.error(errMsg, th);
            throw new RuntimeException(errMsg, th);
        }
    }
}