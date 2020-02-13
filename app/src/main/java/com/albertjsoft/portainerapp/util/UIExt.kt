package com.albertjsoft.portainerapp.util

import android.content.Context
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import android.os.Vibrator

/**
 * Created by albertj on 18/10/2018.
 */
fun View?.setVisible(visible: Boolean) {
    this?.visibility = if (visible) VISIBLE else GONE
}

fun Context?.showToast(title: String?, message: String?) {
    val vib = this!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    vib.vibrate(15)

    Toast.makeText(this, "$title - $message", Toast.LENGTH_SHORT).show()
}
/*
fun Context?.showDialog(title: String?, message: String?) {
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
*/