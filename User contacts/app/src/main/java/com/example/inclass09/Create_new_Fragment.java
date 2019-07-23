package com.example.inclass09;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class Create_new_Fragment extends Fragment {

    EditText name,email,phone;
    ImageButton camera;
    ImageView imageView;
    CreateNewFragmentInterface object;
    Contact contact ;
    private int CAMERA = 1;
    private static final String IMAGE_DIRECTORY = "/photos";
    Bitmap bitmap;

    public Create_new_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_new, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            object =(CreateNewFragmentInterface) getActivity();
        }catch (Exception e){
            Log.d("demo","Exception in Create New Fragment"+e);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        name=getActivity().findViewById(R.id.name);
        email = getActivity().findViewById(R.id.email);
        phone = getActivity().findViewById(R.id.phone);
        imageView = getActivity().findViewById(R.id.imageView);
        imageView.setVisibility(View.INVISIBLE);
        contact=new Contact();
        getActivity().findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact.setName(name.getText().toString()); ;
                contact.setEmail(email.getText().toString());
                contact.setPhone(phone.getText().toString());
                Log.d("demo","In create new contact:"+contact);
                if(name.getText().toString().isEmpty() || email.getText().toString().isEmpty() || phone.getText().toString().isEmpty())
                {
                    Toast.makeText(getActivity(), "Fill in details", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    object.onSubmit(contact, imageView);
                }

            }
        });
        getActivity().findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == CAMERA) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
            saveImage(bitmap);
            Log.d("demo","Image Saved in bitmap");
            //Toast.makeText(getContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
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
            MediaScannerConnection.scanFile(getContext(),
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

    public  interface CreateNewFragmentInterface{
        void onSubmit(Contact c, ImageView image);
    }


}
