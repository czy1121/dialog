package me.reezy.cosmo.dialog.utility

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withResumed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.reezy.cosmo.dialog.CustomDialog
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentLinkedQueue

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