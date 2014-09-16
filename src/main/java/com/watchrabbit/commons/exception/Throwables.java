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
package com.watchrabbit.commons.exception;

import com.watchrabbit.commons.callback.CheckedConsumer;
import com.watchrabbit.commons.callback.CheckedPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mariusz
 */
public class Throwables {

    private static final Logger LOGGER = LoggerFactory.getLogger(Throwables.class);

    @FunctionalInterface
    public static interface ExceptionWrapper<E> {

        E wrap(Exception e);
    }

    public static <T> Consumer<T> propagateFromConsumer(CheckedConsumer<T> consumer) throws SystemException {
        return propagate(consumer, SystemException::new);
    }

    public static <T, E extends RuntimeException> Consumer<T> propagate(CheckedConsumer<T> consumer, ExceptionWrapper<E> wrapper) throws E {
        return (T t) -> {
            try {
                consumer.accept(t);
            } catch (Exception e) {
                throw wrapper.wrap(e);
            }
        };

    }

    public static <T> Predicate<T> propagateFromPredicate(CheckedPredicate<T> predicate) throws SystemException {
        return propagate(predicate, SystemException::new);
    }

    public static <T, E extends RuntimeException> Predicate<T> propagate(CheckedPredicate<T> predicate, ExceptionWrapper<E> wrapper) throws E {
        return (T t) -> {
            try {
                return predicate.test(t);
            } catch (Exception e) {
                throw wrapper.wrap(e);
            }
        };

    }

    public static <T> Predicate<T> suppress(boolean defaultValue, CheckedPredicate<T> predicate) {
        return (T t) -> {
            try {
                return predicate.test(t);
            } catch (Exception ex) {
                LOGGER.info("Supressed exception, returning default value", ex);
                return defaultValue;
            }
        };
    }

    public static <T> Consumer<T> suppress(CheckedConsumer<T> consumer) {
        return (T t) -> {
            try {
                consumer.accept(t);
            } catch (Exception ex) {
                LOGGER.info("Supressed exception", ex);
            }
        };
    }
}
