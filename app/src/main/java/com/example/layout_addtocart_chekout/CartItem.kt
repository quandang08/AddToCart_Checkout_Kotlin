package com.example.layout_addtocart_chekout

import android.os.Parcel
import android.os.Parcelable

data class CartItem(
    val productTitle: String,  // Tên sản phẩm
    val feeEachItem: Double,   // Giá mỗi sản phẩm
    var totalPrice: Double,    // Giá tổng của sản phẩm trong giỏ
    var quantity: Int          // Số lượng sản phẩm trong giỏ
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productTitle)
        parcel.writeDouble(feeEachItem)
        parcel.writeDouble(totalPrice)
        parcel.writeInt(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartItem> {
        override fun createFromParcel(parcel: Parcel): CartItem {
            return CartItem(parcel)
        }

        override fun newArray(size: Int): Array<CartItem?> {
            return arrayOfNulls(size)
        }
    }
}

