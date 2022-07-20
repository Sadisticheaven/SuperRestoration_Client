package com.example.superrestoration_client.utils

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.values
import com.example.superrestoration_client.R
import kotlin.math.max
import kotlin.math.min

class CompareImageView: RelativeLayout {
    val TAG = "CompareImageView"

    private var scaleGestureDetector: ScaleGestureDetector = ScaleGestureDetector(context, MyScaleGestureDetector())
    private var dragGestureDetector: GestureDetector = GestureDetector(context, MyGestureDetector())
    private var lastScaleEndTime = 0L
    private val waitTime = 100 // 避免双指拖动后因为误操作立刻触发单指拖动
    val MAX_SCALE = 4F
    var initScale = 1f
    var widthRate = 0.5F
    var scaleMatrix: Matrix = Matrix()
    private lateinit var leftImageView : ImageView
    private lateinit var rightImageView : ImageView
    private lateinit var mView: View

    // new时调用
    constructor(context: Context) : this(context, null)
    // xml中使用时自动调用
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    // 在xml中设置style时调用
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr){
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CompareImageView)
        widthRate = typedArray.getFloat(R.styleable.CompareImageView_widthRate, 0.5f)
        typedArray.recycle()
        Log.e(TAG, "widthRate = $widthRate")
        inflateView()
    }

    private fun inflateView() {
        mView = LayoutInflater.from(context).inflate(R.layout.compare_image, this, true)
        leftImageView = mView.findViewById(R.id.leftImg)
        rightImageView = mView.findViewById(R.id.rightImg)
        leftImageView.scaleType = ImageView.ScaleType.MATRIX
        rightImageView.scaleType = ImageView.ScaleType.MATRIX
        leftImageView.layoutParams = LayoutParams((width*widthRate).toInt(), LayoutParams.MATCH_PARENT)
        rightImageView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
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

    fun setImageURI(uriLeft: Uri?, uriRight: Uri?){
        leftImageView.setImageURI(uriLeft)
        rightImageView.setImageURI(uriRight)
        adjustPicture()
        updateWidthRate(widthRate)
    }

    fun replaceLeftImageBitmap(bitmap: Bitmap?){
        leftImageView.setImageBitmap(bitmap)
    }

    fun replaceRightImageBitmap(bitmap: Bitmap?){
        rightImageView.setImageBitmap(bitmap)
    }

    fun setImageBitmap(bitmapLeft: Bitmap?, bitmapRight: Bitmap?){
        leftImageView.setImageBitmap(bitmapLeft)
        rightImageView.setImageBitmap(bitmapRight)
        adjustPicture()
        updateWidthRate(widthRate)
    }

    private fun adjustPicture(){
        if (leftImageView.drawable == null) return
        val imgWidth = leftImageView.drawable.intrinsicWidth
        val imgHeight = leftImageView.drawable.intrinsicHeight
        Log.e(TAG, "width: $imgWidth, height: $imgHeight")
        val scale: Float = getAdjustScale(imgWidth, imgHeight)
        Log.e("imageView", "scale=$scale")
        initScale = scale
        scaleMatrix = Matrix()
        scaleMatrix.postTranslate((width - imgWidth)/2F, (height - imgHeight)/2F)
        scaleMatrix.postScale(scale, scale, width / 2F, height / 2F)
        leftImageView.imageMatrix = scaleMatrix
        if (rightImageView.drawable == null) return
        rightImageView.imageMatrix = scaleMatrix
    }

    private fun getAdjustScale(imgWidth: Int, imgHeight: Int): Float {
        var scale = 1F
        if (imgWidth > width && imgHeight <= height)
            scale = width / imgWidth.toFloat()
        else if (imgHeight > height && imgWidth <= width)
            scale = height / imgHeight.toFloat()
        else
            scale = min(width / imgWidth.toFloat(), height / imgHeight.toFloat())
        return scale
    }

    fun getScale(matrix: Matrix): Float {
        return matrix.values()[Matrix.MSCALE_X]
    }

    fun updateWidthRate(rate: Float) {
        widthRate = rate
        Log.e(TAG, "widthRate = $widthRate")
        leftImageView.layoutParams = LayoutParams((width*widthRate).toInt(), LayoutParams.MATCH_PARENT)
        rightImageView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    inner class MyScaleGestureDetector(): ScaleGestureDetector.OnScaleGestureListener {
        override fun onScaleBegin(p0: ScaleGestureDetector?): Boolean {
            return true
        }

        override fun onScale(p0: ScaleGestureDetector?): Boolean {
            val currScale = getScale(scaleMatrix)
            if(p0 != null){
                var scale:Float = p0.scaleFactor
                val expectScale = currScale * scale
                if (expectScale > MAX_SCALE)
                    scale = MAX_SCALE / currScale
                else if (expectScale < initScale)
                    scale = initScale / currScale
                // imageView中没有内容则返回
                if (leftImageView.drawable == null) return true
                // 新的scale = 旧scale * scaleFactor
                scaleMatrix.postScale(scale, scale, p0.focusX, p0.focusY)
                leftImageView.imageMatrix = scaleMatrix
                rightImageView.imageMatrix = scaleMatrix
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
            val picRect = RectF()
            scaleMatrix.mapRect(picRect, RectF(0f,0f,
                leftImageView.drawable.bounds.width().toFloat(),
                leftImageView.drawable.bounds.height().toFloat()))
            if(p0 != null){
                var tranX = -disX
                var tranY = -disY
                val maxTransX = -picRect.left
                val minTransX = width - picRect.right
                val maxTransY = -picRect.top
                val minTransY = height - picRect.bottom
                if (picRect.width() <= width){
                    tranX = -picRect.left + (width - picRect.width())/2
                }else{
                    if(tranX > 0) tranX = min(tranX, maxTransX)
                    else tranX = max(tranX, minTransX)
                }

                if (picRect.height() <= height){
                    tranY = -picRect.top + (height - picRect.height())/2
                }else{
                    if(tranY > 0) tranY = min(tranY, maxTransY)
                    else tranY = max(tranY, minTransY)
                }
                // imageView中没有内容则返回
                if (leftImageView.drawable == null) return true
                // 新的scale = 旧scale * scaleFactor
                scaleMatrix.postTranslate(tranX, tranY)
                leftImageView.imageMatrix = scaleMatrix
                rightImageView.imageMatrix = scaleMatrix
            }
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