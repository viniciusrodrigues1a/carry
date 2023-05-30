package com.viniciusrodriguesaro.carry.shoppingitem.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.viniciusrodriguesaro.carry.R
import com.viniciusrodriguesaro.carry.databinding.FragmentEditShoppingItemBinding
import com.viniciusrodriguesaro.carry.shoppingitem.data.FirestoreShoppingItemRepository
import com.viniciusrodriguesaro.carry.shoppingitem.dto.UnitOfMeasurement
import com.viniciusrodriguesaro.carry.shoppingitem.dto.UpdateShoppingItemInput

class EditShoppingItemFragment : Fragment() {
    private var _binding: FragmentEditShoppingItemBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ArrayAdapter<String>

    private val viewModel: ShoppingItemViewModel by activityViewModels {
        ShoppingItemViewModel.Factory(FirestoreShoppingItemRepository)
    }

    private val args: EditShoppingItemFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditShoppingItemBinding.inflate(inflater, container, false)

        val context = requireContext()
        adapter = ArrayAdapter(
            context,
            R.layout.shopping_item_dropdown_item,
            UnitOfMeasurement.getLocalizedValues(context)
        )
        binding.unitAutoCompleteTextView.setAdapter(adapter)

        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindArgs()
        bindButtons()
    }

    private fun bindArgs() {
        binding.nameEditText.setText(args.name)
        binding.descriptionEditText.setText(args.description)
        if (args.amount != -1) {
            binding.amountEditText.setText(args.amount.toString())
        }

        if (!args.measurementType.isNullOrEmpty()) {
            binding.unitAutoCompleteTextView.setText(args.measurementType)
            adapter.filter.filter(null)
        }

        binding.priceEditText.setText(args.price.toString())
    }

    private fun bindButtons() {
        binding.submitButton.isEnabled = true
        binding.submitButton.setOnClickListener { _ -> handleOnUpdatePress() }

        binding.deleteButton.isEnabled = true
        binding.deleteButton.setBackgroundColor(resources.getColor(R.color.red))
        binding.deleteButton.setOnClickListener { _ -> handleOnDeletePress() }
    }

    private fun handleOnUpdatePress() {
        updateShoppingItem()
        findNavController().popBackStack()
    }

    private fun updateShoppingItem() {
        val name = binding.nameEditText.text.toString()
        val description = binding.descriptionEditText.text.toString()
        val price = binding.priceEditText.text.toString().toIntOrNull()

        val unitText = binding.unitAutoCompleteTextView.text.toString()
        val amountText = binding.amountEditText.text.toString()

        var unit: String? = null
        var amount: Int? = null

        if (!unitText.isNullOrEmpty() && !amountText.isNullOrEmpty()) {
            unit = unitText
            amount = amountText.toInt()
        }

        viewModel.updateShoppingItem(
            UpdateShoppingItemInput(
                id = args.id,
                newName = name,
                newDescription = description,
                newPrice = price,
                newAmount = amount,
                newUnitOfMeasurentString = unit,
            )
        )
    }

    private fun handleOnDeletePress() {
        viewModel.deleteShoppingItem(args.id)
        findNavController().popBackStack()
    }
}