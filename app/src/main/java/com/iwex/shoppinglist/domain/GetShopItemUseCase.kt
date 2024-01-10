package com.iwex.shoppinglist.domain

class GetShopItemUseCase(private val shopItemRepository: ShopItemRepository) {
    fun getShopItemById(shopItemId: Int): ShopItem {
        return shopItemRepository.getShopItem(shopItemId)
    }
}
