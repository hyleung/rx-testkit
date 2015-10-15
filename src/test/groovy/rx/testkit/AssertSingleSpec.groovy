package rx.testkit

import rx.Observable
import spock.lang.Specification
import rx.Single

import java.util.concurrent.TimeUnit

import static rx.testkit.AssertSingle.assertThat

/**
 * Created with IntelliJ IDEA.
 * Date: 15-09-09
 * Time: 6:29 PM
 * To change this template use File | Settings | File Templates.
 */
class AssertSingleSpec extends Specification {

    def "When asserting value should pass"() {
        given:
            def Single single = Single.just(1)
        when:
            def assertThat = assertThat(single).value()
        then:
            assertThat != null
        and:
            assertThat.isEqualTo(1)
    }
    def "When asserting value should fail"() {
        given:
            def Single single = Single.just(2)
        when:
            assertThat(single)
                    .value()
                    .isEqualTo(1)
        then:
            thrown(AssertionError)
    }
    def "When asserting failure should pass"() {
        def expected = new TestException()
        given:
            def Single single = Single.error(expected)
        when:
            def assertion = assertThat(single)
                    .failures()
        then:
            assertion.contains(expected)
    }
    def "When using 'after', throws exception if no TestScheduler is provided"() {
        given:
            def Single<Integer> single = Observable.just(1).toSingle()
        when:
            assertThat(single).after(100, TimeUnit.MILLISECONDS)
        then:
            thrown(IllegalStateException)
    }
    private static class TestException extends Exception{}
}