package org.openqa.selenium.diags;

import org.junit.Test;

/**
 * @author Kristian Rosenvold
 */
public class StatsTest {
    @Test
    public void testCreate() throws Exception {

        Stats stats = new Stats("testFile");
        stats.create("t1", new Object[]{});
        stats.setLastSeenOnThread();
        stats.quit();
    }
}
