package com.albertjsoft.portainerapp.data.api.observer

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Vibrator
import android.support.annotation.CallSuper
import android.view.ContextThemeWrapper
import android.widget.Toast
import com.albertjsoft.portainerapp.R
import com.albertjsoft.portainerapp.app.PortainerApp
import com.albertjsoft.portainerapp.data.api.response.HttpStatus
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * Created by albertj on 18/10/2018.
 */
abstract class ApiObserver<T> : Observer<T> {

    override fun onSubscribe(d: Disposable) {}

    override fun onNext(response: T) { onSuccess(response) }

    abstract fun onSuccess(response: T)

    override fun onComplete(){}

    @CallSuper
    override fun onError(exception: Throwable) {
        when (exception) {
            is HttpException -> onHttpException(exception)
            is SocketTimeoutException -> onTimeout()
            is IOException -> onNetworkError()
            else -> onUnknownError(exception.message)
        }
    }

    private fun onHttpException(httpException: HttpException) {
        val statusCode = httpException.response().code()

        when (statusCode) {
            HttpStatus.NOT_FOUND -> onNotFoundError(httpException)
            HttpStatus.BAD_REQUEST -> onBadRequestError(httpException)
            HttpStatus.INTERNAL_SERVER_ERROR -> onInternalServerError(httpException)
            else -> { onUnknownError(getErrorMessage(httpException)) }
        }
    }

    private fun onNetworkError() {
        PortainerApp.instance.showToast("Ups!", "Network error")
    }

    private fun onTimeout() {
        PortainerApp.instance.showToast("Ups!", "Timeout")
    }

    private fun onUnknownError(errorMessage: String?) {
        PortainerApp.instance.showToast("Unknown Error", errorMessage)
    }

    private fun onNotFoundError(httpException: HttpException) {
        PortainerApp.instance.showToast("Not Found", getErrorMessage(httpException))
    }

    private fun onBadRequestError(httpException: HttpException) {
        PortainerApp.instance.showToast("Bad Request", getErrorMessage(httpException))
    }

    private fun onInternalServerError(httpException: HttpException) {
        PortainerApp.instance.showToast("Internal server error",getErrorMessage(httpException))
    }

    private fun getErrorMessage(httpException: HttpException): String? {
        return try {
            JSONObject(httpException.response().errorBody()!!.string()).getString("err")
        } catch (exception: JSONException) {
            exception.message
        } catch (exception: IOException) {
            exception.message
        }
    }

    private fun Context?.showToast(title: String?, message: String?) {
        val vib = this!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vib.vibrate(15)

        Toast.makeText(this, title + " - " + message, Toast.LENGTH_SHORT).show()
    }

    private fun Context?.showDialog(title: String?, message: String?){
        val vib = this!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vib.vibrate(15)

        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.errorDialog))
        val dialog = builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                })
        dialog.show()
    }
}
