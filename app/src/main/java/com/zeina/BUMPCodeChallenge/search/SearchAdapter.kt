package com.zeina.BUMPCodeChallenge.search

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.zeina.BUMPCodeChallenge.R
import com.zeina.BUMPCodeChallenge.data.Data
import com.zeina.BUMPCodeChallenge.data.SearchModel
import kotlinx.android.synthetic.main.search_view_holder.view.*

class SearchAdapter(private var searches: SearchModel, private val context: Context) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val data = searches.data[position]
        holder.bind(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.search_view_holder, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount() = searches.data.size

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: Data) {
            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "image/gif"
            }

            Glide.with(itemView?.context)
                    .asGif()
                    .load(data.images.fixed_height.url)
                    .apply(RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(ColorDrawable(ContextCompat.getColor(itemView.context, R.color.colorWhite)))
                            .centerCrop())
                    .transition(DrawableTransitionOptions.withCrossFade(android.R.anim.fade_in, 200))
                    .into(itemView.gif_image)
        }
    }

    fun addMoreGifs(searchModel: SearchModel) {
        for (data in searchModel.data) {
            searches.data += data
        }
        notifyItemInserted(itemCount)
    }

}