package rx.testkit;

import org.assertj.core.api.*;
import rx.Observable;
import rx.Single;
import rx.observers.TestSubscriber;
import rx.schedulers.TestScheduler;

import java.util.List;

/**
 * AssertJ {@link org.assertj.core.api.Assert} class for {@link Single}.
 *
 * Uses {@link TestSubscriber} from Rx to subscribe to {@link Single} and
 * perform assertions on the results.
 *
 */
public class AssertSingle<T> extends AbstractAssert<AssertSingle<T>, Single<T>> {
	private final TestSubscriber<T> subscriber;

	private AssertSingle(final Single<T> single) {
		super(single, AssertSingle.class);
		subscriber = new TestSubscriber<>();
		single.toObservable().single().subscribe(subscriber);
	}

	/**
	 * Create an {@link AssertSingle} instance for a {@link Single}.
	 *
	 * @param single the Single.
	 * @param <T> the return type of the Single
	 *
	 * @return an AssertSingle instance.
	 */
	public static <T> AssertSingle<T> assertThat(final Single<T> single) {
		return new AssertSingle<>(single);
	}

	/**
	 * Create an {@link ObjectAssert} from the value emitted by the {@link Single}.
	 *
	 * @return an Object assert
	 */
	public AbstractObjectAssert<?, ? extends T> value() {
		T value = subscriber.getOnNextEvents().get(0);
		return Assertions.assertThat((T) value);

	}

	/**
	 * Create a {@link ListAssert} from the error(s) emitted by the {@link Single}.
	 *
	 * @return a list assert
	 */
	public AbstractListAssert<?,? extends List<? extends Throwable>, Throwable> failures() {
		return Assertions.assertThat(subscriber.getOnErrorEvents());
	}
}
