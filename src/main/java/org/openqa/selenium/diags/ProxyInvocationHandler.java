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

/**
 * @author <a href="mailto:kristian.rosenvold@gmail.com">Kristian Rosenvold</a>
 */
public class ProxyInvocationHandler<T> implements InvocationHandler {
    private final Stats stats;
    private final T underlying;

    public ProxyInvocationHandler(Stats stats, T underlying) {
        this.stats = stats;
        this.underlying = underlying;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final StatEventInstance statEvent = stats.create(method.getName(), args);
        try {
            return invokeUnderlying(method, args);
        } finally {
            statEvent.complete();
        }
    }

    protected Object invokeUnderlying(Method method, Object[] args) throws IllegalAccessException, InvocationTargetException {
        return method.invoke(underlying, args);
    }


    protected T getUnderlying() {
        return underlying;
    }

    public Stats getStats() {
        return stats;
    }
}

