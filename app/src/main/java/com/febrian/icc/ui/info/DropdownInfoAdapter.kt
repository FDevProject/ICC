package com.febrian.icc.ui.info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.febrian.icc.R
import com.febrian.icc.data.source.remote.response.info.DropdownInfoData
import com.febrian.icc.databinding.ItemDropdownInfoBinding

class DropdownInfoAdapter(private val listData: ArrayList<DropdownInfoData>) :
    RecyclerView.Adapter<DropdownInfoAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemDropdownInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(m: DropdownInfoData) {
            binding.titleItem.text = m.key.toString()

            binding.descriptionItem.text = m.value?.replace("\\n", "\n")

            binding.cardItem.setOnClickListener {
                action(binding)
            }

            binding.icon.setOnClickListener {
                action(binding)
            }
        }

        private fun action(binding: ItemDropdownInfoBinding) {
            if (binding.detailExpand.visibility == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(
                    binding.cardItem,
                    AutoTransition()
                )
                binding.icon.setBackgroundResource(R.drawable.ic_baseline_add_circle_outline_24)
                binding.detailExpand.visibility = View.GONE

            } else {
                TransitionManager.beginDelayedTransition(
                    binding.cardItem,
                    AutoTransition()
                )
                binding.icon.setBackgroundResource(R.drawable.ic_baseline_remove_circle_outline_24)
                binding.detailExpand.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DropdownInfoAdapter.ViewHolder {
        val view =
            ItemDropdownInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DropdownInfoAdapter.ViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}