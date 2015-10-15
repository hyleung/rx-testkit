package rx.testkit

import rx.Single
import rx.Observable
import rx.schedulers.TestScheduler
import spock.lang.Specification

import java.util.concurrent.TimeUnit

import static rx.testkit.AssertSingle.assertThat


/**
 * Created with IntelliJ IDEA.
 * Date: 15-10-12
 * Time: 6:14 PM
 * To change this template use File | Settings | File Templates.
 */
class AssertAsyncSingleSpec extends Specification {
    def Single<Integer> single
    def TestScheduler scheduler

    def setup() {
        scheduler = new TestScheduler()
        single = Observable.just(1).delay(100, TimeUnit.MILLISECONDS, scheduler).toSingle()
    }
    def "When asserting hasCompleted, should pass with TestScheduler"() {
        when:
            assertThat(single, scheduler)
                    .after(100, TimeUnit.MILLISECONDS)
                    .hasCompleted()
        then:
            notThrown(AssertionError)
    }

    def "When asserting hasNotCompleted, should pass with TestScheduler"() {
        when:
            assertThat(single, scheduler)
                    .after(99, TimeUnit.MILLISECONDS)
                    .hasNotCompleted()
        then:
            notThrown(AssertionError)
    }
    def "When asserting values, should pass with TestScheduler"() {
        when:
            assertThat(single, scheduler)
                    .after(100, TimeUnit.MILLISECONDS)
                    .value()
                    .isEqualTo(1)
        then:
            notThrown(AssertionError)
    }


    def "When asserting failures, should pass with TestScheduler"() {
        given:
            def expected = new TestException()
            single = Observable.
                    error(expected)
                    .delay(100, TimeUnit.MILLISECONDS, scheduler)
                    .toSingle()
        when:
            assertThat(single, scheduler)
                    .after(100, TimeUnit.MILLISECONDS)
                    .failures()
                    .contains(expected)
        then:
        notThrown(AssertionError)
    }
    def "When asserting failures, should fail assertion with TestScheduler"() {
        given:
            def expected = new TestException()
            Single<Long> single = Observable
                .interval(1L, TimeUnit.SECONDS, scheduler)
                .map{l -> if (l == 1L) throw new RuntimeException() else return l}
                .toSingle()
        when:
            assertThat(single, scheduler)
                    .after(1, TimeUnit.SECONDS)
                    .failures()
                    .contains(expected)
        then:
            thrown(AssertionError)
    }
    private static class TestException extends Exception{

    }
}