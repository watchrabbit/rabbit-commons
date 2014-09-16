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

import com.watchrabbit.commons.callback.UncheckedCallable;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Mariusz
 */
public final class SystemClock implements Clock {

    private UncheckedCallable<Date> dateCallable = Date::new;

    private UncheckedCallable<Instant> instantCallable = Instant::now;

    private UncheckedCallable<Calendar> calendarCallable = Calendar::getInstance;

    private SystemClock() {
    }

    public static SystemClock getInstance() {
        return new SystemClock();
    }

    @Override
    public Date getDate() {
        return dateCallable.call();
    }

    public SystemClock withDateProducer(UncheckedCallable<Date> producer) {
        this.dateCallable = producer;
        return this;
    }

    @Override
    public long getTimestamp() {
        return dateCallable.call().getTime();
    }

    @Override
    public Instant getInstant() {
        return instantCallable.call();
    }

    public SystemClock withInstantProducer(UncheckedCallable<Instant> producer) {
        this.instantCallable = producer;
        return this;
    }

    @Override
    public Calendar getCalendar() {
        return calendarCallable.call();
    }

    public SystemClock withCalendarProducer(UncheckedCallable<Calendar> producer) {
        this.calendarCallable = producer;
        return this;
    }
}
