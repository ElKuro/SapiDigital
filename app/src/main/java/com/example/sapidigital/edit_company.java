package com.example.sapidigital;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class edit_company extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TAG = "TAG";
    EditText ECfullname,ECemail,ECphone;
    MultiAutoCompleteTextView ECaddress;
    CircleImageView PFoto;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    Button saveb;
    StorageReference storageReference;
    ImageView Back,Home;
    String NRole;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_company);

        Intent data = getIntent();
        String CfName = data.getStringExtra("Cfullname");
        String Cemail = data.getStringExtra("Cemail");
        String Cphone = data.getStringExtra("Cphone");
        String Caddress = data.getStringExtra("Caddress");

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        user = fAuth.getCurrentUser();
        Back = findViewById(R.id.back);
        Home = findViewById(R.id.home);


        ECfullname = findViewById(R.id.Profullname);
        ECemail = findViewById(R.id.Proemail);
        ECphone = findViewById(R.id.Prophone);
        ECaddress = findViewById(R.id.profileaddress);
        PFoto = findViewById(R.id.Foto);
        ECfullname.setText(CfName);
        ECemail.setText(Cemail);
        ECphone.setText(Cphone);
        ECaddress.setText(Caddress);
        saveb = findViewById(R.id.saveb);


        Spinner spinnerrole = (Spinner) findViewById(R.id.Spinner1);
        //String Roleid = spinnerrole.getSelectedItem().toString();



        ArrayList<Roles> rolesList = new ArrayList<Roles>();
        rolesList.add(new Roles("USERS","Feedloters"));
        rolesList.add(new Roles("PEMERIKSA","Vet"));
        rolesList.add(new Roles("PENYEMBELIH","Butcher"));

        ArrayAdapter<Roles> SpinnerAdapter = new ArrayAdapter<Roles>(edit_company.this,
                android.R.layout.simple_spinner_dropdown_item,rolesList);
        spinnerrole.setAdapter(SpinnerAdapter);



        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Role, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(adapter);
        //spinner.setOnItemSelectedListener(this);

        StorageReference profileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "/profile Company.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(PFoto);
            }
        });

        PFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media. EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Company.class));
                finish();
            }
        });
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });




        saveb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ECfullname.getText().toString().isEmpty() || ECemail.getText().toString().isEmpty()|| ECphone.getText().toString().isEmpty()|| ECaddress.getText().toString().isEmpty())
                { Toast.makeText(edit_company.this, "Some Field are Emty", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String email = ECemail.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference docRef = fStore.collection("users").document(user.getUid());
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("Cemail",email);
                        edited.put("CfName",ECfullname.getText().toString());
                        edited.put("Cphone",ECphone.getText().toString());
                        edited.put("Caddress",ECaddress.getText().toString());
                        spinnerrole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                Roles spnroles = (Roles) parent.getItemAtPosition(position);
                                Toast.makeText(edit_company.this, spnroles.id,Toast.LENGTH_SHORT).show();


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        edited.put("role",spinnerrole);
                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(edit_company.this, "Profile Update", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Company.class));
                                finish();

                            }
                        });
                        Toast.makeText(edit_company.this, "Email is changed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(edit_company.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                //profileImageView.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);


            }
        }
    }
    private void uploadImageToFirebase(Uri imageUri) {
        // upload Image to Firebase storage
        StorageReference fileRef = storageReference.child ("users/" + fAuth.getCurrentUser().getUid() + "/profile Company.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(edit_company.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(PFoto);
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String Role = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(),Role,Toast.LENGTH_SHORT).show();



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}