package com.example.superrestoration_client.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.superrestoration_client.R
import com.example.superrestoration_client.model.Model

class ModelAdaptor(private var models: List<Model>, private var context: Context) : RecyclerView.Adapter<ModelAdaptor.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = View.inflate(context, R.layout.ryc_item_model, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = models[position].getModelName()
    }

    override fun getItemCount(): Int {
        return models.size
    }
}