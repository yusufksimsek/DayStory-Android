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
import java.text.SimpleDateFormat
import java.util.Locale

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

        val formattedDate = formatDate(daySummary.date)
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

    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM EEEE", Locale("tr"))
        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }
}
