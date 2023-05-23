package com.viniciusrodriguesaro.carry.shoppingitem.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.navArgs
import com.viniciusrodriguesaro.carry.R
import com.viniciusrodriguesaro.carry.databinding.FragmentEditShoppingItemBinding
import com.viniciusrodriguesaro.carry.shoppingitem.dto.UnitOfMeasurement

class EditShoppingItemFragment : Fragment() {
    private var _binding: FragmentEditShoppingItemBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ArrayAdapter<String>

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
        binding.deleteButton.isEnabled = true
        binding.deleteButton.setBackgroundColor(resources.getColor(R.color.red))
    }
}