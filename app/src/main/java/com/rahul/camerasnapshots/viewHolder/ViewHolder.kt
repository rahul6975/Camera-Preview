package com.rahul.camerasnapshots.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rahul.camerasnapshots.clickInterface.ClickListener
import com.rahul.camerasnapshots.room.EntityClass
import kotlinx.android.synthetic.main.all_images.view.*

//ViewHolder class which holds the data in our recycler view
class ViewHolder(private val view: View, private val clickListener: ClickListener) :
    RecyclerView.ViewHolder(view) {

    fun setImage(entityClass: EntityClass) {
        view.apply {
            tvAlbumName.text = entityClass.album
            tvImageName.text = entityClass.name
            tvTime.text = entityClass.time
        }

        view.setOnClickListener {
            clickListener.onClick(adapterPosition)
        }
    }

}