package com.demo.todocrudapp.ui.todo_detail
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import com.demo.todocrudapp.R
import java.util.ArrayList


class SpinnerAdapter(context: Context,
                     @LayoutRes private val layoutResource: Int,
                     private val values: ArrayList<String>)
    : ArrayAdapter<String>(context, layoutResource, values) {

    override fun getItem(position: Int): String = values[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): TextView {
        val view = createViewFromResource(convertView, parent, layoutResource)
        return bindData(getItem(position), view as TextView)
    }


    private fun createViewFromResource(convertView: View?, parent: ViewGroup, layoutResource: Int): View {
        val context = parent.context
        val view = convertView ?: LayoutInflater.from(context).inflate(layoutResource, parent, false)
        return view
    }

    private fun bindData(value: String, view: TextView): TextView {
        view.text = value
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        if (position==0){
           label.setTextColor(getColor(context,R.color.black))
            label.setPadding(
                20,
                30,
                0,
                30
            )
        }
        else{
        label.setTextColor(Color.BLACK)
        label.setText(this.getItem(position))
        label.setPadding(
            20,
            30,
            0,
            30
        )}
        return label
    }
}