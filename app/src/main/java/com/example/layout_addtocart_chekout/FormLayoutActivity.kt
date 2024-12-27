//FormLayout Activity
package com.example.layout_addtocart_chekout

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FormLayoutActivity : AppCompatActivity() {
    private lateinit var edtFullName: EditText
    private lateinit var edtPhoneNumber: EditText
    private lateinit var edtAddress: EditText
    private lateinit var spinnerCity: Spinner
    private lateinit var radioGroupPayment: RadioGroup
    private lateinit var btnConfirm: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_layout)
        setControl()
        setEvent()
    }

    private fun setEvent() {
        // Nhận tổng tiền từ Intent
        val totalPrice = intent.getIntExtra("totalPrice", 0)

        btnConfirm.setOnClickListener {
            val fullName = edtFullName.text.toString().trim()
            val phoneNumber = edtPhoneNumber.text.toString().trim()
            val address = edtAddress.text.toString().trim()
            val selectedCity = spinnerCity.selectedItem.toString()
            val selectedPaymentMethodId = radioGroupPayment.checkedRadioButtonId

            // Kiểm tra thông tin hợp lệ
            if (fullName.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
                showToast("Vui lòng điền đầy đủ thông tin!")
                return@setOnClickListener
            }

            val paymentMethod = when (selectedPaymentMethodId) {
                R.id.rbCash -> "Tiền mặt"
                R.id.rbCard -> "Thẻ ngân hàng"
                R.id.rbPayPal -> "Paypal"
                else -> null
            }

            if (paymentMethod == null) {
                showToast("Vui lòng chọn phương thức thanh toán!")
                return@setOnClickListener
            }

            // Xóa giỏ hàng trước khi chuyển sang Checkout
            clearCart()

            // Chuyển sang CheckOutActivity và truyền dữ liệu
            val intent = Intent(this, CheckOutActivity::class.java)
            intent.putExtra("fullName", fullName)
            intent.putExtra("phoneNumber", phoneNumber)
            intent.putExtra("address", address)
            intent.putExtra("city", selectedCity)
            intent.putExtra("paymentMethod", paymentMethod)
            intent.putExtra("productName", "Giày thể thao") // Thêm thông tin sản phẩm mẫu
            intent.putExtra("productPrice", "500,000 VND")
            intent.putExtra("totalPrice", totalPrice)
            intent.putExtra("orderNumber", "24588164")
            startActivity(intent)
        }
    }


    private fun clearCart() {
        // Xóa dữ liệu giỏ hàng trong SharedPreferences
        val sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("cartList") // Xóa giá trị lưu trữ giỏ hàng
        editor.apply()

        // Hiển thị thông báo xác nhận
        //Toast.makeText(this, "Đã xóa tất cả sản phẩm trong giỏ hàng!", Toast.LENGTH_SHORT).show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun setControl() {
        edtFullName = findViewById(R.id.edtFullName)
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber)
        edtAddress = findViewById(R.id.edtAddress)
        spinnerCity = findViewById(R.id.spinnerCity)
        radioGroupPayment = findViewById(R.id.radioGroupPayment)
        btnConfirm = findViewById(R.id.btnConfirm)

        // Thiết lập dữ liệu cho Spinner
        val cities = listOf("Hà Nội", "Hồ Chí Minh", "Đà Nẵng", "Cần Thơ")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCity.adapter = adapter
    }
}

