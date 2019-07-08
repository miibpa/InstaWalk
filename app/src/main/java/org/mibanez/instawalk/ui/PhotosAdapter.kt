package org.mibanez.instawalk.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_photos_item.view.*
import org.mibanez.instawalk.R
import org.mibanez.instawalk.data.local.FlickrPhotoEntity

class PhotosAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val photos: MutableList<FlickrPhotoEntity> = arrayListOf()

    fun addPics(photos: List<FlickrPhotoEntity>) {
        this.photos.clear()
        this.photos.addAll(photos)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_photos_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(photos[position].url).into(holder.imageViewFlickrPhoto)
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val imageViewFlickrPhoto: ImageView = view.imageViewFlickrPhoto
}