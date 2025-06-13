@file:Suppress("UNCHECKED_CAST")

package me.reezy.cosmo.dialog.utility

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStateAtLeast
import kotlinx.coroutines.launch
import me.reezy.cosmo.dialog.CustomDialog
import java.util.concurrent.ConcurrentLinkedQueue

class TaskChain {
    fun interface Task {
        fun execute(context: AppCompatActivity, data: Any?, done: () -> Unit)
    }


    companion object {

        val instance = TaskChain()


        private val tasks = mutableMapOf<String, Task>()

        fun register(name: String, task: Task) {
            tasks[name] = task
        }


        fun <T : Any> register(name: String, create: (Context, T) -> CustomDialog) {
            register(name, object : Task {
                override fun execute(context: AppCompatActivity, data: Any?, done: () -> Unit) {
                    create(context, data as? T ?: return done()).setDismissAction(done).show()
                }
            })
        }

    }

    private val queue = ConcurrentLinkedQueue<Pair<String, Any?>>()

    private var nowTaskName: String? = null

    fun has(name: String) = nowTaskName == name || queue.any { it.first == name }

    fun add(name: String, data: Any? = null): TaskChain {
        queue.offer(name to data)
        return this
    }

    fun addUnique(name: String, data: Any? = null): TaskChain {
        if (!has(name)) {
            queue.offer(name to data)
        }
        return this
    }

    @Synchronized
    fun start(activity: AppCompatActivity) {
        if (nowTaskName != null) {
            return
        }
        val (taskName, taskData) = queue.poll() ?: return
        nowTaskName = taskName
        val task = tasks[taskName]
        if (task == null) {
            start(activity)
            return
        }
        activity.lifecycleScope.launch {
            activity.lifecycle.withStateAtLeast(Lifecycle.State.RESUMED) {
                val done: () -> Unit = {
                    nowTaskName = null
                    start(activity)
                }
                try {
                    task.execute(activity, taskData, done)
                } catch (ex: Throwable) {
                    done()
                }
            }
        }
    }

}