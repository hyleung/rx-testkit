package rx.testkit

import io.reactivex.Maybe
import spock.lang.Specification

import java.util.concurrent.TimeUnit

import static rx.testkit.AssertMaybe.assertThat

/**
 * Test suite for {@link AssertMaybe}.
 */
class AssertMaybeSpec extends Specification {
    def "When asserting value should pass"() {
        given:
            def Maybe maybe = Maybe.just(1)
        when:
            def assertThat = assertThat(maybe).value()
        then:
            assertThat != null
        and:
            assertThat.isEqualTo(1)
    }
    def "When asserting no value should pass"() {
        given:
            def Maybe maybe = Maybe.never()
        when:
            def assertThat = assertThat(maybe).noValue()
        then:
            assertThat != null
    }
    def "When asserting value should fail"() {
        given:
            def Maybe maybe = Maybe.just(2)
        when:
            assertThat(maybe)
                    .value()
                    .isEqualTo(1)
        then:
            thrown(AssertionError)
    }
    def "When asserting no value should fail"() {
        given:
           def Maybe maybe = Maybe.just(1)
        when:
            assertThat(maybe).noValue()
        then:
            thrown(AssertionError)
    }
    def "When asserting failure should pass"() {
            def expected = new TestException()
        given:
            def Maybe maybe = Maybe.error(expected)
        when:
            def assertion = assertThat(maybe)
                    .failures()
        then:
            assertion.contains(expected)
    }
    def "When asserting hasCompleted should pass"() {
        given:
        def Maybe maybe = Maybe.just(2)
        when:
        assertThat(maybe)
                .hasCompleted()
        then:
        notThrown(AssertionError)
    }

    def "When asserting hasCompleted should fail"() {
        given:
        def Maybe maybe = Maybe.never()
        when:
        assertThat(maybe)
                .hasCompleted()
        then:
        thrown(AssertionError)
    }

    def "When asserting hasNotCompleted should pass"() {
        given:
            def Maybe maybe = Maybe.never()
        when:
            assertThat(maybe)
                .hasNotCompleted()
        then:
            notThrown(AssertionError)
    }

    def "When asserting hasNotCompleted should fail"() {
        given:
            def Maybe maybe = Maybe.just(1)
        when:
            assertThat(maybe)
                .hasNotCompleted()
        then:
            thrown(AssertionError)
    }
    def "When using 'after', throws exception if no TestScheduler is provided"() {
        given:
            def Maybe<Integer> maybe = Maybe.just(1)
        when:
            assertThat(maybe).after(100, TimeUnit.MILLISECONDS)
        then:
            thrown(IllegalStateException)
    }
    private static class TestException extends Exception{}
}
