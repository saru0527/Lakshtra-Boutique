package com.example.e_commerce_1.email_auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.e_commerce_1.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.e_commerce_1.activities.ContactUsActivity;
import com.example.e_commerce_1.activities.FeedbackActivity;
import com.example.e_commerce_1.activities.OrderActivity;
import com.example.e_commerce_1.activities.VideoActivity;
import com.example.e_commerce_1.chatbot.BotActivity;
import com.example.e_commerce_1.ui.profile.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ramotion.circlemenu.CircleMenuView;

public class SettingsActivity extends AppCompatActivity {

    private CardView userProfileCard, logoutCard, updateEmailCard, changePasswordCard, updateProfileCard, deleteProfileCard;
    private FirebaseAuth authProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final CircleMenuView circleMenuView = findViewById(R.id.circleMenu);

        circleMenuView.setEventListener(new CircleMenuView.EventListener(){
            @Override
            public void onMenuOpenAnimationStart(@NonNull CircleMenuView view) {
                //Do something
            }

            @Override
            public void onButtonClickAnimationStart(@NonNull CircleMenuView view, int buttonIndex) {
                super.onButtonClickAnimationStart(view, buttonIndex);
                switch (buttonIndex){
                    case 0:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent i=new Intent(SettingsActivity.this,BotActivity.class);
                                startActivity(i);
                            }
                        }, 600);
                        break;

                    case 1:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent i=new Intent(SettingsActivity.this,FeedbackActivity.class);
                                startActivity(i);
                            }
                        }, 600);
                        break;

                    case 2:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent i=new Intent(SettingsActivity.this,OrderActivity.class);
                                startActivity(i);
                            }
                        }, 600);
                        break;

                    case 3:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent i=new Intent(SettingsActivity.this,VideoActivity.class);
                                startActivity(i);
                            }
                        }, 600);
                        break;

                    case 4:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent i=new Intent(SettingsActivity.this,ContactUsActivity.class);
                                startActivity(i);
                            }
                        }, 600);
                        break;
                }
            }
        });

        /*final CircleMenuView circleMenuView = findViewById(R.id.circleMenu);

        circleMenuView.setEventListener(new CircleMenuView.EventListener(){
            @Override
            public void onMenuOpenAnimationStart(@NonNull CircleMenuView view) {
                //Do something
            }

            @Override
            public void onButtonClickAnimationStart(@NonNull CircleMenuView view, int buttonIndex) {
                super.onButtonClickAnimationStart(view, buttonIndex);
                switch (buttonIndex){
                    case 0:
                        Toast.makeText(SettingsActivity.this, "1st Option", Toast.LENGTH_SHORT).show();
                        break;

                    case 1:
                        Toast.makeText(SettingsActivity.this, "2nd Option", Toast.LENGTH_SHORT).show();
                        break;

                    case 2:
                        Toast.makeText(SettingsActivity.this, "3rd Option", Toast.LENGTH_SHORT).show();
                        break;

                    case 3:
                        Toast.makeText(SettingsActivity.this, "4th Option", Toast.LENGTH_SHORT).show();
                        break;

                    case 4:
                        Toast.makeText(SettingsActivity.this, "5th Option", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }); */

       //getSupportActionBar().setTitle("Settings");

        authProfile = FirebaseAuth.getInstance();

        //userProfileCard = findViewById(R.id.cardVew_user_profile);
        //logoutCard = findViewById(R.id.cardVew_logout);
        updateEmailCard = findViewById(R.id.cardVew_update_email);
        changePasswordCard = findViewById(R.id.cardVew_change_password);
        updateProfileCard = findViewById(R.id.cardVew_update_profile);
        deleteProfileCard = findViewById(R.id.cardVew_delete_profile);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();


        /*userProfileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ProfileFragment.class);
                startActivity(intent);
            }
        });

        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authProfile.signOut();
                Toast.makeText(SettingsActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);

                //Clear stack to prevent user coming back to UserProfileActivity on pressing back button after logging out
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); //Close UserProfileActivity
            }
        }); */

        updateEmailCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, UpdateEmailActivity.class);
                startActivity(intent);
            }
        });

        changePasswordCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        updateProfileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, UpdateProfileActivity.class);
                startActivity(intent);
            }
        });

       deleteProfileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, DeleteProfileActivity.class);
                startActivity(intent);
            }
        });


    }


}