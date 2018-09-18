package xujf.demo.example.customview.xlink_list

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_link_list.*
import xujf.demo.example.customview.R
import xujf.demo.example.customview.xlink_list.adapter.LeftAdapter
import xujf.demo.example.customview.xlink_list.vo.SortBean
import java.io.IOException
import java.util.ArrayList

/**
 * Created by XuJiafeng on 2018/9/17.
 * Content：二级联动列表
 */
class LinkListActivity : AppCompatActivity(), CheckListener {

    private lateinit var leftAdapter: LeftAdapter
    private lateinit var linkRightFragment: LinkRightFragment
    private var linearLayoutManager = LinearLayoutManager(this)
    private var targetPosition: Int = 0//点击左边某一个具体的item的位置
    private var isMoved: Boolean = false
    private var sortBean: SortBean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_list)
        initData()
    }

    private fun initData() {
        //获取asset目录下的资源文件
        val assetsData = getAssetsData("sort.json")
        val gson = Gson()
        sortBean = gson.fromJson(assetsData, SortBean::class.java)
        val categoryOneArray = sortBean!!.categoryOneArray
        val list = ArrayList<String>()
        //初始化左侧列表数据
        for (i in categoryOneArray.indices) {
            list.add(categoryOneArray[i].name)
        }
        leftAdapter = LeftAdapter(this, list, object : LeftAdapter.LeftCallBack {
            override fun onItemClick(id: Int, position: Int) {
                isMoved = true
                targetPosition = position
                setChecked(position, true)
            }
        })
        linearLayoutManager = LinearLayoutManager(this)
        rv_left.layoutManager = linearLayoutManager
        rv_left.adapter = leftAdapter
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        rv_left.addItemDecoration(decoration)
        createFragment()
    }

    //从资源文件中获取分类json
    private fun getAssetsData(path: String): String {
        var result = ""
        try {
            //获取输入流
            val mAssets = assets.open(path)
            //获取文件的字节数
            val lenght = mAssets.available()
            //创建byte数组
            val buffer = ByteArray(lenght)
            //将文件中的数据写入到字节数组中
            mAssets.read(buffer)
            mAssets.close()
            result = String(buffer)
            return result
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("fuck", e.message)
            return result
        }

    }


    private fun createFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        linkRightFragment = LinkRightFragment()
        val bundle = Bundle()
        bundle.putParcelableArrayList("right", sortBean!!.categoryOneArray)
        linkRightFragment.arguments = bundle
        linkRightFragment.setListener(this)
        fragmentTransaction.add(R.id.lin_fragment, linkRightFragment)
        fragmentTransaction.commit()
    }

    private fun setChecked(position: Int, isLeft: Boolean) {
        Log.d("p-------->", position.toString())
        if (isLeft) {
            leftAdapter.setCheckedPosition(position)
            //此处的位置需要根据每个分类的集合来进行计算
            var count = 0
            for (i in 0 until position) {
                count += sortBean!!.categoryOneArray[i].categoryTwoArray.size
            }
            count += position
            linkRightFragment.moveToRightIndex(count)
            ItemHeaderDecoration.setCurrentTag(targetPosition.toString())//凡是点击左边，将左边点击的位置作为当前的tag
        } else {
            if (isMoved) {
                isMoved = false
            } else
                leftAdapter.setCheckedPosition(position)
            ItemHeaderDecoration.setCurrentTag(position.toString())//如果是滑动右边联动左边，则按照右边传过来的位置作为tag

        }
        moveToCenter(position)

    }

    //将当前选中的item居中
    private fun moveToCenter(position: Int) {
        //将点击的position转换为当前屏幕上可见的item的位置以便于计算距离顶部的高度，从而进行移动居中
        val childAt = rv_left.getChildAt(position - linearLayoutManager.findFirstVisibleItemPosition())
        if (childAt != null) {
            val y = childAt.top - rv_left.height / 2
            rv_left.smoothScrollBy(0, y)
        }

    }


    override fun check(position: Int, isScroll: Boolean) {
        setChecked(position, isScroll)
    }
}
