package com.iwex.shoppinglist.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.iwex.shoppinglist.R

class MainActivity : AppCompatActivity(), OnEditingFinishedListener {
    private lateinit var viewModel: MainViewModel

    private lateinit var shopItemsListAdapter: ShopItemListAdapter
    private lateinit var rvShopItemsList: RecyclerView
    private var shopItemFragmentContainer: FragmentContainerView? = null
    private val isPortraitMode: Boolean
        get() = shopItemFragmentContainer == null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        shopItemFragmentContainer = findViewById(R.id.shopItemFragmentContainer)
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopItemList.observe(this) {
            shopItemsListAdapter.submitList(it)
        }
        val buttonAddItem = findViewById<FloatingActionButton>(R.id.buttonAddShopItem)
        buttonAddItem.setOnClickListener {
            if (isPortraitMode) {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                launchShopItemFragment(ShopItemFragment.newInstanceAddItem())
            }
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
            if (isPortraitMode) {
                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            } else {
                launchShopItemFragment(ShopItemFragment.newInstanceEditItem(it.id))
            }
        }
    }

    private fun launchShopItemFragment(fragment: ShopItemFragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.shopItemFragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onEditingFinished() {
        supportFragmentManager.popBackStack()
        Toast.makeText(this, getString(R.string.toast_success), Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
