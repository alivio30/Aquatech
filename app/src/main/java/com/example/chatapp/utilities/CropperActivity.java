package com.example.chatapp.utilities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.UUID;

public class CropperActivity extends AppCompatActivity {

    String result;
    Uri filedUri;
    Uri imageUri;
    Image  image = new Image();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropper);
        readIntent();

        String dest_uri = new StringBuilder(UUID.randomUUID().toString()).append(".jpeg").toString();

        // Configure UCrop options
        UCrop.Options options = new UCrop.Options();
        options.setFreeStyleCropEnabled(true);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG); // Set desired image format
        options.setCompressionQuality(90); // Set desired compression quality (0-100)

        // Start UCrop with configured options
        UCrop.of(filedUri, Uri.fromFile(new File(getCacheDir(), dest_uri)))
                .withOptions(options)
                .withAspectRatio(0, 0)
                .useSourceImageAspectRatio()
                .withMaxResultSize(2000, 2000)
                .start(CropperActivity.this);
    }

    //read or get intent value
    private void readIntent() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            result = intent.getStringExtra("DATA");
            filedUri = Uri.parse(result);
        }
    }

    //result after image crop
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if(resultCode==RESULT_OK && requestCode==UCrop.REQUEST_CROP){
            final Uri resultUri=UCrop.getOutput(data);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("RESULT", resultUri+"");
            setResult(-1,returnIntent);
           //image.setImage(resultUri+"");
            finish();
        } else if(resultCode==UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

}