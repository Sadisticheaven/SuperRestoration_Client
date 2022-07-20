package com.example.superrestoration_client.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.*
import androidx.core.graphics.values
import kotlin.math.max
import kotlin.math.min

class ZoomImageView : androidx.appcompat.widget.AppCompatImageView, ViewTreeObserver.OnGlobalLayoutListener{
    private var scaleGestureDetector: ScaleGestureDetector = ScaleGestureDetector(context, MyScaleGestureDetector())
    private var dragGestureDetector: GestureDetector = GestureDetector(context, MyGestureDetector())
    private var lastScaleEndTime = 0L
    private val waitTime = 100 // 避免双指拖动后因为误操作立刻触发单指拖动
    val MAX_SCALE = 4F
    var initScale = 1F

    val TAG = "ZoomImageView"
    var scaleMatrix: Matrix = Matrix()

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        scaleType = ScaleType.MATRIX
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return when(event?.pointerCount) {
            1 -> {
                if (System.currentTimeMillis() - lastScaleEndTime <= waitTime){
                    return super.onTouchEvent(event)
                }
                else dragGestureDetector.onTouchEvent(event)
            }
            2 -> {
                scaleGestureDetector.onTouchEvent(event)
                return true
            }
            else -> super.onTouchEvent(event)
        }
    }

    override fun setImageBitmap(bm: Bitmap?) {
        Log.e(TAG, "--->setImageBitmap")
        super.setImageBitmap(bm)
    }

    //移动到中心
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {
        // setImage后会调用该函数
        Log.e(TAG, "--->onGlobalLayout")
        adjustPicture()
    }

    private fun adjustPicture(){
        if (drawable == null) return
        val imgWidth = drawable.intrinsicWidth
        val imgHeight = drawable.intrinsicHeight
        Log.e(TAG, "width: $imgWidth, height: $imgHeight")
        val scale: Float = getAdjustScale(imgWidth, imgHeight)
        Log.e("imageView", "scale=$scale")
        initScale = scale
        scaleMatrix = Matrix()
        scaleMatrix.postTranslate((width - imgWidth)/2F, (height - imgHeight)/2F)
        scaleMatrix.postScale(scale, scale, width / 2F, height / 2F)
        imageMatrix = scaleMatrix
    }

    private fun getAdjustScale(imgWidth: Int, imgHeight: Int): Float {
        var scale: Float = 1F
        if (imgWidth > width && imgHeight <= height)
            scale = width / imgWidth.toFloat()
        else if (imgHeight > height && imgWidth <= width)
            scale = height / imgHeight.toFloat()
        else if (imgWidth > width && imgHeight > height)
            scale = min(width / imgWidth.toFloat(), height / imgHeight.toFloat())
        return scale
    }

    fun getScale(): Float {
        return scaleMatrix.values()[Matrix.MSCALE_X]
    }

    inner class MyScaleGestureDetector(): ScaleGestureDetector.OnScaleGestureListener {
        override fun onScaleBegin(p0: ScaleGestureDetector?): Boolean {
            return true
        }

        override fun onScale(p0: ScaleGestureDetector?): Boolean {
            val currScale = getScale()
            if(p0 != null){
                var scale:Float = p0.scaleFactor
                val expectScale = currScale * scale
                if (expectScale > MAX_SCALE)
                    scale = MAX_SCALE / currScale
                else if (expectScale < initScale)
                    scale = initScale / currScale
                // imageView中没有内容则返回
                if (drawable == null) return true
                // 新的scale = 旧scale * scaleFactor
                scaleMatrix.postScale(scale, scale, p0.focusX, p0.focusY)
                imageMatrix = scaleMatrix
            }
            return true
        }

        override fun onScaleEnd(p0: ScaleGestureDetector?) {
//        scale时不会改变原图大小，只改变矩阵
//        val imgWidth = drawable.intrinsicWidth
//        val imgHeight = drawable.intrinsicHeight
//        Log.e(TAG, "width: $imgWidth, height: $imgHeight")
            lastScaleEndTime = System.currentTimeMillis()
        }
    }

    inner class MyGestureDetector(): GestureDetector.SimpleOnGestureListener(), GestureDetector.OnGestureListener{
        override fun onDoubleTap(e: MotionEvent?): Boolean {
            Log.e("imageView", "DoubleTap")
            adjustPicture()
            return true
        }
        // 单指拖动
        override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, disX: Float, disY: Float): Boolean {
            Log.e("imageView", "onScroll")
            scaleMatrix.postTranslate(-disX, -disY)
            imageMatrix = scaleMatrix
            return true
        }

        override fun onDown(p0: MotionEvent?): Boolean {
            Log.e("imageView", "onDown")
            return true
        }

        override fun onShowPress(p0: MotionEvent?) {
            Log.e("imageView", "onShowPress")
        }

        override fun onSingleTapUp(p0: MotionEvent?): Boolean {
            Log.e("imageView", "onSingleTapUp")
            return true
        }

        override fun onLongPress(p0: MotionEvent?) {
            Log.e("imageView", "onLongPress")
        }

        override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
            Log.e("imageView", "onFling")
            return true
        }
    }
}