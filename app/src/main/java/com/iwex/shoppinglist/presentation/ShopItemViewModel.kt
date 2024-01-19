package com.iwex.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iwex.shoppinglist.data.ShopItemRepositoryImpl
import com.iwex.shoppinglist.domain.AddShopItemUseCase
import com.iwex.shoppinglist.domain.EditShopItemUseCase
import com.iwex.shoppinglist.domain.GetShopItemUseCase
import com.iwex.shoppinglist.domain.ShopItem

class ShopItemViewModel() : ViewModel() {
    private val repository = ShopItemRepositoryImpl
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val getShopItemUseCase = GetShopItemUseCase(repository)

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shouldFinish = MutableLiveData<Unit>()
    val shouldFinish: LiveData<Unit>
        get() = _shouldFinish

    fun addShopItem(
        inputName: String?,
        inputCount: String?,
    ) {
        val name = parseInputString(inputName)
        val count = parseCount(inputCount)
        if (validateInput(name, count)) {
            val shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(shopItem)
            finishWork()
        }
    }

    fun editShopItem(
        inputName: String?,
        inputCount: String?,
    ) {
        val name = parseInputString(inputName)
        val count = parseCount(inputCount)
        if (validateInput(name, count)) {
            _shopItem.value?.let {
                val shopItem = it.copy(name = name, count = count)
                editShopItemUseCase.editShopItem(shopItem)
                finishWork()
            }
        }
    }

    fun getShopItem(shopItemId: Int) {
        val shopItem = getShopItemUseCase.getShopItemById(shopItemId)
        _shopItem.value = shopItem
    }

    private fun parseInputString(inputString: String?): String {
        return inputString?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        val trimmedInput = parseInputString(inputCount)
        return try {
            trimmedInput.toInt()
        } catch (e: NumberFormatException) {
            0
        }
    }

    private fun validateInput(
        name: String,
        count: Int,
    ): Boolean {
        var result = true
        if (name.isBlank()) {
            result = false
            _errorInputName.value = true
        }
        if (count <= 0) {
            result = false
            _errorInputCount.value = true
        }
        return result
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

    private fun finishWork() {
        _shouldFinish.value = Unit
    }
}
