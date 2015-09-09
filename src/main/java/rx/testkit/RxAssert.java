package rx.testkit;

import org.assertj.core.api.AbstractAssert;
import rx.Observable;
import rx.observers.TestSubscriber;

/**
 * Created with IntelliJ IDEA.
 * Date: 15-09-08
 * Time: 10:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class RxAssert<T> extends AbstractAssert<RxAssert<T>, Observable<T>> {
	private final Observable<T> observable;
	protected RxAssert(Observable<T> actual) {
		super(actual, RxAssert.class);
		this.observable = actual;
	}
	public static <T> RxAssert<T> assertThat(Observable<T> observable) {
		return new RxAssert<>(observable);
	}
	public RxAssert<T> hasCompleted() {
		TestSubscriber<T> subscriber = new TestSubscriber<>();
		observable.subscribe(subscriber);
		subscriber.assertCompleted();
		return this;
	}
}
