package com.rahul.camerasnapshots.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rahul.camerasnapshots.R
import com.rahul.camerasnapshots.clickInterface.ClickListener
import com.rahul.camerasnapshots.room.EntityClass
import com.rahul.camerasnapshots.viewHolder.ViewHolder

//adapter for our recycler view
class ImageAdapter(
    private var imageList: List<EntityClass>,
    private val clickListener: ClickListener
) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_images, parent, false)
        return ViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel = imageList[position]
        holder.setImage(dataModel)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    //updates the latest data of the list
    fun updateList(imageList: List<EntityClass>) {
        this.imageList = imageList
        notifyDataSetChanged()
    }
}