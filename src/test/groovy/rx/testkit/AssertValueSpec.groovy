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
class AssertValueSpec extends Specification {
    def setup() {
    }

    def "assertValue should fail if no values are emitted"() {
        given:
            def RxTestKitSubscriber<Integer> subscriber = new RxTestKitSubscriber()
            Observable<Integer> o = Observable.empty()
        when:
            o.subscribe(subscriber)
        and:
            subscriber.assertValue(is(1))
        then:
            thrown AssertionError
    }
    def "assertValue should fail if  more than 1 value is emitted"() {
        given:
            def RxTestKitSubscriber<Integer> subscriber = new RxTestKitSubscriber()
            Observable<Integer> o = Observable.from([1,2,3,4,5])
        when:
            o.subscribe(subscriber)
        and:
            subscriber.assertValue(is(1))
        then:
            thrown AssertionError
    }
    def "assertValue should pass if value emitted matches"() {
        given:
            def RxTestKitSubscriber<Integer> subscriber = new RxTestKitSubscriber()
            Observable<Integer> o = Observable.just(1)
        when:
            o.subscribe(subscriber)
        and:
            subscriber.assertValue(is(1))
        then:
            notThrown AssertionError
    }
    def "assertValue should fail if value emitted doesn't match"() {
        given:
        def RxTestKitSubscriber<Integer> subscriber = new RxTestKitSubscriber()
        Observable<Integer> o = Observable.just(1)
        when:
        o.subscribe(subscriber)
        and:
        subscriber.assertValue(is(2))
        then:
        thrown AssertionError
    }

    def "assertValue with reason should fail if no values are emitted"() {
        given:
            def RxTestKitSubscriber<Integer> subscriber = new RxTestKitSubscriber()
            Observable<Integer> o = Observable.empty()
        when:
            o.subscribe(subscriber)
        and:
            subscriber.assertValue("foo", is(1))
        then:
            thrown AssertionError
    }
    def "assertValue with reason should fail if  more than 1 value is emitted"() {
        given:
            def RxTestKitSubscriber<Integer> subscriber = new RxTestKitSubscriber()
            Observable<Integer> o = Observable.from([1,2,3,4,5])
        when:
            o.subscribe(subscriber)
        and:
            subscriber.assertValue("foo", is(1))
        then:
            thrown AssertionError
    }
    def "assertValue with reason should pass if value emitted matches"() {
        given:
            def RxTestKitSubscriber<Integer> subscriber = new RxTestKitSubscriber()
            Observable<Integer> o = Observable.just(1)
        when:
            o.subscribe(subscriber)
        and:
            subscriber.assertValue("foo", is(1))
        then:
            notThrown AssertionError
    }
    def "assertValue with reason should fail if value emitted doesn't match"() {
        given:
            def RxTestKitSubscriber<Integer> subscriber = new RxTestKitSubscriber()
            Observable<Integer> o = Observable.just(1)
        when:
            o.subscribe(subscriber)
        and:
            subscriber.assertValue("foo", is(2))
        then:
            thrown AssertionError
    }

}
