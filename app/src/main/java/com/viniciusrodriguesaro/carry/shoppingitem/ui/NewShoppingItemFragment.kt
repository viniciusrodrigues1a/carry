package com.viniciusrodriguesaro.carry.shoppingitem.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.viniciusrodriguesaro.carry.databinding.FragmentNewShoppingItemBinding
import com.viniciusrodriguesaro.carry.shoppingitem.dto.CreateShoppingItemInput
import com.viniciusrodriguesaro.carry.shoppingitem.dto.MeasurementType
import com.viniciusrodriguesaro.carry.shoppingitem.dto.UnitOfMeasurement

class NewShoppingItemFragment : Fragment() {
    private var _binding: FragmentNewShoppingItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShoppingItemListViewModel by activityViewModels {
        ShoppingItemListViewModel.Factory(MockedShoppingItemRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewShoppingItemBinding.inflate(inflater, container, false)

        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitButton.setOnClickListener { _ -> createShoppingItem() }
        binding.nameEditText.addTextChangedListener {
            binding.submitButton.isEnabled = !it.isNullOrEmpty()
        }
    }

    fun createShoppingItem() {
        val name = binding.nameEditText.text.toString()
        val description = binding.descriptionEditText.text.toString()
        val price = binding.priceEditText.text.toString().toDoubleOrNull()

        viewModel.createShoppingItem(
            CreateShoppingItemInput(
                name = name,
                description = description,
                price = price,
                amount = 1,
                unitOfMeasurement = MeasurementType.EnumMeasurement(UnitOfMeasurement.UNIT)
            )
        )

        findNavController().navigateUp()
    }
}