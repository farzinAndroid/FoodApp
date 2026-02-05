package com.example.foodappmvp.ui.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foodappmvp.data.model.database.FoodEntity
import com.example.foodappmvp.databinding.FoodItemBinding
import javax.inject.Inject

class FavoriteListAdapter @Inject constructor() :
    RecyclerView.Adapter<FavoriteListAdapter.MyViewHolder>() {

    private lateinit var binding: FoodItemBinding
    private lateinit var context: Context
    private var favoriteList = emptyList<FoodEntity>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = FoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return MyViewHolder()
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(favoriteList[position])
        holder.setIsRecyclable(true)
    }


    inner class MyViewHolder() : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FoodEntity) {
            binding.apply {

                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }

                itemFoodsImg.load(item.image) {
                    crossfade(true)
                    crossfade(500)
                }

                itemFoodsCategory.visibility = View.GONE
                itemFoodsArea.visibility = View.GONE
                itemFoodsTitle.text = item.title
                itemFoodsCount.visibility = ViewGroup.GONE


            }
        }
    }


    private var onItemClickListener: ((FoodEntity) -> Unit)? = null

    fun setOnClickListener(listener: ((FoodEntity) -> Unit)) {
        onItemClickListener = listener
    }

    fun setData(data: List<FoodEntity>) {
        val foodsDiffUtils = FoodsDiffUtils(favoriteList, data)
        val diffUtils = DiffUtil.calculateDiff(foodsDiffUtils)
        favoriteList = data
        diffUtils.dispatchUpdatesTo(this)
    }


    class FoodsDiffUtils(
        private val oldItem: List<FoodEntity>,
        private val newItem: List<FoodEntity>
    ) :
        DiffUtil.Callback() {
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