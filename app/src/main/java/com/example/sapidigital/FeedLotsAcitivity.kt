package com.example.sapidigital

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
import com.example.sapidigital.adapter.FeedLotsAdapter
import com.example.sapidigital.models.FeedLotsModel
import com.example.sapidigital.utils.Preferences
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import de.hdodenhof.circleimageview.CircleImageView

class FeedLotsAcitivity : AppCompatActivity() {
    private lateinit var flList: ArrayList<FeedLotsModel>
    private lateinit var mAdapter: FeedLotsAdapter
    var db = FirebaseFirestore.getInstance()
    var flCollection = db.collection("feedlots")

    var recyclerviewFL: RecyclerView? = null
    var btn_add_hewan: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_lots_acitivity)
        setTitle("Feedlots")

        btn_add_hewan = findViewById(R.id.btn_add_hewan)
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
    }

    private fun Init() {
        flList = ArrayList()
        mAdapter = FeedLotsAdapter(this, flList)
        recyclerviewFL?.layoutManager = LinearLayoutManager(this)

        flCollection.get().addOnCompleteListener { task: Task<QuerySnapshot> ->
            flList.clear()
            if (task.isSuccessful) {
                val document = task.result
                if (!document!!.isEmpty) {
                    for (docs in task.result!!) {
//                        flCollection.document(doc.id).delete()
                        val fl = docs.toObject(FeedLotsModel::class.java)
                        fl.doc = docs.id
                        flList.add(fl)
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