package kr.cosine.groupfinder.presentation.view.write.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kr.cosine.groupfinder.databinding.ItemSelectlaneBinding
import kr.cosine.groupfinder.presentation.view.write.LaneSpinnerItem
import kr.cosine.groupfinder.presentation.view.write.WriteActivity

class RequireLaneRecyclerViewAdapter(
    private val lanes: MutableList<String>,
) : RecyclerView.Adapter<RequireLaneRecyclerViewAdapter.ViewHolder>() {
    private val selectedRequireLanes = mutableMapOf<Int, String>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSelectlaneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val laneRemoveIcon = lanes.size <= 1
        holder.bind(holder.itemView.context, laneRemoveIcon)
    }

    override fun getItemCount(): Int {
        return lanes.size
    }

    inner class ViewHolder(
        private val binding: ItemSelectlaneBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.removeImageView.setOnClickListener {
                (binding.root.context as WriteActivity).requireLaneRecyclerViewAdapter.removeLane(
                    bindingAdapterPosition
                )
                Log.d("checkSelectedLansesListRemove", selectedRequireLanes.toString())
            }
        }

        fun bind(context: Context, laneRemoveIcon: Boolean) {
            val spinner = binding.requireLaneSpinner
            val adapter = RequireLaneSpinnerAdapter(context, LaneSpinnerItem.laneItems)
            spinner.adapter = adapter
            binding.tagbackgroundCardView.visibility =
                if (laneRemoveIcon) View.INVISIBLE else View.VISIBLE
            setOnSelectedLaneClickListener(spinner, context)
        }

        private fun setOnSelectedLaneClickListener(spinner: Spinner, context: Context) {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedLane = LaneSpinnerItem.laneItems[position].lane
                    val defaultLane = LaneSpinnerItem.laneItems[0].lane

                    if (selectedLane != defaultLane) {
                        selectedRequireLanes[bindingAdapterPosition] = selectedLane

                    } else {
                        selectedRequireLanes.remove(bindingAdapterPosition)
                    }
                    Log.d("checkSelected", selectedLane)
                    Log.d("checkSelectedLansesList", selectedRequireLanes.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }

    }

    fun removeLane(position: Int) {
        if (lanes.size > 1 && position < lanes.size) {
            lanes.removeAt(position)
            selectedRequireLanes.remove(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, lanes.size)
        }
    }


    //데이터 구조를 다시 잡아야 할듯?
    //보여주고 중복된 값이라고 알려주는게 낫다  SnackBar? Toast?
    fun addLane(newLane: String) {
        lanes.add(newLane)
        notifyItemInserted(lanes.size - 1)
    }


}
