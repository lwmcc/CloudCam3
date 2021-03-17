package com.mccarty.cloudcam3.adapters

import android.net.Uri
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mccarty.cloudcam3.R
import com.mccarty.cloudcam3.db.ImageEntity
import com.mccarty.cloudcam3.ui.home.HomeViewModel
import java.io.File


class MediaAdapter(private val homeViewModel: HomeViewModel,
                   private val mediaArray: List<ImageEntity>):
    RecyclerView.Adapter<MediaAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imageIcon: ImageView = view.findViewById(R.id.main_list_image)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.media_item_layout,
        viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val uri = Uri.fromFile(File(mediaArray[position].localFilePath!!))

        viewHolder.imageIcon.setOnClickListener {
            homeViewModel.navigateToImageView(mediaArray[position])
        }

        viewHolder.imageIcon.setOnLongClickListener {
            homeViewModel.setConfirmDeleteImageDialogTrue(mediaArray[position])
            true
        }

        Glide.with(viewHolder.imageIcon.context)
                .load(uri)
                .transition(withCrossFade())
                .placeholder(R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_dialog_alert)
                .centerCrop()
                .into(viewHolder.imageIcon)
    }

    override fun getItemCount() = mediaArray.size

}