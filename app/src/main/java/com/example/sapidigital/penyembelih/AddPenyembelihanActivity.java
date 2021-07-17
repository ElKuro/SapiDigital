package com.example.sapidigital.penyembelih;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sapidigital.FeedLotsAcitivity;
import com.example.sapidigital.R;
import com.example.sapidigital.models.PemeriksaanModel;
import com.example.sapidigital.models.PenyembelihanModel;
import com.example.sapidigital.pemeriksa.AddPemeriksaanActivity;
import com.example.sapidigital.pemeriksa.PemeriksaanActivity;
import com.example.sapidigital.utils.DatePickerFragment;
import com.example.sapidigital.utils.Preferences;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Map;

public class AddPenyembelihanActivity extends AppCompatActivity {

    EditText edt_name_penyembelih,berat_daging;
    Button btn_tgl,btn_vidio,btn_submit_penyembelih;
    private final int PICK_PDF_CODE = 2342;
    private Uri filepath;
    StorageReference storageReference;
    private FirebaseFirestore firestoreDB;
    String id_fl = "";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_penyembelihan);
        btn_submit_penyembelih  = findViewById(R.id.btn_submit_penyembelih);
        btn_vidio  = findViewById(R.id.btn_vidio);
        btn_tgl  = findViewById(R.id.btn_tgl);
        berat_daging = findViewById((R.id.berat_daging));
        edt_name_penyembelih  = findViewById(R.id.edt_name_penyembelih);
        storageReference = FirebaseStorage.getInstance().getReference(); //To Upload Image
        String idLogin = Preferences.getIdLogin(AddPenyembelihanActivity.this);
        String role = Preferences.getRoleLogin(AddPenyembelihanActivity.this);
        firestoreDB = FirebaseFirestore.getInstance();

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String j =(String) b.get("id_fl");
            id_fl = j;
        }
        btn_tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tampilTgl();
            }
        });

        btn_vidio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Vidio"), PICK_PDF_CODE);
            }
        });


        btn_submit_penyembelih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogs();
                if(filepath != null){
                    StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                            + "." + getFileExtension(filepath));

                    fileReference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Map penyembelihan = new PenyembelihanModel(
                                            edt_name_penyembelih.getText().toString(),
                                            berat_daging.getText().toString(),
                                            btn_tgl.getText().toString(),
                                            uri.toString(),
                                            id_fl
                                    ).toMap();
                                    firestoreDB.collection("penyembelihan")
                                            .add(penyembelihan).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), e + "", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                        }
                    });
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(AddPenyembelihanActivity.this, "vidio tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    private void tampilTgl() {

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (data.getData() != null) {
                filepath=data.getData();
                Toast.makeText(this, data.getData().getPath(), Toast.LENGTH_SHORT).show();
//                tv_sp.setText(data.getData().getPath());
            } else
                Toast.makeText(this, "NO FILE CHOSEN", Toast.LENGTH_SHORT).show();
        }

    }

    private void showDialogs() {
        progressDialog = new ProgressDialog(AddPenyembelihanActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Please wait"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
