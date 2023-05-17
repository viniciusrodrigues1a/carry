package com.viniciusrodriguesaro.carry.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.viniciusrodriguesaro.carry.R

class ShoppingItemListFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val recyclerView =
            inflater.inflate(R.layout.fragment_shopping_item_list, container, false) as RecyclerView

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter =
            ShoppingItemListAdapter(requireContext(), MockedShoppingItems.mockedShoppingItems)
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), R.drawable.divider))

        return recyclerView
    }
}