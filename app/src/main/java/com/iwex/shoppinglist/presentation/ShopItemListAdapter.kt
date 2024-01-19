package com.iwex.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.iwex.shoppinglist.R
import com.iwex.shoppinglist.domain.ShopItem

class ShopItemListAdapter : ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ShopItemViewHolder {
        val layoutId =
            when (viewType) {
                VIEW_TYPE_ENABLED -> R.layout.shop_item_enabled
                VIEW_TYPE_DISABLED -> R.layout.shop_item_disabled
                else -> throw RuntimeException("Unknown view type ($viewType)")
            }
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ShopItemViewHolder,
        position: Int,
    ) {
        val item = currentList[position]
        holder.textViewShopItemName.text = item.name
        holder.textViewShopItemCount.text = item.count.toString()
        holder.view.setOnClickListener {
            onShopItemClickListener?.invoke(item)
        }
        holder.view.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(item)
            true
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = currentList[position]
        return if (item.enabled) VIEW_TYPE_ENABLED else VIEW_TYPE_DISABLED
    }

    companion object {
        const val VIEW_TYPE_DISABLED = 0
        const val VIEW_TYPE_ENABLED = 1
        const val MAX_POOL_SIZE = 15
    }
}
