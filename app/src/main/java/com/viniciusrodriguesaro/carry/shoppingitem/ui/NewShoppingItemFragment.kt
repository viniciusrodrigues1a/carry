package com.viniciusrodriguesaro.carry.shoppingitem.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.viniciusrodriguesaro.carry.R
import com.viniciusrodriguesaro.carry.databinding.FragmentNewShoppingItemBinding
import com.viniciusrodriguesaro.carry.shoppingitem.dto.CreateShoppingItemInput
import com.viniciusrodriguesaro.carry.shoppingitem.dto.UnitOfMeasurement

class NewShoppingItemFragment : Fragment() {
    private var _binding: FragmentNewShoppingItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShoppingItemViewModel by activityViewModels {
        ShoppingItemViewModel.Factory(MockedShoppingItemRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewShoppingItemBinding.inflate(inflater, container, false)

        val context = requireContext()
        val adapter = ArrayAdapter(
            context,
            R.layout.shopping_item_dropdown_item,
            UnitOfMeasurement.getLocalizedValues(context)
        );
        binding.unitAutoCompleteTextView.setAdapter(adapter)

        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitButton.setOnClickListener { _ -> handleOnCreatePress() }
        binding.nameEditText.addTextChangedListener {
            binding.submitButton.isEnabled = !it.isNullOrEmpty()
        }
    }

    private fun handleOnCreatePress() {
        createShoppingItem()

        findNavController().popBackStack()
    }

    private fun createShoppingItem() {
        val name = binding.nameEditText.text.toString()
        val description = binding.descriptionEditText.text.toString()
        val price = binding.priceEditText.text.toString().toIntOrNull()

        val unitText = binding.unitAutoCompleteTextView.text.toString()
        val amountText = binding.amountEditText.text.toString()

        var amount: Int? = null
        var unit: String? = null

        if (!unitText.isNullOrEmpty() && !amountText.isNullOrEmpty()) {
            unit = unitText
            amount = amountText.toInt()
        }

        viewModel.createShoppingItem(
            CreateShoppingItemInput(
                name = name,
                description = description,
                price = price,
                amount = amount,
                unitOfMeasurementString = unit
            )
        )
    }
}