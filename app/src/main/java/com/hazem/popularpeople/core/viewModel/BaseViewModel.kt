package com.hazem.popularpeople.core.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hazem.popularpeople.core.network.RetrofitException
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

abstract class BaseViewModel : ViewModel() {

    val compositeDisposable = CompositeDisposable()
    val error = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    private fun getRetrofitError(exception: Throwable) {
        if (exception is RetrofitException) {
            when (exception.getKind()) {

                RetrofitException.Kind.NETWORK ->
                    error.value = exception.message ?: ""

                RetrofitException.Kind.HTTP ->
                    error.value = exception.message ?: ""

                RetrofitException.Kind.UNEXPECTED ->
                    error.value = exception.message ?: ""
            }
        }
    }

    fun <T> subscribe(
            observable: Observable<T>,
            success: Consumer<T>,
            error: Consumer<Throwable>,
            subscribeScheduler: Scheduler = Schedulers.io(),
            observeOnMainThread: Boolean = true) {

        val observerScheduler =
                if (observeOnMainThread) AndroidSchedulers.mainThread()
                else subscribeScheduler


        compositeDisposable.add(observable
                .subscribeOn(subscribeScheduler)
                .observeOn(observerScheduler)
                .subscribe(success, error))

    }

    fun <T> subscribe(
            observable: Single<T>,
            success: Consumer<T>,
            error: Consumer<Throwable>,
            subscribeScheduler: Scheduler = Schedulers.io(),
            observeOnMainThread: Boolean = true) {

        val observerScheduler =
                if (observeOnMainThread) AndroidSchedulers.mainThread()
                else subscribeScheduler

        compositeDisposable.add(observable
                .subscribeOn(subscribeScheduler)
                .observeOn(observerScheduler)
                .compose { single ->
                    composeSingle<T>(single)
                }
                .subscribe(success, error))

    }

    private fun <T> composeSingle(single: Single<T>): Single<T> {
        return single
                .doOnError {
                    getRetrofitError(it)
                }
                .doOnSubscribe {
                    loading.postValue(true)
                }
                .doAfterTerminate {
                    loading.postValue(false)
                }
    }

    fun clearSubscription() {
        if (compositeDisposable.isDisposed.not()) compositeDisposable.clear()
    }
}