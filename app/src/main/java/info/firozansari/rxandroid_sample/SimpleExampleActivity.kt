package info.firozansari.rxandroid_sample

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class SimpleExampleActivity : AppCompatActivity() {

    private lateinit var mColorListView: RecyclerView
    private lateinit var mSimpleAdapter: SimpleAdapter

    private lateinit var mCounterDisplay: TextView
    private lateinit var mIncrementButton: Button

    private lateinit var mCounterEmitter: PublishSubject<Int>

    private var mCounter = 0
    private val disposables = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureLayout()
        createObservable()
        createCounterEmitter()
    }

    private fun createObservable() {
        val listObservable: Observable<List<String>> = Observable.just(
            colorList
        )
        listObservable.observeOn(Schedulers.newThread())
        listObservable.subscribeOn(AndroidSchedulers.mainThread())
        listObservable.subscribe(object : Observer<List<String>> {
            override fun onComplete() {
                Log.d(TAG, "onComplete: called.")
            }

            override fun onError(e: Throwable?) {}
            override fun onSubscribe(d: Disposable?) {}
            override fun onNext(colors: List<String>) {
                //This is going to be running in Main thread.
                mSimpleAdapter.setStrings(colors)
            }
        })
    }

    private fun configureLayout() {
        setContentView(R.layout.activity_simple_example)
        mColorListView = findViewById(R.id.color_list)
        mColorListView.layoutManager = LinearLayoutManager(this)
        mSimpleAdapter = SimpleAdapter(this)
        mColorListView.adapter = mSimpleAdapter

        mCounterDisplay = findViewById(R.id.counter_display)
        mCounterDisplay.text = mCounter.toString()

        mIncrementButton = findViewById(R.id.increment_button)
        mIncrementButton.setOnClickListener { onIncrementButtonClick() }
    }

    private fun createCounterEmitter() {
        mCounterEmitter = PublishSubject.create()
        mCounterEmitter.subscribe(object : Observer<Int> {
            override fun onComplete() {}
            override fun onError(e: Throwable?) {}
            override fun onSubscribe(d: Disposable?) {}
            override fun onNext(integer: Int) {
                mCounterDisplay.text = integer.toString()
            }
        })
    }

    private fun onIncrementButtonClick() {
        mCounter++
        mCounterEmitter.onNext(mCounter)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    companion object {
        val TAG = SimpleExampleActivity::class.java.simpleName
        private val colorList: List<String>
            get() {
                val colors = ArrayList<String>()
                colors.add("blue")
                colors.add("green")
                colors.add("red")
                colors.add("black")
                return colors
            }
    }
}
