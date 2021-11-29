package com.example.sapidigital.adapter

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.sapidigital.R
import com.example.sapidigital.edit_company
import com.example.sapidigital.lokasi.LokasiActivity
import com.example.sapidigital.models.LokasiModel
import com.example.sapidigital.penyembelih.PenyembelihActivity
import com.google.firebase.firestore.FirebaseFirestore

class LokasiAdapter(var c: Context, private var myList: ArrayList<LokasiModel>?) :
    RecyclerView.Adapter<LokasiAdapter.RecyclerItemViewHolder>() {
    internal var mLastPosition = 0

    var db = FirebaseFirestore.getInstance()

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
        holder.berat_sapi.setText(data.berat_sapi)
        holder.tmpt.setText(data.tempat)

        holder.lohapus.setOnClickListener {
            Log.e("id k", ""+data.id);
            val mAlertDialog = AlertDialog.Builder(c)
            //mAlertDialog.setIcon(R.mipmap.ic_launcher_round) //set alertdialog icon
            mAlertDialog.setTitle("Hapus Data ?") //set alertdialog title
            mAlertDialog.setMessage("Yakin menghapus data anda?") //set alertdialog message
            mAlertDialog.setPositiveButton("Yes") { dialog, id ->
                //perform some tasks here
                //Toast.makeText(c, "Yes", Toast.LENGTH_SHORT).show()
                if (db != null) {
                    db.collection("lokasi").document(""+data.id)
                            .delete()
                            .addOnSuccessListener {
                                Log.d(edit_company.TAG, "DocumentSnapshot successfully deleted!")

                                c.startActivity(Intent(c, LokasiActivity::class.java))

                            }
                            .addOnFailureListener {
                                e -> Log.w(edit_company.TAG, "Error deleting document", e)
                                // startActivity(Intent(context, PenyembelihActivity::class.java))

                            }
                }
            }
            mAlertDialog.setNegativeButton("No") { dialog, id ->
                //perform som tasks here
                //Toast.makeText(c, "No", Toast.LENGTH_SHORT).show()
            }
            mAlertDialog.show()
        }

        mLastPosition = position
    }



    override fun getItemCount(): Int {
        return if (null != myList) myList!!.size else 0
    }

    inner class RecyclerItemViewHolder(parent: View) : RecyclerView.ViewHolder(parent) {
        var tgl: TextView
        var tmpt: TextView
        var berat_sapi: TextView
        var lview: CardView
        var lohapus: Button

        init {
            berat_sapi = parent.findViewById(R.id.berat_sapi) as TextView
            tgl = parent.findViewById(R.id.tv_tgl_lokasi) as TextView
            tmpt = parent.findViewById(R.id.tv_tempat_lokasi) as TextView
            lview = parent.findViewById(R.id.lview) as CardView
            lohapus = parent.findViewById(R.id.lohapus) as Button
        }

    }



}

