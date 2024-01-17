package com.yash.pdfconverter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ImageAdapter(var list:List<Uri>): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    init {
        notifyDataSetChanged()
    }
//    var list=List<Uri>()
//    set(value){
//        field=value
//        notifyDataSetChanged()
//    }


    inner class ImageViewHolder(view: View):RecyclerView.ViewHolder(view){
        var image=view.findViewById<ImageView>(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        var itemLayout=LayoutInflater.from(parent.context).inflate(R.layout.image_item,parent,false)
        return ImageViewHolder(itemLayout)


    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        with(holder){
            image.setImageURI(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}