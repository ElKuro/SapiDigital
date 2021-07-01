package com.example.sapidigital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FeedLotsAcitivity extends AppCompatActivity {

    Button btn_add_hewan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_lots_acitivity);
        btn_add_hewan = findViewById(R.id.btn_add_hewan);

        btn_add_hewan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FeedLotsAcitivity.this,AddFeedlotsActivity.class));
            }
        });

    }
}