package rx.testkit;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.Assert;
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
public class RxAssert<T> extends AbstractAssert<RxAssert<T>, Observable<T>> {
	private final TestSubscriber<T> subscriber;
	protected RxAssert(Observable<T> actual) {
		super(actual, RxAssert.class);
		Observable<T> observable = actual;
		subscriber = new TestSubscriber<>();
		observable.subscribe(subscriber);
	}
	public static <T> RxAssert<T> assertThat(Observable<T> observable) {
		return new RxAssert<>(observable);
	}
	public RxAssert<T> hasCompleted() {
		subscriber.assertCompleted();
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
