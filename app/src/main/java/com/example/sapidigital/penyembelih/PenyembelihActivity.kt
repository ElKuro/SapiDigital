package com.example.sapidigital.penyembelih

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sapidigital.AddFeedlotsActivity
import com.example.sapidigital.MainActivity
import com.example.sapidigital.R
import com.example.sapidigital.adapter.PenyembelihanAdapter
import com.example.sapidigital.models.PemeriksaanModel
import com.example.sapidigital.models.PenyembelihanModel
import com.example.sapidigital.utils.Preferences
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class PenyembelihActivity : AppCompatActivity() {

    private lateinit var flList: ArrayList<PenyembelihanModel>
    private lateinit var mAdapter: PenyembelihanAdapter
    var db = FirebaseFirestore.getInstance()
    var id_fl= "";
    var flCollection = db.collection("penyembelihan")
    var btn_add_penyembelihan: Button? = null
    var rc_penyembelih: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penyembelih)
        setTitle("Penyembelihan")
        btn_add_penyembelihan = findViewById(R.id.btn_add_penyembelihan)
        rc_penyembelih = findViewById(R.id.rc_penyembelih)

        val role = Preferences.getRoleLogin(this@PenyembelihActivity)
        if(role == "PENYEMBELIH"){
            btn_add_penyembelihan?.visibility = View.VISIBLE
        }else{
            btn_add_penyembelihan?.visibility = View.GONE
        }

        val iin = intent
        val b = iin.extras

        if(b != null){
            val j = b["id_fl"] as String?
            id_fl = j.toString();
        }

        inits()
        btn_add_penyembelihan?.setOnClickListener {
            val ii = Intent(applicationContext, AddPenyembelihanActivity::class.java)
            ii.putExtra("id_fl", id_fl)
            startActivity(ii)
        }
    }

    private fun inits() {
        flList = ArrayList()
        mAdapter = PenyembelihanAdapter(this, flList)
        rc_penyembelih?.layoutManager = LinearLayoutManager(this)
        var i = 0;


        flCollection.whereEqualTo("fl_id",id_fl).get().addOnCompleteListener { task: Task<QuerySnapshot> ->
            flList.clear()
            if (task.isSuccessful) {
                val document = task.result

                if (!document!!.isEmpty) {
                    for (docs in task.result!!) {

                        //Log.e("id e - ",""+document.documents[i].id);
                       // Log.e("id e - ",""+docs["name"]);
                        //Log.e("all - ",""+docs.toObject(PenyembelihanModel::class.java));
                        //val fl = docs.toObject(PenyembelihanModel::class.java);

                        val data = PenyembelihanModel();
                        data.berat_daging = docs.toObject(PenyembelihanModel::class.java).berat_daging;
                        data.fl_id = docs.toObject(PenyembelihanModel::class.java).fl_id;
                        data.name = docs.toObject(PenyembelihanModel::class.java).name;
                        data.tgl = docs.toObject(PenyembelihanModel::class.java).tgl;
                        data.vidio = docs.toObject(PenyembelihanModel::class.java).vidio;
                        data.id = document.documents[i].id;



                        flList.add(data)
                        //Log.e("id dadasd- ",""+docs.toObject(PenyembelihanModel::class.java).berat_daging);
                        i++;
                    }


                    mAdapter = PenyembelihanAdapter(this, flList)
                    rc_penyembelih?.adapter = mAdapter

                    if(flList.size <1){
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