package com.hazem.popularpeople.core.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hazem.popularpeople.core.network.ConnectivityReceiver
import com.hazem.popularpeople.core.network.RetrofitException
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import io.reactivex.subjects.PublishSubject
import org.reactivestreams.Publisher


open class BaseViewModel : ViewModel() {

    @Inject
    lateinit var context: Context

    val compositeDisposable = CompositeDisposable()
    val error = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    private var disposable: Disposable? = null
    private val retrySubject = PublishSubject.create<Any>()

    private fun getRetrofitError(exception: Throwable) {
        if (exception is RetrofitException) {
            error.postValue(exception.message ?: "")
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

                .compose { single ->
                    composeSingle<T>(single)
                }
                .retryWhen {
                    retrySubject.observeOn(Schedulers.io()).toFlowable(BackpressureStrategy.LATEST)
                }
                .subscribe(success, requestError)
        )
    }

    private fun <T> composeSingle(single: Single<T>): Single<T> {
        return single
                .doOnError {
                    getRetrofitError(it)
                }
                .doOnSubscribe {
                    if (ConnectivityReceiver.isConnectedOrConnecting(context)){
                        loading.postValue(true)
                    }
                }
                .doAfterTerminate {
                    loading.postValue(false)
                }
                .doAfterSuccess { disposable = null }
    }

    fun clearSubscription() {
        if (compositeDisposable.isDisposed.not()) compositeDisposable.clear()
        disposable?.dispose()
    }
    open fun retry(msg:String) {
        retrySubject.onNext(1)
    }
}

const val NO_NETWORK_AVAILABLE = "no network available"
enum class State{
    LOADING, ERROR, LOADING_MORE, DONE
}