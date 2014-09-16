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

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 *
 * @author Mariusz
 */
public class SystemClockTest {

    @Test
    public void shoudlReturnDate() {
        assertThat(SystemClock.getInstance().getDate()).isNotNull();
    }

    @Test
    public void shoudlReturnTimestamp() {
        assertThat(SystemClock.getInstance().getTimestamp()).isNotNull();
    }

    @Test
    public void shoudlReturnCalendar() {
        assertThat(SystemClock.getInstance().getCalendar()).isNotNull();
    }

    @Test
    public void shoudlReturnInstant() {
        assertThat(SystemClock.getInstance().getInstant()).isNotNull();
    }

    @Test
    public void shoudlReturnProgrammedDate() {
        Date date = new Date();
        SystemClock clock = SystemClock.getInstance().withDateProducer(() -> date);

        assertThat(clock.getDate()).isEqualTo(date);
    }

    @Test
    public void shoudlReturnProgrammedCalendar() {
        Calendar calendar = Calendar.getInstance();
        SystemClock clock = SystemClock.getInstance().withCalendarProducer(() -> calendar);

        assertThat(clock.getCalendar()).isEqualTo(calendar);
    }

    @Test
    public void shoudlReturnProgrammedInstant() {
        Instant instant = Instant.now();
        SystemClock clock = SystemClock.getInstance().withInstantProducer(() -> instant);

        assertThat(clock.getInstant()).isEqualTo(instant);
    }

}
