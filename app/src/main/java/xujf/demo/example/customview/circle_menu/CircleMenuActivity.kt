package xujf.demo.example.customview.circle_menu

import android.animation.Animator
import android.animation.AnimatorSet
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import xujf.demo.example.customview.R
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.AbsListView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_circle_menu.*


class CircleMenuActivity : AppCompatActivity() {

    val data = ArrayList<String>()
    private lateinit var adapter : ArrayAdapter<String>

    private var isMenuOpen = false //菜单是否展开
    private var isClosing = false //是否菜单收起动画进行中，防止用户连续点击
    private var isMenuHide = false //菜单是否隐藏
    private var oldVisibleItem = 0
    private var mAnimator: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle_menu)
        initMenu()
        for (i in 0..30) {
            data.add("Item${i}")
        }
        adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data)
        lv_menu.adapter = adapter
        lv_menu.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if (firstVisibleItem > oldVisibleItem) {
                    // 向上滑动
                    if (!isMenuHide) {
                        xlvWithMenuAnim(false)
                        isMenuHide = !isMenuHide
                    }
                }
                if (firstVisibleItem < oldVisibleItem) {
                    // 向下滑动
                    if (isMenuHide) {
                        xlvWithMenuAnim(true)
                        isMenuHide = !isMenuHide
                    }
                }
                oldVisibleItem = firstVisibleItem
            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
            }
        })
    }

    private fun initMenu() {
        iv_text.setOnClickListener {
            Toast.makeText(this,"文本",Toast.LENGTH_SHORT).show()
        }
        iv_img.setOnClickListener {
            Toast.makeText(this,"图片",Toast.LENGTH_SHORT).show()
        }
        iv_video.setOnClickListener {
            Toast.makeText(this,"视频",Toast.LENGTH_SHORT).show()
        }
        iv_menu.setOnClickListener {
            if (isMenuOpen) {
                closeMenuAnim()
            } else {
                openMenuAnim()
            }
        }
    }

    /**
     * 菜单按钮：上滑隐藏，下拉显示
     */
    private fun xlvWithMenuAnim(isShow: Boolean) {
        if (mAnimator != null && mAnimator!!.isRunning) {
            mAnimator!!.cancel()
        }
        if (isShow) {
            mAnimator = ObjectAnimator.ofFloat(iv_menu,
                    "translationY", iv_menu.translationY, 0f)
        } else {
            mAnimator = ObjectAnimator.ofFloat(iv_menu,
                    "translationY", iv_menu.translationY,
                    iv_menu.height + 100f)
            if (isMenuOpen) {
                //菜单展开则 先收起，再隐藏
                closeMenuAnim()
                mAnimator!!.startDelay = 500
            } else {
                iv_text.visibility = View.GONE
                iv_img.visibility = View.GONE
                iv_video.visibility = View.GONE
            }
        }
        mAnimator!!.start()
    }

    private fun closeMenuAnim() {
        if (!isClosing) {
            isClosing = true
            val baseValue = BaseUtil.dip2px(this, 20f)
            val animator0 = ObjectAnimator.ofFloat(iv_menu,
                    "alpha", 0.5f, 1f)
            val animator1 = ObjectAnimator.ofFloat(iv_img,
                    "translationY", -baseValue * 3f, 0f)
            val animator2 = ObjectAnimator.ofFloat(iv_img,
                    "translationX", -baseValue * 3f, 0f)
            val animator3 = ObjectAnimator.ofFloat(iv_text,
                    "translationY", -baseValue * 4f, 0f)
            val animator4 = ObjectAnimator.ofFloat(iv_video,
                    "translationX", -baseValue * 4f, 0f)
            val set = AnimatorSet()
            set.duration = 500
            set.interpolator = BounceInterpolator()
            set.playTogether(animator0, animator1, animator2, animator3, animator4)
            set.addListener(object : Animator.AnimatorListener {

                override fun onAnimationRepeat(p0: Animator?) {
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationStart(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                    iv_text.visibility = View.GONE
                    iv_img.visibility = View.GONE
                    iv_video.visibility = View.GONE
                    iv_menu.setBackgroundResource(R.drawable.bbs_ic_menu_post)
                    isClosing = false
                    isMenuOpen = false
                }
            })
            set.start()
        }
    }

    private fun openMenuAnim() {
        iv_text.visibility = View.VISIBLE
        iv_img.visibility = View.VISIBLE
        iv_video.visibility = View.VISIBLE
        iv_menu.setBackgroundResource(R.drawable.bbs_ic_menu_close)
        val baseValue = BaseUtil.dip2px(this, 20f)
        val animator0 = ObjectAnimator.ofFloat(
                iv_menu,
                "alpha",
                1f,
                0.5f)
        val animator1 = ObjectAnimator.ofFloat(
                iv_img,
                "translationY",
                -baseValue * 3f)
        val animator2 = ObjectAnimator.ofFloat(
                iv_img,
                "translationX",
                -baseValue * 3f)
        val animator3 = ObjectAnimator.ofFloat(
                iv_text,
                "translationY",
                -baseValue * 4f)
        val animator4 = ObjectAnimator.ofFloat(
                iv_video,
                "translationX",
                -baseValue * 4f)
        val set = AnimatorSet()
        set.duration = 500
        set.interpolator = BounceInterpolator()
        set.playTogether(
                animator0,
                animator1,
                animator2,
                animator3,
                animator4)
        set.start()
        isMenuOpen = true
    }

    override fun onPause() {
        if (isMenuOpen) {
            closeMenuAnim()
        }
        super.onPause()
    }
}
