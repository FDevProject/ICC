package com.febrian.icc.utils

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatButton
import com.febrian.icc.R

class ConnectionReceiver(private val context: Context, private val listener : ReceiveListener) : BroadcastReceiver() {
    override fun onReceive(c: Context, intent: Intent) {
        if (!InternetConnection.isConnected(c)) {

            val builder = AlertDialog.Builder(context)
            val lView = LayoutInflater.from(context)
                .inflate(R.layout.alert_dialog_no_internet, null)
            builder.setView(lView)

            val dialog = builder.create()
            dialog.show()
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            val btnRetry = lView.findViewById<AppCompatButton>(R.id.btn_retry)
            btnRetry.setOnClickListener {
                dialog.dismiss()
                onReceive(c, intent)
                listener.onNetworkChange()
            }
        }
    }

    interface ReceiveListener{
        fun onNetworkChange()
    }
}