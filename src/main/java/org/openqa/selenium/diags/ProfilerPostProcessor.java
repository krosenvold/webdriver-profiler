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

import org.openqa.selenium.WebDriver;

/**
 * Can be hacked into the server to instrument there too.
 * @author Kristian Rosenvold
 */
public class ProfilerPostProcessor
{

    public ProfilerPostProcessor()
    {
    }

    public WebDriver transform(WebDriver original){
        Stats stats = new Stats( "server-profiling");
        WebDriverInvocationHandler invocationHandler = new WebDriverInvocationHandler( stats, original );
        return (WebDriver) ProfilerFactory.createProxy( original, invocationHandler );
    }
}
