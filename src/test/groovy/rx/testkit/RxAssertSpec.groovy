package rx.testkit

import rx.Observable
import spock.lang.Specification
import static rx.testkit.RxAssert.assertThat

/**
 * Created with IntelliJ IDEA.
 * Date: 15-09-08
 * Time: 6:37 PM
 * To change this template use File | Settings | File Templates.
 */
class RxAssertSpec extends Specification {

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
    def "When asserting withValue, should not throw AssertionError if Observable emits value"() {
        given:
            def Observable<Integer> observable = Observable.just(1)
        when:
            assertThat(observable).withValue(1)
        then:
            noExceptionThrown()
    }
    def "When asserting withValue, should throw AssertionError if Observable emits non-matching value"() {
        given:
            def Observable<Integer> observable = Observable.just(2)
        when:
            assertThat(observable).withValue(1)
        then:
            thrown(AssertionError)
    }
    def "When asserting withValue, should throw AssertionError if Observable does not emit value"() {
        given:
            def Observable<Integer> observable = Observable.never()
        when:
            assertThat(observable).withValue(1)
        then:
            thrown(AssertionError)
    }
}