package info.firozansari.rxandroid_sample

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class NetworkActivity : AppCompatActivity() {

    private lateinit var tvShowListView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var mErrorMessage: TextView
    private lateinit var mSimpleAdapter: SimpleAdapter
    private lateinit var restClient: RestClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restClient = RestClient(this)
        configureLayout()
        createSingle()
    }

    private fun createSingle() {
        val tvShowSingle: Observable<List<String>> = Observable.fromCallable {
                /**
                 * return RestClient.getFavoriteTvShowsWithException()
                 */
                restClient.favoriteTvShows
            }
        tvShowSingle
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<String>> {
                override fun onError(e: Throwable?) {
                    displayErrorMessage()
                }
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable?) {}
                override fun onNext(t: List<String>) {
                    displayTvShows(t)
                }
            })
    }

    private fun displayTvShows(tvShows: List<String>) {
        mSimpleAdapter.setStrings(tvShows)
        progressBar.visibility = View.GONE
        tvShowListView.visibility = View.VISIBLE
    }

    private fun displayErrorMessage() {
        progressBar.visibility = View.GONE
        mErrorMessage.visibility = View.VISIBLE
    }

    private fun configureLayout() {
        setContentView(R.layout.activity_network)
        mErrorMessage = findViewById(R.id.error_message)
        progressBar = findViewById(R.id.loader)
        tvShowListView = findViewById(R.id.tv_show_list)
        tvShowListView.layoutManager = LinearLayoutManager(this)
        mSimpleAdapter = SimpleAdapter(this)
        tvShowListView.adapter = mSimpleAdapter
    }
}
