package com.example.sapidigital

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sapidigital.adapter.FeedLotsAdapter
import com.example.sapidigital.models.FeedLotsModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_feed_lots_acitivity.*

class FeedLotsAcitivity : AppCompatActivity() {
    private lateinit var flList: ArrayList<FeedLotsModel>
    private lateinit var mAdapter: FeedLotsAdapter
    var db = FirebaseFirestore.getInstance()
    var flCollection = db.collection("feedlots")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_lots_acitivity)
        Init();

        btn_add_hewan?.setOnClickListener{
            startActivity(Intent(this@FeedLotsAcitivity, AddFeedlotsActivity::class.java))
        }
    }

    private fun Init() {
        flList = ArrayList()
        mAdapter = FeedLotsAdapter(this, flList)
        recyclerviewFL.layoutManager = LinearLayoutManager(this)

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
                    recyclerviewFL.adapter = mAdapter

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
}