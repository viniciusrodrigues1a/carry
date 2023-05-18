package com.viniciusrodriguesaro.carry.shoppingitem.ui

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.viniciusrodriguesaro.carry.R
import com.viniciusrodriguesaro.carry.databinding.ShoppingItemBinding

class ShoppingItemListAdapter(
    private val context: Context,
    private val items: List<ShoppingItem>
) : RecyclerView.Adapter<ShoppingItemListAdapter.ViewHolder>() {

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
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ShoppingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShoppingItem) {
            binding.shoppingItemNameTextview.text = item.name
            binding.shoppingItemCheckbox.isChecked = item.isChecked

            if (item.price != null) {
                binding.shoppingItemPriceTextview.text = item.price.toString()
                binding.shoppingItemPriceTextview.visibility = View.VISIBLE
            }

            if (item.amount != null && item.unitOfMeasurement != null) {
                val localizedUnitOfMeasurement = localizeUnitOfMeasurement(item)
                binding.shoppingItemUnitTextview.text = "${item.amount} $localizedUnitOfMeasurement"
                binding.shoppingItemUnitTextview.visibility = View.VISIBLE
            }
        }

        fun localizeUnitOfMeasurement(item: ShoppingItem): String {
            val u = item.unitOfMeasurement

            if (u is MeasurementType.EnumMeasurement) {
                return when ((u as MeasurementType.EnumMeasurement).value) {
                    UnitOfMeasurement.UNIT -> context.getString(R.string.unit_measurement)
                    UnitOfMeasurement.LITER -> context.getString(R.string.liter_measurement)
                    UnitOfMeasurement.KILOGRAM -> context.getString(R.string.kilogram_measurement)
                    UnitOfMeasurement.MILLIGRAM -> context.getString(R.string.milligram_measurement)
                    UnitOfMeasurement.GRAM -> context.getString(R.string.gram_measurement)
                }
            } else if (u is MeasurementType.StringMeasurement) {
                return (u as MeasurementType.StringMeasurement).value
            } else {
                return context.getString(R.string.unit_measurement)
            }
        }
    }
}