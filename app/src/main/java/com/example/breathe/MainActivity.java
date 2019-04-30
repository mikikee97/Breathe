package com.example.breathe;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


public class MainActivity extends AppCompatActivity{
    //Navigation Drawer
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    //Navigation Header
    private static final int GALLERY_REQUEST = 1;
    private ImageView profileImgView;
    private ImageView uploadImgView;
    private Uri mImageUri = null;
    private TextView profName;
    private TextView profEmail;
    //Database
    private DatabaseReference currentUserDb;
    private DatabaseReference currentUserName;
    private DatabaseReference currentUserEmail;
    private DatabaseReference currentUserImg;
    private FirebaseAuth mAuth;
    private StorageReference mStorageImage;
    private String currentUserId;
    private CardView btntoDo, btnEvent,btnmusic;
    //Button Declaration
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Close Login activity after signing up
        if (LoginActivity.instance != null) {
            try {
                LoginActivity.instance.finish();
            } catch (Exception e) {
            }
        }
        //Database
        FirebaseApp.initializeApp(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("sound");
        mAuth = FirebaseAuth.getInstance();
        currentUserDb = FirebaseDatabase.getInstance().getReference().child("User");
        currentUserId = mAuth.getCurrentUser().getUid();
        currentUserName = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId).child("name");
        currentUserEmail = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId).child("email");
        currentUserImg = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);
        mStorageImage = FirebaseStorage.getInstance().getReference().child("Profile_Images");
//------------------------------------------------------------------------------------------------------------------------------
        //Main Menu button
        btnEvent = findViewById(R.id.btn_event);
        btnmusic =findViewById(R.id.btn_music);
        //Add CLick Listener to the cards
        btnEvent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TaskListActivity.class);startActivity(i);
            }
        });
        btnmusic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SongList.class);startActivity(i);
            }
        });

//------------------------------------------------------------------------------------------------------------------------------
        //Navigation Drawer and Action Bar
        Toolbar toolbar = findViewById(R.id.toolbarid);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView mNavigationView = findViewById(R.id.nav_view);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case (R.id.nav_home):
                        mDrawerLayout.closeDrawers();
                }
                switch (menuItem.getItemId()) {
                    case (R.id.nav_about):
                        Intent aboutActivity = new Intent(getApplicationContext(), AboutUs.class);
                        startActivity(aboutActivity);
                }
                switch (menuItem.getItemId()) {
                    case (R.id.nav_signout):
                        mAuth.signOut();
                        Intent signoutActivity = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(signoutActivity);
                        finish();
                        Toast.makeText(MainActivity.this, "You have already signed out", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
//------------------------------------------------------------------------------------------------------------------------------
        //Navigation Header
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);
        profileImgView =  headerview.findViewById(R.id.profileImageView);
        uploadImgView  = headerview.findViewById(R.id.uploadImageView);
        profName = headerview.findViewById(R.id.profileNameField);
        profEmail = headerview.findViewById(R.id.profileEmailField);

        //Retrieve Name from Firebase
        currentUserName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue().toString();
                profName.setText(value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Retrieve Email from Firebase
        currentUserEmail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue().toString();
                profEmail.setText(value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        currentUserImg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String image = dataSnapshot.child("imageProfile").getValue().toString();
                Glide.with(getApplicationContext()).load(image).into(profileImgView);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        profileImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });
        uploadImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSetupAccount();
            }
        });
    }
    private void startSetupAccount() {
        final String user_id = mAuth.getCurrentUser().getUid();
        if(mImageUri != null){
            final StorageReference filepath = mStorageImage.child(currentUserId+ ".jpg");
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            currentUserDb.child(currentUserId).child("imageProfile").setValue(url);
                        }
                    });
                    //String downloadUri = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                }
            });
        }
        Toast.makeText(MainActivity.this, "Profile image updated.",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                mImageUri = result.getUri();
                profileImgView.setImageURI(mImageUri);
            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}