package rx.testkit;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ListAssert;
import rx.Completable;
import rx.observers.TestSubscriber;
import rx.schedulers.TestScheduler;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * AssertJ {@link org.assertj.core.api.Assert} class for {@link Completable}.
 *
 * Uses {@link TestSubscriber} from Rx to subscribe to {@link Completable} and
 * perform assertions on the results.
 *
 */
public class AssertCompletable extends AbstractAssert<AssertCompletable, Completable> {
  private final TestSubscriber subscriber;
  private TestScheduler scheduler;

  private AssertCompletable(final Completable completable) {
    super(completable, AssertCompletable.class);
    subscriber = new TestSubscriber();
    completable.subscribe(subscriber);
  }

  private AssertCompletable(final Completable completable, final TestScheduler scheduler) {
    super(completable, AssertCompletable.class);
    subscriber = new TestSubscriber();
    this.scheduler = scheduler;
    completable.subscribe(subscriber);
  }

  /**
   * Create an {@link AssertCompletable} instance for a {@link Completable}.
   *
   * @param completable the Completable.
   *
   * @return an AssertCompletable instance.
   */
  public static AssertCompletable assertThat(final Completable completable) {
    return new AssertCompletable(completable);
  }

  /**
   * Create an {@link AssertCompletable} instance for a {@link Completable} with a {@link TestScheduler}.
   *
   * Used for async testing
   *
   * @param completable the Completable
   * @param scheduler the test scheduler
   *
   * @return an AssertCompletable instance
   */
  public static AssertCompletable assertThat(final Completable completable, final TestScheduler scheduler) {
    return new AssertCompletable(completable, scheduler);
  }

  /**
   * If a {@link TestScheduler} is provided, advanced the time by the specified duration.
   *
   * Throws an {@link IllegalStateException} if there is no {@link TestScheduler} provided.
   * Use {@link AssertCompletable#AssertCompletable(Completable, TestScheduler)} to construct
   * an {@link AssertCompletable} instance that can be used for async testing.
   *
   * @param duration the time duration
   * @param timeUnit the time unit of the duration
   *
   * @return the AssertCompletable instance.
   */
  public AssertCompletable after(final long duration, final TimeUnit timeUnit) {
    if (scheduler == null) {
      throw new IllegalStateException("No TestScheduler provided. Perhaps you forgot to 'assertThat(Observable, TestScheduler)'?");
    }
    scheduler.advanceTimeBy(duration, timeUnit);
    return this;
  }

  /**
   * Assert that the {@link AssertCompletable} has completed.
   *
   * @return the AssertCompletable instance
   */
  public AssertCompletable hasCompleted() {
    subscriber.assertCompleted();
    return this;
  }

  /**
   * Assert that the underlying {@link AssertCompletable} has *not* completed.
   *
   * @return the AssertCompletable instance
   */
  public AssertCompletable hasNotCompleted() {
    subscriber.assertNotCompleted();
    return this;
  }

  /**
   * Create a {@link ListAssert} from the error(s) emitted by the {@link Completable}.
   *
   * @return a list assert
   */
  @SuppressWarnings("unchecked")
  public AbstractListAssert<?,? extends List<? extends Throwable>, Throwable> failures() {
    return Assertions.assertThat(subscriber.getOnErrorEvents());
  }

}
