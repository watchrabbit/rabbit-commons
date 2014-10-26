/*
 * Copyright 2014 Mariusz.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.watchrabbit.commons.sleep;

import com.watchrabbit.commons.exception.SystemException;
import static com.watchrabbit.commons.exception.Throwables.suppress;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Mariusz
 */
public class Sleep {

    /**
     * Causes the current thread to wait until the specified waiting time
     * elapses.
     *
     * <p>
     * Any {@code InterruptedException}'s are suppress and logged. Any
     * {@code Exception}'s thrown by callable are propagate as SystemException
     *
     * @param timeout the maximum time to wait
     * @param unit the time unit of the {@code timeout} argument
     * @throws SystemException if callable throws exception
     */
    public static void sleep(long timeout, TimeUnit unit) throws SystemException {
        suppress((Long sleepTimeout) -> Thread.sleep(sleepTimeout))
                .accept(TimeUnit.MILLISECONDS.convert(timeout, unit));
    }

    /**
     * Causes the current thread to wait until the callable is returning
     * {@code true}, or the specified waiting time elapses.
     *
     * <p>
     * If the callable returns {@code false} then this method returns
     * immediately with the value returned by callable.
     *
     * <p>
     * Any {@code InterruptedException}'s are suppress and logged. Any
     * {@code Exception}'s thrown by callable are propagate as SystemException
     *
     * @param callable callable checked by this method
     * @param timeout the maximum time to wait
     * @param unit the time unit of the {@code timeout} argument
     * @return {@code Boolean} returned by callable method
     * @throws SystemException if callable throws exception
     */
    public static Boolean untilTrue(Callable<Boolean> callable, long timeout, TimeUnit unit) throws SystemException {
        return SleepBuilder.<Boolean>sleep()
                .withComparer(argument -> argument)
                .withTimeout(timeout, unit)
                .withStatement(callable)
                .build();
    }

    /**
     * Causes the current thread to wait until the callable is returning
     * {@code false}, or the specified waiting time elapses.
     *
     * <p>
     * If the callable returns {@code true} then this method returns immediately
     * with the value returned by callable.
     *
     * <p>
     * Any {@code InterruptedException}'s are suppress and logged. Any
     * {@code Exception}'s thrown by callable are propagate as SystemException
     *
     * @param callable callable checked by this method
     * @param timeout the maximum time to wait
     * @param unit the time unit of the {@code timeout} argument
     * @return {@code Boolean} returned by callable method
     * @throws SystemException if callable throws exception
     */
    public static Boolean untilFalse(Callable<Boolean> callable, long timeout, TimeUnit unit) {
        return SleepBuilder.<Boolean>sleep()
                .withComparer(argument -> !argument)
                .withTimeout(timeout, unit)
                .withStatement(callable)
                .build();
    }

    /**
     * Causes the current thread to wait until the callable is returning
     * {@code null}, or the specified waiting time elapses.
     *
     * <p>
     * If the callable returns not null then this method returns immediately
     * with the value returned by callable.
     *
     * <p>
     * Any {@code InterruptedException}'s are suppress and logged. Any
     * {@code Exception}'s thrown by callable are propagate as SystemException
     *
     * @param callable callable checked by this method
     * @param timeout the maximum time to wait
     * @param unit the time unit of the {@code timeout} argument
     * @return value returned by callable method
     * @throws SystemException if callable throws exception
     */
    public static <T> T untilNull(Callable<T> callable, long timeout, TimeUnit unit) {
        return SleepBuilder.<T>sleep()
                .withComparer(argument -> argument == null)
                .withTimeout(timeout, unit)
                .withStatement(callable)
                .build();
    }

    /**
     * Causes the current thread to wait until the callable is not returning
     * {@code null}, or the specified waiting time elapses.
     *
     * <p>
     * If the callable returns {@code null} then this method returns immediately
     * with the value returned by callable.
     *
     * <p>
     * Any {@code InterruptedException}'s are suppress and logged. Any
     * {@code Exception}'s thrown by callable are propagate as SystemException
     *
     * @param callable callable checked by this method
     * @param timeout the maximum time to wait
     * @param unit the time unit of the {@code timeout} argument
     * @return value returned by callable method
     * @throws SystemException if callable throws exception
     */
    public static <T> T untilNotNull(Callable<T> callable, long timeout, TimeUnit unit) {
        return SleepBuilder.<T>sleep()
                .withComparer(argument -> argument != null)
                .withTimeout(timeout, unit)
                .withStatement(callable)
                .build();
    }

    /**
     * Causes the current thread to wait until the callable is returning empty
     * collection, or the specified waiting time elapses.
     *
     * <p>
     * If the callable returns not empty collection then this method returns
     * immediately with the value returned by callable.
     *
     * <p>
     * Any {@code InterruptedException}'s are suppress and logged. Any
     * {@code Exception}'s thrown by callable are propagate as SystemException
     *
     * @param callable callable checked by this method
     * @param timeout the maximum time to wait
     * @param unit the time unit of the {@code timeout} argument
     * @return value returned by callable method
     * @throws SystemException if callable throws exception
     */
    public static <T extends Collection> T untilEmpty(Callable<T> callable, long timeout, TimeUnit unit) {
        return SleepBuilder.<T>sleep()
                .withComparer(argument -> argument.isEmpty())
                .withTimeout(timeout, unit)
                .withStatement(callable)
                .build();
    }

    /**
     * Causes the current thread to wait until the callable is returning not
     * empty collection, or the specified waiting time elapses.
     *
     * <p>
     * If the callable returns empty collection then this method returns
     * immediately with the value returned by callable.
     *
     * <p>
     * Any {@code InterruptedException}'s are suppress and logged. Any
     * {@code Exception}'s thrown by callable are propagate as SystemException
     *
     * @param callable callable checked by this method
     * @param timeout the maximum time to wait
     * @param unit the time unit of the {@code timeout} argument
     * @return value returned by callable method
     * @throws SystemException if callable throws exception
     */
    public static <T extends Collection> T untilNotEmpty(Callable<T> callable, long timeout, TimeUnit unit) {
        return SleepBuilder.<T>sleep()
                .withComparer(argument -> !argument.isEmpty())
                .withTimeout(timeout, unit)
                .withStatement(callable)
                .build();
    }
}
