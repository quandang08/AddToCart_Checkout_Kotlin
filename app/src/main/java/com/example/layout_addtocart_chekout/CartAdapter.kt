package com.example.layout_addtocart_chekout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast

class CartAdapter(private val cartList: MutableList<CartItem>, private val context: Context) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartList[position]
        holder.bind(item)
        holder.itemView.setOnLongClickListener {
            // Xóa sản phẩm khi nhấn
            deleteItem(position)
            true
        }
    }

    override fun getItemCount(): Int = cartList.size

    // Phương thức xóa sản phẩm
    fun deleteItem(position: Int) {
        cartList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, cartList.size)

        // Lưu giỏ hàng sau khi xóa sản phẩm
        (context as CartActivity).saveCartToPreferences()

        // Cập nhật tổng tiền
        (context as CartActivity).updateTotal()

        // Thông báo cho người dùng
        Toast.makeText(context, "Sản phẩm đã được xóa!", Toast.LENGTH_SHORT).show()
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pic: ImageView = itemView.findViewById(R.id.cart_pic)
        private val titleTxt: TextView = itemView.findViewById(R.id.cart_titleTxt)
        private val feeEachItem: TextView = itemView.findViewById(R.id.cart_feeEachItem)
        private val totalEachItem: TextView = itemView.findViewById(R.id.cart_totalEachItem)
        private val plusCartBtn: TextView = itemView.findViewById(R.id.plusCartBtn)
        private val minusCartBtn: TextView = itemView.findViewById(R.id.minusCartBtn)
        private val numberItemTxt: TextView = itemView.findViewById(R.id.cart_numberItemTxt)

        fun bind(item: CartItem) {
            titleTxt.text = item.productTitle
            feeEachItem.text = "$${item.feeEachItem}"
            totalEachItem.text = "$${item.totalPrice}"
            pic.setImageResource(R.drawable.shoes)
            numberItemTxt.text = item.quantity.toString()

            plusCartBtn.setOnClickListener {
                updateItemQuantity(adapterPosition, 1)
            }

            minusCartBtn.setOnClickListener {
                if (cartList[adapterPosition].quantity > 1) {
                    updateItemQuantity(adapterPosition, -1)
                }
            }
        }

        private fun updateItemQuantity(position: Int, delta: Int) {
            val item = cartList[position]
            item.quantity += delta
            numberItemTxt.text = item.quantity.toString()
            updateTotalPrice(position)
            (itemView.context as CartActivity).updateTotal()
        }

        private fun updateTotalPrice(position: Int) {
            val item = cartList[position]
            item.totalPrice = item.feeEachItem * item.quantity
            totalEachItem.text = "$${item.totalPrice}"
        }
    }
}


