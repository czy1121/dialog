package me.reezy.cosmo.dialog

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withResumed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentLinkedQueue


fun CustomDialog.showInQueue() {
    if (isShowing) {
        return
    }
    val activity = context.resolveComponentActivity()
    if (activity == null) {
        show()
        return
    }

    val queueViewModel by ViewModelLazy(QueueViewModel::class,
        { activity.viewModelStore },
        { activity.defaultViewModelProviderFactory },
        { activity.defaultViewModelCreationExtras }
    )
    queueViewModel.enqueue(activity, this)
}

private fun Context.resolveComponentActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.resolveComponentActivity()
    else -> null
}


class QueueViewModel : ViewModel() {

    private val queue = ConcurrentLinkedQueue<SoftReference<CustomDialog>>()

    private var showing: WeakReference<CustomDialog>? = null

    fun enqueue(activity: ComponentActivity, dialog: CustomDialog) {
        queue.offer(SoftReference(dialog))

        if (showing == null) {
            showNext(activity)
        }
    }

    private fun next(): CustomDialog? {
        do {
            val ref = queue.poll() ?: return null
            val dialog = ref.get()
            if (dialog != null) {
                return dialog
            }
            Log.e("OoO", "dialog == null, show next")
        } while (true)
    }

    private fun showNext(activity: ComponentActivity) {

        val dialog = next() ?: return

        showing = WeakReference(dialog)
        dialog.setDismissAction {
            showing = null
            showNext(activity)
        }

        activity.lifecycleScope.launch(Dispatchers.Main) {
            activity.lifecycle.withResumed {
                dialog.show()
            }
        }
    }

    override fun onCleared() {
        queue.clear()
    }
}