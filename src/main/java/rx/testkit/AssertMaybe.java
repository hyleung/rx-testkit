package rx.testkit;

import io.reactivex.Maybe;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subscribers.TestSubscriber;
import org.assertj.core.api.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * AssertJ {@link org.assertj.core.api.Assert} class for {@link Maybe}.
 *
 * Uses {@link TestSubscriber} from Rx to subscribe to {@link Maybe} and
 * perform assertions on the results.
 *
 * @param <T> the type of the {@link Maybe}.
 *
 */
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

  /**
   * Create an {@link AssertMaybe} instance for a {@link Maybe}.
   *
   * @param maybe the Maybe.
   * @param <T> the return type of the Maybe
   *
   * @return an AssertMaybe instance.
   */
  public static <T> AssertMaybe<T> assertThat(final Maybe<T> maybe) {
    return new AssertMaybe<>(maybe);
  }

  /**
   * Create an {@link AssertMaybe} instance for a {@link Maybe} with a {@link TestScheduler}.
   *
   * Used for async testing
   *
   * @param maybe the maybe
   * @param scheduler the test scheduler
   * @param <T> the return type of the Maybe
   *
   * @return an AssertMaybe instance
   */
  public static <T> AssertMaybe<T> assertThat(final Maybe<T> maybe, final TestScheduler scheduler) {
    return new AssertMaybe<>(maybe, scheduler);
  }

  /**
   * Create an {@link ObjectAssert} from the value emitted by the {@link Maybe}.
   *
   * @return an Object assert
   */
  public AbstractObjectAssert<?, ? extends T> value() {
    T value = subscriber.values().get(0);
    return Assertions.assertThat(value);
  }

  /**
   * Assert that the {@link Maybe} has emitted no value.
   *
   * @return the AssertMaybe instance
   */
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

  /**
   * Assert that the {@link Maybe} has completed.
   *
   * @return the AssertMaybe instance
   */
  public AssertMaybe<T> hasCompleted() {
    subscriber.assertTerminated();
    return this;
  }

  /**
   * Assert that the {@link Maybe} has NOT completed.
   *
   * @return the AssertMaybe instance
   */
  public AssertMaybe<T> hasNotCompleted() {
    subscriber.assertNotTerminated();
    return this;
  }

  /**
   * If a {@link TestScheduler} is provided, advanced the time by the specified duration.
   *
   * Throws an {@link IllegalStateException} if there is no {@link TestScheduler} provided.
   * Use {@link AssertMaybe#assertThat(Maybe, TestScheduler)} to construct
   * an {@link AssertMaybe} instance that can be used for async testing.
   *
   * @param duration the time duration
   * @param timeUnit the time unit of the duration
   *
   * @return the AssertMaybe instance.
   *
   * @throws IllegalStateException if called when no test scheduler was provided
   */
  public AssertMaybe<T> after(final long duration, final TimeUnit timeUnit) {
    if (scheduler == null) {
      throw new IllegalStateException("No TestScheduler provided. Perhaps you forgot to 'assertThat(Observable, TestScheduler)'?");
    }
    scheduler.advanceTimeBy(duration, timeUnit);
    return this;
  }
}
