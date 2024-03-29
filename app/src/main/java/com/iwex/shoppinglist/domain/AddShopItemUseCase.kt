package com.iwex.shoppinglist.domain

class AddShopItemUseCase(private val shopItemRepository: ShopItemRepository) {
    fun addShopItem(shopItem: ShopItem) {
        shopItemRepository.addShopItem(shopItem)
    }
}
