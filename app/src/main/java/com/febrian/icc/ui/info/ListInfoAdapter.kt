package com.febrian.icc.ui.info

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.febrian.icc.R
import com.febrian.icc.data.source.remote.response.info.ListInfoData
import com.febrian.icc.databinding.ItemListInfoBinding

class ListInfoAdapter(private val listData: ArrayList<ListInfoData>) :
    RecyclerView.Adapter<ListInfoAdapter.ViewHolder>() {


    class ViewHolder(private val binding: ItemListInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(m: ListInfoData) {

            binding.titleItem.text = m.key.toString()
            binding.description.text = m.description?.replace("\\n", "\n")
            Glide.with(itemView.context).load(m.image).into(binding.imageItem)

            itemView.setOnClickListener {

                val builder = AlertDialog.Builder(itemView.context)
                val layoutView = LayoutInflater.from(itemView.context)
                    .inflate(R.layout.alert_dialog_item_list_info, null)
                builder.setView(layoutView)

                val dialog = builder.create()
                dialog.show()
                dialog.setCancelable(false)
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                val title = layoutView.findViewById<TextView>(R.id.title)
                val description = layoutView.findViewById<TextView>(R.id.description)
                val image = layoutView.findViewById<ImageView>(R.id.image)
                Glide.with(itemView.context).load(m.image).into(image)
                title.text = m.key
                description.text = m.description?.replace("\\n", "\n")
                val btnClose = layoutView.findViewById<AppCompatButton>(R.id.btn_close)
                btnClose.setOnClickListener {
                    dialog.dismiss()
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = ItemListInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}