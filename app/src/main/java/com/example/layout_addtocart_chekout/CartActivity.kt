//CartActivity
package com.example.layout_addtocart_chekout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import androidx.recyclerview.widget.ItemTouchHelper //thư viện xử lý sự kiện vuốt (swipe to delete)
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class CartActivity : AppCompatActivity() {

    private lateinit var btnBack: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvSubtotal: TextView
    private lateinit var tvFeeDelivery: TextView
    private lateinit var tvTotal: TextView
    private lateinit var tvTotalPrice: TextView
    private lateinit var buttonConfirm: Button
    private val cartList: MutableList<CartItem> = mutableListOf()

    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Khởi tạo các control
        setControl()

        // Nhận giỏ hàng từ Intent hoặc SharedPreferences
        loadCart()

        // Cập nhật RecyclerView và tổng tiền
        cartAdapter.notifyDataSetChanged()
        updateTotal()
    }

    private fun setControl() {
        // Tìm các control
        btnBack = findViewById(R.id.btnBack)
        recyclerView = findViewById(R.id.recyclerView)
        tvSubtotal = findViewById(R.id.tv_subtotal_price)
        tvFeeDelivery = findViewById(R.id.tv_fee_delivery_price)
        tvTotal = findViewById(R.id.tv_total)
        tvTotalPrice = findViewById(R.id.tv_total_price)
        buttonConfirm = findViewById(R.id.button_confirm)

        // Thiết lập RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(cartList, this)  // Truyền context vào
        recyclerView.adapter = cartAdapter

        // Áp dụng SwipeToDeleteCallback
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(cartAdapter, this))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        // Xử lý sự kiện quay lại
        btnBack.setOnClickListener { finish() }

        // Xử lý sự kiện xác nhận
        buttonConfirm.setOnClickListener {
            if (cartList.isEmpty()) {
                Toast.makeText(this, "Vui lòng thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show()
            } else {
                // Tính tổng tiền giỏ hàng
                val totalPrice = cartList.sumOf { it.totalPrice * it.quantity }

                // Chuyển sang FormLayoutActivity và truyền tổng tiền
                val intent = Intent(this, FormLayoutActivity::class.java)
                intent.putExtra("totalPrice", totalPrice)
                startActivity(intent)
            }
        }
    }

    private fun loadCart() {
        // Tải giỏ hàng từ Intent hoặc SharedPreferences
        val receivedCartList = intent.getParcelableArrayListExtra<CartItem>("cart") ?: ArrayList()
        cartList.clear()
        cartList.addAll(receivedCartList)
        loadCartFromPreferences()
    }

    private fun loadCartFromPreferences() {
        // Tải giỏ hàng từ SharedPreferences nếu có
        val sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE)
        val jsonCartList = sharedPreferences.getString("cartList", null)
        if (jsonCartList != null) {
            val type = object : TypeToken<MutableList<CartItem>>() {}.type
            val cartItems: MutableList<CartItem> = Gson().fromJson(jsonCartList, type)
            cartList.clear()
            cartList.addAll(cartItems)
        }
    }

    fun saveCartToPreferences() {
        // Lưu giỏ hàng vào SharedPreferences
        val sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val jsonCartList = Gson().toJson(cartList)
        editor.putString("cartList", jsonCartList)
        editor.apply()
    }

    fun updateTotal() {
        // Tính tổng tiền giỏ hàng
        var subtotal = 0.0
        val deliveryFee = 10.0  // Phí giao hàng
        val totalTax = 0.0      // Thuế

        for (item in cartList) {
            subtotal += item.totalPrice * item.quantity
        }

        val total = subtotal + deliveryFee + totalTax

        // Cập nhật hiển thị
        tvSubtotal.text = "$${String.format("%.2f", subtotal)}"
        tvFeeDelivery.text = "$${String.format("%.2f", deliveryFee)}"
        tvTotal.text = "$${String.format("%.2f", total)}"
        tvTotalPrice.text = "$${String.format("%.2f", total)}"
    }

    // Cập nhật khi xóa sản phẩm trong giỏ hàng
    fun updateCartAfterDeletion(position: Int) {
        cartList.removeAt(position)
        cartAdapter.notifyItemRemoved(position)
        updateTotal()
        saveCartToPreferences()
        Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show()
    }
}


