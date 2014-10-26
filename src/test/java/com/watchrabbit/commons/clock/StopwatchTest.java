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

import com.watchrabbit.commons.exception.SystemException;
import static com.watchrabbit.commons.sleep.Sleep.sleep;
import java.util.concurrent.TimeUnit;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 *
 * @author Mariusz
 */
public class StopwatchTest {

    @Test
    public void shouldCalculateTimeOnAutostart() {
        long executionTime = Stopwatch
                .createStarted(() -> sleep(1, TimeUnit.SECONDS))
                .getExecutionTime(TimeUnit.MICROSECONDS);

        assertThat(executionTime).isGreaterThan(1000l);
    }

    @Test
    public void shouldCalculateTimeOnCustomStart() {
        long executionTime = Stopwatch
                .create(() -> sleep(1, TimeUnit.SECONDS))
                .start()
                .getExecutionTime(TimeUnit.MICROSECONDS);

        assertThat(executionTime).isGreaterThan(1000l);
    }

    @Test(expected = SystemException.class)
    public void shouldThrowIfNotStarted() {
        Stopwatch
                .create(() -> sleep(1, TimeUnit.SECONDS))
                .getExecutionTime(TimeUnit.MICROSECONDS);
    }
}
