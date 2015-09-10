package rx.testkit

import rx.Observable
import rx.schedulers.TestScheduler
import spock.lang.Specification

import java.util.concurrent.TimeUnit
import static rx.testkit.AssertAsyncObservable.assertThat


/**
 * Created with IntelliJ IDEA.
 * Date: 15-09-10
 * Time: 10:44 AM
 * To change this template use File | Settings | File Templates.
 */
class AssertAsyncObservableSpec extends Specification {
    def "When asserting hasCompleted, should pass with TestScheduler"() {
        given:
            def TestScheduler scheduler = new TestScheduler()
            def Observable<Integer> observable = Observable.just(1).delay(100, TimeUnit.MILLISECONDS, scheduler)
        when:
            assertThat(observable, scheduler)
                    .after(100, TimeUnit.MILLISECONDS)
                    .hasCompleted()
        then:
            notThrown(AssertionError)
    }

    def "When asserting hasNotCompleted, should pass with TestScheduler"() {
        given:
            def TestScheduler scheduler = new TestScheduler()
            def Observable<Integer> observable = Observable.just(1).delay(100, TimeUnit.MILLISECONDS, scheduler)
        when:
            assertThat(observable, scheduler)
                    .after(99, TimeUnit.MILLISECONDS)
                    .hasNotCompleted()
        then:
            notThrown(AssertionError)
    }
    def "When asserting values, should pass with TestScheduler"() {
        given:
            def TestScheduler scheduler = new TestScheduler()
            def Observable<Integer> observable = Observable.just(1).delay(100, TimeUnit.MILLISECONDS, scheduler)
        when:
            assertThat(observable, scheduler)
                    .after(100, TimeUnit.MILLISECONDS)
                    .values()
                    .isNotEmpty()
        then:
            notThrown(AssertionError)
    }

    def "When asserting values, should fail with TestScheduler"() {
        given:
            def TestScheduler scheduler = new TestScheduler()
            def Observable<Integer> observable = Observable.just(1).delay(100, TimeUnit.MILLISECONDS, scheduler)
        when:
            assertThat(observable, scheduler)
                    .after(99, TimeUnit.MILLISECONDS)
                    .values()
                    .isNotEmpty()
        then:
            thrown(AssertionError)
    }

    def "When asserting failures, should pass with TestScheduler"() {
        given:
            def expected = new TestException()
            def TestScheduler scheduler = new TestScheduler()
            def Observable observable = Observable.
                    error(expected)
                    .delay(100, TimeUnit.MILLISECONDS, scheduler)
        when:
            assertThat(observable, scheduler)
                    .after(100, TimeUnit.MILLISECONDS)
                    .failures()
                    .contains(expected)
        then:
            notThrown(AssertionError)
    }
    def "When asserting failures, should fail assertion with TestScheduler"() {
        given:
            def expected = new TestException()
            def TestScheduler scheduler = new TestScheduler()
            def Observable observable = Observable
                    .error(expected)
                    .delay(100, TimeUnit.MILLISECONDS, scheduler)
        when:
            assertThat(observable, scheduler)
                    .after(10, TimeUnit.MILLISECONDS)
                    .failures()
                    .contains(expected)
        then:
            thrown(AssertionError)
    }
    private static class TestException extends Exception{

    }
}