package com.example.sapidigital.adapter

import android.app.AlertDialog
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
import com.example.sapidigital.edit_company.TAG
import com.example.sapidigital.models.PenyembelihanModel
import com.example.sapidigital.penyembelih.AddPenyembelihanActivity
import com.example.sapidigital.penyembelih.PenyembelihActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.okhttp.internal.Internal.instance


class PenyembelihanAdapter(var c: Context, private var myList: ArrayList<PenyembelihanModel>?) :
        RecyclerView.Adapter<PenyembelihanAdapter.RecyclerItemViewHolder>() {
    internal var mLastPosition = 0


    var db = FirebaseFirestore.getInstance()




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
        holder.ic_delete.setOnClickListener {
            Log.e("idK", ""+data.id);
            val mAlertDialog = AlertDialog.Builder(c)
            //mAlertDialog.setIcon(R.mipmap.ic_launcher_round) //set alertdialog icon
            mAlertDialog.setTitle("Hapus Data ?") //set alertdialog title
            mAlertDialog.setMessage("Yakin menghapus data anda?") //set alertdialog message
            mAlertDialog.setPositiveButton("Yes") { dialog, id ->
                //perform some tasks here
                //Toast.makeText(c, "Yes", Toast.LENGTH_SHORT).show()
                if (db != null) {
                    db.collection("penyembelihan").document(""+data.id)
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



