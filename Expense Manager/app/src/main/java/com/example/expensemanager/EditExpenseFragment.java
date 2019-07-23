package com.example.expensemanager;


import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditExpenseFragment extends Fragment {

    TextView name,cost,date;
    Button date_btn,img_btn,save;
    ImageView img;
    Expense expense;
    private DatePickerDialog.OnDateSetListener mlistener;
    Calendar calendar;
    int year, month,dayOfMonth;
    String mon;
    EditExpenseInterface obj;
    boolean cam_flag = false;
    boolean gallery_flag=false;
    boolean always_flag = false;
    private int GALLERY = 1, CAMERA = 2;
    private static final String IMAGE_DIRECTORY = "/photos";
    Bitmap bitmap;

    public EditExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_expense, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            obj = (EditExpenseInterface) getActivity();
        }
        catch (Exception e){
            Log.d("demo","Exception occurred in Add Expense Fragment"+e);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        name = getActivity().findViewById(R.id.name);
        date = getActivity().findViewById(R.id.date);
        cost = getActivity().findViewById(R.id.cost);
        img = getActivity().findViewById(R.id.image);
        date_btn = getActivity().findViewById(R.id.date_btn);
        save = getActivity().findViewById(R.id.save_btn);
        img_btn = getActivity().findViewById(R.id.img_btn);

        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,mlistener,year,month,dayOfMonth);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }

        });
        mlistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String[] month_name = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                mon = month_name[month];
                String d = mon+" "+dayOfMonth+", "+year;
                date.setText(d);
            }
        };

        Bundle bundle = this.getArguments();
        if(bundle!=null) {
            Log.d("demo", "In edit fragment");
            expense = (Expense)(bundle.getSerializable("expense"));
            name.setText(expense.getName());
            date.setText(expense.getDate());
            cost.setText(String.valueOf(expense.getCost()));
            Picasso.get().load(expense.getImage_url()).into(img);
            Log.d("demo","key value in edit:"+expense.getKey());
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty() || cost.getText().toString().isEmpty() || date.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Fill in the details", Toast.LENGTH_SHORT).show();
                }
                else {
                    expense.setName(name.getText().toString());
                    expense.setDate(date.getText().toString());
                    expense.setCost(Double.parseDouble(cost.getText().toString()));
                    Log.d("demo", "key value after edit:" + expense.getKey());
                    obj.saveChanges(expense, img);
                }
            }
        });

        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });


    }

    private void showPictureDialog(){
        final AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getContext());
        pictureDialog.setTitle("Complete action using?");
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.activity_alert, null);
        pictureDialog.setView(dialogLayout);
        pictureDialog.setNegativeButton("Always", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                always_flag = true;
                if(cam_flag){
                    takePhotoFromCamera();
                }
                else if(gallery_flag){
                    choosePhotoFromGallery();
                }
            }
        });
        pictureDialog.setPositiveButton("Just Once", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(cam_flag){
                    takePhotoFromCamera();
                    Log.d("demo",""+which);
                }
                else if(gallery_flag){
                    choosePhotoFromGallery();
                    Log.d("demo",""+which);
                }

                requestMultiplePermissions();
            }
        });
        final ImageView camera = dialogLayout.findViewById(R.id.imageView4);
        final ImageView gallery = dialogLayout.findViewById(R.id.imageView5);

        camera.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gallery_flag=false;
                cam_flag = true;
                return cam_flag;
            }
        });

        gallery.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                cam_flag=false;
                gallery_flag= true;
                return gallery_flag;
            }
        });
        if(!always_flag) {
            pictureDialog.show();

        }
        else{
            if(cam_flag){
                takePhotoFromCamera();
            }
            else if(gallery_flag){
                choosePhotoFromGallery();
            }
        }
    }
    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Log.d("demo","Image Saved in bitmap");
                    //Toast.makeText(getContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    img.setImageBitmap(bitmap);
                    img.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("demo","Image  not Saved in bitmap");
                    //Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            bitmap = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(bitmap);
            img.setVisibility(View.VISIBLE);
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

    private void  requestMultiplePermissions(){
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Log.d("demo","All permissions are granted by user!");
                            //Toast.makeText(getContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }


                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Log.d("demo","Some Error!");
                        //Toast.makeText(getContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();

    }

    public interface EditExpenseInterface{
        void saveChanges(Expense e, ImageView img);
    }
}
