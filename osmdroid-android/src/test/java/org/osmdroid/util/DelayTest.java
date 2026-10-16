package org.osmdroid.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Fabrice Fontaine
 * @since 6.0.2
 */
public class DelayTest {

    @Test
    public void testDelayOne() {
        final long millis = 5000;
        long before = now();
        final Delay delay = new Delay(millis);
        long after = now();
        for (int i = 0; i < 5; i++) {
            check(delay, millis, before, after);
            before = now();
            final long next = delay.next();
            after = now();
            Assert.assertEquals(millis, next);
        }
    }

    @Test
    public void testDelayMulti() {
        final long[] millis = new long[]{500, 600, 800, 1000};
        final long lastDuration = millis[millis.length - 1];
        long next;
        long before = now();
        final Delay delay = new Delay(millis);
        long after = now();
        for (int i = 0; i < millis.length; i++) {
            check(delay, millis[i], before, after);
            before = now();
            next = delay.next();
            after = now();
            Assert.assertEquals(i < millis.length - 1 ? millis[i + 1] : lastDuration, next);
        }
        check(delay, lastDuration, before, after);
        before = now();
        next = delay.next();
        after = now();
        Assert.assertEquals(lastDuration, next);
        check(delay, lastDuration, before, after);
    }

    /**
     * @param pBefore time just before the delay was (re)started
     * @param pAfter  time just after the delay was (re)started
     * Sleep durations are not guaranteed (a loaded machine may oversleep), so the assertions
     * are based on the time actually elapsed rather than on the requested sleep durations
     */
    private void check(final Delay pDelay, final long pMillis, final long pBefore, final long pAfter) {
        sleep((pMillis * 3) / 4);
        final boolean shouldWait = pDelay.shouldWait();
        if (now() < pBefore + pMillis) {
            Assert.assertTrue(shouldWait);
        }
        final long end = pAfter + pMillis;
        long remaining;
        while ((remaining = end - now()) > 0) {
            sleep(remaining);
        }
        Assert.assertFalse(pDelay.shouldWait());
    }

    private static long now() {
        return System.nanoTime() / 1000000L;
    }

    private void sleep(final long pMillis) {
        try {
            Thread.sleep(pMillis, 0);
        } catch (InterruptedException e) {
            //
        }
    }
}
