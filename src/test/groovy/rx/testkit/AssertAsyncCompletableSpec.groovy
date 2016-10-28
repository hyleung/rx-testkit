package rx.testkit

import io.reactivex.Completable
import io.reactivex.schedulers.TestScheduler
import spock.lang.Specification

import java.util.concurrent.TimeUnit

import static rx.testkit.AssertCompletable.*

/**
 * Async test suite for (@link rx.testkit.AssertCompletable}.
 */
class AssertAsyncCompletableSpec extends Specification {
    def Completable completable
    def TestScheduler scheduler

    def setup() {
        scheduler = new TestScheduler()
        completable = Completable.complete().delay(100, TimeUnit.MILLISECONDS, scheduler)
    }
    def "When asserting hasCompleted, should pass with TestScheduler"() {
        when:
            assertThat(completable, scheduler)
                    .after(100, TimeUnit.MILLISECONDS)
                    .hasCompleted()
        then:
            notThrown(AssertionError)
    }

    def "When asserting hasCompleted, should fail with TestScheduler"() {
        when:
            assertThat(completable, scheduler)
                    .after(99, TimeUnit.MILLISECONDS)
                    .hasCompleted()
        then:
            thrown(AssertionError)
    }
    def "When asserting failures, should pass with TestScheduler"() {
        given:
            def expected = new TestException()
            Completable completable = Completable.
                    error(expected)
                    .delay(100, TimeUnit.MILLISECONDS, scheduler)
        when:
            assertThat(completable, scheduler)
                    .after(100, TimeUnit.MILLISECONDS)
                    .failures()
                    .contains(expected)
        then:
            notThrown(AssertionError)
    }

    private static class TestException extends Exception{

    }
}
