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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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

    private final AtomicInteger activeBrowsers = new AtomicInteger();

    private final AtomicInteger saveFileNumber = new AtomicInteger(0);

    private final ConcurrentHashMap<Thread, String> seenThreads = new ConcurrentHashMap<Thread, String>();
    private final ConcurrentHashMap<Thread, Long> startTime = new ConcurrentHashMap<Thread, Long>();
    private final ConcurrentHashMap<Thread, Long> lastSeen = new ConcurrentHashMap<Thread, Long>();

    private final String fileName;

    private final ConcurrentHashMap<String, StatEvent> eventMap = new ConcurrentHashMap<String, StatEvent>();


    public Stats(String fileName) {
        this.fileName = fileName;
    }

    private void doReport() {
        File file = getReportFile();
        Map<String, StatEvent> copy = new HashMap<String, StatEvent>(eventMap);
        eventMap.clear();
        try {
            FileOutputStream fos = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fos);
            ps.print("<html><body>");
            doReportForAll(ps, copy);
            doPerThreadReport(ps, copy);
            ps.print("</body></html>");
            ps.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        }
    }

    long getTotalRuntime() {
        long result = 0;
        for (Thread threadId : seenThreads.keySet()) {
            Long endTime = lastSeen.get(threadId);
            final Long startTime = this.startTime.get(threadId);
            long elapsed = endTime - startTime;
            result += elapsed;
        }
        return result;
    }

    long getRunTime(Long threadId) {
        long result = 0;
        for (StatEvent key : eventMap.values()) {
            result += key.getTotalElapsed(threadId);
        }
        return result;
    }

    long getRunTime() {
        long result = 0;
        for (StatEvent key : eventMap.values()) {
            result += key.getTotalElapsed();
        }
        return result;
    }


    private void doReportForAll(PrintStream out, Map<String, StatEvent> itemMap) {
        long totalElapsed = getTotalRuntime();
        long clientSideElapsed = totalElapsed;
        Set<String> items = new TreeSet<String>(itemMap.keySet());
        table(out);

        tableHeader(out);
        for (String key : items) {
            StatEvent statEvent = itemMap.get(key);

            clientSideElapsed -= statEvent.getTotalElapsed();
            out.println("<tr><td>" + trim(key) + "</td>" + statEvent.getAsTableCells() + "</tr>");
        }
        out.println("<tr><td colspan='4'><b>====== Overall runtime characteristics aggregated all threads =======</b></td></tr>");
        out.println("<tr><td colspan='4'><b>====== Overall runtime characteristics aggregated all threads =======</b></td></tr>");
        out.println("<tr><td colspan='4'>Total elapsed " + totalElapsed + "ms, of which " + clientSideElapsed
                + "ms is within the test fixture itself</td></tr>");
        tableFooter(out);

    }

    private void doPerThreadReport(PrintStream out, Map<String, StatEvent> itemMap) {

        Set<String> items = new TreeSet<String>(itemMap.keySet());
        for (Thread threadId : seenThreads.keySet()) {
            table(out);
            final Long lstSeent = lastSeen.get(threadId);
            final Long startedAt = startTime.get(threadId);
            long totalElapsed = lstSeent - startedAt;
            long clientSideElapsed = totalElapsed;
            tableHeader(out);
            for (String key : items) {
                StatEvent statEvent = itemMap.get(key);
                clientSideElapsed -= statEvent.getTotalElapsed(threadId.getId());
                out.println("<tr><td>" + trim(key) + "</td>" + statEvent.getAsTableCells(threadId.getId()) + "</tr>");
            }
            out.println("<tr><td colspan='4'><b>====== Thread id + " + threadId + "(" + seenThreads.get(threadId) + ") =====</b></td></tr>");
            out.println("<tr><td colspan='4'>Total elapsed " + totalElapsed + "ms, of which " + clientSideElapsed
                    + "ms is within the test fixture itself</td></tr>");
            tableFooter(out);
        }
    }

    private String trim(String key) {
        return key.length() > 80 ? key.substring(0, 80) : key;
    }

    private void tableHeader(PrintStream out) {
        out.println(
                "<tr><th>Event</th><th>#Invocations</th><th>Elapsed(ms)</th><th>Average (ms)</th><tr>");
    }

    private void table(PrintStream out) {
        out.println(
                "<table>");
    }

    private void tableFooter(PrintStream out) {
        out.println("</table><hr/>");
    }


    private File getReportFile() {
        File file;
        do {
            file = new File(fileName + saveFileNumber.incrementAndGet() + ".html");
        } while (file.exists());
        return file;
    }

    public void setLastSeenOnThread() {
        lastSeen.put(Thread.currentThread(), System.currentTimeMillis());
    }

    class ReportRunnable implements Runnable {

        public void run() {
            doReport();
        }
    }

    private ReportRunnable getReporter() {
        return new ReportRunnable();
    }


    public StatEventInstance create(String methodName, Object[] args) {
        startTime.putIfAbsent(Thread.currentThread(), System.currentTimeMillis());
        return getOrCreate(methodName + "#" + getKey(args)).instantiate();
    }

    private String getKey(Object[] args) {
        StringBuilder result = new StringBuilder();
        if (args == null) {
            return "null";
        }
        for (Object arg : args) {
            if (arg instanceof By) {
                result.append(toKey((By) arg));
            } else if (arg instanceof Object[]) {
                Object[] argArray = (Object[]) arg;
                if (argArray.length > 0) {
                    // Maybe need to do something about arrays...
                }
            } else {
                result.append(arg != null ? arg.toString() : "null");
            }
        }
        return result.toString();
    }

    private StatEvent getOrCreate(String key) {
        StatEvent event = new StatEvent();
        addSeenThread();
        final StatEvent existing = eventMap.putIfAbsent(key, event);
        return existing != null ? existing : event;
    }

    private void addSeenThread() {
        seenThreads.putIfAbsent(Thread.currentThread(), Thread.currentThread().getName());
    }

    private String toKey(By by) {
        final String s = by.toString();
        return StringUtils.substringBefore(s, ":");
    }

    public void quit() {
        final int i = activeBrowsers.decrementAndGet();
        if (i == 0) {
            Runnable runnable = new TimedReporter();
            new Thread(runnable).start();
        }
    }


    class TimedReporter implements Runnable {

        public void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (activeBrowsers.get() == 0) {
                getReporter().run();
            }
        }
    }

    public void addWebDriver() {
        activeBrowsers.incrementAndGet();
    }

    public void close() {
        getReporter().run();
    }
}
