package rx.testkit

import io.reactivex.Maybe
import io.reactivex.schedulers.TestScheduler
import spock.lang.Specification

import java.util.concurrent.TimeUnit

import static rx.testkit.AssertMaybe.assertThat


class AssertAsyncMaybeSpec extends Specification {
    def Maybe<Integer> maybe
    def TestScheduler scheduler

    def setup() {
        scheduler = new TestScheduler()
        maybe = Maybe.just(1).delay(100, TimeUnit.MILLISECONDS, scheduler)
    }

    def "When asserting hasCompleted, should pass with TestScheduler"() {
        when:
            assertThat(maybe, scheduler)
                .after(100, TimeUnit.MILLISECONDS)
                .hasCompleted()
        then:
            notThrown(AssertionError)
    }

    def "When asserting hasNotCompleted, should pass with TestScheduler"() {
        when:
            assertThat(maybe, scheduler)
                    .after(99, TimeUnit.MILLISECONDS)
                    .hasNotCompleted()
        then:
            notThrown(AssertionError)
    }

    def "When asserting value, should pass with TestScheduler"() {
        when:
            assertThat(maybe, scheduler)
                    .after(100, TimeUnit.MILLISECONDS)
                    .value()
                    .isEqualTo(1)
        then:
            notThrown(AssertionError)
    }

    def "When asserting failures, should pass with TestScheduler"() {
        given:
            def expected = new TestException()
            maybe = Maybe.error(expected)
                    .delay(100, TimeUnit.MILLISECONDS, scheduler)
        when:
            assertThat(maybe, scheduler)
                    .after(100, TimeUnit.MILLISECONDS)
                    .failures()
                    .contains(expected)
        then:
            notThrown(AssertionError)
    }

    private static class TestException extends Exception{

    }
}
