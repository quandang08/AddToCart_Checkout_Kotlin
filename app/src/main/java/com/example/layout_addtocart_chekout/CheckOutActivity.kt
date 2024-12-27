package com.example.layout_addtocart_chekout

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class CheckOutActivity : AppCompatActivity() {
    private var paymentStatusTextView: TextView? = null
    private var orderNumberTextView: TextView? = null
    private var itemDetailsTextView: TextView? = null
    private var orderNumberLabelTextView: TextView? = null
    private var itemDetailsLabelTextView: TextView? = null
    private var estimatedTimeLabelTextView: TextView? = null
    private var continueButton: AppCompatButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)

        // Ánh xạ các View từ layout
        setControl()

        // Thiết lập nội dung cho các TextView từ Intent
        setEvent()
    }

    private fun setControl() {
        paymentStatusTextView = findViewById(R.id.paymentStatusTextView)
        orderNumberTextView = findViewById(R.id.orderNumberTextView)
        itemDetailsTextView = findViewById(R.id.itemDetailsTextView)
        orderNumberLabelTextView = findViewById(R.id.orderNumberLabelTextView)
        itemDetailsLabelTextView = findViewById(R.id.itemDetailsLabelTextView)
        estimatedTimeLabelTextView = findViewById(R.id.estimatedTimeLabelTextView)
        continueButton = findViewById(R.id.continueButton)
    }

    private fun setEvent() {
        // Lấy dữ liệu từ Intent
        val intent = intent
        val orderNumber = intent.getStringExtra("orderNumber")
        val paymentStatus = intent.getStringExtra("paymentStatus")

        // Hiển thị dữ liệu lên UI
        paymentStatusTextView!!.text = paymentStatus ?: "Payment Successful!"
        orderNumberTextView!!.text = orderNumber ?: "N/A"

        // Sự kiện nút "Continue buy item"
        continueButton!!.setOnClickListener { view: View? ->
            // Quay lại MainActivity và xóa tất cả Activity phía trước
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Đảm bảo xóa tất cả các Activity phía trước
            startActivity(intent)
            finish()
        }

        // Sự kiện khi nhấn vào "details my"
        itemDetailsTextView!!.setOnClickListener { view: View? ->
            // Lấy thêm dữ liệu từ Intent
            // Lấy thêm dữ liệu từ Intent
            val customerName = intent.getStringExtra("fullName")
            val phoneNumber = intent.getStringExtra("phoneNumber")
            val address = intent.getStringExtra("address")
            val productName = intent.getStringExtra("productName")
            val productPrice = intent.getStringExtra("productPrice")
            val totalPrice = intent.getStringExtra("totalPrice") ?: "Chưa có thông tin"  // Nếu null, sử dụng giá trị mặc định
            val paymentMethod = intent.getStringExtra("paymentMethod")

            // Chuyển dữ liệu sang BillDetailActivity
            val billIntent = Intent(this, BillDetailActivity::class.java)
            billIntent.putExtra("fullName", customerName)
            billIntent.putExtra("phoneNumber", phoneNumber)
            billIntent.putExtra("address", address)
            billIntent.putExtra("productName", productName)
            billIntent.putExtra("productPrice", productPrice)
            billIntent.putExtra("totalPrice", totalPrice)
            billIntent.putExtra("paymentMethod", paymentMethod)
            startActivity(billIntent)

        }
    }
}
