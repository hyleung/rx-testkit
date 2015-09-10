package rx.testkit

import spock.lang.Specification
import rx.Single

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
            def assertThat = AssertSingle.assertThat(single).value()
        then:
            assertThat != null
        and:
            assertThat.isEqualTo(1)
    }
    def "When asserting value should fail"() {
        given:
            def Single single = Single.just(2)
        when:
            AssertSingle
                    .assertThat(single)
                    .value()
                    .isEqualTo(1)
        then:
            thrown(AssertionError)
    }

}