package com.iwex.shoppinglist.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iwex.shoppinglist.R

class ShopItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val textViewShopItemName: TextView = view.findViewById(R.id.textViewItemName)
    val textViewShopItemCount: TextView = view.findViewById(R.id.textViewItemCount)
}
