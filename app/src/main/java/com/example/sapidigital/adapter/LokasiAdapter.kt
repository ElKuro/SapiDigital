package com.example.sapidigital.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sapidigital.R
import com.example.sapidigital.models.LokasiModel

class LokasiAdapter(var c: Context, private var myList: ArrayList<LokasiModel>?) :
    RecyclerView.Adapter<LokasiAdapter.RecyclerItemViewHolder>() {
    internal var mLastPosition = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LokasiAdapter.RecyclerItemViewHolder {
        val view = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.item_lokasi,
            parent,
            false
        )
        return RecyclerItemViewHolder(view)
    }
    //set view
    override fun onBindViewHolder(holder: LokasiAdapter.RecyclerItemViewHolder, position: Int) {
        var data = myList!![position];
        holder.tgl.setText(data.tgl)
        holder.tmpt.setText(data.tempat)
        mLastPosition = position
    }

    override fun getItemCount(): Int {
        return if (null != myList) myList!!.size else 0
    }

    inner class RecyclerItemViewHolder(parent: View) : RecyclerView.ViewHolder(parent) {
        var tgl: TextView
        var tmpt: TextView

        init {
            tgl = parent.findViewById(R.id.tv_tgl_lokasi) as TextView
            tmpt = parent.findViewById(R.id.tv_tempat_lokasi) as TextView
        }

    }



}

