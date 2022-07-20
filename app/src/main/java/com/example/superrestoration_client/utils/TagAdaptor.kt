package com.example.superrestoration_client.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.superrestoration_client.R

/**
 * 负责将得到的 model和布局中的 item关联起来，以便在 Recycler View 中显示
 */
class TagAdaptor(private var tags: ArrayList<String>, private var context: Context) : RecyclerView.Adapter<TagAdaptor.ViewHolder>() {
    class ViewHolder(itemView: View, onItemClickListener: OnItemClickListener, itemVisibility: ItemVisibility) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.text_tag)
        init {
            textView.setBackgroundResource(R.drawable.btn_selector)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = View.inflate(context, R.layout.ryc_item_tag, null)
        return ViewHolder(view, mOnItemClickListener, mItemVisibility)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = tags[position]
    }

    override fun getItemCount(): Int {
        return tags.size
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

    fun addItem(tag: String){
        tags.add(tag)
        //添加动画
        notifyItemInserted(itemCount)
    }

    fun removeItem(position: Int){
        tags.removeAt(position)
        //删除动画
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }
}