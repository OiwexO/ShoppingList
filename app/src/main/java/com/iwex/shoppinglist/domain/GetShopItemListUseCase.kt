package com.iwex.shoppinglist.domain

import androidx.lifecycle.LiveData

class GetShopItemListUseCase(private val shopItemRepository: ShopItemRepository) {
    fun getShopItemList(): LiveData<List<ShopItem>> {
        return shopItemRepository.getShopItemList()
    }
}
