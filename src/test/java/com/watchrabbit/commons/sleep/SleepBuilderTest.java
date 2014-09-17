package com.watchrabbit.commons.sleep;

import com.watchrabbit.commons.exception.SystemException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 *
 * @author Mariusz
 */
public class SleepBuilderTest {

    @Test(timeout = 200)
    public void shoudlWaitOnBuild() {
        long currentTimeMillis = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(10);

        SleepBuilder.<Boolean>sleep()
                .withName("name")
                .withTimeout(100, TimeUnit.MILLISECONDS)
                .withInterval(10, TimeUnit.MILLISECONDS)
                .withComparer(argument -> argument)
                .withStatement(() -> {
                    latch.countDown();
                    return Boolean.TRUE;
                })
                .build();

        assertThat(System.currentTimeMillis()).isGreaterThan(currentTimeMillis + 100);
        assertThat(latch.getCount()).isLessThan(2);
    }

    @Test(expected = SystemException.class)
    public void shoudlThrowSystemException() {

        SleepBuilder.<Boolean>sleep()
                .withName("name")
                .withTimeout(100, TimeUnit.MILLISECONDS)
                .withInterval(10, TimeUnit.MILLISECONDS)
                .withComparer(argument -> argument)
                .withStatement(() -> {
                    throw new RuntimeException();
                })
                .build();
    }

    @Test(timeout = 80)
    public void shoudlBreakOnInterrupt() {
        Thread currentThread = Thread.currentThread();
        Executors.newSingleThreadScheduledExecutor()
                .schedule(() -> {
                    currentThread.interrupt();
                }, 50, TimeUnit.MILLISECONDS);

        SleepBuilder.<Boolean>sleep()
                .withTimeout(500, TimeUnit.MILLISECONDS)
                .withInterval(200, TimeUnit.MILLISECONDS)
                .withComparer(argument -> argument)
                .withStatement(() -> {
                    return Boolean.TRUE;
                })
                .build();
    }
}
