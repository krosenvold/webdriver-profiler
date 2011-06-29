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

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:kristian.rosenvold@gmail.com">Kristian Rosenvold</a>
 */
public class Stats {

    private final ConcurrentHashMap<String, StatEvent> eventMap = new ConcurrentHashMap<String, StatEvent>();

    private final AtomicInteger activeBrowsers = new AtomicInteger(  );

    private final AtomicInteger saveFileNumber = new AtomicInteger( 0 );

    private final String fileName;


    public Stats( String fileName )
    {
        this.fileName = fileName;
    }

    private void doReport()
    {
        File file = new File( fileName + saveFileNumber.incrementAndGet() + ".txt");
        Map<String, StatEvent> copy = new HashMap<String, StatEvent>( eventMap);
        eventMap.clear();
        try
        {
            FileOutputStream fos = new FileOutputStream( file );
            PrintStream ps = new PrintStream(  fos );
            doReport(  ps, copy );
            ps.close();
            fos.close();
        }
        catch ( FileNotFoundException e )
        {
            throw new RuntimeException(  e );
        }
        catch ( IOException e )
        {
            throw new RuntimeException(  e );
        }
    }

    private Runnable getReporter(){
        return new Runnable() {
            public void run() {
                doReport();
            }
        };
    }
    private void doReport(PrintStream out, Map<String, StatEvent> itemMap)
    {

        Set<String> items = new TreeSet<String>(itemMap.keySet());
        for ( String key : items )
        {
            StatEvent statEvent = itemMap.get(  key );
            out.println( key + "," + statEvent);
        }
    }


    public StatEventInstance create(String methodName, Object[] args){
        return getOrCreate( methodName + "#" +  getKey(args)).instantiate();
    }

    private String getKey(Object[] args) {
        StringBuilder result = new StringBuilder();
        if (args == null){
            return "null";
        }
        for (Object arg : args) {
            if (arg instanceof  By){
                result.append( toKey( (By) arg ) );
            } else if ( arg instanceof Object[]){
                Object[] argArray = (Object[]) arg;
                if (argArray.length > 0){
                    // Maybe need to do something about arrays...
                }
            } else {
               result.append(arg != null ? arg.toString() : "null");
            }
        }
        return result.toString();
    }

    private StatEvent getOrCreate(String key){
        StatEvent event = new StatEvent();
        final StatEvent existing = eventMap.putIfAbsent(key, event);
        return existing != null ? existing : event;
    }

    private String toKey( By by){
        final String s = by.toString();
        return StringUtils.substringBefore( s, ":" );
    }

    public void quit()
    {
        final int i = activeBrowsers.decrementAndGet();
        if (i == 0){
            Runnable runnable = new Runnable() {
                public void run() {
                    try {
                        Thread.sleep( 5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (activeBrowsers.get() == 0){
                        getReporter().run();
                    }
                }
            };
            new Thread(runnable).start();
            doReport();
        }
    }


    public void addWebDriver()
    {
        activeBrowsers.incrementAndGet();
    }

    public void close() {
        getReporter().run();
    }
}
