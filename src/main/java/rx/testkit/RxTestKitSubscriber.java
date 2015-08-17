package rx.testkit;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import rx.observers.TestSubscriber;

import java.util.List;


/**
 * Extends the stock {@link TestSubscriber} to add a few more assertion methods.
 */
public class RxTestKitSubscriber<T> extends TestSubscriber<T> {
	/**
	 * Assert that a *single* matching value was emitted.
	 *
	 * If more than one value was emitted, the assertion fails.
	 *
	 * @param reason the reason.
	 * @param matcher the matcher.
	 */
	public void assertValue(String reason, Matcher<T> matcher) {
		assertValueCount(1);
		T actual = getOnNextEvents().get(0);
		if (!matcher.matches(actual)){
			throwAssertionError(reason, matcher, actual);
		}
	}

	/**
	 * Assert that a *single* matching value was emitted.
	 *
	 * If more than one value was emitted, the assertion fails.
	 *
	 * @param matcher the matcher.
	 */
	public void assertValue(Matcher<T> matcher) {
		assertValue(null, matcher);
	}

	/**
	 * Assert that all values emitted match the provided Matcher.
	 *
	 * @param matcher the matcher.
	 */
	public void assertValues(Matcher<Iterable<T>> matcher) {
		assertValues(null, matcher);
	}

	/**
	 * Assert that all values emitted match the provided Matcher.
	 *
	 * @param reason the reason.
	 * @param matcher the matcher.
	 */
	public void assertValues(String reason, Matcher<Iterable<T>> matcher) {
		List<T> actual = getOnNextEvents();
		if (!matcher.matches(actual)) {
			throwAssertionError(reason, matcher, actual);
		}
	}

	/**
	 * Assert that the error a matching error is emittted.
	 *
	 * If *no* errors are emitted, the assertion fails.
	 *
	 * @param matcher the matcher.
	 */
	public void assertError(Matcher<Throwable> matcher) {
		assertError(null, matcher);
	}

	/**
	 * Assert that the error a matching error is emittted.
	 *
	 * If *no* errors are emitted, the assertion fails.
	 *
	 * @param reason the reason.
	 * @param matcher the matcher.
	 */
	public void assertError(String reason, Matcher<Throwable> matcher) {
		List<Throwable> onErrorEvents = getOnErrorEvents();
		if (onErrorEvents.isEmpty()) {
			throw new AssertionError("No error events were emitted");
		}
		Throwable actual = onErrorEvents.get(0);
		if  (!matcher.matches(actual)) {
			throwAssertionError(reason, matcher, actual);
		};
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
