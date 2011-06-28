package org.openqa.selenium.diags;

import junit.framework.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * @author Kristian Rosenvold
 */
public class WebDriverProxyFactoryTest
{
    @Test
    public void testCreateProxy()
        throws Exception
    {
        FirefoxDriver firefoxDriver = new FirefoxDriver(  );
        WebDriverProxyFactory webDriverProxyFactory = new WebDriverProxyFactory( "abc.txt" );
        final WebDriver proxy = webDriverProxyFactory.createProxy( firefoxDriver );
        Assert.assertTrue( proxy instanceof WebDriver );
        proxy.quit();


    }
}
