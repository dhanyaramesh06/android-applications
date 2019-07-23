/*
InClass 09
Chella Archana Kandaswamy - 801085762
Dhanya Ramesh - 801073179
 */

package com.example.inclass09;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements Login_Fragment.LoginInterface, SignUp_Fragment.SignUpInterface, Contact_Fragment.ContactInterface, Create_new_Fragment.CreateNewFragmentInterface {

    FirebaseAuth mauth;
    DatabaseReference myref;
    FirebaseDatabase database;
    FirebaseUser user;
    FirebaseStorage storage;
    StorageReference storageRef,imageRef;
    String key="";
    ArrayList<Contact> contactList;
    Contact_Fragment contact_fragment = new Contact_Fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login");
        getSupportFragmentManager().beginTransaction().add(R.id.main_activity,new Login_Fragment(),"login").commit();
        mauth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        storage= FirebaseStorage.getInstance();
        storageRef = storage.getReference();

    }

    @Override
    public void onLogin(String email, String password) {
        mauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                   Log.d("demo","Login Sucessful");
                   user = mauth.getInstance().getCurrentUser();
                   if(user!=null){
                       myref = database.getReference(user.getUid());

                       myref.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               contactList = new ArrayList<>();
                               for(DataSnapshot data :dataSnapshot.getChildren()){
                                   try {
                                       contactList.add(data.getValue(Contact.class));
                                   }catch(Exception e)
                                   {
                                        Log.d("demo","error "+e);
                                   }
                               }
                               contact_fragment = new Contact_Fragment();
                               Bundle b =new Bundle();
                               b.putSerializable("contacts",contactList);
                               Log.d("demo","ContactList "+contactList);
                               contact_fragment.setArguments(b);
                               setTitle("Contacts");
                               getSupportFragmentManager().beginTransaction().replace(R.id.main_activity,contact_fragment,"contact").commit();
                           }
                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });
                   }
                   else{
                       Toast.makeText(MainActivity.this, "Login UnSuccessful", Toast.LENGTH_SHORT).show();
                       Log.d("demo","Login UnSucessful");
                   }
               }
               else{
                   Toast.makeText(MainActivity.this, "Login UnSuccessful", Toast.LENGTH_SHORT).show();
                   Log.d("demo","Login UnSucessful");
               }
            }
        });
    }



    @Override
    public void onSignUp() {
        setTitle("Sign Up");
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity,new SignUp_Fragment(),"signup").commit();
    }

    @Override
    public void onCancel() {
        setTitle("Login");
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity,new Login_Fragment(),"login").commit();
    }

    @Override
    public void onClickSignUp(String email, String password, String confirm_password, final String fname, final String lname) {
        if(password.equals(confirm_password)){
            FirebaseUser currentUser = mauth.getCurrentUser();
            mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        user = mauth.getCurrentUser();
                        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(fname+" "+lname).build();
                        user.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    setTitle("Contacts");
                                    getSupportFragmentManager().beginTransaction().replace(R.id.main_activity,contact_fragment,"contact").commit();
                                    Toast.makeText(MainActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        //Log.d("demo","task Exception "+task.getException());
                        Toast.makeText(MainActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(MainActivity.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onCreateContact() {
        setTitle("Create New Contact");
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity, new Create_new_Fragment(),"new contact").commit();
    }

    @Override
    public void onSignOut() {
        setTitle("Login");
        mauth.signOut();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity,new Login_Fragment(),"login").commit();
    }

    @Override
    public void onLongClick(Contact c) {
        myref = database.getReference(user.getUid()).child(c.key);
        myref.removeValue();
        myref = database.getReference(user.getUid());
    }

    @Override
    public void onSubmit(final Contact c, ImageView image) {
        //contactList = new ArrayList<>();
        myref = database.getReference(user.getUid());
        key = myref.push().getKey();
        c.setKey(key);
        //myref.child(key).setValue(c);
        imageRef = storageRef.child("images/"+key+".jpg");
        image.setDrawingCacheEnabled(true);
        image.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        final UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(MainActivity.this, "Image Upload Unsuccessful", Toast.LENGTH_SHORT).show();
                Log.d("demo"," Unsuccessful Image Upload "+uploadTask.getException());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(MainActivity.this, "Image Upload Successful", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
                {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            Log.d("demo","Task is Unsuccessful"+task.getException());
                        }
                        Log.d("demo","download url "+imageRef.getDownloadUrl());
                        return imageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            Log.d("demo","inside on complete listener ");
                            Uri downloadUri = task.getResult();
                            c.setImage(downloadUri.toString());
                            Log.d("demo","URL" + downloadUri);
                            myref.child(key).setValue(c);
                            //contactList.add(c);

                            myref.addValueEventListener(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                {
                                    contactList = new ArrayList<>();
                                    for(DataSnapshot data :dataSnapshot.getChildren())
                                    {
                                        try
                                        {
                                            contactList.add(data.getValue(Contact.class));
                                        }catch(Exception e)
                                        {
                                            Log.d("demo","error "+e);
                                        }
                                    }
                                    contact_fragment = new Contact_Fragment();
                                    Bundle b =new Bundle();
                                    b.putSerializable("contacts",contactList);
                                    Log.d("demo","ContactList "+contactList);
                                    contact_fragment.setArguments(b);
                                    setTitle("Contacts");
                                    getSupportFragmentManager().beginTransaction().replace(R.id.main_activity, contact_fragment,"contact").commit();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        else
                            {
                            Log.d("demo"," Error in Downloading the image");
                        }
                    }
                });
            }
        });
    }
}
