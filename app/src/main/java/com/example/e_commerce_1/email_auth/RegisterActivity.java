package com.example.e_commerce_1.email_auth;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.e_commerce_1.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextRegisterFullName, editTextRegisterEmail, editTextRegisterDoB, editTextRegisterMobile,
            editTextRegisterPwd, editTextRegisterConfirmPwd;

    CircleImageView imageView;
    private Uri filePath;
    private ProgressBar progressBar;
    private RadioGroup radioGroupRegisterGender;
    private RadioButton radioButtonRegisterGenderSelected;
    private  DatePickerDialog picker;
    private static final String TAG= "RegisterActivity";
    private final int PICK_IMAGE_REQUEST = 22;

    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;


    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        //Set the title
        //getSupportActionBar().setTitle("Register");

        Toast.makeText(RegisterActivity.this, "You can register now", Toast.LENGTH_LONG).show();

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.imageView_profile_dp);
        editTextRegisterFullName = findViewById(R.id.editText_register_full_name);
        editTextRegisterEmail = findViewById(R.id.editText_register_email);
        editTextRegisterDoB = findViewById(R.id.editText_register_dob);
        editTextRegisterMobile = findViewById(R.id.editText_register_mobile);
        editTextRegisterPwd = findViewById(R.id.editText_register_password);
        editTextRegisterConfirmPwd = findViewById(R.id.editText_register_confirm_password);

        //radio Button for gender
        radioGroupRegisterGender = findViewById(R.id.radio_group_register_gender);
        radioGroupRegisterGender.clearCheck();

        //Selecting and uploading pic to firebase
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(
                                intent,
                                "Select Image from here..."),
                        PICK_IMAGE_REQUEST);
            }
        });



        //Setting up DatePicker on EditText
        editTextRegisterDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance(); //getInstance is used to get current date and time
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // Date Picker Dialog
                //The listener is used to indicate the user has finished selecting a date.
                picker = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextRegisterDoB.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int selectedGenderId = radioGroupRegisterGender.getCheckedRadioButtonId();
                radioButtonRegisterGenderSelected = findViewById(selectedGenderId);

                //Obtain the entered data

                String textFullName = editTextRegisterFullName.getText().toString();
                String textEmail = editTextRegisterEmail.getText().toString();
                String textDob = editTextRegisterDoB.getText().toString();
                String textMobile = editTextRegisterMobile.getText().toString();
                String textPwd = editTextRegisterPwd.getText().toString();
                String textConfirmPwd = editTextRegisterConfirmPwd.getText().toString();
                String textGender; //Can't obtain the value before verifying if any button was selected or not.

                //Validate Mobile number using Matcher and Patter (Regular Expression)
                String mobileRegex = "[6-9][0-9]{9}"; // First number can be 6,7,8,9 and rest 9 no. can be any
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobileRegex);
                mobileMatcher = mobilePattern.matcher(textMobile);





                if(imageView.getDrawable() == null){
                    Toast.makeText(RegisterActivity.this, "Please enter image", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(textFullName)){
                    Toast.makeText(RegisterActivity.this, "Please enter your full name", Toast.LENGTH_SHORT).show();
                    editTextRegisterFullName.setError("Full name is required");
                    editTextRegisterFullName.requestFocus();
                } else if(TextUtils.isEmpty(textFullName)){
                    Toast.makeText(RegisterActivity.this, "Please enter your full name", Toast.LENGTH_SHORT).show();
                    editTextRegisterFullName.setError("Full name is required");
                    editTextRegisterFullName.requestFocus();
                } else if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(RegisterActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("Email is required");
                    editTextRegisterEmail.requestFocus();
                } else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(RegisterActivity.this, "Please re-enter your email", Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("Valid Email is required");
                    editTextRegisterEmail.requestFocus();
                } else if(TextUtils.isEmpty(textDob)){
                    Toast.makeText(RegisterActivity.this, "Please enter your date of birth", Toast.LENGTH_SHORT).show();
                    editTextRegisterDoB.setError("Date of Birth is required");
                    editTextRegisterDoB.requestFocus();
                } else if(radioGroupRegisterGender.getCheckedRadioButtonId() == -1){
                    Toast.makeText(RegisterActivity.this, "Please select your gender", Toast.LENGTH_SHORT).show();
                    radioButtonRegisterGenderSelected.setError("Gender is required");
                    radioButtonRegisterGenderSelected.requestFocus();
                } else if(TextUtils.isEmpty(textMobile)){
                    Toast.makeText(RegisterActivity.this, "Please enter your mobile number", Toast.LENGTH_SHORT).show();
                    editTextRegisterMobile.setError("Mobile Number is required");
                    editTextRegisterMobile.requestFocus();
                } else if(textMobile.length() != 10) {
                    Toast.makeText(RegisterActivity.this, "Please re-enter your mobile number", Toast.LENGTH_SHORT).show();
                    editTextRegisterMobile.setError("Mobile no. should be 10 digits");
                    editTextRegisterMobile.requestFocus();
                } else if(!mobileMatcher.find()){
                    Toast.makeText(RegisterActivity.this, "Please re-enter your mobile number", Toast.LENGTH_SHORT).show();
                    editTextRegisterMobile.setError("Mobile no. is not valid");
                    editTextRegisterMobile.requestFocus();
                } else if(TextUtils.isEmpty(textPwd)){
                    Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    editTextRegisterPwd.setError("password is required");
                    editTextRegisterPwd.requestFocus();
                } else if(textPwd.length() < 6){
                    Toast.makeText(RegisterActivity.this, "password should be at least 6 digits", Toast.LENGTH_SHORT).show();
                    editTextRegisterPwd.setError("Password too weak");
                    editTextRegisterPwd.requestFocus();
                } else if(TextUtils.isEmpty(textConfirmPwd)){
                    Toast.makeText(RegisterActivity.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
                    editTextRegisterPwd.setError("Password confirmation is required");
                    editTextRegisterPwd.requestFocus();
                } else if(!textPwd.equals(textConfirmPwd)){
                    Toast.makeText(RegisterActivity.this, "Password and Confirm password does not match", Toast.LENGTH_SHORT).show();
                    editTextRegisterConfirmPwd.setError("Password and confirm password should match");
                    editTextRegisterConfirmPwd.requestFocus();

                    // Clear the entered passwords
                    editTextRegisterPwd.clearComposingText();
                    editTextRegisterConfirmPwd.clearComposingText();
                }
                else{
                    textGender = radioButtonRegisterGenderSelected.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textFullName, textEmail, textDob, textGender, textMobile, textPwd);
                }
            }
        });
    }


   /* @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Adding profile pic in firebase in the name  profile_picture
        if(data.getData() != null){
            Uri profileUri = data.getData();
            imageView.setImageURI(profileUri);

            final StorageReference reference = storage.getReference().child("profile_picture")
                    .child(FirebaseAuth.getInstance().getUid());

            reference.putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(RegisterActivity.this, "Upload Success", Toast.LENGTH_SHORT).show();


                    //Uploading user profile pic link in data base
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Registered Users").child(FirebaseAuth.getInstance().getUid())
                                    .child("profileImg").setValue(uri.toString()); // here it will be profileImg only
                            Toast.makeText(RegisterActivity.this, "Profile Picture Uploaded", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });
        }
    } */
   // Override onActivityResult method
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);

       // checking request code and result code
       // if request code is PICK_IMAGE_REQUEST and
       // resultCode is RESULT_OK
       // then set image in the image view
       if (requestCode == PICK_IMAGE_REQUEST
               && resultCode == RESULT_OK
               && data != null
               && data.getData() != null) {

           // Get the Uri of data
           filePath = data.getData();
           try {
               // Setting image on image view using Bitmap
               Bitmap bitmap = MediaStore
                       .Images
                       .Media
                       .getBitmap(
                               getContentResolver(),
                               filePath);
               imageView.setImageBitmap(bitmap);
           }
           catch (IOException e) {
               // Log the exception
               e.printStackTrace();
           }
       }
   }






    // Register user using the credentials given
    private void registerUser(String textFullName, String textEmail, String textDob, String textGender, String textMobile, String textPwd) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //Create user profile
        auth.createUserWithEmailAndPassword(textEmail, textPwd).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser firebaseUser = auth.getCurrentUser();

                        //Update Display Name of User
                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                        firebaseUser.updateProfile(profileChangeRequest);

                        //User data into Firebase Realtime Database
                        ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textDob, textGender, textMobile);

                        //Extracting user reference from Database for "Registered users"
                        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

                        referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){
                                    uploadImage();
                                    //send verification Email
                                    firebaseUser.sendEmailVerification();
                                    Toast.makeText(RegisterActivity.this, "User registered successfully. Please verify your email to login ", Toast.LENGTH_SHORT).show();





                                } else{
                                    Toast.makeText(RegisterActivity.this, "Registration Failed, Please try again", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });


                    } else{
                        try{
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e){
                            editTextRegisterPwd.setError("Your password is too weak. Kindly use a mix of alphabets, numbers and special characters");
                            editTextRegisterPwd.requestFocus();
                        } catch (FirebaseAuthInvalidCredentialsException e){
                            editTextRegisterEmail.setError("Your email is invalid or already in use. Kindly re-enter");
                            editTextRegisterEmail.requestFocus();
                        } catch (FirebaseAuthUserCollisionException e){
                            editTextRegisterEmail.setError("User is already registered with this email. Use another email");
                            editTextRegisterEmail.requestFocus();
                        } catch (Exception e){
                            Log.e(TAG, e.getMessage());
                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);

                    }
            }
        });
    }

    private void uploadImage() {
       if(filePath != null){
           final StorageReference reference = storage.getReference().child("profile_picture")
                   .child(FirebaseAuth.getInstance().getUid());

           reference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   Toast.makeText(RegisterActivity.this, "Upload Success", Toast.LENGTH_SHORT).show();


                   //Uploading user profile pic link in data base
                   reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                       @Override
                       public void onSuccess(Uri uri) {
                           database.getReference().child("Registered Users Profile").child(FirebaseAuth.getInstance().getUid())
                                   .child("profileImg").setValue(uri.toString()); // here it will be profileImg only
                           Toast.makeText(RegisterActivity.this, "Profile Picture Uploaded", Toast.LENGTH_SHORT).show();
                           progressBar.setVisibility(View.GONE);

                           //Open user profile after successful registration
                           Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                           //To prevent user from returning back to register Activity on pressing back button after registration
                           intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                   | Intent.FLAG_ACTIVITY_NEW_TASK);

                            auth.signOut();
                           startActivity(intent);
                           finish(); // To close register activity
                       }
                   });
               }
           });
       }
    }
}