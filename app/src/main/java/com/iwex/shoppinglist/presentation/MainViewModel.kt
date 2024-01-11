package com.iwex.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iwex.shoppinglist.data.ShopItemRepositoryImpl
import com.iwex.shoppinglist.domain.DeleteShopItemUseCase
import com.iwex.shoppinglist.domain.EditShopItemUseCase
import com.iwex.shoppinglist.domain.GetShopItemListUseCase
import com.iwex.shoppinglist.domain.ShopItem
import com.iwex.shoppinglist.domain.ShopItemRepository

class MainViewModel : ViewModel() {
    private val repository: ShopItemRepository = ShopItemRepositoryImpl

    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val getShopItemListUseCase = GetShopItemListUseCase(repository)

    private val _shopItemList = MutableLiveData<List<ShopItem>>()
    val shopItemList = _shopItemList as LiveData<List<ShopItem>>

    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
        getShopItemList()
    }

    fun changeShopItemEnabledState(shopItem: ShopItem) {
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(shopItem)
        getShopItemList()
    }

    fun getShopItemList() {
        val list = repository.getShopItemList()
        _shopItemList.value = list
    }
}
