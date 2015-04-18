/*
 * Copyright 2015 Mariusz.
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
package com.watchrabbit.commons.async;

import com.watchrabbit.commons.exception.SystemException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mariusz
 */
public class FutureContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(FutureContext.class);

    private static final ThreadLocal<FutureContext> threadLocal = new ThreadLocal<FutureContext>() {
        @Override
        protected FutureContext initialValue() {
            return new FutureContext();
        }
    };

    private final Map<Future, Map.Entry<Consumer, Long>> calls = new HashMap<>();

    private FutureContext() {
    }

    /**
     * Adds new {@code Future} and {@code Consumer} to the context of this
     * thread. To resolve this future and invoke the result consumer use method
     * {@link resolve()}
     *
     * @param <T> type of {@code future} and {@code consumer}
     * @param future {@code future} that returns argument of type {@code <T>}
     * used by {@code consumer}
     * @param consumer {@code consumer} of object obtained from {@code future}
     */
    public static <T> void register(Future<T> future, Consumer<T> consumer) {
        LOGGER.debug("Registering new future {} and consumer {}", future, consumer);
        getFutureContext().add(future, consumer);
    }

    /**
     * Adds new {@code Future} and {@code Consumer} to the context of this
     * thread. To resolve this future and invoke the result consumer use method
     * {@link resolve()} Use this method to specify maximum {@code timeout} used
     * when obtaining object from {@code future}
     *
     * @param <T> type of {@code future} and {@code consumer}
     * @param future {@code future} that returns argument of type {@code <T>}
     * used by {@code consumer}
     * @param consumer {@code consumer} of object obtained from {@code future}
     * @param timeout the maximum time to wait
     * @param timeUnit the time unit of the {@code timeout} argument
     */
    public static <T> void register(Future<T> future, Consumer<T> consumer, long timeout, TimeUnit timeUnit) {
        LOGGER.debug("Registering new future {} and consumer {} with timeout {} {}", future, consumer, timeout, timeUnit);
        getFutureContext().add(future, consumer, timeout, timeUnit);
    }

    /**
     * Resolves all registered {@code Future}'s to this thread and invokes all
     * {@code Consumer}'s using retrieved result. This method wraps every
     * exception thrown by {@code future} in {@link SystemException}.
     *
     * Regardless of success or exception every registered {@code future} and
     * {@code consumer} are removed from context.
     */
    public static void resolve() {
        try {
            getFutureContext().resolveAll();
        } catch (ExecutionException | InterruptedException | TimeoutException ex) {
            LOGGER.error("Exception during resolving", ex);
            throw new SystemException("Error thrown during resolve", ex);
        }
    }

    private static FutureContext getFutureContext() {
        return threadLocal.get();
    }

    private <T> void add(Future<T> future, Consumer<T> consumer) {
        this.calls.put(future, new AbstractMap.SimpleEntry<>(consumer, null));
    }

    private <T> void add(Future<T> future, Consumer<T> consumer, long timeout, TimeUnit timeUnit) {
        this.calls.put(future, new AbstractMap.SimpleEntry<>(consumer, timeUnit.toMillis(timeout)));
    }

    private void resolveAll() throws InterruptedException, ExecutionException, TimeoutException {
        try {
            for (Future future : calls.keySet()) {
                LOGGER.debug("Resolving future {}", future);
                Map.Entry<Consumer, Long> value = calls.get(future);
                Object result;
                if (value.getValue() == null) {
                    LOGGER.debug("Future {} registered without timeout, retrieving the result", future);
                    result = future.get();
                } else {
                    LOGGER.debug("Future {} registered with timeout, retrieving the result with timeout {} MILISECONDS", future, value.getValue());
                    result = future.get(value.getValue(), TimeUnit.MILLISECONDS);
                }
                LOGGER.debug("Invoking consumer of result {}", result);
                value.getKey().accept(result);

            }
        } finally {
            LOGGER.debug("Clearing registered calls");
            calls.clear();
        }
    }
}
