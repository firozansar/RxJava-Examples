package info.firozansari.rxandroid_sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class Example1Activity : AppCompatActivity() {
    var mColorListView: RecyclerView? = null
    var mSimpleAdapter: SimpleAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureLayout()
        createObservable()
    }

    private fun createObservable() {
        val listObservable: Observable<List<String>> = Observable.just(
            colorList
        )
        listObservable.observeOn(Schedulers.newThread())
        listObservable.subscribeOn(AndroidSchedulers.mainThread())
        listObservable.subscribe(object : Observer<List<String?>?>() {
            override fun onComplete() {
                Log.d(TAG, "onComplete: called.")
            }

            override fun onError(e: Throwable?) {}
            override fun onSubscribe(d: Disposable?) {}
            override fun onNext(colors: List<String?>?) {
                //This is going to be running in Main thread.
                mSimpleAdapter!!.setStrings(colors)
            }
        })
    }

    private fun configureLayout() {
        setContentView(R.layout.activity_example_1)
        mColorListView = findViewById(R.id.color_list) as RecyclerView?
        mColorListView.setLayoutManager(LinearLayoutManager(this))
        mSimpleAdapter = SimpleAdapter(this)
        mColorListView.setAdapter(mSimpleAdapter)
    }

    companion object {
        val TAG = Example1Activity::class.java.simpleName
        private val colorList: List<String>
            private get() {
                val colors = ArrayList<String>()
                colors.add("blue")
                colors.add("green")
                colors.add("red")
                colors.add("chartreuse")
                colors.add("Van Dyke Brown")
                return colors
            }
    }
}
