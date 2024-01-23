package com.iwex.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.iwex.shoppinglist.R
import com.iwex.shoppinglist.domain.ShopItem

class ShopItemActivity : AppCompatActivity(), OnEditingFinishedListener {
    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        if (savedInstanceState == null) {
            launchShopItemFragment()
        }
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Intent does not have EXTRA_SCREEN_MODE")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Unknown screen mode: $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Intent does not have EXTRA_SHOP_ITEM_ID")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun launchShopItemFragment() {
        val fragment =
            when (screenMode) {
                MODE_ADD -> ShopItemFragment.newInstanceAddItem()
                MODE_EDIT -> ShopItemFragment.newInstanceEditItem(shopItemId)
                else -> throw RuntimeException("Unknown screen mode: $screenMode")
            }
        supportFragmentManager.beginTransaction()
            .replace(R.id.shopItemFragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onEditingFinished() {
        supportFragmentManager.popBackStack()
        Toast.makeText(this, getString(R.string.toast_success), Toast.LENGTH_SHORT).show()
        finish()
    }

    companion object {
        private const val TAG = "ShopItemActivity"
        private const val EXTRA_SCREEN_MODE = "screen_mode"
        private const val EXTRA_SHOP_ITEM_ID = "shop_item_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddItem(context: Context) =
            Intent(context, ShopItemActivity::class.java)
                .putExtra(EXTRA_SCREEN_MODE, MODE_ADD)

        fun newIntentEditItem(
            context: Context,
            shopItemId: Int,
        ) = Intent(context, ShopItemActivity::class.java)
            .putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            .putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
    }
}
