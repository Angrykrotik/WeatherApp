package com.fivesysdev.weatherapp.utils

import android.app.AlertDialog
import android.content.Context

object DialogManager {
    fun locationSettingDialog(context: Context, listener: Listener) {
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        dialog.setTitle("Enable Location?")
        dialog.setMessage("Location disabled, do you want enable?")
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { _, _ ->
            listener.onPositive()
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL") { _, _ ->
            dialog.dismiss()
        }
        dialog.show()

    }

    interface Listener {
        fun onPositive()
    }
}