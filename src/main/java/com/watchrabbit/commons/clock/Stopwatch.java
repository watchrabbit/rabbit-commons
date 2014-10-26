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
package com.watchrabbit.commons.clock;

import com.watchrabbit.commons.callback.VoidCallable;
import com.watchrabbit.commons.exception.SystemException;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Mariusz
 */
public class Stopwatch {

    private VoidCallable callable;

    private Long nanosStart;

    private Long nanosEnd;

    private boolean ended;

    public static Stopwatch create(VoidCallable callable) {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.callable = callable;
        return stopwatch;
    }

    public static Stopwatch createStarted(VoidCallable callable) {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.callable = callable;
        stopwatch.start();
        return stopwatch;
    }

    public Stopwatch start() {
        nanosStart = System.nanoTime();
        callable.call();
        ended = true;
        nanosEnd = System.nanoTime();
        return this;
    }

    public long getExecutionTime(TimeUnit timeUnit) {
        if (!ended) {
            throw new SystemException("Cannot return execution time if Stopwatch wasn't started");
        }
        return timeUnit.convert(nanosEnd - nanosStart, TimeUnit.NANOSECONDS);
    }
}
