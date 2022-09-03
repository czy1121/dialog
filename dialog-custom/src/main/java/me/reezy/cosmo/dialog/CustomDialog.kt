package me.reezy.cosmo.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import me.reezy.cosmo.R


open class CustomDialog(context: Context, themeId: Int = 0) : Dialog(context, context.resolveDialogTheme(themeId)), LifecycleOwner {

    companion object {
        const val MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT
        const val WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT

        private fun Context.resolveDialogTheme(@StyleRes themeId: Int): Int {
            if (themeId.ushr(24) and 0x000000ff >= 0x00000001) return themeId

            val outValue = TypedValue()
            theme.resolveAttribute(R.attr.customDialogTheme, outValue, true)

            if (outValue.resourceId > 0) {
                return outValue.resourceId
            }
            return R.style.CustomDialog_Dialog
        }

        fun translateY(from: Float, to: Float): Animation {
            val type = Animation.RELATIVE_TO_SELF
            val animation = TranslateAnimation(0, 0f, 0, 0f, type, from, type, to)
            animation.duration = 250
            return animation
        }

        fun translateX(from: Float, to: Float): Animation {
            val type = Animation.RELATIVE_TO_SELF
            val animation = TranslateAnimation(type, from, type, to, 0, 0f, 0, 0f, )
            animation.duration = 250
            return animation
        }
    }


    private val root: ViewGroup = LinearLayoutCompat(getContext()).apply {
        orientation = LinearLayoutCompat.VERTICAL
    }
    private var contentView: View? = null

    private var mAnimationEnter: Animation? = null
    private var mAnimationExit: Animation? = null
    private var mIsCanceledOnTouchOutside = false

    private val registry: LifecycleRegistry by lazy { LifecycleRegistry(this) }

    override fun getLifecycle(): Lifecycle = registry

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setWindowAnimations(0)
    }

    open fun setDimAmount(amount: Float): CustomDialog {
        window?.apply {
            if (amount > 0) {
                addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                setDimAmount(amount)
            } else {
                clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                setDimAmount(0f)
            }
        }
        return this
    }

    fun setLayout(width: Int, height: Int): CustomDialog {
        window?.decorView?.layoutParams = WindowManager.LayoutParams(width, height)
        window?.setLayout(width, height)

        val lp = root.layoutParams?.also {
            it.width = width
            it.height = height
        } ?: ViewGroup.LayoutParams(width, height)
        root.layoutParams = lp
        return this
    }

    fun setAnimation(enter: Animation, exit: Animation): CustomDialog {
        mAnimationEnter = enter
        mAnimationExit = exit
        return this
    }

    fun setView(view: View): CustomDialog {
        setContentView(view)
        return this
    }

    fun setView(@LayoutRes resId: Int): CustomDialog {
        setContentView(resId)
        return this
    }

    fun setButton(id: Int, listener: View.OnClickListener): CustomDialog {
        require<View>(id).setOnClickListener(listener)
        return this;
    }

    fun requireView(): View = contentView!!

    fun dismissImmediately() {
        super.dismiss()
    }

    fun <T : View> require(id: Int): T {
        return root.findViewById(id) ?: throw IllegalArgumentException("ID does not reference a View inside this Dialog")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(root, root.layoutParams ?: ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        registry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun onStart() {
        super.onStart()
        // on show
        registry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    override fun onStop() {
        // on hide
        registry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        // on destroy
        registry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        super.onStop()
    }


    override fun <T : View?> findViewById(id: Int): T? {
        return root.findViewById<T>(id)
    }

    final override fun setCanceledOnTouchOutside(cancel: Boolean) {
        super.setCanceledOnTouchOutside(cancel)
        mIsCanceledOnTouchOutside = cancel
    }


    override fun setContentView(view: View) {
        contentView = view
        root.addView(view)
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams?) {
        contentView = view
        root.addView(view, params)
    }

    override fun setContentView(layoutResID: Int) {
        setContentView(LayoutInflater.from(context).inflate(layoutResID, root, false))
    }

    override fun onAttachedToWindow() {
        if (root.childCount == 0 || mAnimationEnter == null) {
            return
        }
        if (!isAnimating(mAnimationEnter)) {
            mAnimationEnter!!.reset()
            root.startAnimation(mAnimationEnter)
        }
    }

    override fun dismiss() {
        if (root.childCount == 0 || mAnimationExit == null) {
            dismissImmediately()
        } else if (!isAnimating(mAnimationExit)) {
            mAnimationExit?.reset()

            mAnimationExit?.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {

                }

                override fun onAnimationEnd(animation: Animation) {
                    dismissImmediately()
                }

                override fun onAnimationRepeat(animation: Animation) {

                }
            })
            root.startAnimation(mAnimationExit)
        }
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (isAnimating(mAnimationEnter) || isAnimating(mAnimationExit)) {
            return true
        }
        if (ev.action == MotionEvent.ACTION_DOWN) {
            if (mIsCanceledOnTouchOutside) {
                val rect = Rect()
                root.getHitRect(rect)
                if (!rect.contains(ev.x.toInt(), ev.y.toInt())) {
                    dismiss()
                    return true
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    override fun onBackPressed() {
        if (isAnimating(mAnimationEnter) || isAnimating(mAnimationExit)) {
            return
        }
        super.onBackPressed()
    }


    private fun isAnimating(animation: Animation?): Boolean {
        return animation != null && animation.hasStarted() && !animation.hasEnded()
    }

    protected val Float.dp: Float get() = this * context.resources.displayMetrics.density

}