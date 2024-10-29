package User;

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

import com.example.zenithevents.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class UserPage extends AppCompatActivity {

    private EditText editFirstName,editLastName, editEmail, editPassword, editPhoneNumber;
    private Button btnUpload, btnDelete, btnSave;
    private ImageView profileImage;
    private String initials;
    private FirebaseFirestore db;
    private StorageReference storageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_page);

        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        profileImage = findViewById(R.id.profileImage);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        btnUpload = findViewById(R.id.btnUpload);
        btnDelete = findViewById(R.id.btnRemove);
        btnSave = findViewById(R.id.btnSave);

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        initials = editFirstName.getText().toString().substring(0, 1) + editLastName.getText().toString().substring(0, 1);
        btnUpload.setOnClickListener(v -> selectImage());
        btnDelete.setOnClickListener(v -> deleteImage(initials));
        btnSave.setOnClickListener(v -> saveProfile());


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
            uploadImageToFirebase(imageUri);
        }
    }

    protected void uploadImageToFirebase(Uri imageUri){
        StorageReference fileRef = storageRef.child("users/" + imageUri.getLastPathSegment());
        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                db.collection("users").document(userID).update("imageUrl", imageUrl).addOnSuccessListener(aVoid -> {
                    Toast.makeText(UserPage.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(exception -> {
                    Toast.makeText(UserPage.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                });
            })).addOnFailureListener(exception -> {
                Toast.makeText(UserPage.this, "Could Not Download Image", Toast.LENGTH_SHORT).show();
            });
    }

    private void deleteImage(String initials) {

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Bitmap image = createProfileImage(initials);;

        db.collection("users").document(userID).update("imageUrl", image).addOnSuccessListener(aVoid -> {
            Toast.makeText(UserPage.this, "Image Deleted", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(exception -> {
            Toast.makeText(UserPage.this, "Image Delete Failed", Toast.LENGTH_SHORT).show();
        });
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


    private void saveProfile(){
                String FirstName = editFirstName.getText().toString();
                String LastName = editLastName.getText().toString();
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                String phoneNumber = editPhoneNumber.getText().toString();

                Map<String, Object> profile = new HashMap<>();
                profile.put("FirstName", FirstName);
                profile.put("LastName", LastName);
                profile.put("email", email);
                profile.put("password", password);
                profile.put("phoneNumber", phoneNumber);

                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                db.collection("users").document(userId).set(profile).addOnSuccessListener(aVoid ->
                        Toast.makeText(UserPage.this, "Profile Updated", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(exception ->
                        Toast.makeText(UserPage.this, "Profile Update Failed", Toast.LENGTH_SHORT).show());
        }

    }

