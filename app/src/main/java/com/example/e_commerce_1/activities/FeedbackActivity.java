package com.example.e_commerce_1.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.e_commerce_1.email_auth.ReadWriteUserDetails;
import com.example.e_commerce_1.email_auth.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.e_commerce_1.R;

public class FeedbackActivity extends AppCompatActivity {

    // creating variables for
    // EditText and buttons.
    private String name, email;
    private EditText NameEdt, EmailEdt, DescriptionEdt, RatingEdt;
    private Button sendDatabtn;

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    // creating a variable for
    // our object class
    FeedbackModel feedbackModel;

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();


        // initializing our edittext and button
        NameEdt = findViewById(R.id.edittext_feedback_name);
        EmailEdt = findViewById(R.id.edittext_feedback_email);
        DescriptionEdt = findViewById(R.id.edittext_feedback_description);
        RatingEdt = findViewById(R.id.edittext_feedback_rating);

        // below line is used to get the
        // instance of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        EmailEdt.setEnabled(false);


        // below line is used to get reference for our database.


        // initializing our object
        // class variable.


        sendDatabtn = findViewById(R.id.button_feedback);



        //If profile is present in firebase showing
        firebaseDatabase.getReference().child("Registered Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                        if(readUserDetails != null) {
                            name = firebaseUser.getDisplayName();
                            email = firebaseUser.getEmail();

                            NameEdt.setText(name);
                            EmailEdt.setText(email);

                        }

                    }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });




        // adding on click listener for our button.
        sendDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting text from our edittext fields.
                String name = NameEdt.getText().toString();
                String email = EmailEdt.getText().toString();
                String description = DescriptionEdt.getText().toString().trim();
                String rating = RatingEdt.getText().toString().trim();

                // below line is for checking weather the
                // edittext fields are empty or not.
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(description) || TextUtils.isEmpty(rating)) {
                    // if the text fields are empty
                    // then show the below message.
                    Toast.makeText(FeedbackActivity.this, "Please add some data.", Toast.LENGTH_SHORT).show();

                }  else {
                    // else call the method to add
                    // data to our database.
                    addDatatoFirebase(name, email, description, rating);
                    DescriptionEdt.setText("");
                    RatingEdt.setText("");
                }
            }
        });
    }

    private void addDatatoFirebase(String name, String email, String description, String rating) {
        // below 3 lines of code is used to set
        // data in our object class.

        FirebaseUser firebaseUser = auth.getCurrentUser();

        //User data into Firebase Realtime Database
        FeedbackModel feedbackModel = new FeedbackModel(name, email, description, rating);

        //Extracting user reference from Database for "Registered users"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Feedbacks");

        referenceProfile.child(firebaseUser.getUid()).setValue(feedbackModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(FeedbackActivity.this, "Feedback Sent successfully", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(FeedbackActivity.this, "Not able to send", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}