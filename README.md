#RX TestKit: AssertJ for RxJava [![Build Status](https://travis-ci.org/hyleung/rx-testkit.svg?branch=master)](https://travis-ci.org/hyleung/rx-testkit)

This library provides a set of [AssertJ](http://joel-costigliola.github.io/assertj/) assertions that can be used to make unit testing of [RxJava](https://github.com/ReactiveX/RxJava) code a little easier. 

#Usage

RxTestKit currently supports AssertJ `assertThat` style assertions on `rx.Observable` as well as `rx.Single`, including async testing using `rx.schedulers.TestScheduler`.  

### via Maven:
```
<dependency>
    <groupId>com.github.hyleung</groupId>
    <artifactId>rx-testkit-java</artifactId>
    <version>1.1.0</version>
    <scope>test</scope>
</dependency>
```

### via Gradle
```
test 'com.github.hyleung:rx-testkit-java:1.1.0'
```

##Examples:

Assert that an Observable has completed…
```
Observable<String> observable = Observable.just("foo");

assertThat(observable)
    .hasCompleted();
```

…or hasn't completed:
```
Observable<String> observable = Observable.just("foo");

assertThat(observable)
    .hasNotCompleted();
```

…or has values (returns an `AbstractListAssert`)
```
Observable<String> observable = Observable.just("foo");

assertThat(observable)
    .values()
    .contains("foo");
```
…or emits a certain error (also returns an `AbstractListAssert`)

```
Exception myException = new Exception();

Observable<String> observable = Observable.error(myException);

assertThat(observable)
    .failures()
    .contains(myException);
```

...or mess around with time using a `TestScheduler`:

```
 TestScheduler scheduler = new TestScheduler();
 Observable<Integer> observable = Observable.just(1).delay(99, TimeUnit.MILLISECONDS, scheduler);

 assertThat(observable, scheduler)
         .after(100, TimeUnit.MILLISECONDS)
         .values()
         .isNotEmpty();
```

There's also support for `rx.Single` and `rx.Completable`.
