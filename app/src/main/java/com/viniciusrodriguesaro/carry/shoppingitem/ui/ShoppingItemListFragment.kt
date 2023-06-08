package com.viniciusrodriguesaro.carry.shoppingitem.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.viniciusrodriguesaro.carry.R
import com.viniciusrodriguesaro.carry.databinding.FragmentShoppingItemListBinding
import com.viniciusrodriguesaro.carry.shoppingitem.data.FirestoreShoppingItemListRepository
import com.viniciusrodriguesaro.carry.shoppingitem.data.FirestoreShoppingItemRepository
import com.viniciusrodriguesaro.carry.shoppingitem.utils.MeasurementTypeConverter

class ShoppingItemListFragment : Fragment() {
    private lateinit var adapter: ShoppingItemListAdapter

    private var _binding: FragmentShoppingItemListBinding? = null
    private val binding get() = _binding!!

    private val shoppingItemViewModel: ShoppingItemViewModel by activityViewModels {
        ShoppingItemViewModel.Factory(FirestoreShoppingItemRepository(MeasurementTypeConverter(requireContext())))
    }
    private val shoppingItemListViewModel: ShoppingItemListViewModel by activityViewModels {
        ShoppingItemListViewModel.Factory(FirestoreShoppingItemListRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter =
            ShoppingItemListAdapter(requireContext(), findNavController(), shoppingItemViewModel)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShoppingItemListBinding.inflate(inflater, container, false)
        val recyclerView = binding.list

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), R.drawable.divider))

        shoppingItemListViewModel.stateOnceAndStream().observe(viewLifecycleOwner) {
            if (it?.id != null) {
                shoppingItemViewModel.setShoppingItemListId(it.id)
            }
        }

        shoppingItemViewModel.stateOnceAndStream().observe(viewLifecycleOwner) {
            bindUiState(it)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener { _ -> findNavController().navigate(R.id.action_shoppingItemListFragment_to_newShoppingItemFragment) }
    }

    private fun bindUiState(uiState: ShoppingItemViewModel.UiState) {
        adapter.updateShoppingItems(uiState.shoppingItemList.sortedByDescending { it.createdAt }.sortedBy { it.isCompleted })
    }
}