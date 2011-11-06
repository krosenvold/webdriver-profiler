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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An event of a given type, all entries across all threads
 *
 * @author <a href="mailto:kristian.rosenvold@gmail.com">Kristian Rosenvold</a>
 */
public class StatEvent {
    private final List<StatEventInstance> items = Collections.synchronizedList(new ArrayList<StatEventInstance>());

    public StatEventInstance instantiate() {
        final StatEventInstance statEventInstance = new StatEventInstance();
        items.add(statEventInstance);
        return statEventInstance;
    }

    public long getTotalElapsed(){
        long result = 0;
        for (StatEventInstance item : items) {
            result += item.getElapsed();
        }
        return result;
    }
    public long getTotalElapsed(Long threadId){
        long result = 0;
        for (StatEventInstance item : items) {
            result += item.getElapsed(threadId);
        }
        return result;
    }

    @Override
    public String toString() {
        final long invocationCount = items.size();
        final long totalElepased = getTotalElapsed();
        final long average = invocationCount > 0 ? totalElepased / invocationCount : 0;
        return invocationCount + "," + totalElepased + "," + average;
    }
}
