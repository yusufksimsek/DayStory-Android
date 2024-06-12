package com.example.daystory.UI.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.daystory.R
import com.example.daystory.api.service.EventService

class GalleryAdapter(private val daySummaries: List<EventService.DaySummary>,
                     private val onItemClicked: (EventService.DaySummary) -> Unit) :
    RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewImageDate: TextView = itemView.findViewById(R.id.textViewImageDate)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_layout, parent, false)
        return GalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val daySummary = daySummaries[position]
        holder.textViewImageDate.text = daySummary.date

        val dateParts = daySummary.date.split("-")
        val formattedDate = "${dateParts[0]}/${dateParts[1]}/${dateParts[2]}"

        holder.textViewImageDate.text = formattedDate

        val imageUrl = daySummary.imagePath
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            onItemClicked(daySummary)
        }
    }

    override fun getItemCount(): Int = daySummaries.size
}
