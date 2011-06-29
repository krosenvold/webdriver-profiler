package org.openqa.selenium.diags;
/*
Copyright 2011 WebDriver committers

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.openqa.selenium.WebDriver;

/**
 * @author <a href="mailto:kristian.rosenvold@gmail.com">Kristian Rosenvold</a>
 */
public class ProfilerFactory
{
    private final Stats stats;


    public ProfilerFactory(String fileName)
    {
        this.stats = new Stats( fileName );
    }


    public void close(){
        stats.close();
    }

    public WebDriver createProxy( WebDriver underlying )
    {
        stats.addWebDriver();
        WebDriverInvocationHandler invocationHandler = new WebDriverInvocationHandler( stats, underlying );
        return (WebDriver) createProxy( underlying, invocationHandler );
    }

    static Object createProxy( final Object targetInterface, final InvocationHandler invocationHandler )
    {
        return Proxy.newProxyInstance( targetInterface.getClass().getClassLoader(), getInterfaces( targetInterface ),
                                       invocationHandler );
    }

    private static Class[] getInterfaces( Object target )
    {
        Class base = target.getClass();
        Set<Class> interfaces = new HashSet<Class>();
        if ( base.isInterface() )
        {
            interfaces.add( base );
        }
        while ( base != null && !Object.class.equals( base ) )
        {
            interfaces.addAll( Arrays.asList( base.getInterfaces() ) );
            base = base.getSuperclass();
        }
        return interfaces.toArray( new Class[interfaces.size()] );

    }
}
