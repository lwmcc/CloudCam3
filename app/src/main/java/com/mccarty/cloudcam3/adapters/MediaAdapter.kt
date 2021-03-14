package com.mccarty.cloudcam3.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mccarty.cloudcam3.R
import com.mccarty.cloudcam3.db.MediaEntity

class MediaAdapter(private val mediaArray: Array<MediaEntity>):
    RecyclerView.Adapter<MediaAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.media_item_layout,
        viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

    }

    override fun getItemCount() = mediaArray.size

    companion object {

    }
}