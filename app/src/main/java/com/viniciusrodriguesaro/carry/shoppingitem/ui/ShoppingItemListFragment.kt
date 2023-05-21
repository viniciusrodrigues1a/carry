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

class ShoppingItemListFragment : Fragment() {
    private lateinit var adapter: ShoppingItemListAdapter

    private var _binding: FragmentShoppingItemListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShoppingItemListViewModel by activityViewModels {
        ShoppingItemListViewModel.Factory(MockedShoppingItemRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ShoppingItemListAdapter(requireContext(), viewModel)
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

        viewModel.stateOnceAndStream().observe(viewLifecycleOwner) {
            bindUiState(it)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener { _ -> findNavController().navigate(R.id.action_shoppingItemListFragment_to_newShoppingItemFragment) }
    }

    private fun bindUiState(uiState: ShoppingItemListViewModel.UiState) {
        adapter.updateShoppingItems(uiState.shoppingItemList.sortedBy { it.isCompleted })
    }
}