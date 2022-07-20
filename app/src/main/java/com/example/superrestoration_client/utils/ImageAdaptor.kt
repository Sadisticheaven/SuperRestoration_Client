package com.example.superrestoration_client.utils

import android.content.Context
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.superrestoration_client.R
import kotlin.collections.ArrayList

/**
 * 负责将得到的 model和布局中的 item关联起来，以便在 Recycler View 中显示
 */
class ImageAdaptor(private var imgsUrl: ArrayList<String>, private var context: Context) : RecyclerView.Adapter<ImageAdaptor.ViewHolder>() {
    class ViewHolder(itemView: View, onItemClickListener: OnItemClickListener, itemVisibility: ItemVisibility) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.text_name)
        var imgView: ImageView = itemView.findViewById(R.id.show_img)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION)
                    onItemClickListener.onItemClick(it, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = View.inflate(context, R.layout.ryc_item_img, null)
        return ViewHolder(view, mOnItemClickListener, mItemVisibility)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tmp = imgsUrl[position].split('/')
        holder.textView.text = tmp[tmp.size - 1]
        // imageUrl: username/res/***
        GlideApp.with(context).load(Config.baseUrl + "/result/" + imgsUrl[position])
            .thumbnail(0.1f)
            .placeholder(R.drawable.xpixel)
            .into(holder.imgView)
    }

    override fun getItemCount(): Int {
        return imgsUrl.size
    }

    // 44-52:为Item中的控件添加回调
    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
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

    fun addItem(img: String){
        imgsUrl.add(img)
        //添加动画
        notifyItemInserted(itemCount)
    }

    fun removeItem(position: Int){
        imgsUrl.removeAt(position)
        //删除动画
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }
}