package rx.testkit;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import rx.Observable;
import rx.Single;
import rx.observers.TestSubscriber;

/**
 * Created with IntelliJ IDEA.
 * Date: 15-09-09
 * Time: 6:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class AssertSingle<T> extends AbstractAssert<AssertSingle<T>, Single<T>> {
	private final TestSubscriber<T> subscriber;
	private AssertSingle(final Single<T> single) {
		super(single, AssertSingle.class);
		subscriber = new TestSubscriber<>();
		single.toObservable().single().subscribe(subscriber);
	}
	public static <T> AssertSingle<T> assertThat(final Single<T> single) {
		return new AssertSingle<>(single);
	}

	public AbstractObjectAssert<?, ? extends T> value() {
		T value = subscriber.getOnNextEvents().get(0);
		return Assertions.assertThat((T) value);

	}

	public Assert<?, ? extends Throwable> failures() {
		throw new RuntimeException("not implemented");
	}
}
