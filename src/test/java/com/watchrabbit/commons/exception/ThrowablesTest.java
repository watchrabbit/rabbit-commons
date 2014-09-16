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

import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 *
 * @author Mariusz
 */
public class ThrowablesTest {

    @Test(expected = SystemException.class)
    public void shouldPropagateSystemExceptionFromConsumer() {
        Stream.of(1).forEach(
                Throwables.propagateFromConsumer((Integer integer) -> {
                    throw new Exception();
                })
        );
    }

    @Test(expected = SystemException.class)
    public void shouldPropagateSystemExceptionFromPredicate() {
        Stream.of(1).anyMatch(
                Throwables.propagateFromPredicate((Integer integer) -> {
                    throw new Exception();
                })
        );
    }

    @Test
    public void shouldSuppressAndReturnTrue() {
        boolean value = Stream.of(1).anyMatch(
                Throwables.suppress(true, (Integer integer) -> {
                    throw new Exception();
                })
        );

        assertThat(value);
    }

    @Test
    public void shouldSuppress() {
        Stream.of(1).forEach(
                Throwables.suppress((Integer integer) -> {
                    throw new Exception();
                })
        );
    }
}
