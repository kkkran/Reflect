package com.shijie.reflect

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class SimpleAdapter<T>(
    private var data: List<T>
) : RecyclerView.Adapter<SimpleAdapter<T>.SimpleViewHolder>() {
    private var layoutResId: Int = 0
    private var binder: ((View, T, Int) -> Unit)? = null

    inner class SimpleViewHolder(view: View) : ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutResId, parent, false)
        return SimpleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        binder?.invoke(holder.itemView, data[position], position)
    }

    fun setLayout(layoutResId: Int): SimpleAdapter<T> {
        this.layoutResId = layoutResId
        return this
    }

    fun setBinder(binder: (View, T, Int) -> Unit): SimpleAdapter<T> {
        this.binder = binder
        return this
    }

    fun updateData(newData: List<T>): SimpleAdapter<T> {
        this.data = data
        notifyDataSetChanged()
        return this
    }

}
