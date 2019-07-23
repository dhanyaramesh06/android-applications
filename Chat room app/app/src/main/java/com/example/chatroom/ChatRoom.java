package com.example.chatroom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class ChatRoom extends AppCompatActivity {

    MessageAdapter adapter;
    ArrayList<Message> messages;
    DatabaseReference myref;
    FirebaseDatabase database;
    FirebaseAuth mauth;
    FirebaseStorage storage;
    StorageReference storageRef,imageRef;
    Uri downloadUri;
    Message message;
    String key="",fname,lname,user_name;
    int uriflag = 0;
    private int GALLERY = 1;
    private static final String IMAGE_DIRECTORY = "/photos";
    Bitmap bitmap;
    ImageView addimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        setTitle("Chat Room");

        mauth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myref = database.getReference("Messages");
        storage= FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        final ListView listView = findViewById(R.id.messagelist);
        final EditText text = findViewById(R.id.type_msg);
        final TextView username = findViewById(R.id.name);
        addimage = findViewById(R.id.addimg_btn);

        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    try {
                        messages.add(data.getValue(Message.class));
                        Log.d("demo", "Message from Database: " + messages);
                    } catch (Exception e) {
                        Log.d("demo", "error " + e);
                    }
                }
                Log.d("demo", "already logged in" + messages);
                adapter = new MessageAdapter(getBaseContext(), R.layout.messagelist, messages);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       if(getIntent()!=null && getIntent().getExtras()!=null)
       {
           if(getIntent().getExtras().containsKey("username"))
           {
               user_name = (String) getIntent().getExtras().getSerializable("username");
               String[] str = user_name.split(" ");
               fname = str[0];
               lname = str[1];
               Log.d("demo", "User name: " + user_name);
               username.setText(user_name);
           }

           else
           {
               fname = (String) getIntent().getExtras().getSerializable("firstname");
               lname = (String) getIntent().getExtras().getSerializable("lastname");
               Log.d("demo", "first name: " + fname);
               Log.d("demo", "last name: " + lname);
               username.setText(fname+" "+lname);
           }
       }

        findViewById(R.id.sendmsg_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text.getText().length()>0 || uriflag == 1 )
                {
                    key = myref.push().getKey();
                    message = new Message();
                    message.setKey(key);
                    message.setMessage(text.getText().toString());
                    Date date = new Date();
                    message.setTime(date);
                    message.setFirst_name(fname);
                    message.setLast_name(lname);
                    if(uriflag == 1)
                    {
                        message.setImage_url(downloadUri.toString());
                    }
                    else
                        message.setImage_url("No image found");

                    myref.child(key).setValue(message);
                    text.setText(null);
                    uriflag = 0;
                    addimage.setImageResource(R.drawable.addimage);
                }
                else
                    Toast.makeText(ChatRoom.this, "Type a message or Select an image to post!", Toast.LENGTH_SHORT).show();
                
            }
        });

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mauth.signOut();
                Toast.makeText(ChatRoom.this, "Successfully logged out!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChatRoom.this,MainActivity.class);
                startActivity(intent);
            }
        });

        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoFromGallery();
            }
        });


    }
    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ChatRoom.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Log.d("demo", "Image Saved!");
                    addimage.setImageBitmap(bitmap);
                    uploadimage();


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }


        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getBaseContext(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public void uploadimage(){
        imageRef = storageRef.child("images/"+ UUID.randomUUID() +".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        final UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(ChatRoom.this, "Image Upload Unsuccessful", Toast.LENGTH_SHORT).show();
                Log.d("demo"," Unsuccessful Image Upload "+uploadTask.getException());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(ChatRoom.this, "Image Upload Successful", Toast.LENGTH_SHORT).show();
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
                            downloadUri = task.getResult();
                            uriflag = 1;
                            Log.d("demo","URL" + downloadUri);

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
