package rx.testkit;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subscribers.TestSubscriber;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.Assertions;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * AssertJ {@link org.assertj.core.api.Assert} class for {@link Observable}.
 *
 * Uses {@link TestSubscriber} from Rx to subscribe to and {@link Observable} and
 * perform assertions on the results.
 *
 * Optionally, a {@link TestScheduler} can be used to perform async testing.
 */
public class AssertObservable<T> extends AbstractAssert<AssertObservable<T>, Observable<T>> {
	private final TestObserver<T> subscriber;
	private TestScheduler scheduler;
	private AssertObservable(Observable<T> observable)
	{
		super(observable, AssertObservable.class);
		subscriber = new TestObserver<>();
		observable.subscribe(subscriber);
	}
	private AssertObservable(Observable<T> observable, TestScheduler scheduler) {
		this(observable);
		this.scheduler = scheduler;
	}

	/**
	 * Constructs an {@link AssertObservable} for a given {@link Observable}.
	 *
	 * @param observable the Observable to perform assertions on.
	 * @param <T> the return type of the Observable
	 *
	 * @return an AssertObservable instance
	 */
	public static <T> AssertObservable<T> assertThat(Observable<T> observable) {
		return new AssertObservable<>(observable);
	}

	/**
	 * Constructs an {@link AssertObservable} for a given {@link Observable} with a {@link TestScheduler}.
	 *
	 * Used for async testing.
	 *
	 * @param observable the Observable.
	 * @param scheduler the test scheduler to use
	 * @param <T> the return type of the Observable
	 *
	 * @return an AssertObservable instance.
	 */
	public static <T> AssertObservable<T> assertThat(Observable<T> observable, TestScheduler scheduler) {
		return new AssertObservable<>(observable, scheduler);
	}

	/**
	 * Assert that the underlying {@link Observable} has completed.
	 *
	 * @return the AssertObservable instance
	 */
	public AssertObservable<T> hasCompleted() {
		subscriber.assertTerminated();
		return this;
	}

	/**
	 * Assert that the underlying {@link Observable} has *not* completed.
	 *
	 * @return the AssertObservable instance
	 */
	public AssertObservable<T> hasNotCompleted() {
		subscriber.assertNotTerminated();
		return this;
	}

	/**
	 * Create a {@link org.assertj.core.api.ListAssert} for the values emitted.
	 *
	 * @return a ListAssert instance
	 */
	public AbstractListAssert<?, ? extends List<? extends T>, T> values() {
		List<T> onNextEvents = subscriber.values();
		return Assertions.assertThat(onNextEvents);
	}

	/**
	 * Create a {@link org.assertj.core.api.ListAssert} for the failures emitted.
	 *
	 * @return a ListAssert instance
	 */
	public AbstractListAssert<?,? extends List<? extends Throwable>, Throwable> failures() {
		return Assertions.assertThat(subscriber.errors());
	}

	/**
	 * If a {@link TestScheduler} is provided, advanced the time by the specified duration.
	 *
	 * Throws an {@link IllegalStateException} if there is no {@link TestScheduler} provided.
	 * Use {@link AssertObservable#AssertObservable(Observable, TestScheduler)} to construct
	 * an {@link AssertObservable} instance that can be used for async testing.
	 *
	 * @param duration the time duration
	 * @param timeUnit the time unit of the duration
	 *
	 * @return the AssertObservableInstance.
	 */
	public AssertObservable<T> after(final long duration, final TimeUnit timeUnit) {
		if (scheduler == null) {
			throw new IllegalStateException("No TestScheduler provided. Perhaps you forgot to 'assertThat(Observable, TestScheduler)'?");
		}
		scheduler.advanceTimeBy(duration, timeUnit);
		return this;
	}
}
