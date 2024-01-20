package info.firozansari.rxandroid_sample

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class Example2Activity : AppCompatActivity() {
    private val mTvShowSubscription: Disposable? = null
    private var mTvShowListView: RecyclerView? = null
    private var mProgressBar: ProgressBar? = null
    private var mSimpleAdapter: SimpleAdapter? = null
    private var mRestClient: RestClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRestClient = RestClient(this)
        configureLayout()
        createObservable()
    }

    private fun createObservable() {
        val tvShowObservable: Observable<List<String>> = Observable.fromCallable { mRestClient?.favoriteTvShows }
        tvShowObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : Observer<List<String>> {
                    override fun onError(e: Throwable?) {}
                    override fun onComplete() {}
                    override fun onSubscribe(d: Disposable?) {}
                    override fun onNext(t: List<String>) {
                        displayTvShows(t)
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mTvShowSubscription != null && !mTvShowSubscription.isDisposed()) {
            mTvShowSubscription.dispose()
        }
    }

    private fun displayTvShows(tvShows: List<String>) {
        mSimpleAdapter?.setStrings(tvShows)
        mProgressBar?.visibility = View.GONE
        mTvShowListView?.visibility = View.VISIBLE
    }

    private fun configureLayout() {
        setContentView(R.layout.activity_example_2)
        mProgressBar = findViewById(R.id.loader) as ProgressBar?
        mTvShowListView = findViewById(R.id.tv_show_list) as RecyclerView?
        mTvShowListView?.layoutManager = LinearLayoutManager(this)
        mSimpleAdapter = SimpleAdapter(this)
        mTvShowListView?.adapter = mSimpleAdapter
    }
}
