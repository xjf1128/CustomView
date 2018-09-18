package xujf.demo.example.customview.xlink_list

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_link_right.*
import xujf.demo.example.customview.R
import xujf.demo.example.customview.xlink_list.adapter.RightAdapter
import xujf.demo.example.customview.xlink_list.vo.RightBean
import xujf.demo.example.customview.xlink_list.vo.SortBean
import java.util.ArrayList

/**
 * Created by XuJiafeng on 2018/9/17.
 * Content：二级联动列表-右侧
 */
class LinkRightFragment: Fragment(),CheckListener {

    private lateinit var activity: Activity
    private var checkListener: CheckListener? = null
    private lateinit var mManager: GridLayoutManager
    private val rightData = ArrayList<RightBean>()
    private lateinit var itemDecoration: ItemHeaderDecoration
    private var move = false
    private var index = 0
    private lateinit var rightAdapter: RightAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity = getActivity()
        val view = layoutInflater.inflate(R.layout.fragment_link_right, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initList()
    }

    private fun initList() {
        initData()

        mManager = GridLayoutManager(activity, 3)
        mManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (rightData[position].isTitle) 3 else 1
            }
        }
        rv_right.addOnScrollListener(RecyclerViewListener())
        rv_right.layoutManager = mManager
        rightAdapter = RightAdapter(activity, rightData)

        rv_right.adapter = rightAdapter
        itemDecoration = ItemHeaderDecoration(activity, rightData)
        rv_right.addItemDecoration(itemDecoration)
        itemDecoration.setCheckListener(this)

    }

    private fun initData() {
        val rightList = arguments.getParcelableArrayList<SortBean.CategoryOneArrayBean>("right")
        for (i in rightList.indices) {
            val head = RightBean(rightList[i].name)
            //头部设置为true
            head.isTitle = true
            head.titleName = rightList[i].name
            head.tag = i.toString()
            rightData.add(head)
            val categoryTwoArray = rightList[i].categoryTwoArray
            for (j in categoryTwoArray.indices) {
                val body = RightBean(categoryTwoArray[j].name)
                body.tag = i.toString()
                val name = rightList[i].name
                body.titleName = name
                rightData.add(body)
            }

        }
    }

    fun setListener(listener: CheckListener) {
        this.checkListener = listener
    }

    fun moveToRightIndex(n: Int) {
        index = n
        rv_right.stopScroll()
        smoothMoveToPosition(n)
    }

    override fun check(position: Int, isScroll: Boolean) {
        checkListener!!.check(position, isScroll)
    }

    private fun smoothMoveToPosition(n: Int) {
        val firstItem = mManager.findFirstVisibleItemPosition()
        val lastItem = mManager.findLastVisibleItemPosition()
        Log.d("first--->", firstItem.toString())
        Log.d("last--->", lastItem.toString())
        if (n <= firstItem) {
            rv_right.scrollToPosition(n)
        } else if (n <= lastItem) {
            Log.d("pos---->", n.toString() + "VS" + firstItem)
            val top = rv_right.getChildAt(n - firstItem).getTop()
            Log.d("top---->", top.toString())
            rv_right.scrollBy(0, top)
        } else {
            rv_right.scrollToPosition(n)
            move = true
        }
    }

    private inner class RecyclerViewListener : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (move && newState == RecyclerView.SCROLL_STATE_IDLE) {
                move = false
                val n = index - mManager.findFirstVisibleItemPosition()
                Log.d("n---->", n.toString())
                if (0 <= n && n < rv_right.getChildCount()) {
                    val top = rv_right.getChildAt(n).getTop()
                    Log.d("top--->", top.toString())
                    rv_right.smoothScrollBy(0, top)
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (move) {
                move = false
                val n = index - mManager.findFirstVisibleItemPosition()
                if (0 <= n && n < rv_right.getChildCount()) {
                    val top = rv_right.getChildAt(n).getTop()
                    rv_right.scrollBy(0, top)
                }
            }
        }
    }
}