package com.example.superrestoration_client.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.superrestoration_client.R
import com.example.superrestoration_client.model.Model

/**
 * 负责将得到的 model和布局中的 item关联起来，以便在 Recycler View 中显示
 */
class ModelAdaptor(private var models: List<Model>, private var context: Context) : RecyclerView.Adapter<ModelAdaptor.ViewHolder>() {
    class ViewHolder(itemView: View, onItemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.text)
        var addButton: ImageButton = itemView.findViewById(R.id.add_model_to_list)
        var removeButton: ImageButton = itemView.findViewById(R.id.remove_model_from_list)
        init {
            // 为item中控件添加回调
            addButton.setOnClickListener(View.OnClickListener {
                if (onItemClickListener != null){
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION)
                        onItemClickListener.onAddButtonClick(it, position)
                }
            })
            removeButton.setOnClickListener(View.OnClickListener {
                if (onItemClickListener != null){
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION)
                        onItemClickListener.onRemoveButtonClick(it, position)
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = View.inflate(context, R.layout.ryc_item_model, null)
        return ViewHolder(view, mOnItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = models[position].getModelName()
    }

    override fun getItemCount(): Int {
        return models.size
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
}