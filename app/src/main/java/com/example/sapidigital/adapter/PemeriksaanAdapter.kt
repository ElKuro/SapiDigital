package com.example.sapidigital.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sapidigital.AddFeedlotsActivity
import com.example.sapidigital.FeedLotsAcitivity
import com.example.sapidigital.R
import com.example.sapidigital.edit_company
import com.example.sapidigital.edit_company.TAG
import com.example.sapidigital.models.PemeriksaanModel
import com.example.sapidigital.pemeriksa.PemeriksaanActivity
import com.example.sapidigital.penyembelih.PenyembelihActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FieldValue.arrayRemove
import com.google.firebase.firestore.FirebaseFirestore





class PemeriksaanAdapter(var c: Context, private var myList: ArrayList<PemeriksaanModel>?) :
        RecyclerView.Adapter<PemeriksaanAdapter.RecyclerItemViewHolder>() {
    internal var mLastPosition = 0

    var db = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): PemeriksaanAdapter.RecyclerItemViewHolder {
        val view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_pemeriksaan,
                parent,
                false
        )
        return RecyclerItemViewHolder(view)


    }
    //set view
    override fun onBindViewHolder(holder: PemeriksaanAdapter.RecyclerItemViewHolder, position: Int) {
        var data = myList!![position];
        holder.tv_berat_sapi.setText(data.berat_sapi)
        holder.tv_hasil_pemeriksaan.setText(data.hasil_pemeriksaan)
        holder.tv_ket.setText(data.ket)
        holder.tv_name.setText(data.name)
        holder.tv_tgl.setText(data.tgl)
        holder.btn_sp.setOnClickListener {
            Log.e("das", "asdhk");
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(data.surat_pemeriksaan))
            holder.itemView.context.startActivity(browserIntent)
        }
        holder.btn_dlp.setOnClickListener {
            Log.e("delete", ""+data.id);

            val mAlertDialog = AlertDialog.Builder(c)
            //mAlertDialog.setIcon(R.mipmap.ic_launcher_round) //set alertdialog icon
            mAlertDialog.setTitle("Hapus Data ?") //set alertdialog title
            mAlertDialog.setMessage("Yakin menghapus data anda?") //set alertdialog message
            mAlertDialog.setPositiveButton("Yes") { dialog, id ->
                //perform some tasks here
                //Toast.makeText(c, "Yes", Toast.LENGTH_SHORT).show()
                if (db != null) {
                    db.collection("pemeriksaan").document(""+data.id)
                            .delete()
                            .addOnSuccessListener {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!")

                                c.startActivity(Intent(c, PenyembelihActivity::class.java))

                            }
                            .addOnFailureListener {
                                e -> Log.w(TAG, "Error deleting document", e)
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
        var tv_berat_sapi: TextView
        var iv_pemeriksaan: ImageView
        var tv_ket: TextView
        var tv_hasil_pemeriksaan: TextView
        var tv_tgl: TextView
        var tv_name: TextView
        var btn_sp: Button
        var btn_dlp: ImageView

        init {
            tv_berat_sapi = parent.findViewById(R.id.tv_berat_sapi) as TextView
            tv_tgl = parent.findViewById(R.id.tv_tgl) as TextView
            tv_ket = parent.findViewById(R.id.tv_ket) as TextView
            tv_hasil_pemeriksaan = parent.findViewById(R.id.tv_hasil_pemeriksaan) as TextView
            tv_name = parent.findViewById(R.id.tv_name) as TextView
            iv_pemeriksaan = parent.findViewById(R.id.iv_pemeriksaan) as ImageView
            btn_sp = parent.findViewById(R.id.btn_sp) as Button
            btn_dlp = parent.findViewById(R.id.btn_dlp) as ImageView
        }

    }



}





