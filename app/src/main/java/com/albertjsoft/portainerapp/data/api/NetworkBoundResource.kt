package com.albertjsoft.portainerapp.data.api

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.support.annotation.MainThread
import com.albertjsoft.portainerapp.data.api.observer.ApiObserver
import com.albertjsoft.portainerapp.data.api.response.ApiResponse
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by albertj on 18/10/2018.
 */
abstract class NetworkBoundResource<ResultType> @MainThread constructor() {

    private val result = MutableLiveData<Resource<ResultType?>>()

    init {
        result.value = Resource.loading()
        subscribe()
    }

    abstract fun createObservable(): Observable<out ApiResponse<ResultType>>

    private fun subscribe() {
        createObservable()
                .apiSubscribe(object : ApiObserver<ApiResponse<ResultType>>() {
                    override fun onSuccess(response: ApiResponse<ResultType>) {
                        result.value = Resource.success(response.getData())
                    }

                    override fun onError(exception: Throwable) {
                        super.onError(exception)
                        result.value = Resource.error(exception.message)
                    }
                })
    }

    fun asLiveData(): LiveData<Resource<ResultType?>> = result

    private fun <T> Observable<T>.apiSubscribe(observer: Observer<in T>) {
        observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(observer)
    }
}

class NetworkBoundResourceBuilder {
    companion object {
        fun <ResultType> asLiveData(createObservable: () -> Observable<out ApiResponse<ResultType>>):
                LiveData<Resource<ResultType?>> {
            return object : NetworkBoundResource<ResultType?>() {
                override fun createObservable(): Observable<out ApiResponse<ResultType?>> {
                    return createObservable()
                }
            }.asLiveData()
        }
    }
}