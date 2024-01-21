package info.firozansari.rxandroid_sample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class SimpleExampleActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var resultTxt: TextView
    private lateinit var choiceSpinner: Spinner
    private lateinit var choiceList: List<String>

    private lateinit var mCounterEmitter: PublishSubject<Int>

    private var mCounter = 0
    private val disposables = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureLayout()
        populateSpinner()
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
                //mSimpleAdapter.setStrings(colors)
            }
        })
    }

    private fun configureLayout() {
        setContentView(R.layout.activity_simple_example)
        resultTxt = findViewById(R.id.result_text)
        choiceSpinner = findViewById(R.id.choice_spinner)
        choiceSpinner.onItemSelectedListener = this
    }

    private fun createCounterEmitter() {
        mCounterEmitter = PublishSubject.create()
        mCounterEmitter.subscribe(object : Observer<Int> {
            override fun onComplete() {}
            override fun onError(e: Throwable?) {}
            override fun onSubscribe(d: Disposable?) {}
            override fun onNext(integer: Int) {
                //mCounterDisplay.text = integer.toString()
            }
        })
    }

    private fun populateSpinner(){
        choiceList = resources.getStringArray(R.array.planets_array).toList()

        ArrayAdapter.createFromResource(
            this,
            R.array.planets_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            choiceSpinner.adapter = adapter
        }
    }

    private fun onIncrementButtonClick() {
        mCounter++
        mCounterEmitter.onNext(mCounter)
        resultTxt.text = mCounter.toString()
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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val value = parent?.getItemAtPosition(position).toString()
        resultTxt.text = value
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        resultTxt.text = "Nothing selected"
    }
}
