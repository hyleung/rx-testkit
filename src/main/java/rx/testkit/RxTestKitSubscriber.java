package rx.testkit;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.StringDescription;
import rx.observers.TestSubscriber;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * Date: 15-08-14
 * Time: 7:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class RxTestKitSubscriber<T> extends TestSubscriber<T> {
	public void assertValueMatching(String reason, Matcher<T> matcher) {
		assertValueCount(1);
		T actual = getOnNextEvents().get(0);
		if (!matcher.matches(actual)){
			throwAssertionError(reason, matcher, actual);
		}
	}
	public void assertValueMatching(Matcher<T> matcher) {
		assertValueMatching(null, matcher);
	}
	public void assertValuesMatching(Matcher<Iterable<T>> matcher) {
		assertValuesMatching(null,matcher);
	}
	public void assertValuesMatching(String reason, Matcher<Iterable<T>> matcher) {
		List<T> actual = getOnNextEvents();
		if (!matcher.matches(actual)) {
			throwAssertionError(reason, matcher, actual);
		}
	}

	private void throwAssertionError(String reason, Matcher matcher, Object actual) {
		Description description = new StringDescription();
		if (reason != null) {
			description.appendText(reason)
					.appendText("\nExpected: ")
					.appendDescriptionOf(matcher)
					.appendText("\n     but: ");
		}
		matcher.describeMismatch(actual, description);
		throw new AssertionError(description.toString());
	}
}
