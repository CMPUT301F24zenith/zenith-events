package com.example.zenithevents.WaitingListPackage;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zenithevents.R;

public class WaitingList extends AppCompatActivity{
    Button viewMapButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_list);
        viewMapButton = findViewById(R.id.viewMapButton);
    }
}
