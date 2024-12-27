package com.example.layout_addtocart_chekout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(private var productList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // Khai báo interface để nhận sự kiện click item
    private var onItemClickListener: ((Product) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_recommended, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.titleTextView.text = product.title
        holder.priceTextView.text = "$${product.price}"  // Chuyển giá sang chuỗi với ký tự $
        holder.ratingTextView.text = product.rating.toString()

        // Lấy ID tài nguyên hình ảnh từ tên hình ảnh (picRes)
        val imageResId = holder.itemView.context.resources.getIdentifier(
            product.picRes, "drawable", holder.itemView.context.packageName
        )
        holder.productImageView.setImageResource(imageResId)

        // Xử lý sự kiện click vào item
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(product)
        }
    }

    override fun getItemCount(): Int = productList.size

    // Cập nhật dữ liệu cho Adapter
    fun updateData(newProductList: List<Product>) {
        productList = newProductList
        notifyDataSetChanged()  // Để RecyclerView cập nhật lại khi dữ liệu thay đổi
    }

    // Phương thức để thiết lập listener cho sự kiện click item
    fun setOnItemClickListener(listener: (Product) -> Unit) {
        onItemClickListener = listener
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTxt)
        val priceTextView: TextView = itemView.findViewById(R.id.priceText)
        val ratingTextView: TextView = itemView.findViewById(R.id.ratingTxt)
        val productImageView: ImageView = itemView.findViewById(R.id.pic)
    }
}

