package kr.cosine.groupfinder.presentation.view.write.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.enums.Mode

class GameModeSpinnerAdapter(
    context: Context,
    private val items: Array<Mode>
) : ArrayAdapter<Mode>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_gamemodespinner, parent, false)

        val textView = view.findViewById<TextView>(R.id.gameModeTextView)
        textView.text = items[position].displayName

        return view
    }
}