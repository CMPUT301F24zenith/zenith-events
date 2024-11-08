package com.example.zenithevents;

import android.view.View;
import android.widget.ImageView;

import org.hamcrest.TypeSafeMatcher;
import org.junit.runner.Description;

public class DrawableMatcher {
    public static TypeSafeMatcher<View> hasDrawable() {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("ImageView has drawable");

            }

            @Override
            protected boolean matchesSafely(View view) {
                if (!(view instanceof ImageView)) {
                    return false;
                }
                ImageView imageView = (ImageView) view;
                return imageView.getDrawable() != null;
            }

        };
    }

}
