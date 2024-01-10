package com.iwex.shoppinglist.domain

class DeleteShopItemUseCase(private val shopItemRepository: ShopItemRepository) {
    fun deleteShopItem(shopItem: ShopItem) {
        shopItemRepository.deleteShopItem(shopItem)
    }
}
