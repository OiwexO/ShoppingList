package com.iwex.shoppinglist.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.iwex.shoppinglist.R

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    private lateinit var shopItemsListAdapter: ShopItemListAdapter
    private lateinit var rvShopItemsList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopItemList.observe(this) {
            shopItemsListAdapter.shopItemList = it
        }
    }

    private fun setupRecyclerView() {
        rvShopItemsList =
            findViewById<RecyclerView?>(R.id.rvShopItemsList).apply {
                shopItemsListAdapter = ShopItemListAdapter()
                adapter = shopItemsListAdapter
                recycledViewPool.setMaxRecycledViews(
                    ShopItemListAdapter.VIEW_TYPE_ENABLED,
                    ShopItemListAdapter.MAX_POOL_SIZE,
                )
                recycledViewPool.setMaxRecycledViews(
                    ShopItemListAdapter.VIEW_TYPE_DISABLED,
                    ShopItemListAdapter.MAX_POOL_SIZE,
                )
            }
    }
}
