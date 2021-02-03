package com.j1c1m1b1.udf

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar

fun Snackbar.onDismissed(action: () -> Unit): Snackbar = apply {
    addCallback(object : Snackbar.Callback() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)
            action.invoke()
        }
    })
}

fun Activity.showAlertDialog(message: String) {
    AlertDialog.Builder(this)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog?.dismiss() }
        .show()
}
