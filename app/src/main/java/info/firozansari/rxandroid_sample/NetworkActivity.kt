package info.firozansari.rxandroid_sample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import info.firozansari.rxandroid_sample.model.User
import info.firozansari.rxandroid_sample.model.UserDetails
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

    private lateinit var cricketFansObservable: Observable<List<User>>
    private lateinit var footballFansObservable: Observable<List<User>>
    private lateinit var userListObservable: Observable<List<User>>
    private lateinit var userDetailsObservable: Observable<UserDetails>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restClient = RestClient(this)
        configureLayout()
        //createSimpleExample()
        //createZipExample()
        createFlatmapZipExample()
    }

    private fun createSimpleExample() {
        val tvShowObservable: Observable<List<String>> = Observable.fromCallable {
                /**
                 * For error: restClient.favoriteTvShowsWithException
                 */
                restClient.favoriteTvShows
            }
        tvShowObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<String>> {
                override fun onError(e: Throwable?) {
                    displayErrorMessage()
                }
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable?) {}
                override fun onNext(t: List<String>) {
                    displayResults(t)
                }
            })
    }

    private fun createZipExample() {
        cricketFansObservable = Observable.fromCallable {
            restClient.cricketLoverUsers()
        }
        footballFansObservable = Observable.fromCallable {
            restClient.footballLoverUsers()
        }
        findUsersWhoLovesBoth()
    }

    private fun createFlatmapZipExample() {
        userListObservable = Observable.fromCallable {
            restClient.userList
        }
        userDetailsObservable = Observable.fromCallable {
            restClient.getUserDetails
        }
        doFlatMapWithZip()
    }

    private fun displayResults(values: List<String>) {
        mSimpleAdapter.setStrings(values)
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

    private fun findUsersWhoLovesBoth() {
        // zip operator to combine both request returning same type: User
        Observable.zip(cricketFansObservable, footballFansObservable
        ) { cricketFans: List<User>, footballFans: List<User> ->
            val userWhoLovesBoth = filterUserWhoLovesBoth(cricketFans, footballFans)
            userWhoLovesBoth
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<User>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(users: List<User>) {
                    Log.d(TAG, "userList size : " + users.size)
                    displayResults(users.map { it.firstname})
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, "error", e)
                    displayErrorMessage()
                }

                override fun onComplete() {
                    Log.d(TAG, "onComplete")
                }
            })
    }

    private fun filterUserWhoLovesBoth(cricketFans: List<User>, footballFans: List<User>): List<User> {
        val userWhoLovesBoth: MutableList<User> = ArrayList()
        for (footballFan in footballFans) {
            if (cricketFans.contains(footballFan)) {
                userWhoLovesBoth.add(footballFan)
            }
        }
        return userWhoLovesBoth
    }

    private fun doFlatMapWithZip() {
        // flatMap - to return users one by one
        userListObservable
                .flatMap { usersList: List<User> ->
                    Observable.fromIterable(usersList) // returning user one by one from usersList.
                }
            .flatMap { user: User ->
                Observable.zip(userDetailsObservable,
                    Observable.just(user)
                ) { userDetail: UserDetails, user1: User ->
                    Pair(userDetail, user1) // returning the pair(userDetail, user)
                }
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Pair<UserDetails, User>> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onError(e: Throwable) {
                        Log.e(TAG, "error", e)
                        displayErrorMessage()
                    }
                    override fun onNext(pair: Pair<UserDetails, User>) {
                        //here we are getting the userDetail for the corresponding user one by one
                        val userDetail = pair.first
                        val user = pair.second
                        val details = userDetail.toString() + user.toString()
                        displayResults(listOf(details))
                    }
                    override fun onComplete() {
                        Log.d(TAG, "onComplete")
                    }
                })
    }

    companion object {
        val TAG: String = NetworkActivity::class.java.simpleName
    }
}
