package com.example.superrestoration_client.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.superrestoration_client.R

class ModelNameAdaptor (private var modelsName: ArrayList<String>, private var context: Context) : RecyclerView.Adapter<ModelNameAdaptor.ViewHolder>() {
    class ViewHolder(itemView: View, onItemClickListener: OnItemClickListener, itemVisibility: ItemVisibility) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.text)
        private var addButton: ImageButton = itemView.findViewById(R.id.add_model_to_list)
        private var removeButton: ImageButton = itemView.findViewById(R.id.remove_model_from_list)
        init {
            // 为item中控件添加回调
            addButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION)
                    onItemClickListener.onAddButtonClick(it, position)
            }
            removeButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION)
                    onItemClickListener.onRemoveButtonClick(it, position)
            }
            addButton.visibility = itemVisibility.addButton
            removeButton.visibility = itemVisibility.removeButton
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.ryc_item_model, parent, false)
        return ViewHolder(view, mOnItemClickListener, mItemVisibility)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = modelsName[position]
    }

    override fun getItemCount(): Int {
        return modelsName.size
    }

    // 为Item中的控件添加回调
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
            addButton = View.VISIBLE
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
}