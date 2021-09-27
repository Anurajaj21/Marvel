package com.example.marvel.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marvel.Models.CharacterResponse
import com.example.marvel.Models.ComicsResponse
import com.example.marvel.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ComicsAdapter() : RecyclerView.Adapter<ComicsAdapter.ComicsViewHolder>() {

    private var list : ArrayList<ComicsResponse> = ArrayList()

    fun getData(data : ArrayList<ComicsResponse>){
        list = data
        notifyDataSetChanged()
    }

    class ComicsViewHolder(view : View) : RecyclerView.ViewHolder(view){
        fun Comicbind(comic : ComicsResponse){
            val image = itemView.findViewById<ImageView>(R.id.characterImg)
            val name = itemView.findViewById<TextView>(R.id.characterName)
            val imageUrl = comic.thumbnail.path + "/portrait_xlarge." + comic.thumbnail.extension
            GlobalScope.launch(Dispatchers.Main) {
                Glide.with(image)
                    .load(imageUrl)
                    .placeholder(R.color.redPassed)
                    .into(image)
            }
            name.text = comic.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicsViewHolder {
        return ComicsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.character_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ComicsViewHolder, position: Int) {
        val comic = list[position]
        holder.Comicbind(comic)
    }

    override fun getItemCount() = list.size
}