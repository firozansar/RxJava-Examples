package info.firozansari.rxjava.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import info.firozansari.rxjava.R
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.AsyncSubject
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.ReplaySubject

class SubjectActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener  {
    private lateinit var resultTxt: TextView
    private lateinit var choiceSpinner: Spinner
    private lateinit var choiceList: List<String>

    private val disposables = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureLayout()
        populateSpinner()
    }

    private fun configureLayout() {
        setContentView(R.layout.activity_operator)
        resultTxt = findViewById(R.id.result_text)
        choiceSpinner = findViewById(R.id.choice_spinner)
        choiceSpinner.onItemSelectedListener = this
    }

    private fun populateSpinner(){
        choiceList = resources.getStringArray(R.array.subject_array).toList()

        ArrayAdapter.createFromResource(
            this,
            R.array.subject_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            choiceSpinner.adapter = adapter
        }
    }

    /* An AsyncSubject emits the last value (and only the last value) emitted by the source
     * Observable, and only after that source Observable completes. (If the source Observable
     * does not emit any values, the AsyncSubject also completes without emitting any values.)
     */
    private fun createAsyncSubject() {
        resultTxt.text = ""

        val source = AsyncSubject.create<Int>()
        source.subscribe(firstObserver) // it will emit only 4 and onComplete
        source.onNext(1)
        source.onNext(2)
        source.onNext(3)

        /*
         * it will emit 4 and onComplete for second observer also.
         */
        source.subscribe(secondObserver)
        source.onNext(4)
        source.onComplete()
    }

    /* When an observer subscribes to a BehaviorSubject, it begins by emitting the item most
     * recently emitted by the source Observable (or a seed/default value if none has yet been
     * emitted) and then continues to emit any other items emitted later by the source Observable(s).
     * It is different from Async Subject as async emits the last value (and only the last value)
     * but the Behavior Subject emits the last and the subsequent values also.
     */
    private fun createBehaviourSubject() {
        resultTxt.text = ""

        val source = BehaviorSubject.create<Int>()
        source.subscribe(firstObserver) // it will get 1, 2, 3, 4 and onComplete
        source.onNext(1)
        source.onNext(2)
        source.onNext(3)

        /*
         * it will emit 3(last emitted), 4 and onComplete for second observer also.
         */
        source.subscribe(secondObserver)
        source.onNext(4)
        source.onComplete()
    }

   /*  Subject, which is an abstract type that implements both Observable and Observer.
     This means that you can manually call onNext(), onComplete(), and onError() on a Subject,
     and it will, in turn, pass those events downstream toward its Observers.
     The simplest Subject type is the PublishSubject, which, like all Subjects, hotly
     broadcasts to its downstream Observers. Other Subject types add more behaviors.
     PublishSubject emits to an observer only those items that are emitted
     by the source Observable, subsequent to the time of the subscription.
     */
    private fun createPublishSubject() {
       resultTxt.text = ""

       val source = PublishSubject.create<Int>()
       source.subscribe(firstObserver) // it will get 1, 2, 3, 4 and onComplete
       source.onNext(1)
       source.onNext(2)
       source.onNext(3)

       /*
        * it will emit 4 and onComplete for second observer also.
        */source.subscribe(secondObserver)
       source.onNext(4)
       source.onComplete()

       // Complex example
//       val subject: Subject<String> = PublishSubject.create()
//       subject.map { obj: String -> obj.length }
//           .subscribe(firstObserver)
//       subject.onNext("Alpha")
//       subject.onNext("Beta")
//       subject.map { obj: String -> obj.length }
//           .subscribe(secondObserver)
//       subject.onNext("Gamma")
//       subject.onComplete()
//
//       // More likely, you will use Subjects to eagerly subscribe to an unknown number of multiple
//       // source Observables and consolidate their emissions as a single Observable. Since Subjects
//       // are an Observer, you can pass them to a subscribe() method easily. This can be helpful
//       // in modularized code bases where decoupling between Observables and Observers takes
//       // place and executing Observable.merge() is not that easy. Here, I use Subject to merge
//       // and multicast two Observable interval sources.
//       val source1 = Observable.interval(1, TimeUnit.SECONDS)
//           .map { l: Long -> (l + 1).toString() + " seconds" }
//       val source2 = Observable.interval(300, TimeUnit.MILLISECONDS)
//           .map { l: Long -> ((l + 1) * 300).toString() + " milliseconds" }
//       val subject2: Subject<String> = PublishSubject.create()
//       disposables.add(subject2.subscribe({ value: String ->
//           resultTxt.append(" onNext : value : $value")
//           Log.d(TAG, " onNext : value : $value")
//       }
//       ) { e: Throwable ->
//           resultTxt.append(" onError : " + e.message)
//           Log.d(TAG, " onError : " + e.message)
//       }
//       )
//       source1.subscribe(subject2)
//       source2.subscribe(subject2)
//       sleep(3000)
    }

    /* ReplaySubject emits to any observer all of the items that were emitted
     * by the source Observable, regardless of when the observer subscribes.
     */
    private fun createReplaySubject() {
        resultTxt.text = ""

        val source = ReplaySubject.create<Int>()
        source.subscribe(firstObserver) // it will get 1, 2, 3, 4
        source.onNext(1)
        source.onNext(2)
        source.onNext(3)
        source.onNext(4)
        source.onComplete()

        /*
         * it will emit 1, 2, 3, 4 for second observer also as we have used replay
         */
        source.subscribe(secondObserver)
    }

    private val firstObserver: Observer<Int>
        private get() = object : Observer<Int> {
            override fun onSubscribe(d: Disposable) {
                resultTxt.append(" First onSubscribe \n")
                Log.d(TAG, " First onSubscribe : " + d.isDisposed)
            }

            override fun onNext(value: Int) {
                resultTxt.append(" First onNext : value : $value \n")
                Log.d(TAG, " First onNext value : $value")
            }

            override fun onError(e: Throwable) {
                resultTxt.append(" First onError : " + e.message)
                Log.d(TAG, " First onError : " + e.message)
            }

            override fun onComplete() {
                resultTxt.append(" First onComplete \n")
                Log.d(TAG, " First onComplete")
            }
        }
    private val secondObserver: Observer<Int>
        private get() = object : Observer<Int> {
            override fun onSubscribe(d: Disposable) {
                resultTxt.append(" Second onSubscribe \n")
                Log.d(TAG, " Second onSubscribe : " + d.isDisposed)
            }

            override fun onNext(value: Int) {
                resultTxt.append(" Second onNext : value : $value \n")
                Log.d(TAG, " Second onNext value : $value")
            }

            override fun onError(e: Throwable) {
                resultTxt.append(" Second onError : " + e.message)
                Log.d(TAG, " Second onError : " + e.message)
            }

            override fun onComplete() {
                resultTxt.append(" Second onComplete \n")
                Log.d(TAG, " Second onComplete")
            }
        }
    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(position){
            0 -> createAsyncSubject()
            1 -> createBehaviourSubject()
            2 -> createPublishSubject()
            3 -> createReplaySubject()
            else -> createAsyncSubject()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        resultTxt.text = ""
    }

    companion object {
        val TAG = OperatorActivity::class.java.simpleName
    }
}
