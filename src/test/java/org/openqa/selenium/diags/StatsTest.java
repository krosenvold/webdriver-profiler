package org.openqa.selenium.diags;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * @author Kristian Rosenvold
 */
public class StatsTest {
    Stats stats = new Stats("testFile");
    @Test
    public void testCreate() throws Exception {

        stats.create("t1", new Object[]{});
        stats.setLastSeenOnThread();
      final long totalRuntime = stats.getTotalRuntime();
      assertTrue( totalRuntime < 100);
    }


  @Test
  public void moreThreads() throws Exception {
    final Worker w1 = new Worker("w1");
    w1.start();
    final Worker w2 = new Worker("w2");
    w2.start();
    final Worker w3 = new Worker("w3");
    w3.start();
    w1.join();
    w2.join();
    w3.join();
    final long totalRuntime = stats.getTotalRuntime();
    System.out.println("totalRuntime = " + totalRuntime);
    assertTrue( totalRuntime >= 300);
    final long runTime = stats.getRunTime(w1.getId());
    System.out.println("runTime = " + runTime);
    assertTrue(runTime >= 200 && runTime < 300);
  }

    class Worker extends Thread {
      private final String name;

      Worker(String name) {
        this.name = name;
      }

      @Override
      public void run() {
        final StatEventInstance statEventInstance = stats.create(name, new Object[]{});
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
        }

        statEventInstance.complete();
        final StatEventInstance statEventInstance2 = stats.create(name +"1", new Object[]{});
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        statEventInstance2.complete();

        stats.setLastSeenOnThread();
      }
    }
    
}
