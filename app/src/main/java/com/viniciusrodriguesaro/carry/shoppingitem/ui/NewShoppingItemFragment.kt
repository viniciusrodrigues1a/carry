package com.viniciusrodriguesaro.carry.shoppingitem.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.viniciusrodriguesaro.carry.shoppingitem.data.FirestoreShoppingItemRepository
import com.viniciusrodriguesaro.carry.shoppingitem.dto.CreateShoppingItemInput
import com.viniciusrodriguesaro.carry.shoppingitem.dto.UnitOfMeasurement
import java.text.NumberFormat
import java.text.ParseException

class NewShoppingItemFragment : Fragment() {
    private var _binding: FragmentNewShoppingItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShoppingItemViewModel by activityViewModels {
        ShoppingItemViewModel.Factory(FirestoreShoppingItemRepository)
    }

    private var previousText = ""

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

        addPriceFormatterMaskToPriceInput()
    }

    private fun addPriceFormatterMaskToPriceInput() {
        val formatter = NumberFormat.getCurrencyInstance()

        val priceTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                binding.priceEditText.removeTextChangedListener(this)

                var isDeletingText: Boolean

                /* Check if user is pressing backspace */
                if (s.toString().length < previousText.length) {
                    isDeletingText = true
                    binding.priceEditText.setText(previousText);
                    binding.priceEditText.setSelection(previousText.length);
                } else {
                    isDeletingText = false
                }

                var priceInt = s.toString().toIntOrNull()

                /* Parse string to int */
                try {
                    if (isDeletingText) {
                        val parsedString = formatter.parse(previousText)
                        priceInt =
                            (parsedString.toDouble() * 100).toInt().toString().dropLast(1).toInt()
                    } else {
                        val parsedString = formatter.parse(s.toString())
                        val last = s?.lastOrNull().toString().toIntOrNull()
                        priceInt =
                            ((parsedString.toDouble() * 100).toInt()
                                .toString() + last).toIntOrNull()
                    }
                } catch (e: ParseException) {
                } catch (e: java.lang.NumberFormatException) {
                    priceInt = 0
                }

                /* Format int to currency */
                if (priceInt != null) {
                    val formatted = formatter.format(priceInt / 100.0)

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