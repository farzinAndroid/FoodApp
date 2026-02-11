package com.example.recipeappmvvm.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.recipeappmvvm.data.model.remote.ResponseFoodList
import com.example.ui.databinding.FoodItemBinding
import javax.inject.Inject

class FoodsListAdapter @Inject constructor() :
    RecyclerView.Adapter<FoodsListAdapter.MyViewHolder>() {

    private lateinit var binding: FoodItemBinding
    private lateinit var context: Context
    private var foodsListList = emptyList<ResponseFoodList.Meal>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = FoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return MyViewHolder()
    }

    override fun getItemCount(): Int {
        return foodsListList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(foodsListList[position])
        holder.setIsRecyclable(false)
    }


    inner class MyViewHolder() : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseFoodList.Meal) {
            binding.apply {

                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }

                itemFoodsImg.load(item.strMealThumb) {
                    crossfade(true)
                    crossfade(500)
                }

                if (item.strCategory.isNullOrEmpty().not()) {
                    itemFoodsCategory.text = item.strCategory
                    itemFoodsCategory.visibility = View.VISIBLE
                } else {
                    itemFoodsCategory.visibility = View.GONE
                }

                if (item.strArea.isNullOrEmpty().not()) {
                    itemFoodsArea.text = item.strArea
                    itemFoodsArea.visibility = View.VISIBLE
                } else {
                    itemFoodsArea.visibility = View.GONE
                }

                itemFoodsTitle.text = item.strMeal

                itemFoodsArea.text = item.strArea

                if (item.strSource != null) {
                    itemFoodsCount.visibility = ViewGroup.VISIBLE
                } else {
                    itemFoodsCount.visibility = ViewGroup.GONE
                }


            }
        }
    }


    private var onItemClickListener: ((ResponseFoodList.Meal) -> Unit)? = null

    fun setOnClickListener(listener: ((ResponseFoodList.Meal) -> Unit)) {
        onItemClickListener = listener
    }

    fun setData(data: List<ResponseFoodList.Meal>) {
        val foodsDiffUtils = FoodsDiffUtils(foodsListList, data)
        val diffUtils = DiffUtil.calculateDiff(foodsDiffUtils)
        foodsListList = data
        diffUtils.dispatchUpdatesTo(this)
    }


    class FoodsDiffUtils(private val oldItem: List<ResponseFoodList.Meal>, private val newItem: List<ResponseFoodList.Meal>) :
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