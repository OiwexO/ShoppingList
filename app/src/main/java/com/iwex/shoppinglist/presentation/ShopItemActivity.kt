package com.iwex.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.iwex.shoppinglist.R
import com.iwex.shoppinglist.domain.ShopItem

class ShopItemActivity : AppCompatActivity() {
    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: TextInputEditText
    private lateinit var etCount: TextInputEditText
    private lateinit var buttonSave: Button

    private lateinit var viewModel: ShopItemViewModel

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews()
        addTextChangedListeners()
        observeViewModel()
        setScreenMode()
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

    private fun initViews() {
        tilName = findViewById(R.id.tilName)
        tilCount = findViewById(R.id.tilCount)
        etName = findViewById(R.id.etName)
        etCount = findViewById(R.id.etCount)
        buttonSave = findViewById(R.id.buttonSave)
    }

    private fun setScreenMode() {
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
    }

    private fun launchAddMode() {
        buttonSave.setOnClickListener {
            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(this) {
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        buttonSave.setOnClickListener {
            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun observeViewModel() {
        viewModel.errorInputName.observe(this) {
            val message =
                if (it) {
                    getString(R.string.error_incorrect_name)
                } else {
                    null
                }
            tilName.error = message
        }
        viewModel.errorInputCount.observe(this) {
            val message =
                if (it) {
                    getString(R.string.error_incorrect_count)
                } else {
                    null
                }
            tilCount.error = message
        }
        viewModel.shouldFinish.observe(this) {
            finish()
        }
    }

    private fun addTextChangedListeners() {
        addNameChangedListener()
        addCountChangedListener()
    }

    private fun addNameChangedListener() {
        etName.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int,
                ) {
                    viewModel.resetErrorInputName()
                }

                override fun afterTextChanged(s: Editable?) {}
            },
        )
    }

    private fun addCountChangedListener() {
        etCount.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int,
                ) {
                    viewModel.resetErrorInputCount()
                }

                override fun afterTextChanged(s: Editable?) {}
            },
        )
    }

    companion object {
        private const val TAG = "ShopItemActivity"
        private const val EXTRA_SCREEN_MODE = "screen_mode"
        private const val EXTRA_SHOP_ITEM_ID = "shop_item_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddItem(context: Context): Intent {
            return Intent(context, ShopItemActivity::class.java)
                .putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
        }

        fun newIntentEditItem(
            context: Context,
            shopItemId: Int,
        ): Intent {
            return Intent(context, ShopItemActivity::class.java)
                .putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
                .putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
        }
    }
}
