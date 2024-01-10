package com.iwex.shoppinglist.domain

class GetShopItemListUseCase(private val shopItemRepository: ShopItemRepository) {
    fun getShopItemList(): List<ShopItem> {
        return shopItemRepository.getShopItemList()
    }
}
