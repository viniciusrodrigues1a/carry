package com.viniciusrodriguesaro.carry.shoppingitem.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.viniciusrodriguesaro.carry.R

class ShoppingItemListFragment : Fragment() {
    private lateinit var adapter: ShoppingItemListAdapter

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
        val view =
            inflater.inflate(R.layout.fragment_shopping_item_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.list)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), R.drawable.divider))

        viewModel.stateOnceAndStream().observe(viewLifecycleOwner) {
            bindUiState(it)
        }

        return view
    }

    private fun bindUiState(uiState: ShoppingItemListViewModel.UiState) {
        adapter.updateShoppingItems(uiState.shoppingItemList)
    }
}