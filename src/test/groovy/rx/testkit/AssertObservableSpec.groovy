package rx.testkit

import rx.Observable
import spock.lang.Specification

import java.util.concurrent.TimeUnit

import static AssertObservable.assertThat

/**
 * Created with IntelliJ IDEA.
 * Date: 15-09-08
 * Time: 6:37 PM
 * To change this template use File | Settings | File Templates.
 */
class AssertObservableSpec extends Specification {

    def setup() {

    }
    def "When asserting hasCompleted, should not throw AssertionError if Observable completes"() {
        given:
            def Observable<Integer> observable = Observable.just(1)
        when:
            assertThat(observable).hasCompleted()
        then:
            noExceptionThrown()
    }
    def "When asserting hasCompleted, should throw AssertionError if Observable fails to complete"() {
        given:
            def Observable<Integer> observable = Observable.never()
        when:
            assertThat(observable).hasCompleted()
        then:
            thrown(AssertionError)
    }
    def "When asserting hasNotCompleted, should throw AssertionError if the Observable completes"() {
        given:
            def Observable observable = Observable.just(1)
        when:
            assertThat(observable).hasNotCompleted()
        then:
            thrown(AssertionError)
    }
    def "When asserting hasNotCompleted, should not throw AssertionError if the Observable never completes"() {
        given:
            def Observable observable = Observable.never()
        when:
            assertThat(observable).hasNotCompleted()
        then:
            notThrown(AssertionError)
    }

    def "When asserting values, should return an Assertion if values are present"() {
        given:
            def Observable<Integer> observable = Observable.just(1)
        when:
            def assertion = assertThat(observable).values()
        then:
            assertion != null
        and:
            assertion.isNotEmpty()
    }
    def "When asserting values, should return an Assertion no values are present"() {

        given:
            def Observable<Integer> observable = Observable.empty()
        when:
            def assertion = assertThat(observable).values()
        then:
            assertion != null
        and:
            assertion.isEmpty()
    }
    def "When asserting values, should return an Assertion Observable doesn't complete"() {
        given:
            def Observable<Integer> observable = Observable.never()
        when:
            def assertThat = assertThat(observable).values()
        then:
            assertThat != null
        and:
            assertThat.isEmpty()
    }


    def "When asserting failures, should return Assertion"() {
        def expected = new TestException()
        given:
            def Observable observable = Observable.error(expected)
        when:
            def assertThat = assertThat(observable).failures()
        then:
            assertThat != null
        and:
            assertThat.contains(expected)
    }
    def "When asserting failures, should return Assertion for no failures"() {
        given:
            def Observable observable = Observable.just(1)
        when:
            def assertThat = assertThat(observable).failures()
        then:
            assertThat != null
        and:
            assertThat.isEmpty()
    }
    def "When using 'after', throws exception if no TestScheduler is provided"() {
        given:
            def Observable<Integer> observable = Observable.just(1)
        when:
            assertThat(observable).after(100, TimeUnit.MILLISECONDS)
        then:
            thrown(IllegalStateException)
    }


    private static class TestException extends Exception{

    }
}