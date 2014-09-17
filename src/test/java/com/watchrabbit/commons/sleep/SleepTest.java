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

import com.watchrabbit.commons.wrapper.Wrapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 *
 * @author Mariusz
 */
public class SleepTest {

    @Test(timeout = 1000)
    public void shoudlStopBeforeTestTimeoutWaitnigOnTrue() {
        long currentTimeMillis = System.currentTimeMillis();

        Sleep.untilTrue(() -> true, 500, TimeUnit.MILLISECONDS);

        assertThat(System.currentTimeMillis()).isGreaterThan(currentTimeMillis + 500);
    }

    @Test
    public void shoudlReturnCalculatedValueTrue() {
        boolean value = Sleep.untilTrue(() -> true, 10, TimeUnit.MILLISECONDS);

        assertThat(value);
    }

    @Test(timeout = 1000)
    public void shoudlStopBeforeTestTimeoutWaitnigOnFalse() {
        long currentTimeMillis = System.currentTimeMillis();

        Sleep.untilFalse(() -> false, 500, TimeUnit.MILLISECONDS);

        assertThat(System.currentTimeMillis()).isGreaterThan(currentTimeMillis + 500);
    }

    @Test
    public void shoudlReturnCalculatedValueFalse() {
        boolean value = Sleep.untilFalse(() -> false, 10, TimeUnit.MILLISECONDS);

        assertThat(value).isFalse();
    }

    @Test(timeout = 1000)
    public void shoudlStopBeforeTestTimeoutWaitnigOnEmpty() {
        long currentTimeMillis = System.currentTimeMillis();

        Sleep.untilEmpty(Arrays::asList, 500, TimeUnit.MILLISECONDS);

        assertThat(System.currentTimeMillis()).isGreaterThan(currentTimeMillis + 500);
    }

    @Test
    public void shoudlReturnCalculatedValueEmptyList() {
        List list = Collections.EMPTY_LIST;
        List returnedList = Sleep.untilEmpty(() -> list, 10, TimeUnit.MILLISECONDS);

        assertThat(list).isEqualTo(returnedList);
    }

    @Test(timeout = 1000)
    public void shoudlStopBeforeTestTimeoutWaitnigOnNotEmpty() {
        long currentTimeMillis = System.currentTimeMillis();

        Sleep.untilNotEmpty(() -> Arrays.asList(""), 500, TimeUnit.MILLISECONDS);

        assertThat(System.currentTimeMillis()).isGreaterThan(currentTimeMillis + 500);
    }

    @Test
    public void shoudlReturnCalculatedValueNotEmptyList() {
        List list = Arrays.asList("");
        List returnedList = Sleep.untilNotEmpty(() -> list, 10, TimeUnit.MILLISECONDS);

        assertThat(list).isEqualTo(returnedList);
    }

    @Test(timeout = 1000)
    public void shoudlStopBeforeTestTimeoutWaitnigOnNull() {
        long currentTimeMillis = System.currentTimeMillis();

        Sleep.untilNull(() -> null, 500, TimeUnit.MILLISECONDS);

        assertThat(System.currentTimeMillis()).isGreaterThan(currentTimeMillis + 500);
    }

    @Test
    public void shoudlReturnCalculatedValueNull() {
        Object value = Sleep.untilNull(() -> null, 10, TimeUnit.MILLISECONDS);

        assertThat(value).isNull();
    }

    @Test(timeout = 1000)
    public void shoudlStopBeforeTestTimeoutWaitnigOnNotNull() {
        long currentTimeMillis = System.currentTimeMillis();

        Sleep.untilNotNull(Object::new, 500, TimeUnit.MILLISECONDS);

        assertThat(System.currentTimeMillis()).isGreaterThan(currentTimeMillis + 500);
    }

    @Test
    public void shoudlReturnCalculatedValueNotNull() {
        Object value = new Object();
        Object returnedValue = Sleep.untilNotNull(() -> value, 10, TimeUnit.MILLISECONDS);

        assertThat(value).isEqualTo(returnedValue);
    }

    @Test(timeout = 1000)
    public void shoudlBreakWaiting() {
        Wrapper<Boolean> value = new Wrapper<>(Boolean.TRUE);
        Executors.newSingleThreadScheduledExecutor()
                .schedule(() -> {
                    value.setValue(Boolean.FALSE);
                }, 500, TimeUnit.MILLISECONDS);
        Sleep.untilTrue(() -> value.getValue(), 1000, TimeUnit.MILLISECONDS);

        assertThat(value.getValue()).isFalse();
    }
}
