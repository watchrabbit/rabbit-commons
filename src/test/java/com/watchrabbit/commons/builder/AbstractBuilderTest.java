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
package com.watchrabbit.commons.builder;

import java.util.concurrent.CountDownLatch;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 *
 * @author Mariusz
 */
public class AbstractBuilderTest {

    @Test
    public void shouldCallCreatorMethod() {
        CountDownLatch latch = new CountDownLatch(1);
        new SomeItemBuilder()
                .build(object -> latch.countDown());

        assertThat(latch.getCount()).isEqualTo(0);
    }

    public class SomeItemBuilder extends AbstractBuilder<Object> {

    }
}
