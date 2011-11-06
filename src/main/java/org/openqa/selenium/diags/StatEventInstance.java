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

/**
 * One statistics event, on a specific thread
 * @author <a href="mailto:kristian.rosenvold@gmail.com">Kristian Rosenvold</a>
 */
public class StatEventInstance {
    private final long startAt = System.currentTimeMillis();
    private final long owningThreadId;
    private volatile long endedAt;

    public StatEventInstance() {
        this.owningThreadId = Thread.currentThread().getId();
    }

    public long getElapsed(){
        return endedAt - startAt;
    }
  
    public long getElapsed(long threadId){
      return threadId == owningThreadId ? getElapsed() : 0;
    }

    public void complete() {
        endedAt = System.currentTimeMillis();
    }
}
