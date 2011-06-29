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

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author <a href="mailto:kristian.rosenvold@gmail.com">Kristian Rosenvold</a>
 */
public class StatEvent {

    private final AtomicLong invocationElapsed = new AtomicLong();
    private final AtomicLong invocationCount = new AtomicLong();

    public void setComplete( StatEventInstance statEventInstance) {
        invocationElapsed.addAndGet( statEventInstance.getElapsed());
        invocationCount.incrementAndGet();
    }

    public StatEventInstance instantiate() {
        return new StatEventInstance(this);
    }

    @Override
    public String toString()
    {
        final long invocationCount = this.invocationCount.longValue();
        final long totalElepased = invocationElapsed.longValue();
        final long average = invocationCount > 0 ? totalElepased / invocationCount : 0;
        return invocationCount + "," + totalElepased + "," + average;
    }
}
