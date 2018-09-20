package xujf.demo.example.customview.dials_view

import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.util.AttributeSet
import android.view.View
import java.text.DecimalFormat

/**
 * Created by XuJiafeng on 2018/9/18.
 * Content：渐变色刻度盘（动画效果）
 * <p>
 * 使用方法：
 * 设置总分和当前分数
 * setScore(int max, int current)
 */

class DialsProgressView: View {
    private var radius = 0f
    private var sWith = 0f
    private val mCount = 80
    private val angle = 250f //环形角度
    private var initAng = (180 - angle) / 2 //画圆弧的起始角度
    private var ang = angle / mCount
    private var progress = 0f
    private var newProgress = 0f
    private var totalScore = 0f
    private var currentScore = 0f

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var animator: ObjectAnimator? = null


    init {
        paint.textSize = dpToPixel(40f)
        paint.textAlign = Paint.Align.CENTER
    }

    constructor(context: Context): super(context) {

    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {

    }

    /**
     * 设置总分和当前分数
     *
     * @param max
     * @param current
     */
    fun setScore(max: Float, current: Float) {
        this.totalScore = max
        this.currentScore = current
        this.newProgress = (current * 1.0 / totalScore * 100).toInt().toFloat()
        animator = ObjectAnimator.ofFloat(this, "progress", 0f, newProgress)
        animator!!.duration = 2000
        //   animator.setRepeatCount(-1);
        animator!!.interpolator = FastOutSlowInInterpolator()
        animator!!.start()
    }

    fun getProgress(): Float {
        return progress
    }

    fun setProgress(newProgress: Float) {
        this.progress = newProgress
        invalidate()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (animator != null) {
            animator!!.end()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(View.getDefaultSize(0, widthMeasureSpec), View.getDefaultSize(0, heightMeasureSpec))
        this.radius = (measuredWidth / 2).toFloat()
        this.sWith = radius / 6
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(measuredWidth, View.MeasureSpec.EXACTLY)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(measuredWidth, View.MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = (width / 2).toFloat()
        val centerY = (height / 2).toFloat()

        paint.color = Color.GRAY
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.BUTT
        paint.strokeWidth = dpToPixel(2f)
        //刻度
        canvas.save()
        //分界点
        val demarcationPint = (progress / (100.0 / mCount)).toInt()
        canvas.rotate(initAng, centerX, centerY)
        for (i in 0 until mCount + 1) {
            //渐变色 刻度盘划过部分
            paint.color = setColor(i.toFloat(), mCount)
            //刻度盘 未划过部分
            if (i > demarcationPint) {
                paint.color = Color.GRAY
            }
            canvas.drawLine(centerX - radius, centerY, centerX - radius + sWith, centerY, paint)
            canvas.rotate(ang, centerX, centerY)
        }
        canvas.restore()

        //        //指针
        //        canvas.save();
        //        canvas.rotate(initAng + progress * angle / 100,centerX,centerY);
        //        canvas.drawLine(centerX-radius/2,centerY,centerX,centerY,paint);
        //        canvas.restore();

        //下方文字
        paint.style = Paint.Style.FILL
        paint.strokeWidth = dpToPixel(1f)
        paint.textSize = radius / 8
        paint.color = Color.GRAY
        canvas.save()
        canvas.drawText("总分:" + DecimalFormat("##0.0").format(totalScore.toDouble()),
                centerX, centerY - radius / 4, paint)

        paint.style = Paint.Style.FILL
        paint.strokeWidth = dpToPixel(1f)
        paint.textSize = radius / 2
        paint.color = setColor(demarcationPint.toFloat(), mCount)
        canvas.save()
        //        canvas.drawText((int) progress + "%", centerX, centerY + dpToPixel(50), paint);
        var num = DecimalFormat("##0.0").format((progress * totalScore / 100).toDouble())
        if (progress == newProgress) {
            //防止 progress*totalScore/100 产生的偏差，导致结果分数与currentScore不同
            num = DecimalFormat("##0.0").format(currentScore.toDouble())
        }
        canvas.drawText(num, centerX, centerY + radius / 4, paint)

    }

    private fun dpToPixel(dp: Float): Float {
        val metrics = Resources.getSystem().displayMetrics
        return dp * metrics.density
    }

    private fun setColor(`val`: Float, mCount: Int): Int {
        val one = 255.toFloat() / (mCount / 3)
        var r = 0
        var g = 0
        val b = 0
        if (`val` < mCount * 1 / 3) {
            r = 255
        } else if (`val` >= mCount * 1 / 3 && `val` < mCount * 2 / 3) {
            r = 255
            g = ((`val` - mCount * 1 / 3) * one).toInt()
        } else {
            r = 255 - ((`val` - mCount * 2 / 3) * one).toInt()
            g = 255
        }//最后一个三等分
//        Log.e("xujf","RGB=" + r + g + b);
        return Color.rgb(r, g, b)
    }
}