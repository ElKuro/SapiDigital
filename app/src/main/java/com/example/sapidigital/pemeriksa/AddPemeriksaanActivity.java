package com.example.sapidigital.pemeriksa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sapidigital.AddFeedlotsActivity;
import com.example.sapidigital.FeedLotsAcitivity;
import com.example.sapidigital.R;
import com.example.sapidigital.models.PemeriksaanModel;
import com.example.sapidigital.penyembelih.AddPenyembelihanActivity;
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

public class AddPemeriksaanActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner sp_hp;
    EditText edt_np,edt_kt,berat_sapi;
    Button btn_tp,btn_sp,btn_submit;
    ProgressDialog progressDialog;
    TextView tv_sp;
    private Uri filepath;
    String id_fl = "";
    StorageReference storageReference;
    private FirebaseFirestore firestoreDB;
    String[] hp_list = {"Boleh disembelih", "Ditunda untuk disembelih","Disembelih dengan syarat","Ditolak untuk disembelih","Pemeriksaan Biasa"};
    String hpValue = "Boleh disembelih";
    private final int PICK_PDF_CODE = 2342;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pemeriksaan);
        berat_sapi = findViewById(R.id.tv_berat_sapi);
        sp_hp  = findViewById(R.id.sp_hp);
        edt_np  = findViewById(R.id.edt_np);
        edt_kt  = findViewById(R.id.edt_kt);
        btn_sp  = findViewById(R.id.btn_sp);
        btn_tp  = findViewById(R.id.btn_tp);
        btn_submit  = findViewById(R.id.btn_submit);
        tv_sp  = findViewById(R.id.tv_sp);
        storageReference = FirebaseStorage.getInstance().getReference(); //To Upload Image
        String idLogin = Preferences.getIdLogin(AddPemeriksaanActivity.this);
        String role = Preferences.getRoleLogin(AddPemeriksaanActivity.this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hp_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_hp.setAdapter(adapter);
        sp_hp.setOnItemSelectedListener(this);
        firestoreDB = FirebaseFirestore.getInstance();

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String j =(String) b.get("id_fl");
            id_fl = j;
        }

        btn_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
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
                                    Map pemeriksaanModel = new PemeriksaanModel(
                                            
                                            berat_sapi.getText().toString(),
                                            edt_np.getText().toString(),
                                            btn_tp.getText().toString(),
                                            hpValue,
                                            uri.toString(),
                                            edt_kt.getText().toString(),
                                            id_fl
                                    ).toMap();
                                    firestoreDB.collection("pemeriksaan")
                                            .add(pemeriksaanModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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
                    Toast.makeText(AddPemeriksaanActivity.this, "document tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_tp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tampilTanggal();
            }
        });
    }

    private void selectImage() {
        final CharSequence[] options = { "Photo", "Pdf","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(AddPemeriksaanActivity.this);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Photo")) {
                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(gallery, 100);
                } else if (options[item].equals("Pdf")) {
                    Log.e("dsadas", "pick pdf");
                    Intent intent = new Intent();
                    intent.setType("application/pdf");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Document"), PICK_PDF_CODE);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (data.getData() != null) {
                filepath=data.getData();
                tv_sp.setText(data.getData().getPath());
            } else
                Toast.makeText(this, "NO FILE CHOSEN", Toast.LENGTH_SHORT).show();
        }

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
                btn_tp.setText(text);
//                myDate = text;
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        hpValue = hp_list[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private void showDialogs() {
        progressDialog = new ProgressDialog(AddPemeriksaanActivity.this);
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
}