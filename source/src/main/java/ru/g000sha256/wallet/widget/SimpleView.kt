package ru.g000sha256.wallet.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class SimpleView : View {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = calculateSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = calculateSize(suggestedMinimumHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    private fun calculateSize(size: Int, spec: Int): Int {
        val specMode = MeasureSpec.getMode(spec)
        val specSize = MeasureSpec.getSize(spec)
        when (specMode) {
            MeasureSpec.AT_MOST -> return min(size, specSize)
            MeasureSpec.EXACTLY -> return specSize
            MeasureSpec.UNSPECIFIED -> return size
            else -> return size
        }
    }

}