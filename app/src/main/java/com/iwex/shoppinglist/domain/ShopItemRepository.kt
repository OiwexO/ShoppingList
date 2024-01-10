package com.iwex.shoppinglist.domain

interface ShopItemRepository {
    fun addShopItem(item: ShopItem)

    fun deleteShopItem(shopItem: ShopItem)

    fun editShopItem(shopItem: ShopItem)

    fun getShopItem(shopItemId: Int): ShopItem

    fun getShopItemList(): List<ShopItem>
}
