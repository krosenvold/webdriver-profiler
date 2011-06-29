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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:kristian.rosenvold@gmail.com">Kristian Rosenvold</a>
 */
public class WebDriverInvocationHandler  implements InvocationHandler
{

    private final Stats stats;
    private final Object underlying;

    public WebDriverInvocationHandler(Stats stats, Object underlyingWebDriver) {
        this.stats = stats;
        this.underlying = underlyingWebDriver;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final StatEventInstance statEvent = stats.create(method.getName(), args);
        try {
            return invokeUnderlying(method, args);
        } finally {
            statEvent.complete();
        }
    }

    protected Object invokeUnderlying( Method method, Object[] args )
        throws IllegalAccessException, InvocationTargetException
    {
        final Object o = method.invoke(underlying, args);
        if ( "findElement".equals( method.getName() ) )
        {
            return createElementProxy( o );
        }
        if ( "findElements".equals( method.getName() ) )
        {
            List items = (List) o;
            List<Object> replaced = new ArrayList<Object>();
            for ( Object item : items )
            {
                replaced.add( createElementProxy( item ) );

            }
            return replaced;
        }

        if ( "quit".equals( method.getName() ) )
        {
            stats.quit();
        }
        return o;
    }

    private Object createElementProxy( Object o )
    {
        WebDriverInvocationHandler webElementInvocationHandler = new WebDriverInvocationHandler( stats, o );
        return ProfilerFactory.createProxy(o, webElementInvocationHandler);
    }
}

