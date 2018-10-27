package com.albertjsoft.portainerapp.util

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE

/**
 * Created by albertj on 18/10/2018.
 */
fun View?.setVisible(visible: Boolean) {
    this?.visibility = if (visible) VISIBLE else GONE
}