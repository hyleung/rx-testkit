package rx.testkit;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.Assertions;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Date: 15-09-08
 * Time: 10:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class AssertObservable<T> extends AbstractAssert<AssertObservable<T>, Observable<T>> {
	private final TestSubscriber<T> subscriber;
	private AssertObservable(Observable<T> observable) {
		super(observable, AssertObservable.class);
		subscriber = new TestSubscriber<>();
		observable.subscribe(subscriber);
	}
	public static <T> AssertObservable<T> assertThat(Observable<T> observable) {
		return new AssertObservable<>(observable);
	}
	public AssertObservable<T> hasCompleted() {
		subscriber.assertCompleted();
		return this;
	}

	public AssertObservable<T> hasNotCompleted() {
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
}
