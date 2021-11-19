package com.gx.app.gappxutils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import com.gx.app.gappxutils.utils.dp
import com.gx.app.gappxutils.utils.dpInt
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Created by Android-小强 on 2021/6/21.
 * mailbox:980766134@qq.com
 * description: listview 滑动bar 的biew
 */
@Keep
class BarListView : View {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, set: AttributeSet?) : this(context, set, 0)
    constructor(context: Context, set: AttributeSet?, defStyle: Int) : super(context, set, defStyle)

    private val metrics: Paint.FontMetrics = Paint.FontMetrics()
    private var textPaint = TextPaint()

    init {
        textPaint.textSize = 12F.dp()
        textPaint.color = Color.WHITE
        textPaint.textAlign = Paint.Align.CENTER
    }


    private var drawable: Drawable? = null
    var size = 50F.dpInt()
    var text = ""
        set(value) {
            field = value
            postInvalidate()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthM = widthMeasureSpec
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            widthM = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthM, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rect.set(0, topSize, w, topSize + w)
        leftSize = 30F.dpInt()
    }

    var drawableRes = 0
        set(value) {
            field = value
            drawable = null
        }
    var rect = Rect()
    var topSize = 0
    var leftSize = 0

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawARGB(100, 255, 255, 255)
        if (drawable == null) {
            drawable = ContextCompat.getDrawable(context, drawableRes)
        }
        drawable?.run {
            val bottom = leftSize + intrinsicHeight
            if (bottom > height) {
                leftSize -= bottom - height
            }
            val min = min(max(0, topSize), height - intrinsicHeight)
            setBounds(leftSize, min, width, min + intrinsicHeight)
            drawable?.draw(canvas!!)
            text?.run {
                textPaint.getFontMetrics(metrics)
                val fl = abs(metrics.leading) + abs(metrics.ascent) + abs(metrics.descent)
                val fl1 = fl * 0.5F - abs(metrics.descent)
                canvas?.drawText(
                    this,
                    rect.width() / 2F,
                    min + intrinsicHeight * 0.5F + fl1,
                    textPaint
                )
            }
        }


    }

    var downY = 0F
    var listener: ((progress: Int) -> Unit)? = null
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)
        parent.parent.requestDisallowInterceptTouchEvent(true)
        return super.dispatchTouchEvent(event)
//        return onTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
//                downY = event.rawY
                leftSize = 0
            }
            MotionEvent.ACTION_UP -> {
//                downY = event.rawY
                leftSize = 30F.dpInt()
            }
            MotionEvent.ACTION_CANCEL -> {
//                downY = event.rawY
                leftSize = 30F.dpInt()
            }
            MotionEvent.ACTION_MOVE -> {
                val fl = (event.y * 100F / height.toFloat()).toInt()

                topSize = event.y.toInt()
                listener?.invoke(fl)
            }
        }
        postInvalidate()
        return true
    }
}