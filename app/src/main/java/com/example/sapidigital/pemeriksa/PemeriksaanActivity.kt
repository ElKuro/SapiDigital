package com.example.sapidigital.pemeriksa

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sapidigital.R
import com.example.sapidigital.adapter.PemeriksaanAdapter
import com.example.sapidigital.models.PemeriksaanModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_pemeriksaan.*


class PemeriksaanActivity : AppCompatActivity() {
    private lateinit var flList: ArrayList<PemeriksaanModel>
    private lateinit var mAdapter: PemeriksaanAdapter
    var db = FirebaseFirestore.getInstance()
    var id_fl = "";
    var flCollection = db.collection("pemeriksaan")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pemeriksaan)
        setTitle("Pemeriksaan")

        val iin = intent
        val b = iin.extras

        if (b != null) {
            val j = b["id_fl"] as String?
            id_fl = j.toString();
        }

        inits()

        btn_add_pemeriksaan.setOnClickListener {
            val ii = Intent(applicationContext, AddPemeriksaanActivity::class.java)
            ii.putExtra("id_fl", id_fl)
            startActivity(ii)
        }
    }

    private fun inits() {
        flList = ArrayList()
        mAdapter = PemeriksaanAdapter(this, flList)
        rc_pemeriksa.layoutManager = LinearLayoutManager(this)

        flCollection.whereEqualTo("fl_id", id_fl).get().addOnCompleteListener { task: Task<QuerySnapshot> ->
            flList.clear()
            if (task.isSuccessful) {
                val document = task.result
                if (!document!!.isEmpty) {
                    for (docs in task.result!!) {
                        val fl = docs.toObject(PemeriksaanModel::class.java)
                        flList.add(fl)
                    }
                    mAdapter = PemeriksaanAdapter(this, flList)
                    rc_pemeriksa.adapter = mAdapter

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



