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

import com.watchrabbit.commons.callback.ArgumentCallback;
import com.watchrabbit.commons.exception.SystemException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mariusz
 */
public class SleepBuilder<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SleepBuilder.class);

    private long interval = 100;

    private long timeout;

    private ArgumentCallback<Boolean, T> comparer;

    private Callable<T> statement;

    private String name = "sleeper";

    private SleepBuilder() {
    }

    /**
     * Interval is used by {@code SleepBuilder} to sleep between invocations of
     * breaking condition.
     *
     * @param interval used to sleep between invocations of statement
     * @param timeUnit of passed interval
     * @return {@code SleepBuilder} with comparer
     */
    public SleepBuilder<T> withInterval(long interval, TimeUnit timeUnit) {
        this.interval = timeUnit.toMillis(interval);
        return this;
    }

    /**
     * Timeout after with {@code SleepBuilder.build()} is awaked
     *
     * @param timeout after with sleeper should awake
     * @param timeUnit of passed timeout
     * @return {@code SleepBuilder} with comparer
     */
    public SleepBuilder<T> withTimeout(long timeout, TimeUnit timeUnit) {
        this.timeout = timeUnit.toMillis(timeout);
        return this;
    }

    /**
     * Name is used in logs to indicate with sleeper is logging.
     *
     * @param name name of sleeper
     * @return {@code SleepBuilder} with name
     */
    public SleepBuilder<T> withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * {@code SleepBuilder} invokes statement with provided interval and checks
     * with comparer if returned value should stop sleep loop.
     *
     * @param statement to invoke with interval
     * @return {@code SleepBuilder} with statement
     */
    public SleepBuilder<T> withStatement(Callable<T> statement) {
        this.statement = statement;
        return this;
    }

    /**
     * Comparer is used by {@code SleepBuilder} to evaluate if value returned by
     * callback should stop sleep loop.
     *
     * @param comparer to set in builder
     * @return {@code SleepBuilder} with comparer
     */
    public SleepBuilder<T> withComparer(ArgumentCallback<Boolean, T> comparer) {
        this.comparer = comparer;
        return this;
    }

    /**
     * Causes the current thread to wait until the value returned by statement
     * is evaluated by comparer to {@code false}, or the specified waiting time
     * elapses.
     *
     * <p>
     * Condition is checked with interval.
     *
     * <p>
     * Any {@code InterruptedException}'s are suppress and logged. Any
     * {@code Exception}'s thrown by callable are propagate as SystemException
     *
     * @return value returned by callable method
     * @throws SystemException if callable throws exception
     */
    public T build() {
        long start = System.currentTimeMillis();
        long sleepingFor = start + timeout - System.currentTimeMillis();
        T result;
        try {
            result = statement.call();
            while (comparer.call(result) && 0 < sleepingFor) {
                LOGGER.debug("Sleeping on: {}", name);
                LOGGER.debug("Wake up in: {}", sleepingFor);
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException ex) {
                    LOGGER.error("Sleeper: " + name + " interupted!", ex);
                    break;
                }
                sleepingFor = start + timeout - System.currentTimeMillis();
                result = statement.call();
            };
        } catch (Exception ex) {
            LOGGER.error("Callable on sleeper: " + name + " throwed exception!", ex);
            throw new SystemException("Callable on sleeper: " + name + " throwed exception!", ex);
        }
        return result;
    }

    public static <T> SleepBuilder<T> sleep() {
        return new SleepBuilder<>();
    }

}
