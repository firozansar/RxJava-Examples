package info.firozansari.rxandroid_sample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.functions.Consumer

class SimpleExampleActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var resultTxt: TextView
    private lateinit var choiceSpinner: Spinner
    private lateinit var choiceList: List<String>

    private val disposables = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureLayout()
        populateSpinner()
    }

    private fun createSimpleObservable() {
        resultTxt.text = ""
        // Example 1
        val source = Observable.create { emitter: ObservableEmitter<String> ->
            emitter.onNext("Alpha")
            emitter.onNext("Beta")
            emitter.onNext("Gamma")
            emitter.onNext("Delta")
            emitter.onNext("Epsilon")
            emitter.onComplete()
        }
        disposables.add(source.subscribe { value: String ->
            resultTxt.append(" onNext : value : $value \n")
        })
        // we can catch errors that may occur within our
        // Observable.create() block and emit them through onError
        disposables.add(source.subscribe({ s: String -> Log.d(TAG, "RECEIVED: $s") }
        ) { e: Throwable -> Log.d(TAG, " onError : " + e.message) })

        //the subscribe() method is overloaded to accept lambda arguments for our three events. This
        //is likely what we will want to use for most cases, and we can specify three lambda
        //parameters separated by commas: the onNext lambda, the onError lambda, and the
        //onComplete lambda. We can consolidate our three method implementations using these three lambdas:
        val onNext = Consumer { value: String ->
            resultTxt.append(" onNext : value : $value \n")
        }
        val onError = Consumer { e: Throwable ->
            resultTxt.append(" onError : " + e.message)
        }
        val onComplete = Action {
            resultTxt.append(" onComplete \n")
        }
        disposables.add(source.subscribe(onNext, onError, onComplete))

        // Note that there are other overloads for subscribe(). You can omit onComplete() and
        // only implement onNext() and onError(). This will no longer perform any action for
        // onComplete(), but there will likely be cases where you do not need one. However,
        // not implementing onError() is something you want to avoid doing in
        // production. Errors that happen anywhere in the Observable chain will be propagated
        // to onError() to be handled and then terminate the Observable with no more emissions.
        // If you do not specify an action for onError, the error will go unhandled.

        // It is critical to note that most of the subscribe() overload variants (including the
        //shorthand lambda ones we just covered) return a Disposable that we did not do anything
        //with. disposables allow us to disconnect an Observable from an Observer so emissions
        //are terminated early, which is critical for infinite or long-running Observables.

        // it emits 5 followed by the next nine
        //consecutive integers following it (for a grand total of 10 emissions)
        disposables.add(Observable.range(5, 10)
            .subscribe { s: Int -> Log.d(TAG, "RECEIVED: $s") })

        // RxJava Observables are much more robust and expressive than Futures, but if you have
        //existing libraries that yield Futures, you can easily turn them into Observables via
        //Observable.future()
        /*Future<String> futureValue = null;
        disposables.add(Observable.fromFuture(futureValue)
                .map(String::length)
                .subscribe(System.out::println));*/


        // An empty Observable is essentially RxJava's concept of null. It is the absence of a value (or
        //technically, "values"). Empty Observables are much more elegant than nulls because
        //operations will simply continue empty rather than throw NullPointerExceptions.
        val empty = Observable.empty<String>()
        disposables.add(empty.subscribe({ x: String? -> println(x) }, { obj: Throwable -> obj.printStackTrace() }
        ) { Log.d(TAG, "Done!") })


        // A close cousin of Observable.empty() is Observable.never(). The only difference
        //between them is that it never calls onComplete(), forever leaving observers waiting for
        //emissions but never actually giving any
        val never = Observable.never<String>()
        disposables.add(never.subscribe({ x: String? -> println(x) }, { obj: Throwable -> obj.printStackTrace() }
        ) { Log.d(TAG, "Done!") })


        // This too is something you likely will only do with testing, but you can create an
        //Observable that immediately calls onError() with a specified exception
        /*Observable.error(new Exception("Crash and burn!"))
                .subscribe(i -> System.out.println("RECEIVED: " + i),
                        Throwable::printStackTrace,
                        () -> System.out.println("Done!"));*/

        // If you need to perform a calculation or action and then emit it, you can use
        // Observable.just() (or Single.just() or Maybe.just()). But sometimes, we want to do
        // this in a lazy or deferred manner. Also, if that procedure throws an error, we want
        // it to be emitted up the Observable chain through onError() rather than throw the
        // error at that location in traditional Java fashion. For instance, if you try to wrap
        // Observable.just() around an expression that divides 1 by 0, the exception will be thrown,
        // not emitted up to Observer. Perhaps you would like the error to be emitted down the
        // chain to the Observer where it will be handled. If that is the case, use
        // Observable.fromCallable() instead, as it accepts a lambda Supplier<T> and it will emit
        // any error that occurs down to Observer. The error was emitted to the Observer rather than
        // being thrown where it occurred. If initializing your emission has a likelihood of throwing
        // an error, you should use Observable.fromCallable() instead of Observable.just().
        /*disposables.add(Observable.fromCallable(() -> 1 / 0)
                .subscribe(i -> Log.d(TAG,"Received: " + i), e -> Log.d(TAG,"Error Captured: " + e)));*/
    }

    private fun flatmapFilterExample() {
        resultTxt.text = ""
        // Here is another example: let's take a sequence of String values (each a concatenated series
        // of values separated by "/"), use flatMap() on them, and filter for only numeric values
        // before converting them into Integer emissions. We broke up each String by the / character,
        // which yielded an array. We turned that into an Observable and used flatMap() on it to emit
        // each String. We filtered only for String values that are numeric using a regular expression [0-9]+
        // (eliminating FOXTROT, TANGO, and WHISKEY) and then turned each emission into an Integer.
        resultTxt.append(" Initial value : " + "521934/2342/FOXTROT, 21962/12112/78886/TANGO, 283242/4542/WHISKEY/2348562 \n")
        disposables.add(Observable.just("521934/2342/FOXTROT", "21962/12112/78886/TANGO", "283242/4542/WHISKEY/2348562")
            .flatMap { s: String -> Observable.fromArray(*s.split("/").toTypedArray()) }
            .filter { s: String -> s.matches("[0-9]+".toRegex()) } //use regex to filter integers
            .map { s: String? -> Integer.valueOf(s) }
            .subscribe({ value: Int ->
                resultTxt.append(" onNext : value : $value \n")
            },
                { e: Throwable ->
                    resultTxt.append(" onError : " + e.message + "\n")
                }
            ) {
                resultTxt.append(" onComplete ")
            }
        )
    }

    private fun configureLayout() {
        setContentView(R.layout.activity_simple_example)
        resultTxt = findViewById(R.id.result_text)
        choiceSpinner = findViewById(R.id.choice_spinner)
        choiceSpinner.onItemSelectedListener = this
    }

    private fun populateSpinner(){
        choiceList = resources.getStringArray(R.array.operator_array).toList()

        ArrayAdapter.createFromResource(
            this,
            R.array.operator_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            choiceSpinner.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(position){
            0 -> createSimpleObservable()
            1 -> flatmapFilterExample()
            else -> createSimpleObservable()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        resultTxt.text = ""
    }

    companion object {
        val TAG = SimpleExampleActivity::class.java.simpleName
    }
}
