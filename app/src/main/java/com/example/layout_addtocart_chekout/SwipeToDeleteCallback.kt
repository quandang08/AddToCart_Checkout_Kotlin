//SwipeToDeleteCallback

package com.example.layout_addtocart_chekout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeToDeleteCallback(
    private val adapter: CartAdapter, // Adapter để gọi hàm xóa phần tử khi vuốt
    context: Context
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {  // Cho phép vuốt trái và phải

    private val deleteIcon: Drawable? = ContextCompat.getDrawable(context, R.drawable.ic_garbage)
    private val backgroundColor = ColorDrawable(Color.RED)
    private val iconMargin = 20

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    // Gọi khi item bị vuốt (thực hiện hành động xóa)
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        adapter.deleteItem(position)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val itemView = viewHolder.itemView
        val iconSize = deleteIcon?.intrinsicHeight ?: 0

        // Vẽ màu nền đỏ (kéo dài theo hướng vuốt)
        backgroundColor.setBounds(
            if (dX > 0) itemView.left else (itemView.right + dX).toInt(),
            itemView.top,
            if (dX > 0) (itemView.left + dX).toInt() else itemView.right,
            itemView.bottom
        )
        backgroundColor.draw(c) // Vẽ màu nền lên canvas

        // Vẽ icon xóa
        deleteIcon?.let {
            val iconTop = itemView.top + (itemView.height - iconSize) / 2
            val iconLeft = if (dX > 0) itemView.left + iconMargin else itemView.right - iconMargin - iconSize
            val iconRight = iconLeft + iconSize
            val iconBottom = iconTop + iconSize

            it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            it.draw(c)
        }

        // Gọi hàm gốc để xử lý phần vuốt mặc định
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}


