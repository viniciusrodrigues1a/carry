package com.viniciusrodriguesaro.carry.shoppingitem.ui

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.AccessibilityDelegate
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.viniciusrodriguesaro.carry.R
import com.viniciusrodriguesaro.carry.databinding.ShoppingItemBinding
import com.viniciusrodriguesaro.carry.shoppingitem.dto.MeasurementType
import com.viniciusrodriguesaro.carry.shoppingitem.utils.MeasurementTypeConverter
import com.viniciusrodriguesaro.carry.shoppingitem.utils.priceFormatter

class ShoppingItemListAdapter(
    private val context: Context,
    private val navController: NavController,
    private val viewModel: ShoppingItemViewModel
) : RecyclerView.Adapter<ShoppingItemListAdapter.ViewHolder>() {
    private val asyncListDiffer: AsyncListDiffer<ShoppingItem> = AsyncListDiffer(this, DiffCallback)
    private val measurementTypeConverter = MeasurementTypeConverter(context)

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
        holder.bind(asyncListDiffer.currentList[position], position)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    fun updateShoppingItems(shoppingItems: List<ShoppingItem>) {
        asyncListDiffer.submitList(shoppingItems)
    }

    inner class ViewHolder(private val binding: ShoppingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShoppingItem, position: Int) {
            bindShoppingItemCheckbox(item)
            bindShoppingItemConstraintLayout(item)
            bindInputs(item)
            makeViewsAccessible(item, position)
        }

        private fun bindShoppingItemConstraintLayout(item: ShoppingItem) {
            val action =
                ShoppingItemListFragmentDirections.actionShoppingItemListFragmentToEditShoppingItemFragment(
                    item.id,
                    item.name,
                    item.description,
                    item.amount ?: -1,
                    measurementTypeConverter.measurementTypeToLocalizedString(item.unitOfMeasurement),
                    item.price ?: -1
                )
            binding.shoppingItemConstraintLayout.setOnClickListener { _ ->
                navController.navigate(
                    action
                )
            }
        }

        private fun bindShoppingItemCheckbox(item: ShoppingItem) {
            binding.shoppingItemCheckbox.setOnCheckedChangeListener(null)
            binding.shoppingItemCheckbox.isChecked = item.isCompleted
            binding.shoppingItemCheckbox.setOnCheckedChangeListener { _, _ ->
                viewModel.toggleShoppingItemCompleted(item.id)
            }
        }

        private fun bindInputs(item: ShoppingItem) {
            binding.shoppingItemNameTextview.text = item.name

            if (item.price != null && item.price!! > 0) {
                binding.shoppingItemPriceTextview.text = priceFormatter.format(item.price!!)
                binding.shoppingItemPriceTextview.visibility = View.VISIBLE
            }

            if (item.amount != null && item.unitOfMeasurement != null) {
                val localizedUnitOfMeasurement =
                    measurementTypeConverter.measurementTypeToLocalizedString(item.unitOfMeasurement)
                binding.shoppingItemUnitTextview.text = "${item.amount} $localizedUnitOfMeasurement"
                binding.shoppingItemUnitTextview.visibility = View.VISIBLE
            }
        }

        private fun makeViewsAccessible(item: ShoppingItem, position: Int) {
            val contentDescription = context.getString(R.string.content_description_shopping_item)
                .format(position + 1, item.name)
            binding.shoppingItemConstraintLayout.contentDescription = contentDescription

            val notSelectedText =
                context.getString(R.string.content_description_shopping_item_checkbox_not_selected)
                    .format(position + 1, item.name);
            val selectedText =
                context.getString(R.string.content_description_shopping_item_checkbox_selected)
                    .format(position + 1, item.name);
            val accessibilityText = if (item.isCompleted) selectedText else notSelectedText

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                binding.shoppingItemCheckbox.stateDescription = accessibilityText
            } else {
                binding.shoppingItemCheckbox.contentDescription = accessibilityText
            }

            if (item.amount != null && item.unitOfMeasurement != null) {
                val accessibleString =
                    measurementTypeConverter.measurementTypeToAccessibleLocalizedString(item.unitOfMeasurement)
                binding.shoppingItemUnitTextview.contentDescription = "${item.amount} $accessibleString"
            }

        }
    }

    object DiffCallback : DiffUtil.ItemCallback<ShoppingItem>() {
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem == newItem
        }
    }
}