package rx.testkit

import static org.hamcrest.CoreMatchers.*
import spock.lang.Specification
import rx.Observable
/**
 * Created with IntelliJ IDEA.
 * Date: 15-08-14
 * Time: 7:50 PM
 * To change this template use File | Settings | File Templates.
 */
class RxTestKitSubscriberSpec extends Specification {
    def setup() {
    }
    def "assertValueMatching reason should pass if emitted values match"() {
        given:
            def RxTestKitSubscriber<Integer> subscriber = new RxTestKitSubscriber<>()
            Observable<Integer> o = Observable.from([1,2,3,4,5])
        when:
            o.subscribe(subscriber)
        then:
            subscriber.assertValuesMatching(hasItem(is(3)))
    }
    def "assertValueMatching reason should fail if emitted values don't match"() {
        given:
            def RxTestKitSubscriber<Integer> subscriber = new RxTestKitSubscriber<>()
            Observable<Integer> o = Observable.from([1,2,3,4,5])
        when:
            o.subscribe(subscriber)
        and:
            subscriber.assertValuesMatching(hasItem(is(30)))
        then:
            thrown AssertionError
    }
    def "assertValueMatching with reason should pass if emitted values match"() {
        given:
            def RxTestKitSubscriber<Integer> subscriber = new RxTestKitSubscriber<>()
            Observable<Integer> o = Observable.from([1,2,3,4,5])
        when:
            o.subscribe(subscriber)
        then:
            subscriber.assertValuesMatching("foo", hasItem(is(3)))
    }
    def "assertValueMatching with reason should fail if emitted values don't match"() {
        given:
            def RxTestKitSubscriber<Integer> subscriber = new RxTestKitSubscriber<>()
            Observable<Integer> o = Observable.from([1,2,3,4,5])
        when:
            o.subscribe(subscriber)
        and:
            subscriber.assertValuesMatching("foo", hasItem(is(30)))
        then:
            thrown AssertionError
    }
}
