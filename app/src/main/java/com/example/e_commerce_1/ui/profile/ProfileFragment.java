package com.example.e_commerce_1.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.e_commerce_1.R;
import com.example.e_commerce_1.email_auth.ReadWriteUserDetails;
import com.example.e_commerce_1.email_auth.UpdateProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    CircleImageView imageView;
    Button update;
    private TextView textViewWelcome, textViewFullName, textViewEmail, textViewDoB, textViewGender, textViewMobile;
    private ProgressBar progressBar;
    private String fullName, email, dob, gender, mobile;
    private ImageView  profileView, genderView;

    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

    View root = inflater.inflate(R.layout.fragment_profile, container, false);

    auth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = auth.getCurrentUser();
    database = FirebaseDatabase.getInstance();
    storage = FirebaseStorage.getInstance();

        //getSupportActionBar().setTitle("Home");

        textViewWelcome = root.findViewById(R.id.textView_show_welcome);
        textViewFullName = root.findViewById(R.id.textView_show_full_name);
        textViewEmail = root.findViewById(R.id.textView_show_email);
        textViewDoB = root.findViewById(R.id.textView_show_dob);
        textViewGender = root.findViewById(R.id.textView_show_gender);
        textViewMobile = root.findViewById(R.id.textView_show_mobile);
        progressBar = root.findViewById(R.id.progressBar);
        update = root.findViewById(R.id.update);

        //Set onClickListener on ImageView to open up uploadProfilePicActivity
        imageView = root.findViewById(R.id.imageView_profile_dp);
        profileView = root.findViewById(R.id.ic_profile);
        genderView = root.findViewById(R.id.ic_gender);


        //If profile is present in firebase showing
        database.getReference().child("Registered Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                        if(readUserDetails != null) {
                            fullName = firebaseUser.getDisplayName();
                            email = firebaseUser.getEmail();
                            dob = readUserDetails.doB;
                            gender = readUserDetails.gender;
                            mobile = readUserDetails.mobile;

                            textViewWelcome.setText("Welcome, " + fullName + "!");
                            textViewFullName.setText(fullName);
                            textViewEmail.setText(email);
                            textViewDoB.setText(dob);
                            textViewGender.setText(gender);
                            textViewMobile.setText(mobile);

                            if(gender.matches("Male")){
                                profileView.setImageResource(R.drawable.ic_male_profile);
                                genderView.setImageResource(R.drawable.ic_male);
                            } else{
                                profileView.setImageResource(R.drawable.ic_female_profile);
                                genderView.setImageResource(R.drawable.ic_female);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        database.getReference().child("Registered Users Profile").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                        if(readUserDetails != null) {
                            Glide.with(getContext()).load(readUserDetails.getProfileImg()).into(imageView);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });















        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });

        return root;
    }

    private void updateUserProfile() {
        Intent intent = new Intent(getContext(), UpdateProfileActivity.class);
        startActivity(intent);
    }

}