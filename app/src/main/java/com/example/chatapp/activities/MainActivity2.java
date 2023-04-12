package com.example.chatapp.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.utilities.CropperActivity;
import com.example.chatapp.utilities.Image;
import com.google.firebase.storage.StorageReference;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    Button button;
    ImageView imageView;
    String currentPhotoPath;
    Bitmap croppedImage;
    Uri resultUri;
    Image image = new Image();
    private static final int REQUEST_CROP = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        button = findViewById(R.id.testButton);
        imageView = findViewById(R.id.testImageView);
        imageView.setVisibility(View.VISIBLE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = "photo";
                File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                try {
                    File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);
                    currentPhotoPath = imageFile.getAbsolutePath();

                    Uri imageUri = FileProvider.getUriForFile(MainActivity2.this,
                            "com.example.chatapp.fileprovider", imageFile);

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, 12);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if (requestCode == REQUEST_CROP && resultCode == RESULT_OK) {
            String result = data.getStringExtra("RESULT");
            Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
            resultUri = null;
            if(result!=null)
                try {
                    Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
                    resultUri = Uri.parse(result);
                    croppedImage = getBitmapFromUri(resultUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }
        }*/
        if (requestCode == 12 && resultCode == RESULT_OK) {
            Uri imageUri = Uri.fromFile(new File(currentPhotoPath));

            Intent intent = new Intent(MainActivity2.this, CropperActivity.class);
            intent.putExtra("IMAGE_URI", imageUri.toString());
            startActivity(intent);
        }

         /*else if(resultCode==-1 && requestCode==101)
        {
            Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
            String result = data.getStringExtra("RESULT");
            resultUri = null;
            if(result!=null)
                try {
                    resultUri=Uri.parse(result);
                    croppedImage = getBitmapFromUri(resultUri);
                    imageView.setImageBitmap(croppedImage);
                    Toast.makeText(getApplicationContext(), "sulod image bruh", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }*/
    }
    public Bitmap getBitmapFromUri(Uri uri) throws IOException {
        InputStream input = getContentResolver().openInputStream(uri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, options);
        input.close();
        return bitmap;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(image.getImage() == null){

        }else{
            try {
                croppedImage = getBitmapFromUri(Uri.parse(image.getImage()));
                imageView.setImageBitmap(croppedImage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}