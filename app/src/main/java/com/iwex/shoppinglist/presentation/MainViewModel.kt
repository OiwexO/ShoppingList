package com.iwex.shoppinglist.presentation

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

    val shopItemList = getShopItemListUseCase.getShopItemList()

    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
    }

    fun changeShopItemEnabledState(shopItem: ShopItem) {
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(newItem)
    }
}
