package com.example.sapidigital.lokasi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sapidigital.R;
import com.example.sapidigital.models.LokasiModel;
import com.example.sapidigital.models.PemeriksaanModel;
import com.example.sapidigital.utils.DatePickerFragment;
import com.example.sapidigital.utils.Preferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Map;

public class AddLokasiActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner sp_hp;
    EditText edt_tempat_pemeliharaan;
    Button btn_tgl,btn_submit_penyembelih;
    ProgressDialog progressDialog;
    TextView tv_sp;
    private Uri filepath;
    String id_fl = "";
    StorageReference storageReference;
    private FirebaseFirestore firestoreDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lokasi);
        edt_tempat_pemeliharaan  = findViewById(R.id.edt_tempat_pemeliharaan);
        btn_submit_penyembelih  = findViewById(R.id.btn_submit_penyembelih);
        btn_tgl  = findViewById(R.id.btn_tgl);

        storageReference = FirebaseStorage.getInstance().getReference(); //To Upload Image
        String idLogin = Preferences.getIdLogin(AddLokasiActivity.this);
        String role = Preferences.getRoleLogin(AddLokasiActivity.this);

        firestoreDB = FirebaseFirestore.getInstance();

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String j =(String) b.get("id_fl");
            id_fl = j;
        }


        btn_submit_penyembelih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!id_fl.equals("")){
                                    showDialogs();

                Map lokasi_model = new LokasiModel(
                        edt_tempat_pemeliharaan.getText().toString(),
                        btn_tgl.getText().toString(),
                        id_fl
                ).toMap();
                firestoreDB.collection("lokasi")
                        .add(lokasi_model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                }).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), e + "", Toast.LENGTH_SHORT).show();
                    }
                });
                }
            }
        });

        btn_tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tampilTanggal();
            }
        });
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void tampilTanggal() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "data");
        datePickerFragment.setOnDateClickListener(new DatePickerFragment.onDateClickListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String tahun = "" + datePicker.getYear();
                String bulan = "" + (datePicker.getMonth() + 1);
                String hari = "" + datePicker.getDayOfMonth();
                String text = hari + "-" + bulan + "-" + tahun;
                btn_tgl.setText(text);
            }
        });
    }

    private void showDialogs() {
        progressDialog = new ProgressDialog(AddLokasiActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Please wait"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}