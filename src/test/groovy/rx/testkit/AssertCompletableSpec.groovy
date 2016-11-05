package rx.testkit

import io.reactivex.Completable
import spock.lang.Specification

import java.util.concurrent.TimeUnit

import static rx.testkit.AssertCompletable.assertThat

/**
 * Test suite for (@link rx.testkit.AssertCompletable}.
 */
class AssertCompletableSpec extends Specification {
    def "When asserting completed should pass"() {
        given:
            def Completable completable = Completable.complete()
        when:
            def assertThat = assertThat(completable).hasCompleted()
        then:
            assertThat != null
        and:
            noExceptionThrown()
    }
    def "When asserting completed should throw AssertionError"() {
        given:
            def Completable completable = Completable.never()
        when:
            assertThat(completable).hasCompleted()
        then:
            thrown(AssertionError)
    }
    def "When asserting not completed should pass"() {
        given:
            def Completable completable = Completable.never()
        when:
            def assertThat = assertThat(completable).hasNotCompleted()
        then:
            assertThat != null
        and:
            noExceptionThrown()
    }
    def "When asserting not completed should throw AssertionError"() {
        given:
            def Completable completable = Completable.complete()
        when:
            assertThat(completable).hasNotCompleted()
        then:
            thrown(AssertionError)
    }
    def "When asserting failure should pass"() {
        def expected = new TestException()
        given:
            def Completable completable = Completable.error(expected)
        when:
            def assertion = assertThat(completable)
                    .failures()
        then:
            assertion.contains(expected)
    }
    def "When using 'after', throws exception if no TestScheduler is provided"() {
        given:
            def Completable completable = Completable.complete()
        when:
                assertThat(completable).after(100, TimeUnit.MILLISECONDS)
        then:
            thrown(IllegalStateException)
    }
    private static class TestException extends Exception{}
}
