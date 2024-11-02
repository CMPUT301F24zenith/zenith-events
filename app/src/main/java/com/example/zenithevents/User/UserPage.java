package com.example.zenithevents.User;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.zenithevents.HelperClasses.UserUtils;
import com.example.zenithevents.Objects.User;
import com.example.zenithevents.R;


public class UserPage extends AppCompatActivity {

    private EditText editFirstName,editLastName, editEmail, editPhoneNumber;
    private Button btnUpload, btnDelete, btnSave, btnBack;
    private ImageView profileImage;
    private String initials;

    private UserUtils userUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_page);

        userUtils = new UserUtils();

        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editEmail = findViewById(R.id.editEmail);
        profileImage = findViewById(R.id.profileImage);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        btnUpload = findViewById(R.id.btnUpload);
        btnDelete = findViewById(R.id.btnRemove);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        fetchUserProfile();
        btnBack.setOnClickListener(v -> finish());





        btnUpload.setOnClickListener(v -> selectImage());


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void selectImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            profileImage.setImageURI(imageUri);

        }
    }





    private Bitmap createProfileImage(String initials) {
        int size = 200;
        int background = Color.BLUE;
        int textColor = Color.WHITE;
        float textSize = 60f;

        Bitmap image = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawColor(background);
        Paint paint = new Paint();
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        float x = size / 2f;
        float y = size / 2f - (paint.ascent() + paint.descent()) / 2f;
        canvas.drawText(initials, x, y, paint);
        return image;
    }



    private void fetchUserProfile(){
        userUtils.fetchUserProfile(this, new UserUtils.UserFetchCallback() {
            @Override
            public void onUserFetchComplete(User user) {
                if (user != null) {
                    // Set user data to the fields
                    editFirstName.setText(user.getFirstName() != null ? user.getFirstName() : "");
                    editLastName.setText(user.getLastName() != null ? user.getLastName() : "");
                    editEmail.setText(user.getEmail() != null ? user.getEmail() : "");
                    editPhoneNumber.setText(user.getPhoneNumber() != null ? user.getPhoneNumber() : "");

                    // Calculate initials safely
                    String firstName = editFirstName.getText().toString();
                    String lastName = editLastName.getText().toString();

                    initials = "";
                    if (!firstName.isEmpty()) {
                        initials += firstName.substring(0, 1);
                    }
                    if (!lastName.isEmpty()) {
                        initials += lastName.substring(0, 1);
                    }

                    // Set profile image or initials image
                    if (user.getProfileImageURL() != null && !user.getProfileImageURL().isEmpty()) {
                        profileImage.setImageURI(Uri.parse(user.getProfileImageURL()));
                    } else {
                        profileImage.setImageBitmap(createProfileImage(initials));
                    }
                } else {
                    Toast.makeText(UserPage.this, "User profile not found.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    }

