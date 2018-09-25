package xujf.demo.example.customview.dials_view

import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.util.AttributeSet
import android.view.View
import java.text.DecimalFormat
import android.content.res.TypedArray
import xujf.demo.example.customview.R


/**
 * Created by XuJiafeng on 2018/9/18.
 * Content：环形渐变条（动画效果）
 * <p>
 * 使用方法：
 * 设置总分和当前分数
 * setScore(int max, int current)
 */

class RingProgressView: View {
    private var radius = 0f
    private var sWith = 0f
    private val mCount = 80
    private val angle = 270f //环形角度
    private var initAng = 270 - angle / 2 //画圆弧的起始角度
    private var ang = angle / mCount
    private var progress = 0f
    private var newProgress = 0f
    private var totalScore = 0f
    private var currentScore = 0f
    private var rect = RectF()

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var animator: ObjectAnimator? = null
    private var grade = ""
    private var barBackGround = Color.GRAY
    private var barStartColor = Color.RED
    private var barEndColor = Color.GREEN
    private var numColor = Color.RED
    private var textColor = Color.GRAY


    init {
        paint.textSize = dpToPixel(40f)
        paint.textAlign = Paint.Align.CENTER
    }

    constructor(context: Context): this(context, null) {

    }

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0) {

    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView)
        barBackGround = array.getColor(R.styleable.CircleProgressView_barBackground, Color.GRAY)
        barStartColor = array.getColor(R.styleable.CircleProgressView_barStartColor, Color.RED)
        barEndColor = array.getColor(R.styleable.CircleProgressView_barEndColor, Color.GREEN)
        numColor = array.getColor(R.styleable.CircleProgressView_numColor, Color.RED)
        textColor = array.getColor(R.styleable.CircleProgressView_textColor, Color.GRAY)
        array.recycle()
    }

    /**
     * 设置总分和当前分数
     *
     * @param max
     * @param current
     */
    fun setScore(max: Float, current: Float, grade: String) {
        this.grade = grade
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
        this.radius = (measuredWidth / 2.5).toFloat()
        this.sWith = radius / 6
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(measuredWidth, View.MeasureSpec.EXACTLY)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(measuredWidth, View.MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = (width / 2).toFloat()
        val centerY = (height / 2).toFloat()

        //背景条
        paint.color = barBackGround
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = sWith

        rect.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        canvas.drawArc(rect, initAng, angle, false, paint)

        // 环形文字
        val strs = ArrayList<String>()
        strs.add("")
        strs.add("继续努力")
        strs.add("")
        strs.add("合格")
        strs.add("良好")
        strs.add("优秀")

        val spaceAngle = angle / strs.size
        val rectText = RectF(centerX - radius + sWith * 3/2, centerY - radius + sWith* 3/2, centerX + radius - sWith* 3/2, centerY + radius - sWith* 3/2)
        paint.style = Paint.Style.FILL
        paint.textSize = sWith * 2 / 3
        for (i in 0 until strs.size) {
            val path = Path()
            path.addArc(rectText, initAng + spaceAngle * i, spaceAngle)
            //沿着路径绘制字符串
            canvas.drawTextOnPath(strs[i], path, 0f, 0f, paint)
        }
        canvas.save()

        //进度条
        val colors = intArrayOf(barStartColor, barEndColor)
        val shader = LinearGradient(0f, centerY, width.toFloat(), centerY, colors, null, Shader.TileMode.CLAMP)
        paint.shader = shader

        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = sWith

        rect.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        canvas.drawArc(rect, initAng, progress * angle / 100, false, paint)

        //        //指针
        //        canvas.save();
        //        canvas.rotate(initAng + progress * angle / 100,centerX,centerY);
        //        canvas.drawLine(centerX-radius/2,centerY,centerX,centerY,paint);
        //        canvas.restore();

        //分数文字
        paint.shader = null

        paint.style = Paint.Style.FILL
        paint.strokeWidth = dpToPixel(1f)
        paint.textSize = radius / 2
        paint.color = numColor
        canvas.save()
        //        canvas.drawText((int) progress + "%", centerX, centerY + dpToPixel(50), paint);
        var num = DecimalFormat("##0.0").format((progress * totalScore / 100).toDouble())
        if (progress == newProgress) {
            //防止 progress*totalScore/100 产生的偏差，导致结果分数与currentScore不同
            num = DecimalFormat("##0.0").format(currentScore.toDouble())
        }
        canvas.drawText(num, centerX, centerY + radius / 8, paint)

        paint.style = Paint.Style.FILL
        paint.strokeWidth = dpToPixel(1f)
        paint.textSize = radius / 4
        paint.color = textColor
        canvas.save()
        canvas.drawText(grade, centerX, centerY + radius / 2, paint)

    }

    private fun dpToPixel(dp: Float): Float {
        val metrics = Resources.getSystem().displayMetrics
        return dp * metrics.density
    }
}