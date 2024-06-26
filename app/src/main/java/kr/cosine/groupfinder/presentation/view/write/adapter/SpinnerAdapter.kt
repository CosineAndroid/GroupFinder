package kr.cosine.groupfinder.presentation.view.write.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.presentation.view.write.SpinnerModel

class SpinnerAdapter(
    private val context: Context,
    private val items: List<SpinnerModel>
) : ArrayAdapter<SpinnerModel>(context, 0, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_lane_spinner, parent, false)
        val imageView = view.findViewById<ImageView>(R.id.myLaneImageView)
        val textView = view.findViewById<TextView>(R.id.myLaneTextview)
        val item = items[position]
        imageView.setImageResource(item.image)
        textView.text = item.lane
        return view
    }
}