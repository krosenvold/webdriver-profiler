package org.openqa.selenium.diags;

import junit.framework.Assert;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * @author Kristian Rosenvold
 */
public class ProfilerPostProcessorTest
{
    @Test
    public void testTransform()
        throws Exception
    {
        FirefoxDriver firefoxDriver = new FirefoxDriver(  );
        ProfilerPostProcessor profilerPostProcessor = new ProfilerPostProcessor();
        final WebDriver proxy = profilerPostProcessor.transform( firefoxDriver );
        Assert.assertTrue( proxy instanceof JavascriptExecutor );
        proxy.quit();

    }
}
