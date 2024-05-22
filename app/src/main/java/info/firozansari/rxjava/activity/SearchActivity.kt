package info.firozansari.rxjava.activity

import android.os.Bundle
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import info.firozansari.rxjava.R
import info.firozansari.rxjava.util.RestClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SearchActivity : AppCompatActivity() {

    private lateinit var restClient: RestClient
    private lateinit var searchView: SearchView
    private lateinit var textViewResult: TextView
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restClient = RestClient(this)
        configureLayout()
        createObservables()
    }

    private fun createObservables() {
        disposables.add(
            RxSearchObservable.fromView(searchView)
            .debounce(300, TimeUnit.MILLISECONDS)
            .filter { text: String? ->
                if (text.isNullOrEmpty()) {
                    textViewResult.text = ""
                    return@filter false
                } else {
                    return@filter true
                }
            }
            .distinctUntilChanged()
            .switchMap { query: String? -> dataFromNetwork(query) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result: String? -> textViewResult.text = result }
        )
    }

    /**
     * Simulation of network data
     */
    private fun dataFromNetwork(query: String?): Observable<String> {
        return Observable.just(true)
            .delay(2, TimeUnit.SECONDS)
            .map { query }
    }

    private fun configureLayout() {
        setContentView(R.layout.activity_search)
        searchView = findViewById(R.id.searchView)
        textViewResult = findViewById(R.id.textViewResult)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    object RxSearchObservable {
        fun fromView(searchView: SearchView?): Observable<String?> {
            val subject = PublishSubject.create<String?>()
            searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(s: String): Boolean {
                    subject.onComplete()
                    return true
                }

                override fun onQueryTextChange(text: String): Boolean {
                    subject.onNext(text)
                    return true
                }
            })
            return subject
        }
    }
}
