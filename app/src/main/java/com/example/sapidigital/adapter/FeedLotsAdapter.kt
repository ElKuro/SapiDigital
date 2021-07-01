package com.example.sapidigital.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sapidigital.R
import com.example.sapidigital.models.FeedLotsModel

class FeedLotsAdapter(var c: Context, private var myList: ArrayList<FeedLotsModel>?) :
    RecyclerView.Adapter<FeedLotsAdapter.RecyclerItemViewHolder>() {
    internal var mLastPosition = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeedLotsAdapter.RecyclerItemViewHolder {
        val view = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.item_feedlots,
            parent,
            false
        )
        return RecyclerItemViewHolder(view)
    }
    //set view
    override fun onBindViewHolder(holder: FeedLotsAdapter.RecyclerItemViewHolder, position: Int) {
        var data = myList!![position];
        holder.name.setText(data.jenis_sapi)
        Glide.with(holder.itemView.context)
                .load(data.foto)
                .error(R.drawable.button_pilihan)
                .placeholder(R.drawable.button_pilihan)
                .error(R.drawable.button_pilihan)
                .into(holder.image)
        holder.detail.setOnClickListener {
//            val mIntent = Intent(c, TampilanAkhir::class.java)
//
//            //data yang dikirim ke activity TampilanAkhir
//            mIntent.putExtra("name", data.name)
//            mIntent.putExtra("address", data.alamat)
//            mIntent.putExtra("telp", data.telp)
//            mIntent.putExtra("img", data.image)
//            mIntent.putExtra("asd", data.tes)
//            mIntent.putExtra("id", data.id.toString())
//            c.startActivity(mIntent)
        }
        mLastPosition = position
    }

    override fun getItemCount(): Int {
        return if (null != myList) myList!!.size else 0
    }

    inner class RecyclerItemViewHolder(parent: View) : RecyclerView.ViewHolder(parent) {
        var detail: Button
        var image: ImageView
        var name: TextView

        init {
            detail = parent.findViewById(R.id.btn_detail) as Button
            image = parent.findViewById(R.id.iv_image) as ImageView
            name = parent.findViewById(R.id.tv_name) as TextView
        }

    }



}
