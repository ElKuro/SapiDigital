package com.example.sapidigital;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Company extends AppCompatActivity {


    TextView Cfullname,Cemail,Cphone,Caddress,Crole;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    Button maddoccupation,mchangeprofile;
    FirebaseUser user;
    ImageView Back,Home,profileImageView;
    StorageReference storageReference;
    View ocp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        Intent data = getIntent();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference(); //To Upload Image

        profileImageView = findViewById(R.id.profileimage );


        StorageReference profileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "/profile Company.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImageView);
            }
        });



        Back = findViewById(R.id.back);
        Home = findViewById(R.id.home);
        ocp = findViewById(R.id.occupation);
        Crole = findViewById(R.id.userrole);

        Cfullname = findViewById(R.id.Profullname);
        Cemail = findViewById(R.id.Proemail);
        Cphone = findViewById(R.id.Prophone);
        Caddress = findViewById(R.id.profileaddress);
        userId = fAuth.getCurrentUser().getUid();

        maddoccupation = findViewById(R.id.addoccupation);
        mchangeprofile = findViewById(R.id.changeprofile);

        user = fAuth.getCurrentUser();

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Profile.class));
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


        maddoccupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), edit_company.class);
                i.putExtra("Cemail", Cemail.getText().toString());
                i.putExtra("Cphone", Cphone.getText().toString());
                i.putExtra("Cfullname", Cfullname.getText().toString());
                i.putExtra("Caddress", Caddress.getText().toString());

                startActivity(i);
            }
        });
        mchangeprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), edit_company.class);
                i.putExtra("Cemail", Cemail.getText().toString());
                i.putExtra("Cphone", Cphone.getText().toString());
                i.putExtra("Cfullname", Cfullname.getText().toString());
                i.putExtra("Caddress", Caddress.getText().toString());

                startActivity(i);
            }
        });




        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                Cemail.setText(documentSnapshot.getString("Cemail"));
                Cfullname.setText(documentSnapshot.getString("CfName"));
                Cphone.setText(documentSnapshot.getString("Cphone"));
                Caddress.setText(documentSnapshot.getString("Caddress"));
                Crole.setText(documentSnapshot.getString("role"));
            }
        });




}}