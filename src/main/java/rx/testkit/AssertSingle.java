package rx.testkit;

import io.reactivex.observers.TestObserver;
import org.assertj.core.api.*;
import io.reactivex.Single;
import io.reactivex.subscribers.TestSubscriber;
import io.reactivex.schedulers.TestScheduler;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * AssertJ {@link org.assertj.core.api.Assert} class for {@link Single}.
 *
 * Uses {@link TestSubscriber} from Rx to subscribe to {@link Single} and
 * perform assertions on the results.
 *
 */
public class AssertSingle<T> extends AbstractAssert<AssertSingle<T>, Single<T>> {
	private final TestObserver<T> subscriber;
	private TestScheduler scheduler;

	private AssertSingle(final Single<T> single) {
		super(single, AssertSingle.class);
		subscriber = new TestObserver<>();
		single.subscribe(subscriber);
	}

	private AssertSingle(final Single<T> single, final TestScheduler scheduler) {
		super(single, AssertSingle.class);
		subscriber = new TestObserver<>();
		this.scheduler = scheduler;
		single.subscribe(subscriber);
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
	 * Create an {@link AssertSingle} instance for a {@link Single} with a {@link TestScheduler}.
	 *
	 * Used for async testing
	 *
	 * @param single the single
	 * @param scheduler the test scheduler
	 * @param <T> the return type of the Single
	 *
	 * @return an AssertSingle instance
	 */
	public static <T> AssertSingle<T> assertThat(final Single<T> single, TestScheduler scheduler) {
		return new AssertSingle<T>(single, scheduler);
	}

	/**
	 * Create an {@link ObjectAssert} from the value emitted by the {@link Single}.
	 *
	 * @return an Object assert
	 */
	public AbstractObjectAssert<?, ? extends T> value() {
		T value = subscriber.values().get(0);
		return Assertions.assertThat((T) value);

	}

	/**
	 * Create a {@link ListAssert} from the error(s) emitted by the {@link Single}.
	 *
	 * @return a list assert
	 */
	public AbstractListAssert<?,? extends List<? extends Throwable>, Throwable> failures() {
		return Assertions.assertThat(subscriber.errors());
	}

	/**
	 * If a {@link TestScheduler} is provided, advanced the time by the specified duration.
	 *
	 * Throws an {@link IllegalStateException} if there is no {@link TestScheduler} provided.
	 * Use {@link AssertSingle#AssertSingle(Single, TestScheduler)} to construct
	 * an {@link AssertSingle} instance that can be used for async testing.
	 *
	 * @param duration the time duration
	 * @param timeUnit the time unit of the duration
	 *
	 * @return the AssertSingle instance.
	 */
	public AssertSingle<T> after(final long duration, final TimeUnit timeUnit) {
		if (scheduler == null) {
			throw new IllegalStateException("No TestScheduler provided. Perhaps you forgot to 'assertThat(Observable, TestScheduler)'?");
		}
		scheduler.advanceTimeBy(duration, timeUnit);
		return this;
	}

	/**
	 * Assert that the {@link Single} has completed.
	 *
	 * @return the AssertSingle instance
	 */
	public AssertSingle<T> hasCompleted() {
		subscriber.isTerminated();
		return this;
	}

	/** Assert that the {@link Single} has *not* completed.
	 *
	 * @return the AssertSingle instance
	 */
 	public AssertSingle<T> hasNotCompleted() {
		subscriber.assertNotTerminated();
		return this;
	}
}
