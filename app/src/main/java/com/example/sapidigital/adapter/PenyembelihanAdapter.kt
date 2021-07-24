package com.example.sapidigital.adapter

import android.content.Context
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.sapidigital.R
import com.example.sapidigital.models.PenyembelihanModel


class PenyembelihanAdapter(var c: Context, private var myList: ArrayList<PenyembelihanModel>?) :
        RecyclerView.Adapter<PenyembelihanAdapter.RecyclerItemViewHolder>() {
    internal var mLastPosition = 0

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): PenyembelihanAdapter.RecyclerItemViewHolder {
        val view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_penyembelihan,
                parent,
                false
        )
        return RecyclerItemViewHolder(view)
    }
    //set view
    override fun onBindViewHolder(holder: PenyembelihanAdapter.RecyclerItemViewHolder, position: Int) {
        var data = myList!![position];
        holder.warna_daging.setText(data.warna_daging)
        holder.warna_lemak.setText(data.warna_lemak)
        holder.marbling.setText(data.marbling)
        holder.tv_name_penyembelih.setText(data.name)
        holder.tv_tgl_penyembelih.setText(data.tgl)
        holder.tv_berat_daging.setText(data.berat_daging)
        holder.btn_vidio.setOnClickListener {
            Log.e("das", "asdhk");
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(data.vidio))
            holder.itemView.context.startActivity(browserIntent)


        }
        mLastPosition = position

    }

    private fun startActivity(browserIntent: Intent) {


    }

    override fun getItemCount(): Int {
        return if (null != myList) myList!!.size else 0
    }

    inner class RecyclerItemViewHolder(parent: View) : RecyclerView.ViewHolder(parent) {
        var tv_tgl_penyembelih: TextView
        var tv_name_penyembelih: TextView
        var tv_berat_daging: TextView
        var warna_daging: TextView
        var warna_lemak: TextView
        var marbling: TextView
        var btn_vidio: Button
        var ic_delete: ImageView



        init {
            ic_delete = parent.findViewById(R.id.ic_delete) as ImageView
            warna_daging = parent.findViewById(R.id.warna_daging) as TextView
            warna_lemak = parent.findViewById(R.id.warna_lemak) as TextView
            marbling = parent.findViewById(R.id.marbling) as TextView
            tv_tgl_penyembelih = parent.findViewById(R.id.tv_tgl_penyembelih) as TextView
            tv_name_penyembelih = parent.findViewById(R.id.tv_name_penyembelih) as TextView
            tv_berat_daging = parent.findViewById(R.id.tv_berat_daging) as TextView
            btn_vidio = parent.findViewById(R.id.btn_vidio) as Button




        }


    }
}

