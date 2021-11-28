package com.example.sapidigital;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
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

import com.bumptech.glide.Glide;
import com.example.sapidigital.lokasi.LokasiActivity;
import com.example.sapidigital.models.FeedLotsModel;
import com.example.sapidigital.pemeriksa.AddPemeriksaanActivity;
import com.example.sapidigital.pemeriksa.PemeriksaanActivity;
import com.example.sapidigital.penyembelih.PenyembelihActivity;
import com.example.sapidigital.utils.DatePickerFragment;
import com.example.sapidigital.utils.Preferences;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.WriterException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class AddFeedlotsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageView iv_sapi,qr;
    Spinner sp_gender;
    TextView title_app;
    String docs = "";
    String parse_id = "";
    String parse_image = "";
    EditText edt_jenis,edt_umur,edt_bobot,edt_riwayat,edt_ket,no_ternak;
    Button btn_date, btn_periksa,btn_penyembelih,btn_image, btn_submit,btn_lokasi;
    List<String> listGender = new ArrayList<String>();
    String[] gender = {"betina", "jantan"};
    String genderValue = "betina";
    StorageReference storageReference;
    private static final int PICK_IMAGE = 100;
    QRGEncoder qrgEncoder;
    public static final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static Random RANDOM = new Random();

    String myDate= "Pilih Tanggal";
    Uri imageUri;
    private FirebaseFirestore firestoreDB;
    ProgressDialog progressDialog;
    String TAG = "GENERATORQR";

    Bitmap bmQr;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feedlots);
        sp_gender = (Spinner) findViewById(R.id.sp_gender);
        qr = (ImageView) findViewById(R.id.qr);
        no_ternak = (EditText) findViewById(R.id.no_ternak);
        title_app = (TextView) findViewById(R.id.title_app);
        edt_jenis = (EditText) findViewById(R.id.edt_jenis);
        edt_bobot = (EditText) findViewById(R.id.edt_bobot);
        edt_umur = (EditText) findViewById(R.id.edt_umur);
        edt_riwayat = (EditText) findViewById(R.id.edt_riwayat);
        edt_ket = (EditText) findViewById(R.id.edt_ket);
        btn_date = (Button) findViewById(R.id.btn_date);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_lokasi = (Button) findViewById(R.id.btn_lokasi);
        btn_periksa = (Button) findViewById(R.id.btn_periksa);
        btn_penyembelih = (Button) findViewById(R.id.btn_penyembelih);
        btn_image = (Button) findViewById(R.id.btn_image);
        iv_sapi = (ImageView) findViewById(R.id.iv_sapi);
        firestoreDB = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference(); //To Upload Image
        String idLogin = Preferences.getIdLogin(AddFeedlotsActivity.this);
        String role = Preferences.getRoleLogin(AddFeedlotsActivity.this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gender);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_gender.setAdapter(adapter);
        sp_gender.setOnItemSelectedListener(this);


        Intent i= getIntent();
        String noternak = i.getStringExtra("no_ternak");
        String jenis = i.getStringExtra("jenis");
        String status = i.getStringExtra("status");
        parse_image = i.getStringExtra("image");
        String parse_umur = i.getStringExtra("umur");
        String parse_ket = i.getStringExtra("ket");
        String parse_riwayat = i.getStringExtra("riwayat");
        String parse_bobot = i.getStringExtra("bobot");
        parse_id = i.getStringExtra("id");
        String parse_date = i.getStringExtra("tgl");
        String parse_gender = i.getStringExtra("gender");
        String parse_user = i.getStringExtra("user");
        docs = i.getStringExtra("doc");


        if(role.equals("PEMERIKSA")){
            title_app.setText("Periksa Hewan");
        }
        if(role.equals("PENYEMBELIH")){
            title_app.setText("Penyembelihan");
        }

        if(status !=null ){
            if(parse_user.equals(idLogin)){
                title_app.setText("");
                btn_periksa.setVisibility(View.VISIBLE);
                btn_penyembelih.setVisibility(View.VISIBLE);
                btn_lokasi.setVisibility(View.VISIBLE);
                btn_submit.setText("update");
            }else{
                edt_riwayat.setEnabled(false);
                edt_umur.setEnabled(false);
                sp_gender.setEnabled(false);
                sp_gender.setClickable(false);
                no_ternak.setEnabled(false);
                no_ternak.setClickable(false);
                btn_date.setEnabled(false);
                edt_ket.setEnabled(false);
                edt_bobot.setEnabled(false);
                edt_jenis.setEnabled(false);
                edt_jenis.setEnabled(false);
                btn_submit.setVisibility(View.GONE);
                btn_image.setVisibility(View.GONE);
                btn_periksa.setVisibility(View.VISIBLE);
                btn_penyembelih.setVisibility(View.VISIBLE);
                btn_lokasi.setVisibility(View.VISIBLE);
                title_app.setText("");
            }
            qr.setVisibility(View.VISIBLE);
            no_ternak.setText(noternak);
            edt_jenis.setText(jenis);
            Glide.with(AddFeedlotsActivity.this)
                    .load(parse_image)
                    .error(R.drawable.button_pilihan)
                    .placeholder(R.drawable.button_pilihan)
                    .error(R.drawable.button_pilihan)
                    .into(iv_sapi);
            edt_bobot.setText(parse_bobot);
            edt_umur.setText(parse_umur);
            edt_ket.setText(parse_ket);
            edt_riwayat.setText(parse_riwayat);
            btn_date.setText(parse_date);
            genderValue = parse_gender;
            sp_gender.setSelection(((ArrayAdapter<String>)sp_gender.getAdapter()).getPosition(genderValue));

            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

            // initializing a variable for default display.
            Display display = manager.getDefaultDisplay();

            // creating a variable for point which
            // is to be displayed in QR Code.
            Point point = new Point();
            display.getSize(point);

            // getting width and
            // height of a point
            int width = point.x;
            int height = point.y;

            // generating dimension from width and height.
            int dimen = width < height ? width : height;
            dimen = dimen * 3 / 4;

            // setting this dimensions inside our qr code
            // encoder to generate our qr code.
            qrgEncoder = new QRGEncoder(parse_id, null, QRGContents.Type.TEXT, dimen);
            try {
                // getting our qrcode in the form of bitmap.
                bmQr = qrgEncoder.encodeAsBitmap();
                // the bitmap is set inside our image
                // view using .setimagebitmap method.
                qr.setImageBitmap(bmQr);
            } catch (WriterException e) {
                // this method is called for
                // exception handling.
            }

        }


        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tampilTanggal();
            }
        });

        btn_lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii=new Intent(getApplicationContext(), LokasiActivity.class);
                ii.putExtra("id_fl", parse_id);
                startActivity(ii);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogs();
                if(status==null){
                StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                        + "." + getFileExtension(imageUri));

                fileReference.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Map feedLotsModel = new FeedLotsModel(
                                                randomString(10),
                                                no_ternak.getText().toString(),
                                                edt_jenis.getText().toString(),
                                                edt_umur.getText().toString(),
                                                genderValue,
                                                edt_bobot.getText().toString(),
                                                myDate,
                                                uri.toString(),
                                                edt_riwayat.getText().toString(),
                                                idLogin,
                                                myDate,
                                                edt_ket.getText().toString())
                                                .toMap();
                                        firestoreDB.collection("feedlots")
                                                .add(feedLotsModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(),FeedLotsAcitivity.class));
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Note could not be added!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddFeedlotsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });}else{
                    Map<String, Object> data = new HashMap<>();
                    data.put("no_ternak",no_ternak.getText().toString());
                    data.put("bobot_terakhir", edt_bobot.getText().toString());
                    data.put("jenis_sapi", edt_jenis.getText().toString());
                    data.put("riwayat", edt_riwayat.getText().toString());
                    data.put("umur_sapi", edt_umur.getText().toString());
                    data.put("tgl", myDate);
                    data.put("ket", edt_ket.getText().toString());
                    if(imageUri != null){
//                        Uri urlUri = Uri.parse(imageUri);
                        StorageReference fileReference2 = storageReference.child(System.currentTimeMillis()
                                + "." + getFileExtension(imageUri));
                        fileReference2.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        data.put("foto",uri.toString());
                                        firestoreDB.collection("feedlots").document(docs).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getApplicationContext(), "Successfull Update Data", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(),FeedLotsAcitivity.class));
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(AddFeedlotsActivity.this, "gagal " + e, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        });

                    }else{
                        firestoreDB.collection("feedlots").document(docs).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Successfull Update Data", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),FeedLotsAcitivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(AddFeedlotsActivity.this, "gagal " + e, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        btn_penyembelih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii=new Intent(getApplicationContext(), PenyembelihActivity.class);
                ii.putExtra("id_fl", parse_id);
                startActivity(ii);
            }
        });

        btn_periksa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii=new Intent(getApplicationContext(), PemeriksaanActivity.class);
                ii.putExtra("id_fl", parse_id);
                startActivity(ii);
            }
        });
    }

    private void showDialogs() {
        progressDialog = new ProgressDialog(AddFeedlotsActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Please wait"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            iv_sapi.setVisibility(View.VISIBLE);
            iv_sapi.setImageURI(imageUri);
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
                btn_date.setText(text);
                myDate = text;
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        genderValue = gender[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }

        return sb.toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent ii=new Intent(getApplicationContext(), FeedLotsAcitivity.class);
        startActivity(ii);
    }
}