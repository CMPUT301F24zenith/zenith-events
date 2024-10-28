package com.example.zenithevents.WaitingListPackage;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.zenithevents.R;

public class WaitingList extends AppCompatActivity{
    Button viewMapButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_list);
        viewMapButton = findViewById(R.id.viewMapButton);

        ImageView profileIcon = findViewById(R.id.profile_icon);
        String imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQiXN9xSEe8unzPBEQOeAKXd9Q55efGHGB9BA&s";

        Glide.with(this)
                .load(imageUrl)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                .into(profileIcon);
    }
}
