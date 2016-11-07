package rx.testkit;

import io.reactivex.Maybe;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assertions;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AssertMaybe<T>  extends AbstractAssert<AssertMaybe<T>, Maybe> {
  private final TestObserver<T> subscriber;
  private TestScheduler scheduler;

  private AssertMaybe(final Maybe<T> maybe) {
    super(maybe, AssertMaybe.class);
    subscriber = new TestObserver<>();
    maybe.subscribe(subscriber);
  }

  private AssertMaybe(final Maybe<T> maybe, final TestScheduler scheduler) {
    super(maybe, AssertMaybe.class);
    subscriber = new TestObserver<>();
    this.scheduler = scheduler;
    maybe.subscribe(subscriber);
  }

  public static <T> AssertMaybe<T> assertThat(final Maybe<T> maybe) {
    return new AssertMaybe<>(maybe);
  }

  public static <T> AssertMaybe<T> assertThat(final Maybe<T> maybe, final TestScheduler scheduler) {
    return new AssertMaybe<>(maybe, scheduler);
  }

  public AbstractObjectAssert<?, ? extends T> value() {
    T value = subscriber.values().get(0);
    return Assertions.assertThat(value);
  }

  public AssertMaybe<T> noValue() {
    int count = subscriber.valueCount();
    if (count != 0) {
      throw new AssertionError("Expecting no values but found " + count);
    }
    return this;
  }

  public AbstractListAssert<?,? extends List<? extends Throwable>, Throwable> failures() {
    return Assertions.assertThat(subscriber.errors());
  }

  public AssertMaybe<T> hasCompleted() {
    subscriber.assertTerminated();
    return this;
  }

  public AssertMaybe<T> hasNotCompleted() {
    subscriber.assertNotTerminated();
    return this;
  }

  public AssertMaybe<T> after(final long duration, final TimeUnit timeUnit) {
    if (scheduler == null) {
      throw new IllegalStateException("No TestScheduler provided. Perhaps you forgot to 'assertThat(Observable, TestScheduler)'?");
    }
    scheduler.advanceTimeBy(duration, timeUnit);
    return this;
  }
}
