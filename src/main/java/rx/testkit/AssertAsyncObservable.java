package rx.testkit;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.Assertions;
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.TestScheduler;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * Date: 15-09-10
 * Time: 10:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class AssertAsyncObservable<T> extends AbstractAssert<AssertAsyncObservable<T>, Observable<T>> {
	private final TestSubscriber<T> subscriber;
	private final TestScheduler scheduler;

	private AssertAsyncObservable(Observable<T> observable, TestScheduler scheduler) {
		super(observable, AssertAsyncObservable.class);
		subscriber = new TestSubscriber<>();
		this.scheduler = scheduler;
		observable.subscribe(subscriber);
	}

	public static <T> AssertAsyncObservable<T> assertThat(Observable<T> observable, TestScheduler scheduler) {
		return new AssertAsyncObservable<T>(observable, scheduler);
	}

	public AssertAsyncObservable<T> hasCompleted() {
		subscriber.assertCompleted();
		return this;
	}

	public AssertAsyncObservable<T> hasNotCompleted() {
		subscriber.assertNotCompleted();
		return this;
	}

	public AbstractListAssert<?, ? extends List<? extends T>, T> values() {
		List<T> onNextEvents = subscriber.getOnNextEvents();
		return (AbstractListAssert<?, ? extends List<? extends T>, T>) Assertions.assertThat(onNextEvents);
	}

	public AbstractListAssert<?,? extends List<? extends Throwable>, Throwable> failures() {
		return (AbstractListAssert<?,? extends List<? extends Throwable>, Throwable> )Assertions.assertThat(subscriber.getOnErrorEvents());
	}

	public AssertAsyncObservable<T> after(final long duration, final TimeUnit timeUnit) {
		scheduler.advanceTimeBy(duration, timeUnit);
		return this;
	}
}
