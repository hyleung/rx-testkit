package rx.testkit

import rx.Observable
import spock.lang.Specification

import static org.hamcrest.CoreMatchers.hasItem
import static org.hamcrest.CoreMatchers.is

/**
 * Specification for {@link RxTestKitSubscriber#assertValues(org.hamcrest.Matcher)}
 */
class AssertValuesSpec extends Specification {
    def RxTestKitSubscriber<Integer> subscriber
    def setup() {
        subscriber = new RxTestKitSubscriber()
    }
    def "assertValues reason should pass if emitted values match"() {
        given:
            Observable<Integer> o = Observable.from([1,2,3,4,5])
        when:
            o.subscribe(subscriber)
        then:
            subscriber.assertValues(hasItem(is(3)))
    }
    def "assertValues reason should fail if emitted values don't match"() {
        given:
            Observable<Integer> o = Observable.from([1,2,3,4,5])
        when:
            o.subscribe(subscriber)
        and:
            subscriber.assertValues(hasItem(is(30)))
        then:
            thrown AssertionError
    }
    def "assertValues with reason should pass if emitted values match"() {
        given:
            Observable<Integer> o = Observable.from([1,2,3,4,5])
        when:
            o.subscribe(subscriber)
        then:
            subscriber.assertValues("foo", hasItem(is(3)))
    }
    def "assertValues with reason should fail if emitted values don't match"() {
        given:
            Observable<Integer> o = Observable.from([1,2,3,4,5])
        when:
            o.subscribe(subscriber)
        and:
            subscriber.assertValues("foo", hasItem(is(30)))
        then:
            thrown AssertionError
    }
}