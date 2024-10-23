package com.example.zenithevents.ArrayAdapters;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public abstract class CustomArrayAdapter<T>{
    public CustomArrayAdapter(Context context, int i, ArrayList<T> objects){

    }
    @NonNull
    public abstract View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent);


}
