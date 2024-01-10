package com.iwex.shoppinglist.data

import com.iwex.shoppinglist.domain.ShopItem
import com.iwex.shoppinglist.domain.ShopItemRepository
import java.lang.RuntimeException

object ShopItemRepositoryImpl : ShopItemRepository {
    private val shopItemList = mutableListOf<ShopItem>()

    private var autoIncrementId = 0

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        shopItemList.add(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopItemList.remove(shopItem)
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldShopItem = getShopItem(shopItem.id)
        shopItemList.remove(oldShopItem)
        addShopItem(shopItem)
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopItemList.find {
            it.id == shopItemId
        } ?: throw RuntimeException("ShopItem with id $shopItemId not found")
    }

    override fun getShopItemList(): List<ShopItem> {
        return shopItemList.toList()
    }
}
