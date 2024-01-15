package com.iwex.shoppinglist.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
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
            shopItemsListAdapter.submitList(it)
        }
    }

    private fun setupRecyclerView() {
        shopItemsListAdapter = ShopItemListAdapter()
        rvShopItemsList = findViewById(R.id.rvShopItemsList)
        with(rvShopItemsList) {
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
        setOnShopItemClickListener()
        setOnShopItemLongClickListener()
        setOnShopItemHorizontalSwipeListener(rvShopItemsList)
    }

    private fun setOnShopItemHorizontalSwipeListener(recyclerView: RecyclerView) {
        val horizontalSwipeCallback =
            object : SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder,
                ): Boolean {
                    return false
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int,
                ) {
                    val item = shopItemsListAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.deleteShopItem(item)
                }
            }
        val touchHelper = ItemTouchHelper(horizontalSwipeCallback)
        touchHelper.attachToRecyclerView(recyclerView)
    }

    private fun setOnShopItemLongClickListener() {
        shopItemsListAdapter.onShopItemLongClickListener = {
            viewModel.changeShopItemEnabledState(it)
        }
    }

    private fun setOnShopItemClickListener() {
        shopItemsListAdapter.onShopItemClickListener = {
            Log.d(TAG, it.toString())
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
