package com.example.zenithevents.ArrayAdapters;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Abstract base class for creating custom array adapters for use with Android {@link android.widget.ListView} or other
 * adapter-based UI components. This class allows for the customization of how individual list items are displayed.
 *
 * <p>The adapter is designed to handle a collection of objects of any type {@code T}. The subclass must implement
 * the {@link #getView(int, View, ViewGroup)} method to define how the data should be presented in the UI.</p>
 *
 * <p>Note: The Javadocs for this class were generated with the assistance of an AI language model.</p>
 *
 * @param <T> The type of the items managed by the adapter. Can be any class or data structure.
 */
public abstract class CustomArrayAdapter<T>{

    /**
     * Constructor for the custom array adapter.
     *
     * <p>This constructor initializes the adapter with the given context, layout resource, and the list of objects
     * to be displayed in the UI component. The subclass can use this constructor to perform initialization.</p>
     *
     * @param context The current context in which the adapter is running. This is used to access resources and
     *                create views for list items.
     * @param i       The resource ID for a layout file containing a view to represent a single item in the list.
     * @param objects The list of objects to be displayed by the adapter. It can be any collection of objects.
     */
    public CustomArrayAdapter(Context context, int i, ArrayList<T> objects){

    }

    /**
     * Abstract method to get the view for a specific list item in the adapter.
     *
     * <p>This method must be overridden by a subclass to define how each item in the list should be displayed
     * using the provided data at the specified position.</p>
     *
     * @param position The position of the item within the adapter’s data set to be displayed.
     * @param convertView A recycled view that can be reused (or {@code null} if not available).
     *                    You can use this view to populate the UI with the data, or return a new view.
     * @param parent The parent view that this view will eventually be attached to.
     *
     * @return A {@link View} object representing the item at the given position. This view will be displayed in
     *         the UI.
     */
    @NonNull
    public abstract View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent);


}
