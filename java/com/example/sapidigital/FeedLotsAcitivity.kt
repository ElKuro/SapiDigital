package com.example.sapidigital

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sapidigital.adapter.FeedLotsAdapter
import com.example.sapidigital.models.FeedLotsModel
import com.example.sapidigital.models.LokasiModel
import com.example.sapidigital.utils.Preferences
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot


class FeedLotsAcitivity : AppCompatActivity() {
    private lateinit var flList: ArrayList<FeedLotsModel>
    private lateinit var mAdapter: FeedLotsAdapter
    var db = FirebaseFirestore.getInstance()
    var flCollection = db.collection("feedlots").orderBy("tgl", Query.Direction.DESCENDING)

    var recyclerviewFL: RecyclerView? = null
    var btn_add_hewan: Button? = null
    var back : ImageView? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_lots_acitivity)
        setTitle("Feedlots")

        btn_add_hewan = findViewById(R.id.btn_add_hewan)
        back = findViewById(R.id.back)
        recyclerviewFL = findViewById(R.id.recyclerviewFL)

        val role = Preferences.getRoleLogin(this@FeedLotsAcitivity)
        if (role.equals("USER")) {
            btn_add_hewan?.visibility = View.VISIBLE
        } else {
            btn_add_hewan?.visibility = View.GONE
        }

        Init();

        btn_add_hewan?.setOnClickListener {
            startActivity(Intent(this@FeedLotsAcitivity, AddFeedlotsActivity::class.java))
        }
        back?.setOnClickListener {
            startActivity(Intent(this@FeedLotsAcitivity, MainActivity::class.java))
        }

    }

    private fun Init() {
        flList = ArrayList()
        mAdapter = FeedLotsAdapter(this, flList)
//        recyclerviewFL?.layoutManager = LinearLayoutManager(this)
        var i = 0;

        recyclerviewFL?.setLayoutManager(GridLayoutManager(this, 2))

        flCollection.get().addOnCompleteListener { task: Task<QuerySnapshot> ->
            flList.clear()
            if (task.isSuccessful) {
                val document = task.result
                if (!document!!.isEmpty) {
                    for (docs in task.result!!) {
//                        flCollection.document(doc.id).delete()
                        val fl = docs.toObject(FeedLotsModel::class.java)

                        val data = FeedLotsModel();
                        data.no_ternak = docs.toObject(FeedLotsModel::class.java).no_ternak;
                        data.umur_sapi = docs.toObject(FeedLotsModel::class.java).umur_sapi;
                        data.riwayat = docs.toObject(FeedLotsModel::class.java).riwayat;
                        data.bobot_terakhir = docs.toObject(FeedLotsModel::class.java).bobot_terakhir;
                        data.jenis_sapi = docs.toObject(FeedLotsModel::class.java).jenis_sapi;
                        data.ket = docs.toObject(FeedLotsModel::class.java).ket;
                        data.foto = docs.toObject(FeedLotsModel::class.java).foto;
                        data.tgl = docs.toObject(FeedLotsModel::class.java).tgl;
                        data.gender = docs.toObject(FeedLotsModel::class.java).gender;
                        data.user = docs.toObject(FeedLotsModel::class.java).user;
                        data.id = docs.toObject(FeedLotsModel::class.java).id;
                        data.doc = docs.id;

                        fl.doc = docs.id;
                        data.ids =  document.documents[i].id;
                        flList.add(data)
                        i++;
                    }
                    mAdapter = FeedLotsAdapter(this, flList)
                    mAdapter.notifyDataSetChanged()
                    recyclerviewFL?.adapter = mAdapter

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

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@FeedLotsAcitivity, MainActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_more -> Init()
        }
        return super.onOptionsItemSelected(item)

    }
}