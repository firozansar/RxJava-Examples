package info.firozansari.rxandroid_sample

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject

class Example4Activity : AppCompatActivity() {
    private var mCounterDisplay: TextView? = null
    private var mIncrementButton: Button? = null
    private lateinit var mCounterEmitter: PublishSubject<Int>
    private var mCounter = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureLayout()
        createCounterEmitter()
    }

    private fun createCounterEmitter() {
        mCounterEmitter = PublishSubject.create()
        mCounterEmitter.subscribe(object : Observer<Int> {
            override fun onComplete() {}
            override fun onError(e: Throwable?) {}
            override fun onSubscribe(d: Disposable?) {}
            override fun onNext(integer: Int) {
                mCounterDisplay?.text = integer.toString()
            }
        })
    }

    private fun configureLayout() {
        setContentView(R.layout.activity_example_4)
        configureCounterDisplay()
        configureIncrementButton()
    }

    private fun configureCounterDisplay() {
        mCounterDisplay = findViewById(R.id.counter_display) as TextView?
        mCounterDisplay!!.text = mCounter.toString()
    }

    private fun configureIncrementButton() {
        mIncrementButton = findViewById(R.id.increment_button) as Button?
        mIncrementButton!!.setOnClickListener { onIncrementButtonClick() }
    }

    private fun onIncrementButtonClick() {
        mCounter++
        mCounterEmitter.onNext(mCounter)
    }
}
