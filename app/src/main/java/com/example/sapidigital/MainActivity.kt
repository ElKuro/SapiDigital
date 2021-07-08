package com.example.sapidigital

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.sapidigital.utils.Preferences
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.zxing.integration.android.IntentIntegrator



class MainActivity : AppCompatActivity() {
    var id_fl= "";
    var db = FirebaseFirestore.getInstance()
    var flCollection = db.collection("feedlots")
    private var pressedTime: Long = 0
    var tv_name: TextView? = null
    var btn_scan: CardView? = null
    var btn_feedlots: CardView? = null
    var ProfileBtn: CardView? = null
    var logoutBtn: CardView? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_name = findViewById(R.id.tv_name)
        btn_scan = findViewById(R.id.btn_scan)
        btn_feedlots = findViewById(R.id.btn_feedlots)
        ProfileBtn = findViewById(R.id.ProfileBtn)
        logoutBtn = findViewById(R.id.logoutBtn)

        val email = Preferences.getEmailLogin(this@MainActivity)
        tv_name?.text = "     $email"

        btn_scan?.setOnClickListener{
            val intentIntegrator = IntentIntegrator(this@MainActivity)
            intentIntegrator.setPrompt("Scan a barcode or QR Code")
            intentIntegrator.setOrientationLocked(true)
            intentIntegrator.initiateScan()
        }

        btn_feedlots?.setOnClickListener{
            startActivity(Intent(this@MainActivity, FeedLotsAcitivity::class.java))
        }

        ProfileBtn?.setOnClickListener{
            startActivity(Intent(applicationContext, Profile::class.java))
            finish()
        }

        logoutBtn?.setOnClickListener {
            logout();
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(applicationContext, Login::class.java))
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult != null) {
            if (intentResult.contents == null) {
                Toast.makeText(baseContext, "Cancelled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, intentResult.contents + "", Toast.LENGTH_SHORT).show()
                if(!intentResult.contents.isNullOrEmpty()){
                    flCollection.whereEqualTo("id", intentResult.contents).get().addOnCompleteListener { task: Task<QuerySnapshot> ->
                        if (task.isSuccessful) {
                            val document = task.result
                            if (!document!!.isEmpty) {
                                Log.e("dsasad ", document.documents[0]["id"].toString())
                                val datas = document.documents[0]
                                val mIntent = Intent(baseContext, AddFeedlotsActivity::class.java)
                                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mIntent.putExtra("umur", datas["umur_sapi"].toString())
                                mIntent.putExtra("riwayat", datas["riwayat"].toString())
                                mIntent.putExtra("bobot", datas["bobot_terakhir"].toString())
                                mIntent.putExtra("jenis", datas["jenis_sapi"].toString())
                                mIntent.putExtra("ket", datas["ket"].toString())
                                mIntent.putExtra("image", datas["foto"].toString())
                                mIntent.putExtra("status", "update")
                                mIntent.putExtra("tgl", datas["tgl"].toString())
                                mIntent.putExtra("gender", datas["gender"].toString())
                                mIntent.putExtra("user", datas["user"].toString())
                                mIntent.putExtra("id", datas["id"].toString())
                                mIntent.putExtra("doc", task.result!!.documents[0].id.toString())
                                baseContext.startActivity(mIntent)
                            } else {
                                println("No such data")
                            }
                        } else {
                            println(task.exception)
                        }
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else {
            Toast.makeText(baseContext, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
        pressedTime = System.currentTimeMillis()
    }
}