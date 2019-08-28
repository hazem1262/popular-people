package com.hazem.popularpeople.core.dataSource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.hazem.popularpeople.core.viewModel.State
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

abstract class BaseDataSource<Key, Value>(private val compositeDisposable : CompositeDisposable): PageKeyedDataSource<Key, Value>() {
    var networkState = MutableLiveData<State> ()
    // keep a function reference for the retry event
    var retry: (() -> Any)? = null
    fun <T> subscribe(
        observable: Single<T>,
        success: Consumer<T>,
        requestError: Consumer<Throwable>,
        subscribeScheduler: Scheduler = Schedulers.io(),
        observeOnMainThread: Boolean = true) {

        val observerScheduler =
            if (observeOnMainThread) AndroidSchedulers.mainThread()
            else subscribeScheduler

        compositeDisposable.add(
            observable
                .subscribeOn(subscribeScheduler)
                .observeOn(observerScheduler)
                .subscribe(success, requestError)
        )
    }

    fun retryPaging(){
        retry?.let { it() }
    }
}