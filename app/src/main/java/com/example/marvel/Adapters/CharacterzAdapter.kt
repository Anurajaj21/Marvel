package com.example.marvel.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marvel.Models.CharacterResponse
import com.example.marvel.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CharacterzAdapter() : RecyclerView.Adapter<CharacterzAdapter.adapterViewHolder>() {

    private var list : ArrayList<CharacterResponse> = ArrayList()

    fun getData(data : ArrayList<CharacterResponse>){
        list = data
        notifyDataSetChanged()
    }

    class adapterViewHolder(view : View) : RecyclerView.ViewHolder(view){
        fun Characterbind(character : CharacterResponse){
            val image = itemView.findViewById<ImageView>(R.id.characterImg)
            val name = itemView.findViewById<TextView>(R.id.characterName)
            val imageUrl = character.thumbnail.path + "/standard_fantastic." + character.thumbnail.extension
            GlobalScope.launch(Dispatchers.Main) {
                Glide.with(image)
                    .load(imageUrl)
                    .placeholder(R.color.redPassed)
                    .into(image)
            }
            name.text = character.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adapterViewHolder {
        return adapterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.character_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: adapterViewHolder, position: Int) {
        val character = list[position]
        holder.Characterbind(character)
    }

    override fun getItemCount() = list.size
}