package rx.testkit

import spock.lang.Specification
import rx.Observable
import static org.hamcrest.CoreMatchers.*
/**
 * Created with IntelliJ IDEA.
 * Date: 15-08-15
 * Time: 2:13 PM
 * To change this template use File | Settings | File Templates.
 */
class AssertErrorSpec extends Specification{
    def RxTestKitSubscriber<Integer> subscriber
    def setup() {
        subscriber = new RxTestKitSubscriber<>()
    }
    def "assertError should fail if no errors are emitted"() {
        given:
            def Observable<Integer> observable = Observable.just(1)
        and:
            observable.subscribe(subscriber)
        when:
            subscriber.assertError(any(Throwable))
        then:
            thrown AssertionError
    }
    def "assertError should pass if matching error is emitted"() {
        given:
            def Observable<Integer> observable = Observable.error(new CustomException())
        and:
            observable.subscribe(subscriber)
        when:
            subscriber.assertError(any(CustomException))
        then:
            notThrown AssertionError
    }
    def "assertError should fail if non-matching error is emitted"() {
        given:
            def Observable<Integer> observable = Observable.error(new Exception())
        and:
            observable.subscribe(subscriber)
        when:
            subscriber.assertError(any(CustomException))
        then:
            thrown AssertionError
    }
    private static class CustomException extends Throwable{}
}
