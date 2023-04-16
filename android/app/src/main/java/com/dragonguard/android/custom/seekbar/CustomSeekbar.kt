package com.dragonguard.android.custom.seekbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue

class CustomSeekBar : androidx.appcompat.widget.AppCompatSeekBar {
    private var paint: Paint? = null
    private val coordCenterTemp = intArrayOf(0, 0)

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        paint = Paint()
        paint!!.color = Color.BLACK
        paint!!.style = Paint.Style.STROKE
        paint!!.textSize = sp2px(20).toFloat()
        paint!!.textAlign = Paint.Align.CENTER
    }

    private fun sp2px(sp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp.toFloat(),
            resources.displayMetrics
        ).toInt()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val str = progress.toString()

        val thumb_x = (progress.toDouble() / max * width).toInt()
        val thumb_y = 30

        val textMeasure = paint!!.measureText(str, 0, str.length)

        if (textMeasure / 2 + thumb_x <= right) {
            coordCenterTemp[0] = thumb_x
            coordCenterTemp[1] = thumb_y
        }
        canvas.drawText(str, coordCenterTemp[0].toFloat(), coordCenterTemp[1].toFloat(), paint!!)
    }
}