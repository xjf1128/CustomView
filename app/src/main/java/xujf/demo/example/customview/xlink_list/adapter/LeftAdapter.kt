package xujf.demo.example.customview.xlink_list.adapter

import android.app.Activity
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_left_item.view.*
import xujf.demo.example.customview.R

/**
 * Created by xujf on 2018/9/17.
 */
class LeftAdapter(var activity: Activity, var data: ArrayList<String>, var callBack: LeftCallBack): RecyclerView.Adapter<LeftAdapter.LeftViewHolder>() {
    interface LeftCallBack {
        fun onItemClick(id: Int, position: Int)
    }

    private var checkedPosition: Int = 0

    fun setCheckedPosition(checkedPosition: Int) {
        this.checkedPosition = checkedPosition
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LeftViewHolder {
        val layoutId = R.layout.item_left_item
        return LeftViewHolder(activity.layoutInflater.inflate(layoutId, parent, false))
    }

    override fun onBindViewHolder(holder: LeftViewHolder?, position: Int) {
        holder!!.tvTitle.text = data[position]
        if (position == checkedPosition) {
            holder.llItem.setBackgroundColor(Color.parseColor("#f3f3f3"))
            holder.tvTitle.setTextColor(Color.parseColor("#0068cf"))
        } else {
            holder.llItem.setBackgroundColor(Color.parseColor("#FFFFFF"))
            holder.tvTitle.setTextColor(Color.parseColor("#1e1d1d"))
        }
        holder.llItem.setOnClickListener {
            callBack.onItemClick(it.id, position)
        }
    }

    inner class LeftViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val llItem = view.ll_item
        val tvTitle = view.tv_title
    }
}