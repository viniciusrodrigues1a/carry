package com.viniciusrodriguesaro.carry.shoppingitem.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityManager
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.viniciusrodriguesaro.carry.R
import com.viniciusrodriguesaro.carry.databinding.FragmentNewShoppingItemBinding
import com.viniciusrodriguesaro.carry.shoppingitem.data.FirestoreShoppingItemRepository
import com.viniciusrodriguesaro.carry.shoppingitem.dto.CreateShoppingItemInput
import com.viniciusrodriguesaro.carry.shoppingitem.dto.UnitOfMeasurement
import com.viniciusrodriguesaro.carry.shoppingitem.utils.MeasurementTypeConverter
import com.viniciusrodriguesaro.carry.shoppingitem.utils.priceFormatter

class NewShoppingItemFragment : Fragment() {
    private var _binding: FragmentNewShoppingItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShoppingItemViewModel by activityViewModels {
        ShoppingItemViewModel.Factory(FirestoreShoppingItemRepository(MeasurementTypeConverter(requireContext())))
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
        var adapter = ArrayAdapter(
            context,
            R.layout.shopping_item_dropdown_item,
            UnitOfMeasurement.getLocalizedValues(context),
        )
        binding.unitAutoCompleteTextView.setAdapter(adapter)

        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitButton.setOnClickListener { _ -> handleOnCreatePress() }
        binding.nameEditText.addTextChangedListener {
            binding.submitButton.isEnabled = !it.isNullOrEmpty()
        }

        addPriceFormatterMaskToPriceInput()
    }

    private fun addPriceFormatterMaskToPriceInput() {
        val priceTextWatcher = object : TextWatcher {
            private var previousText = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                binding.priceEditText.removeTextChangedListener(this)

                /* Check if user is pressing backspace */
                val isDeletingText = s.toString().length < previousText.length

                /* Control input */
                binding.priceEditText.setText(previousText);
                binding.priceEditText.setSelection(previousText.length);

                /* Parse string to int */
                var priceInt = s.toString().toIntOrNull()

                if (isDeletingText) {
                    priceInt = priceFormatter.removeOneDigit(previousText)
                } else {
                    val addition = priceFormatter.addOneDigit(s.toString())
                    priceInt = addition ?: priceInt
                }

                /* Format int to currency */
                if (priceInt != null) {
                    val formatted = priceFormatter.format(priceInt)

                    previousText = formatted

                    binding.priceEditText.setText(formatted)
                    binding.priceEditText.setSelection(formatted.length)
                }

                binding.priceEditText.addTextChangedListener(this)
            }
        }

        binding.priceEditText.addTextChangedListener(priceTextWatcher)
    }

    private fun handleOnCreatePress() {
        createShoppingItem()

        findNavController().popBackStack()
    }

    private fun createShoppingItem() {
        val name = binding.nameEditText.text.toString()
        val description = binding.descriptionEditText.text.toString()
        val price = priceFormatter.parse(binding.priceEditText.text.toString())
        val amount = binding.amountEditText.text.toString().toIntOrNull()

        val unitText = binding.unitAutoCompleteTextView.text.toString()
        val defaultUnitText = getDefaultUnitText()
        val localizedUnitText = if (unitText.isNullOrEmpty()) defaultUnitText else unitText
        val unit = if (amount != null) localizedUnitText else null

        viewModel.createShoppingItem(
            CreateShoppingItemInput(
                name = name,
                description = description,
                price = price,
                amount = amount,
                unitOfMeasurementString = unit,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    private fun getDefaultUnitText(): String {
        val context = requireContext()
        val accessibilityManager =
            context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val isScreenReaderEnabled = accessibilityManager.isEnabled && accessibilityManager.isTouchExplorationEnabled

        if (isScreenReaderEnabled) {
            return context.getString(R.string.content_description_unit_measurement)
        } else {
            return context.getString(R.string.unit_measurement)
        }

    }
}