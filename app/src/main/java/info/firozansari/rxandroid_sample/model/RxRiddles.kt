package info.firozansari.rxandroid_sample.model

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.IOException
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.SECONDS

fun main() {
    // https://github.com/vanniktech/RxRiddles

    /**
     * Transform the given [value] into an Observable that emits the value and then completes.
     *
     * Use case: You want to transform some value to the reactive world.
     */
    fun solve1(value: Int): Observable<Int> = Observable.just(value)

    /**
     * Increment each emitted value of the given [source] by 1.
     *
     * Use case: You want to transform the data.
     */
    fun solve2(source: Observable<Int>): Observable<Int> = source.map { it + 1 }

    /**
     * Don't emit odd numbers from the [source] Observable.
     *
     * Use case: You want to filter certain items out.
     */
    fun solve3(source: Observable<Int>): Observable<Int> = source.filter { it.rem(2) == 0 }

    /**
     * Implement a toggle mechanism. Initially we want to return false.
     * Every time [source] emits, we want to negate the previous value.
     *
     * Use case: Some button that can toggle two states. For instance a switch between White & Dark theme.
     */
    fun solve4(source: Observable<Unit>): Observable<Boolean> =
        source.scan(false) { toggle, _ -> !toggle }

    /**
     * Sum up the latest values of [first] and [second] whenever one of the Observables emits a new value.
     *
     * Use case: Two input fields in a calculator that need to be summed up and the result should be updated every time one of the inputs change.
     */
    fun solve5(first: Observable<Int>, second: Observable<Int>): Observable<Int> =
        Observable.combineLatest(first, second, BiFunction<Int, Int, Int> { t1, t2 -> t1 + t2 })

    /**
     * Execute both [first] and [second] Single's in parallel and provide both results as a pair.
     * Assume both [first] and [second] will execute on a different thread already.
     * This is a slightly simpler version of [Riddle102], where no schedulers are applied by default.
     *
     * Use case: Execute two network requests in parallel and wait for each other and process the combined data.
     */
    fun solve6(first: Single<Int>, second: Single<Int>): Single<Pair<Int, Int>> =
        Single.zip(first, second, BiFunction<Int, Int, Pair<Int, Int>> { t1, t2 -> t1 to t2 })

    /**
     * When the [source] emits the same value multiple times, only allow the first value to travel downstream.
     *
     * Use case: You never want to show the same value twice.
     */
    fun solve7(source: Observable<Int>): Observable<Int> = source.distinct()

    /**
     * Delay the entire [source] by 200ms. This includes subscribing, emissions and terminal events.
     *
     * Use case: Make an Observable "lazy" for some time. For instance, when wanting to postpone some UI action.
     */
    fun solve8(source: Observable<Unit>): Observable<Unit> =
        source.delaySubscription(200, MILLISECONDS)

    /**
     * As long as the [trigger] Observable does not emit an item, keep the [main] Observable alive.
     *
     * Use case: Cancel an Observable when something has happened. For instance, stop polling when the user has been logged out.
     */
    fun solve9(main: Observable<Unit>, trigger: Observable<Unit>): Observable<Unit> =
        main.takeUntil(trigger)

    /**
     * Use the [first] Observable and flatten it with the results of the [function] that returns an Observable.
     *
     * Use case: Get some user data and perform a network request with the user data and have both data accessible afterwards.
     */
    fun solve10(
        first: Observable<Int>,
        function: (Int) -> Observable<String>
    ): Observable<Pair<Int, String>> = first.flatMap(function) { t1, t2 -> t1 to t2 }

    /**
     * Let the first emission of the [source] within a time window of 300ms travel downstream but don't emit any other events until the next time window.
     *
     * Use case: Handle the click of a button right away but prevent double clicking by not handling multiple click events within a given time window.
     */
    fun solve11(source: Observable<Unit>): Observable<Unit> = source.throttleFirst(300, MILLISECONDS)

    /**
     * In case the [source] Observable emits an error, don't emit the error and instead complete the Observable with a value of 5.
     *
     * Use case: Getting a network error and you want to recover and show some default state.
     */
    fun solve12(source: Observable<Int>): Observable<Int> = source.onErrorReturnItem(5)
    /**
     * When the [source] emits the same value as it did last time, don't allow it to travel downstream.
     *
     * Use case: You only want to observe changes of a value but don't care if the same value has been emitted consecutively.
     */
    fun solve13(source: Observable<Int>): Observable<Int> = source.distinctUntilChanged()

    /**
     * Try the given [source] up to three times unless an [IllegalArgumentException] has been emitted.
     *
     * Use case: Retry an operation for a number of times or until a valid error occurred.
     */
    fun solve14(source: Single<Unit>): Single<Unit> = source.retry(2) { it !is IllegalArgumentException }

    /**
     * Concatenate the [first] Observable with the [second] while subscribing to both early.
     *
     * Use case: You have two sources of your data (cache & network request). You want to subscribe to both right away and keep the emission order.
     */
    fun solve15(first: Observable<Int>, second: Observable<Int>): Observable<Int> = Observable.concatEager(listOf(first, second))

    /**
     * For each emission of the [source] Observable use the [function] and return its value.
     * Dispose all previously non terminated returned Singles from the [function] upon receiving a new emission from [source].
     *
     * Use case: The [source] Observable is a TextField and you want to issue a network request while disposing the old requests in case the user has typed something new.
     */
    fun solve16(source: Observable<String>, function: (String) -> Single<Int>): Observable<Int> = source.switchMapSingle(function)
    /**
     * Return a Single that emits the value from the given [function] when being subscribed to.
     *
     * Use case: Reactive types are lazy by default. Hence you might also want to get the value upon the subscription and not execution time.
     */
    fun solve17(function: () -> Int): Single<Int>  = Single.fromCallable(function)

    /**
     * Return an Observable that mirrors either the [first] or [second] Observable depending on whoever emits or terminates first.
     *
     * Use case: You have multiple sources and want to get the data from either one and then be consistent and not switch between multiple sources.
     */
    fun solve18(first: Observable<Int>, second: Observable<Int>): Observable<Int> = Observable.ambArray(first, second)

    /**
     * Use the given [Interaction] interface and use its listener to transform the [Int] callback to an Observable that emits every time the listener is called.
     * When the Observable is being disposed the listener should be set to null.
     *
     * Use case: Transform any listener into an Observable.
     */
    fun solve19(interaction: Interaction): Observable<Int> = Observable.create<Int> { emitter ->
        interaction.listener = emitter::onNext

        emitter.setCancellable {
            interaction.listener = null
        }
    }

    /**
     * Merge the [first] and [second] Observable together.
     *
     * Use case: There something you want to execute and in your UI you have multiple trigger points.
     */
    fun solve20(first: Observable<Int>, second: Observable<Int>): Observable<Int>  = first.mergeWith(second)

    /**
     * Return the first emission of the [source] in a blocking fashion.
     *
     * Use case: Sometimes you can't do everything reactively and need to break out of it.
     */
    fun solve21(source: Observable<Int>): Int = source.blockingFirst()

    /**
     * Group emissions of the [source] always in a list of 2 elements and skip every third element.
     *
     * Use case: Group related data while skipping over some of it.
     */
    fun solve22(source: Observable<Int>): Observable<List<Int>> = source.buffer(2, 3)

    /**
     * Cast each emission of the [source] from [Any] to [String].
     *
     * Use case: You get some data from a bad source and know for sure it's of a certain type that you require.
     */
    fun solve23(source: Observable<Any>): Observable<String> = source.cast(String::class.java)

    /**
     * Upon completion of the [source] return a Single containing the number of emissions from [source].
     *
     * Use case: Know how many emissions have been sent out.
     */
    fun solve24(source: Observable<Any>): Single<Long> = source.count()

    /**
     * In case the [source] is empty return a default value of 5.
     *
     * Use case: Continue with data if the stream is empty.
     */
    fun solve25(source: Observable<Int>): Observable<Int> = source.defaultIfEmpty(5)

    /**
     * Delay each emission of the [source] by 300 milliseconds.
     *
     * Use case: Delay emission of events to simulate some indication.
     */
    fun solve26(source: Observable<Long>): Observable<Long> = source.delay(300, MILLISECONDS)

    /**
     * Call the given [function] each time the [source] emits a value.
     *
     * Use case: Add some logging.
     */
    fun solve27(source: Observable<Long>, function: (Long) -> Unit): Observable<Long> = source.doOnNext(function)

    /**
     * Call the given [function] when the [source] completes.
     *
     * Use case: Add some logging.
     */
    fun solve28(source: Completable, function: () -> Unit): Completable  = source.doOnComplete(function)

    /**
     * Call the given [function] when the [source] errors.
     *
     * Use case: Add some logging.
     */
    fun solve29(source: Maybe<Int>, function: (Throwable) -> Unit): Maybe<Int> = source.doOnError(function)

    /**
     * Call the given [function] when the [source] is being subscribed to.
     *
     * Use case: Add some logging.
     */
    fun solve30(source: Single<Int>, function: () -> Unit): Single<Int> = source.doOnSubscribe { function.invoke() }

    /**
     * Duplicate the entire [source] three times. After emitting all events three times it should complete.
     *
     * Use case: You want to re-run a certain Observable a number of times.
     */
    fun solve31(source: Observable<Int>): Observable<Int> = source.repeat(3)

    /**
     * Signal a [TimeoutException] when the given [source] does not terminate within 3 seconds.
     *
     * Use case: You want to terminate the given reactive type and stop the operation.
     */
    fun solve32(source: Single<Long>): Single<Long> = source.timeout(3, SECONDS)

    /**
     * Return a Single that emits the value from the [first] source if present, otherwise emit from the [second] source.
     *
     * Use case: You have a local cache and only want to hit the network if the cache misses.
     */
    fun solve33(first: Maybe<String>, second: Single<String>): Single<String> = first.switchIfEmpty(second)

    /**
     * Return an Observable that only emits items from [source] if there isn't another emission before [milliseconds] has passed.
     *
     * Use case: You want the user-input to trigger a search request for the entered text but only when no changes have been made for a pre-determined time to avoid unnecessary requests.
     */
    fun solve34(source: Observable<String>, milliseconds: Long): Observable<String> = source.debounce(milliseconds, MILLISECONDS)

    /**
     * Return an Observable that emits 'false' when the [source] emits an IOException.
     *
     * Use case: You want to recover from an expected error and map them to a particular result.
     */
    fun solve35(source: Observable<Boolean>): Observable<Boolean> = source.onErrorResumeNext { t: Throwable ->
        when (t) {
            is IOException -> Observable.just(false)
            else -> Observable.error(t)
        }
    }

    /**
     * If the [source] emits more than once within 300ms we want to emit [Unit].
     * If there is only one or non emissions within 300ms we don't want to emit anything.
     *
     * Use case: Double click detection mechanism for a button.
     */
    fun solve36(source: Observable<Unit>): Observable<Unit> = source.buffer(300, MILLISECONDS)
        .filter { it.size > 1 }
        .map { }

    /**
     * Create an Observable that acts as a countdown. Counting down from number of [seconds] to 0
     * while emitting the number of seconds remaining each time.
     *
     * Use case: You have some countdown functionality and want to display how many seconds are left.
     */
    fun solve37(seconds: Long): Observable<Long> = Observable.interval(0, 1, SECONDS)
        .map { ticksPassed -> seconds - ticksPassed }
        .takeUntil { it == 0L }

    /**
     * Execute both [first] and [second] Single's in parallel and provide both results as a pair.
     *
     * Use case: Execute two network requests in parallel and wait for each other and process the combined data.
     */
    fun solve38(first: Single<Int>, second: Single<Int>): Single<Pair<Int, Int>> = Single.zip(
        first.subscribeOn(Schedulers.io()),
        second.subscribeOn(Schedulers.io()),
        BiFunction<Int, Int, Pair<Int, Int>> { t1, t2 -> t1 to t2 })
}

interface Interaction {
    var listener: ((Int) -> Unit)?
}