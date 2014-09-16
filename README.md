[![][coverage img]][coverage]
[![][travis img]][travis]

Watchrabbit - Commons
=====================

This library is a mix of useful developer tools, with special emphasis on java8 
syntax.

Sleep
-----

The `Sleep` class provides few methods for sleeping current thread until a 
condition is meet.

### Basic usage

Sleep can be started just one line of code. For example, this method would sleep
current thread until value is equal to false for 500 milliseconds:

```java
   Wrapper<Boolean> wrapper = new Wrapper(false);
   boolean returned = Sleep.untilFalse(() -> wrapper.getValue(), 500, TimeUnit.MILLISECONDS);
```

If other thread modify wrapper value during this 500 millisecond sleep will interrupt.
Sleep method returns last value returned by callback before sleep was interrupted.

### Advanced

Every method in `Sleep` class is a shortcut with produces `SleepBuilder` with 
few default settings. Additional settings in builder are:

* `name` of current sleeper used in logs,
* `interval` used to sleep between invocation of callback method,
* `comparer` used to calculate if argument returned by callback should stop sleep.

For example, this method will sleep for 1000 millisecond unless constructor 
of `Object` return null:

```java
    SleepBuilder.<Object>sleep()
        .withComparer((object) -> object != null)
        .withTimeout(1000, TimeUnit.MILLISECONDS)
        .withInterval(10, TimeUnit.MILLISECONDS)
        .withStatement(Object::new)
        .build();
```

Clock
-----

`SystemClock` improves testability of code by delegation of creation 
time-connected objects. When there is a need of testing time-based code, in unit 
test anyone can mock clock or provide custom callback factory methods. For example:

```java
public class Foo {
    Clock clock = SystemClock.getInstance();

    public void bar() {
        Date date = clock.getDate();
        // do something
        ...
    }
}

...

public class FooTest {
    Foo foo = new Foo();

    public void testBar() {
        Date date = //pre calculated date
        foo.clock = SystemClock.getInstance()
            .withDateProducer(() -> date);

        foo.bar();
    }
}
```


Throwables
----------

The `Throwables` class provides few method that can be used in lambdas for 
propagate or suppress `Exceptions`. For example, this snipped of code that uses 
function with checked exception: 

```java
    void bar(int i) throws Exception {
        // do something
        ...
    }

    void foo(int[] numbers) {
        Stream.of(numbers).forEach(number -> {
            try {
                this.bar(number);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }
```

can be rewritten to one line:
```java
    void bar(int i) throws Exception {
        // do something
        ...
    }

    void foo(int[] numbers) {
        Stream.of(numbers).forEach(Throwables.propagateFromConsumer(this::bar));
    }
```

Developer annotations
---------------------

### Todo

The `@Todo` annotation with `String` value can be used to mark some code as 
uncompleted. The `@Todos` annotation can aggregate `@Todo`. For example:

```java
public class Foo {

    @Todos({
        @Todo("Implement this method"),
        @Todo("Change method parameters")})
    public void bar() {
        // do something
        ...
    }
}
```

### Feature

The `@Feature` annotation has similar role as `@Todo`. `@Feature` can be used to
store some ideas of improvements or refactoring in code. For example:

```java
public class Foo {

    @Features({
        @Feature("Improve implementation"),
        @Feature("Move this method to another class")})
    public void bar() {
        // do something
        ...
    }
}
```

[coverage]:https://coveralls.io/r/watchrabbit/commons
[coverage img]:https://img.shields.io/coveralls/watchrabbit/commons.png
[travis]:https://travis-ci.org/watchrabbit/commons
[travis img]:https://travis-ci.org/watchrabbit/commons.svg?branch=master
