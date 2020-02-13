package com.albertjsoft.portainerapp.data.api


import android.support.annotation.CallSuper
import com.albertjsoft.portainerapp.app.PortainerApp
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import com.albertjsoft.portainerapp.util.showToast

/**
 * Created by albertj on 18/10/2018.
 */
abstract class ApiObserver<T> : Observer<T> {

    override fun onSubscribe(d: Disposable) {}

    override fun onNext(response: T) {
        onSuccess(response)
    }

    abstract fun onSuccess(response: T)

    override fun onComplete() {}

    @CallSuper
    override fun onError(exception: Throwable) =
            when (exception) {
                is HttpException -> onHttpException(exception)
                is SocketTimeoutException -> onTimeout()
                is IOException -> onNetworkError()
                else -> onUnknownError(exception.message)
            }

    private fun onHttpException(httpException: HttpException) =
            when (httpException.response().code()) {
                NOT_FOUND -> onNotFoundError(httpException)
                BAD_REQUEST -> onBadRequestError(httpException)
                INTERNAL_SERVER_ERROR -> onInternalServerError(httpException)
                else -> {
                    onUnknownError(getErrorMessage(httpException))
                }
            }

    private fun onNetworkError() =
            PortainerApp.instance.showToast("Ups!", "Network error")


    private fun onTimeout() =
            PortainerApp.instance.showToast("Ups!", "Timeout")

    private fun onUnknownError(errorMessage: String?) =
            PortainerApp.instance.showToast("Unknown Error", errorMessage)

    private fun onNotFoundError(httpException: HttpException) =
            PortainerApp.instance.showToast("Not Found", getErrorMessage(httpException))

    private fun onBadRequestError(httpException: HttpException) =
            PortainerApp.instance.showToast("Bad Request", getErrorMessage(httpException))

    private fun onInternalServerError(httpException: HttpException) =
            PortainerApp.instance.showToast("Internal server error", getErrorMessage(httpException))

    private fun getErrorMessage(httpException: HttpException): String? =
            try {
                JSONObject(httpException.response().errorBody()!!.string()).getString("err")
            } catch (exception: JSONException) {
                exception.message
            } catch (exception: IOException) {
                exception.message
            }

    companion object {
        const val BAD_REQUEST = 400
        const val NOT_FOUND = 404
        const val INTERNAL_SERVER_ERROR = 500
    }
}

