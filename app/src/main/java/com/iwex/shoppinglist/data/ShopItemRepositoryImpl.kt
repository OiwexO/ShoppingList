package com.iwex.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iwex.shoppinglist.domain.ShopItem
import com.iwex.shoppinglist.domain.ShopItemRepository
import kotlin.random.Random

object ShopItemRepositoryImpl : ShopItemRepository {
    private val shopItemListLiveData = MutableLiveData<List<ShopItem>>()
    private val shopItemList = sortedSetOf<ShopItem>({ o1, o2 -> o1.id.compareTo(o2.id) })

    private var autoIncrementId = 0

    init {
        for (i in 0 until 10) {
            val item = ShopItem("Name $i", i, Random.nextBoolean())
            addShopItem(item)
        }
    }

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        shopItemList.add(shopItem)
        updateList()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopItemList.remove(shopItem)
        updateList()
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

    override fun getShopItemList(): LiveData<List<ShopItem>> {
        return shopItemListLiveData
    }

    private fun updateList() {
        shopItemListLiveData.value = shopItemList.toList()
    }
}
