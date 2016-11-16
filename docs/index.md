---
layout: default
---

# RX TestKit: AssertJ for RxJava [![Build Status](https://travis-ci.org/hyleung/rx-testkit.svg?branch=master)](https://travis-ci.org/hyleung/rx-testkit)

This library provides a set of [AssertJ](http://joel-costigliola.github.io/assertj/) assertions that can be used to make unit testing of [RxJava](https://github.com/ReactiveX/RxJava) code a little easier. 

Javadoc for this library can be found [here](javadoc/index.html).

# Usage

### via Maven:

{% highlight xml %}
<dependency>
    <groupId>com.github.hyleung</groupId>
    <artifactId>rx-testkit-java</artifactId>
    <version>1.1.0</version>
    <scope>test</scope>
</dependency>
{% endhighlight %}

### via Gradle

{% highlight groovy %}
test 'com.github.hyleung:rx-testkit-java:1.1.0'
{% endhighlight %}

## RxJava 2.x

For RxJava 2.x, there's a snapshot dependency available.

### via Maven:

{% highlight xml %}
<repositories>
    <repository>
    <id>snapshots-repo</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <releases><enabled>false</enabled></releases>
    <snapshots><enabled>true</enabled></snapshots>
    </repository>
</repositories>
...
<dependency>
    <groupId>com.github.hyleung</groupId>
    <artifactId>rx-testkit-java</artifactId>
    <version>2.0.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
{% endhighlight %}

### via Gradle

{% highlight groovy %}
repositories {
    maven {
        url "https://oss.sonatype.org/content/repositories/snapshots/"
    } 
}
...
test 'com.github.hyleung:rx-testkit-java:2.0.0-SNAPSHOT'
{% endhighlight %}

## Examples:

Assert that an Observable has completed…

{% highlight java %}
Observable<String> observable = Observable.just("foo");

assertThat(observable)
    .hasCompleted();
{% endhighlight %}

…or hasn't completed:

{% highlight java %}
Observable<String> observable = Observable.just("foo");

assertThat(observable)
.hasNotCompleted();
{% endhighlight %}

…or has values (returns an `AbstractListAssert`)

{% highlight java %}
Observable<String> observable = Observable.just("foo");

assertThat(observable)
    .values()
    .contains("foo");
{% endhighlight %}

…or emits a certain error (also returns an `AbstractListAssert`)

{% highlight java %}
Exception myException = new Exception();

Observable<String> observable = Observable.error(myException);

assertThat(observable)
    .failures()
    .contains(myException);
{% endhighlight %}
    
...or mess around with time using a `TestScheduler`:

{%highlight java %}
TestScheduler scheduler = new TestScheduler();
Observable<Integer> observable = Observable.just(1).delay(99, TimeUnit.MILLISECONDS, scheduler);

assertThat(observable, scheduler)
    .after(100, TimeUnit.MILLISECONDS)
    .values()
    .isNotEmpty();
    {%endhighlight %}

There's also support for `rx.Single` and `rx.Completable`.

