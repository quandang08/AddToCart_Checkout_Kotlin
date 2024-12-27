//Main Activit
package com.example.layout_addtocart_chekout

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private var productList: List<Product> = mutableListOf()  // Khởi tạo productList là một danh sách trống
    private val cartList: MutableList<CartItem> = mutableListOf() // Danh sách giỏ hàng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setControl()
        setEvent()

        // Tải dữ liệu từ JSON vào RecyclerView
        loadDataFromJSON()
    }

    private fun setControl() {
        // Khởi tạo RecyclerView và Adapter
        recyclerView = findViewById(R.id.viewPopular)
        recyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductAdapter(productList)
        recyclerView.adapter = productAdapter
    }

    private fun setEvent() {
        // Thiết lập sự kiện click vào item trong RecyclerView
        productAdapter.setOnItemClickListener { product ->
            // Thêm sản phẩm vào giỏ hàng
            addToCart(product)
        }

        //Chuyển trang cart
        val iconShop: LinearLayout = findViewById(R.id.cart)
        iconShop.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            // Truyền giỏ hàng sang CartActivity
            intent.putParcelableArrayListExtra("cart", ArrayList(cartList)) // Truyền giỏ hàng
            startActivity(intent)
        }
    }

    private fun loadDataFromJSON() {
        try {
            // Đọc tệp JSON từ assets
            val inputStream = assets.open("product.json")
            val reader = InputStreamReader(inputStream)

            // Parse JSON thành đối tượng ProductList
            val loadedProductList = Gson().fromJson(reader, ProductList::class.java)

            // Cập nhật dữ liệu cho adapter
            productList = loadedProductList.Items  // Sửa lại cho phù hợp
            productAdapter.updateData(productList)  // Cập nhật lại dữ liệu cho adapter
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addToCart(product: Product) {
        // Kiểm tra xem sản phẩm đã có trong giỏ chưa
        val existingItem = cartList.find { it.productTitle == product.title }
        if (existingItem != null) {
            // Nếu đã có sản phẩm trong giỏ hàng, tăng số lượng lên 1
            existingItem.quantity++
            existingItem.totalPrice = existingItem.quantity * existingItem.feeEachItem
        } else {
            // Nếu chưa có, thêm sản phẩm mới vào giỏ
            val cartItem = CartItem(
                product.title,
                product.price,
                product.price,
                1
            )
            cartList.add(cartItem) // Thêm vào giỏ hàng
        }

        // Lưu giỏ hàng vào SharedPreferences sau khi thay đổi
        saveCartToPreferences()

        // Hiển thị thông báo thêm thành công
        Toast.makeText(this, "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show()
    }

    private fun saveCartToPreferences() {
        val sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val jsonCartList = Gson().toJson(cartList)
        editor.putString("cartList", jsonCartList)   // Lưu chuỗi JSON vào SharedPreferences
        editor.apply()
    }
}
