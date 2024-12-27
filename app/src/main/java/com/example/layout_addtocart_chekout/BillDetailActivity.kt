package com.example.layout_addtocart_chekout

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BillDetailActivity : AppCompatActivity() {
    private lateinit var tvCustomerName: TextView
    private lateinit var tvPhoneNumber: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvProductName1: TextView
    private lateinit var tvProductPrice1: TextView
    private lateinit var ivProduct1: ImageView
    private lateinit var tvTotalPrice: TextView
    private lateinit var tvPaymentMethod: TextView
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill_detail)

        // Ánh xạ các View từ layout
        setControl()

        // Hiển thị thông tin chi tiết hóa đơn
        displayBillDetails()

        // Thiết lập sự kiện quay lại
        setEvent()
    }

    private fun setControl() {
        tvCustomerName = findViewById(R.id.tvCustomerName)
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber)
        tvAddress = findViewById(R.id.tvAddress)
        tvProductName1 = findViewById(R.id.tvProductName1)
        tvProductPrice1 = findViewById(R.id.tvProductPrice1)
        ivProduct1 = findViewById(R.id.ivProduct1)
        tvTotalPrice = findViewById(R.id.tvTotalPrice)
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod)
        btnBack = findViewById(R.id.btnBack)
    }

    private fun displayBillDetails() {
        val intent = intent
        val customerName = intent.getStringExtra("fullName")
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val address = intent.getStringExtra("address")
        val productName = intent.getStringExtra("productName")
        val productPrice = intent.getStringExtra("productPrice")
        val totalPrice = intent.getStringExtra("totalPrice") ?: "Không có thông tin"  // Giá trị mặc định nếu totalPrice null
        Log.d("BillDetailActivity", "Received Total Price: $totalPrice")
        val paymentMethod = intent.getStringExtra("paymentMethod")

        tvCustomerName.text = "Tên khách hàng: $customerName"
        tvPhoneNumber.text = "Số điện thoại: $phoneNumber"
        tvAddress.text = "Địa chỉ: $address"
        tvProductName1.text = "Sản phẩm: $productName"
        tvProductPrice1.text = "Giá: $productPrice"
        tvTotalPrice.text = "Tổng tiền: $totalPrice"  // Hiển thị totalPrice
        tvPaymentMethod.text = "Phương thức thanh toán: $paymentMethod"

        // Thay thế bằng ảnh sản phẩm thực tế
        ivProduct1.setImageResource(R.drawable.shoes1)
    }


    private fun setEvent() {
        btnBack.setOnClickListener {
            finish()
        }
    }
}
