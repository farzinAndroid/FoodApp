package com.example.foodappmvp.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foodappmvp.data.model.ResponseCategoriesList.Category
import com.example.ui.R
import com.example.ui.databinding.CategoryItemBinding
import javax.inject.Inject

class CategoriesListAdapter @Inject constructor() : RecyclerView.Adapter<CategoriesListAdapter.MyViewHolder>()  {

//    private lateinit var binding: CategoryItemBinding
    private lateinit var context: Context
    private var categoriesList = emptyList<Category>()

    private var selectedItem = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(categoriesList[position])
        holder.setIsRecyclable(true)
    }


    inner class MyViewHolder(private val binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Category){
            binding.apply {

                itemCategoriesImg.load(item.strCategoryThumb) {
                    crossfade(true)
                    crossfade(500)
                }
                itemCategoriesTxt.text = item.strCategory
                //Click
                root.setOnClickListener {
                    selectedItem = adapterPosition
                    notifyDataSetChanged()
                    onItemClickListener?.let {
                        it(item)
                    }
                }
                //Change color
                if (selectedItem == adapterPosition) {
                    root.setBackgroundResource(R.drawable.bg_rounded_selcted)
                } else {
                    root.setBackgroundResource(R.drawable.bg_rounded_white)
                }


            }
        }
    }


    private var onItemClickListener : ((Category) ->  Unit)? = null

    fun setOnClickListener(listener:((Category) ->  Unit)) {
        onItemClickListener = listener
    }

    fun setData(data:List<Category>){
        val categoriesDiffUtils = CategoriesDiffUtils(categoriesList,data)
        val diffUtils = DiffUtil.calculateDiff(categoriesDiffUtils)
        categoriesList = data
        diffUtils.dispatchUpdatesTo(this)
    }


    class CategoriesDiffUtils(private val oldItem:List<Category>, private val newItem:List<Category>) : DiffUtil.Callback(){
        override fun getOldListSize(): Int {
            return oldItem.size
        }

        override fun getNewListSize(): Int {
            return newItem.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] == newItem[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] == newItem[newItemPosition]
        }
    }



}