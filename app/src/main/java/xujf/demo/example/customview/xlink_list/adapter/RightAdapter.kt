package xujf.demo.example.customview.xlink_list.adapter

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_right_item.view.*
import kotlinx.android.synthetic.main.item_right_title.view.*
import xujf.demo.example.customview.R
import xujf.demo.example.customview.xlink_list.vo.RightBean
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by xujf on 2018/9/17.
 * 二级联动列表，右侧
 */
class RightAdapter(var activity: Activity, var data: ArrayList<RightBean>): RecyclerView.Adapter<RightAdapter.RightViewHolder>() {

    private val cacheMap = HashMap<String, List<RightBean>>()

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].isTitle) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RightViewHolder {
        val layoutId = if (viewType == 0) R.layout.item_right_title else R.layout.item_right_item
        return RightViewHolder(activity.layoutInflater.inflate(layoutId, parent, false))
    }

    override fun onBindViewHolder(holder: RightViewHolder?, position: Int) {
        val holder = holder!!
        when (getItemViewType(position)) {
            0 -> {
                holder.tvTitle.text = data[position].titleName + data[position].num
                holder.llRoot.setOnClickListener {
                    if (cacheMap[data[position].titleName] != null) {
                        data.addAll(position + 1, cacheMap[data[position].titleName] as List<RightBean>)
                        cacheMap.remove(data[position].titleName)
                    } else {
                        val goneList = ArrayList<RightBean>()
                        var i = position + 1
                        while (i < data.size) {
                            if (!data[i].isTitle && data[i].titleName == (data[position].titleName)) {
                                goneList.add(data[i])
                                data.removeAt(i)
                                i--
                            } else {
                                break
                            }
                            i++
                        }
                        cacheMap[data[position].titleName] = goneList
                    }
                    notifyDataSetChanged()
                }
            }

            1 -> {
                holder.tvName.text = data[position].name
                holder.llContent.setOnClickListener {
                    for (i in data.indices) {
                        if (data[i].titleName == data[position].titleName) {
                            data[i].num = data[i].num + 1
                        }
                    }
                    notifyDataSetChanged()
                }
            }
        }

    }

    inner class RightViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val llContent = view.ll_content
        val tvName = view.tv_name
        val ivAvatar = view.iv_avatar

        val llRoot = view.ll_root
        val tvTitle = view.tv_title
    }
}