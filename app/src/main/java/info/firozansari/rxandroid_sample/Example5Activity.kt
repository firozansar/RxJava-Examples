package info.firozansari.rxandroid_sample

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

class Example5Activity : AppCompatActivity() {
    private var mRestClient: RestClient? = null
    private var mSearchInput: EditText? = null
    private var mNoResultsIndicator: TextView? = null
    private var mSearchResults: RecyclerView? = null
    private var mSearchResultsAdapter: SimpleAdapter? = null
    private lateinit var mSearchResultsSubject: PublishSubject<String>
    private lateinit var mTextWatchSubscription: Subscription
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRestClient = RestClient(this)
        configureLayout()
        createObservables()
        listenToSearchInput()
    }

    private fun createObservables() {
        mSearchResultsSubject = PublishSubject.create()
//        mTextWatchSubscription = mSearchResultsSubject
//            .debounce(400, TimeUnit.MILLISECONDS)
//            .subscribeOn(Schedulers.io()) // "work" on io thread
//            .observeOn(AndroidSchedulers.mainThread()) // "listen" on UIThread
//            .map { t -> mRestClient?.searchForCity(t) }
//            .subscribe(object : Consumer<List<String>> {
//                override fun accept(t: List<String>) {
//                    handleSearchResults(t)
//                }
//            })
    }

    private fun handleSearchResults(cities: List<String>) {
        if (cities.isEmpty()) {
            showNoSearchResults()
        } else {
            showSearchResults(cities)
        }
    }

    private fun showNoSearchResults() {
        mNoResultsIndicator!!.visibility = View.VISIBLE
        mSearchResults?.visibility = View.GONE
    }

    private fun showSearchResults(cities: List<String>) {
        mNoResultsIndicator?.visibility = View.GONE
        mSearchResults?.visibility = View.VISIBLE
        mSearchResultsAdapter?.setStrings(cities)
    }

    private fun listenToSearchInput() {
        mSearchInput?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                mSearchResultsSubject?.onNext(s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun configureLayout() {
        setContentView(R.layout.activity_example_5)
        mSearchInput = findViewById(R.id.search_input) as EditText?
        mNoResultsIndicator = findViewById(R.id.no_results_indicator) as TextView?
        mSearchResults = findViewById(R.id.search_results) as RecyclerView?
        mSearchResults?.setLayoutManager(LinearLayoutManager(this))
        mSearchResultsAdapter = SimpleAdapter(this)
        mSearchResults?.setAdapter(mSearchResultsAdapter)
    }

    override fun onDestroy() {
        super.onDestroy()
//        if (mTextWatchSubscription != null && !mTextWatchSubscription.isUnsubscribed()) {
//            mTextWatchSubscription.unsubscribe()
//        }
    }
}
