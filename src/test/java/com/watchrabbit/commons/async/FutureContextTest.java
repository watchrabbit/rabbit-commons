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

import static com.watchrabbit.commons.sleep.Sleep.untilFalse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 *
 * @author Mariusz
 */
public class FutureContextTest {

    @Test
    public void shouldResolveOne() {
        CompletableFuture<CountDownLatch> future = new CompletableFuture<>();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        future.complete(countDownLatch);

        FutureContext.register(future, latch -> latch.countDown());
        FutureContext.resolve();

        assertThat(countDownLatch.getCount()).isEqualTo(0);
    }

    @Test
    public void shouldClearContextOnResolve() {
        CompletableFuture<CountDownLatch> future = new CompletableFuture<>();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        future.complete(countDownLatch);

        FutureContext.register(future, latch -> latch.countDown());
        FutureContext.resolve();

        FutureContext.register(future, latch -> latch.countDown());
        FutureContext.resolve();

        assertThat(countDownLatch.getCount()).isEqualTo(0);
    }

    @Test
    public void shouldBeThreadSingletonAndResolveOnlyOnCurrentThread() {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        CompletableFuture<CountDownLatch> future = new CompletableFuture<>();
        future.complete(countDownLatch);

        FutureContext.register(future, latch -> latch.countDown());

        Executors.newSingleThreadExecutor().submit(() -> {
            CompletableFuture<CountDownLatch> futureInThread = new CompletableFuture<>();
            futureInThread.complete(countDownLatch);

            FutureContext.register(futureInThread, latch -> latch.countDown());
            FutureContext.resolve();
        });

        untilFalse(() -> countDownLatch.getCount() == 1, 1, TimeUnit.SECONDS);
        assertThat(countDownLatch.getCount()).isEqualTo(1);
    }

    @Test
    public void shouldWorkWithDifferentTypes() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CompletableFuture<CountDownLatch> future = new CompletableFuture<>();
        future.complete(countDownLatch);

        CompletableFuture<String> secondFuture = new CompletableFuture<>();
        Holder holder = new Holder();
        secondFuture.complete("Hello world");

        FutureContext.register(future, latch -> latch.countDown());
        FutureContext.register(secondFuture, holder::setValue);
        FutureContext.resolve();

        assertThat(countDownLatch.getCount()).isEqualTo(0);
        assertThat(holder.value).isEqualTo("Hello world");

    }

    public static class Holder {

        private String value;

        public void setValue(String value) {
            this.value = value;
        }
    }
}
