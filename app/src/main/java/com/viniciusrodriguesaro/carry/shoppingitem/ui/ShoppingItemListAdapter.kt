package com.viniciusrodriguesaro.carry.shoppingitem.ui

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.viniciusrodriguesaro.carry.databinding.ShoppingItemBinding
import com.viniciusrodriguesaro.carry.shoppingitem.ui.utils.unitOfMeasurementToLocalizedString

class ShoppingItemListAdapter(
    private val context: Context,
    private val viewModel: ShoppingItemListViewModel
) : RecyclerView.Adapter<ShoppingItemListAdapter.ViewHolder>() {
    private val asyncListDiffer: AsyncListDiffer<ShoppingItem> = AsyncListDiffer(this, DiffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ShoppingItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(asyncListDiffer.currentList[position])
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    fun updateShoppingItems(shoppingItems: List<ShoppingItem>) {
        asyncListDiffer.submitList(shoppingItems)
    }

    inner class ViewHolder(private val binding: ShoppingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShoppingItem) {
            binding.shoppingItemCheckbox.isChecked = item.isCompleted
            binding.shoppingItemCheckbox.addOnCheckedStateChangedListener { _, _ ->
                viewModel.toggleShoppingItemCompleted(
                    item.id
                )
            }

            binding.shoppingItemNameTextview.text = item.name
            binding.shoppingItemCheckbox.isChecked = item.isCompleted

            if (item.price != null) {
                binding.shoppingItemPriceTextview.text = item.price.toString()
                binding.shoppingItemPriceTextview.visibility = View.VISIBLE
            }

            if (item.amount != null && item.unitOfMeasurement != null) {
                val localizedUnitOfMeasurement = unitOfMeasurementToLocalizedString(context, item.unitOfMeasurement)
                binding.shoppingItemUnitTextview.text = "${item.amount} $localizedUnitOfMeasurement"
                binding.shoppingItemUnitTextview.visibility = View.VISIBLE
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<ShoppingItem>() {
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.isCompleted == newItem.isCompleted
        }
    }
}