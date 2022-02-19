package com.example.superrestoration_client.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.superrestoration_client.R
import com.example.superrestoration_client.model.Model
import com.example.superrestoration_client.model.ProcessHistory

/**
 * 负责将得到的 model和布局中的 item关联起来，以便在 Recycler View 中显示
 */
class HistoryAdaptor(private var histories: ArrayList<ProcessHistory>, private var context: Context) : RecyclerView.Adapter<HistoryAdaptor.ViewHolder>() {
    class ViewHolder(itemView: View, onItemClickListener: OnItemClickListener, itemVisibility: ItemVisibility) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.text)
        init {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = View.inflate(context, R.layout.ryc_item_history, null)
        return ViewHolder(view, mOnItemClickListener, mItemVisibility)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val time = histories[position].getCommitTime().split('_')
        val ymd = time[0].split('-')
        val hms = time[1].split('-')
        (ymd[0] + "年" + ymd[1] + "月" + ymd[2] + "日" +
                hms[0] + "时" + hms[1] + "分" + hms[2] + "秒").also { holder.textView.text = it }
    }

    override fun getItemCount(): Int {
        return histories.size
    }

    // 44-52:为Item中的控件添加回调
    interface OnItemClickListener{
        fun onAddButtonClick(view: View, position: Int)
        fun onRemoveButtonClick(view: View, position: Int)
    }

    private lateinit var mOnItemClickListener: OnItemClickListener

    fun setOnItemClickListener(clickListener: OnItemClickListener){
        this.mOnItemClickListener = clickListener
    }

    class ItemVisibility{
        var addButton: Int
        var removeButton: Int
        constructor(){
            addButton = View.GONE
            removeButton= View.GONE
        }
        constructor(addButtonVisibility: Int, removeButtonVisibility: Int){
            addButton = addButtonVisibility
            removeButton = removeButtonVisibility
        }
    }

    private var mItemVisibility: ItemVisibility = ItemVisibility()

    fun setItemVisibility(itemVisibility: ItemVisibility){
        this.mItemVisibility = itemVisibility
    }

    fun addItem(processHistory: ProcessHistory){
        histories.add(processHistory)
        //添加动画
        notifyItemInserted(itemCount)
    }

    fun removeItem(position: Int){
        histories.removeAt(position)
        //删除动画
        notifyItemRemoved(position)
        refresh()
    }

    fun refresh(){
        notifyDataSetChanged()
    }
}