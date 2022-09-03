package me.reezy.cosmo.dialog.queue

import android.app.Dialog
import java.lang.ref.WeakReference
import java.util.*


object DialogQueue {

    private val queue = LinkedList<Dialog>()

    private var current: WeakReference<Dialog> = WeakReference(null)

    val isShown: Boolean
        get() = current.get() != null

    fun enqueue(dialog: Dialog) {

        if (dialog.isShowing) return

        queue.offer(dialog)

        next()
    }

    private fun next() {
        if (current.get() == null) {
            queue.poll()?.also {
                current = WeakReference(it)

                it.setOnDismissListener {
                    current = WeakReference(null)
                    next()
                }
            }?.show()
        }
    }

}