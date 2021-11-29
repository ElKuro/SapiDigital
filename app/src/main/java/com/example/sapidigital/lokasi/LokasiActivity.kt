package com.example.sapidigital.lokasi

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sapidigital.R
import com.example.sapidigital.adapter.LokasiAdapter
import com.example.sapidigital.models.LokasiModel
import com.example.sapidigital.utils.Preferences
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class LokasiActivity : AppCompatActivity() {
    private lateinit var flList: ArrayList<LokasiModel>
    private lateinit var mAdapter: LokasiAdapter
    var db = FirebaseFirestore.getInstance()
    var id_fl = "";
    var flCollection = db.collection("lokasi")
    var btn_add_lokasi: Button? = null
    var recyclerviewlokasi: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lokasi_acitivity)
        setTitle("Pemeliharaan")


        btn_add_lokasi = findViewById(R.id.btn_add_lokasi)
        recyclerviewlokasi = findViewById(R.id.recyclerviewlokasi)
        val role = Preferences.getRoleLogin(this@LokasiActivity)

        val iin = intent
        val b = iin.extras

        if (b != null) {
            val j = b["id_fl"] as String?
            id_fl = j.toString();
        }

        inits()

        btn_add_lokasi?.setOnClickListener {
            if(!id_fl.equals("")){
                val ii = Intent(applicationContext, AddLokasiActivity::class.java)
                ii.putExtra("id_fl", id_fl)
                startActivity(ii)
            }
        }
    }

    private fun inits() {
        flList = ArrayList()
        mAdapter = LokasiAdapter(this, flList)
        recyclerviewlokasi?.layoutManager = LinearLayoutManager(this)
        var i = 0;

        flCollection.whereEqualTo("id_fl", id_fl).get().addOnCompleteListener { task: Task<QuerySnapshot> ->
            flList.clear()
            if (task.isSuccessful) {
                val document = task.result
                if (!document!!.isEmpty) {
                    for (docs in task.result!!) {
                        //val fl = docs.toObject(LokasiModel::class.java)
                             val data = LokasiModel();
                        data.berat_sapi = docs.toObject(LokasiModel::class.java).berat_sapi;
                        data.id_fl = docs.toObject(LokasiModel::class.java).id_fl;
                        data.tempat = docs.toObject(LokasiModel::class.java).tempat;
                        data.tgl = docs.toObject(LokasiModel::class.java).tgl;
                        data.id = document.documents[i].id;
                        flList.add(data)
                        i++;
                    }
                    mAdapter = LokasiAdapter(this, flList)
                    recyclerviewlokasi?.adapter = mAdapter

                    if (flList.size < 1) {
                        Toast.makeText(this, "tidak ada data", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    println("No such document")
                }
            } else {
                println(task.exception)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_more -> inits()
        }
        return super.onOptionsItemSelected(item)

    }
}



