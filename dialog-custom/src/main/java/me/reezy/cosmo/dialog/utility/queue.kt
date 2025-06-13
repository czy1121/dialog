package me.reezy.cosmo.dialog.utility

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import me.reezy.cosmo.dialog.CustomDialog


fun CustomDialog.showInQueue() {
    if (isShowing) {
        return
    }
    val activity = context.resolveComponentActivity()
    if (activity == null) {
        show()
        return
    }

    ViewModelProvider(activity)[QueueViewModel::class.java].enqueue(activity, this)
}

private fun Context.resolveComponentActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.resolveComponentActivity()
    else -> null
}


