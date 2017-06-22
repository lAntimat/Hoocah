package ru.lantimat.hoocah;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Created by lantimat on 22.06.17.
 */

public class UpdateProfileActivity extends AppCompatActivity {
    private EditText inputName, inputImgUrl;
    private Button btnUpdate;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        inputName = (EditText) findViewById(R.id.name);
        inputImgUrl = (EditText) findViewById(R.id.img_url);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputName.getText().toString().trim();
                String imgUrl = inputImgUrl.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Введите имя!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(imgUrl)) {
                    Toast.makeText(getApplicationContext(), "Введи url для фото", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .setPhotoUri(Uri.parse(imgUrl))
                        .build();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    finish();
                                }
                            }
                        });
            }
        });
    }
}
