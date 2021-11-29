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
import com.example.sapidigital.lokasi.LokasiActivity
import com.example.sapidigital.models.FeedLotsModel
import com.example.sapidigital.penyembelih.PenyembelihActivity
import com.google.firebase.firestore.FirebaseFirestore

class FeedLotsAdapter(var c: Context, private var myList: ArrayList<FeedLotsModel>?) :
    RecyclerView.Adapter<FeedLotsAdapter.RecyclerItemViewHolder>() {
    internal var mLastPosition = 0

    var db = FirebaseFirestore.getInstance()

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
        holder.itemView.setOnClickListener {
            Log.e("shhshs ", data.tgl)
            val mIntent = Intent(c, AddFeedlotsActivity::class.java)
            mIntent.putExtra("no_ternak", data.no_ternak)
            mIntent.putExtra("umur", data.umur_sapi)
            mIntent.putExtra("riwayat", data.riwayat)
            mIntent.putExtra("bobot", data.bobot_terakhir)
            mIntent.putExtra("jenis", data.jenis_sapi)
            mIntent.putExtra("ket", data.ket)
            mIntent.putExtra("image", data.foto)
            mIntent.putExtra("status", "update")
            mIntent.putExtra("tgl", data.tgl)
            mIntent.putExtra("gender", data.gender)
            mIntent.putExtra("user", data.user)
            mIntent.putExtra("id", data.id)
            mIntent.putExtra("doc", data.doc)
            c.startActivity(mIntent)
        }
        holder.shapus.setOnClickListener {
            Log.e("das", ""+data.ids);


                val mAlertDialog = AlertDialog.Builder(c)
                //mAlertDialog.setIcon(R.mipmap.ic_launcher_round) //set alertdialog icon
                mAlertDialog.setTitle("Hapus Data ?") //set alertdialog title
                mAlertDialog.setMessage("Yakin menghapus data anda?") //set alertdialog message
                mAlertDialog.setPositiveButton("Yes") { dialog, id ->
                    //perform some tasks here
                    //Toast.makeText(c, "Yes", Toast.LENGTH_SHORT).show()
                    if (db != null) {
                        db.collection("feedlots").document(""+data.ids)
                                .delete()
                                .addOnSuccessListener {
                                    Log.d(edit_company.TAG, "DocumentSnapshot successfully deleted!")

                                    c.startActivity(Intent(c, FeedLotsAcitivity::class.java))

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
        var image: ImageView
        var name: TextView
        var shapus: Button

        init {
            image = parent.findViewById(R.id.iv_image) as ImageView
            name = parent.findViewById(R.id.tv_name) as TextView
            shapus = parent.findViewById(R.id.shapus) as Button
        }

    }



}

