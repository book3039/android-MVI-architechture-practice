package com.syncrown.mviarchitechturepractice.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syncrown.mviarchitechturepractice.api.AnimalService
import com.syncrown.mviarchitechturepractice.databinding.AnimalItemBinding
import com.syncrown.mviarchitechturepractice.model.Animal

class AnimalListAdapter(private val animals: ArrayList<Animal>) :
    RecyclerView.Adapter<AnimalListAdapter.DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AnimalItemBinding.inflate(inflater, parent, false)

        return DataViewHolder(binding)
    }

    override fun getItemCount() = animals.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(animals[position])
    }

    fun newAnimals(newAnimals: List<Animal>) {
        animals.clear()
        animals.addAll(newAnimals)
        notifyDataSetChanged()
    }

    class DataViewHolder(private val binding: AnimalItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(animal: Animal) {
            binding.animalName.text = animal.name
            binding.animalLocation.text = animal.location
            val url = AnimalService.BASE_URL + animal.image
            Glide.with(binding.animalImage.context)
                .load(url)
                .into(binding.animalImage)
        }
    }

}