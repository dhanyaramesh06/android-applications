/*
Homework 5
Chella Archana Kandaswamy (801085762)
Dhanya Ramesh (801073179)
*/

package com.example.expensemanager;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements MainScreenFragment.GotoAddFragment, AddExpenseFragment.AddExpenseList, EditExpenseFragment.EditExpenseInterface, DisplayExpenseFragment.DisplayInterface {

    ArrayList<Expense> expense_list =new ArrayList<>();
    String key, image_url;
    MainScreenFragment mainScreenFragment;
    EditExpenseFragment editExpenseFragment;
    DisplayExpenseFragment displayExpenseFragment;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseStorage storage;
    StorageReference storageRef,imageRef;
    Expense e;
    AlertDialog dialog;
    Button yes,no;
    double total =0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_main);
        setTitle("Expense Manager");
        getSupportFragmentManager().beginTransaction().add(R.id.main_activity,new MainScreenFragment(),"main").commit();

        storage= FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("expenses");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                expense_list = new ArrayList<>();
                for(DataSnapshot d:dataSnapshot.getChildren())
                {
                    expense_list.add(d.getValue(Expense.class));
                }


                Collections.sort(expense_list, new Comparator<Expense>() {
                    @Override
                    public int compare(Expense o1, Expense o2) {
                        String str_date1 = "";
                        String str_date2 = "";
                        SimpleDateFormat sdf=new SimpleDateFormat("MMM dd, yyyy");
                        SimpleDateFormat sdf1=new SimpleDateFormat("MMM dd, yyyy");
                        try{

                            Date date1=sdf.parse(o1.date);
                            Log.d("demo","Datee 1: "+date1 );
                            sdf=new SimpleDateFormat("dd/MM/yyyy");
                            str_date1 = sdf.format(date1);
                            Log.d("demo","Date string 1: "+str_date1 );

                            Date date2=sdf1.parse(o2.date);
                            sdf1=new SimpleDateFormat("dd/MM/yyyy");
                            str_date2 = sdf1.format(date2);
                            Log.d("demo","Date string 2: "+str_date2 );
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        return str_date1.compareTo(str_date2);
                    }
                });
                mainScreenFragment = new MainScreenFragment();
                Bundle b = new Bundle();
                Log.d("demo","Expenses before expense going to Main Fragment "+expense_list);
                b.putSerializable("list",expense_list);
                b.putSerializable("total",total);
                mainScreenFragment.setArguments(b);
                setTitle("Expense Manager");
                getSupportFragmentManager().beginTransaction().replace(R.id.main_activity,mainScreenFragment,"main").commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu,menu);
        Drawable yourdrawable = menu.getItem(0).getIcon(); // change 0 with 1,2 ...
        yourdrawable.mutate();
        yourdrawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.subitem1:{
                //Collections.sort(expense_list,Expense.Comparators.COST);
                Collections.sort(expense_list, new Comparator<Expense>() {
                    @Override
                    public int compare(Expense o1, Expense o2) {
                        return Double.compare(o1.getCost(),o2.getCost());
                    }
                });
                Toast.makeText(this, "Sort by Cost selected", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.subitem2:
                {
                Collections.sort(expense_list, new Comparator<Expense>() {
                    @Override
                    public int compare(Expense o1, Expense o2) {
                        String str_date1 = "";
                        String str_date2 = "";
                        SimpleDateFormat sdf=new SimpleDateFormat("MMM dd, yyyy");
                        SimpleDateFormat sdf1=new SimpleDateFormat("MMM dd, yyyy");
                        try{

                            Date date1=sdf.parse(o1.date);
                            Log.d("demo","Datee 1: "+date1 );
                            sdf=new SimpleDateFormat("dd/MM/yyyy");
                            str_date1 = sdf.format(date1);
                            Log.d("demo","Date string 1: "+str_date1 );

                            Date date2=sdf1.parse(o2.date);
                            sdf1=new SimpleDateFormat("dd/MM/yyyy");
                            str_date2 = sdf1.format(date2);
                            Log.d("demo","Date string 2: "+str_date2 );
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        return str_date1.compareTo(str_date2);
                    }
                });
                Toast.makeText(this, "Sort by Date selected", Toast.LENGTH_SHORT).show();
                return true;
            }

            case R.id.subitem3:
            {
                //Button yes, no;
                final AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.alert, null);
                pictureDialog.setView(dialogLayout);
                dialog = pictureDialog.create();
                yes = dialogLayout.findViewById(R.id.button2);
                no = dialogLayout.findViewById(R.id.button);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myRef.setValue(null);
                        dialog.cancel();

                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }

                Toast.makeText(this, "Reset all selected", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addExpense() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity,new AddExpenseFragment(),"add").commit();
    }

    @Override
    public void addToExpenses(String name, String date, double cost, ImageView image) {
        addImage(image, name, date, cost);
        Log.d("demo","before add image call");
        Log.d("demo","expense "+e);
        Log.d("demo","In Main Activity" + name+date+cost);
    }

    public void addImage(ImageView image, String name, String date, double cost) {
        Log.d("demo","inside add image ");
        e = new Expense();
        e.setName(name);
        e.setDate(date);
        e.setCost(cost);
        key = myRef.push().getKey();
        e.setKey(key);
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
                Log.d("demo"," Unscucessful Image Upload "+uploadTask.getException());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(MainActivity.this, "Image Upload Successful", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            Log.d("demo","Task is Unsuccessful"+task.getException());
                        }
                        Log.d("demo","download url "+imageRef.getDownloadUrl());
                        return imageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Log.d("demo","inside on complete listener ");
                            Uri downloadUri = task.getResult();
                            image_url = downloadUri.toString();
                            e.setImage_url(image_url);
                            Log.d("demo","URL" + downloadUri);
                            expense_list.add(e);
                            myRef.child(key).setValue(e);
                        }else{
                            Log.d("demo"," Error in Downloading the image");
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onEdit(Expense e) {
        Bundle b = new Bundle();
        b.putSerializable("expense",e);
        editExpenseFragment =new EditExpenseFragment();
        editExpenseFragment.setArguments(b);
        Log.d("demo","inside on edit method--"+e);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity,editExpenseFragment,"edit").commit();
    }



    @Override
    public void saveChanges(Expense e, ImageView image) {
        final Expense expense = e;
        imageRef = storageRef.child("images/"+expense.getKey()+".jpg");
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
                //Toast.makeText(MainActivity.this, "Image Upload Unsuccessful", Toast.LENGTH_SHORT).show();
                Log.d("demo"," Unsuccessful Image Upload "+uploadTask.getException());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(MainActivity.this, "Image Upload Successful", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            Log.d("demo","Task is Unsuccessful"+task.getException());
                        }
                        Log.d("demo","download url "+imageRef.getDownloadUrl());
                        return imageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Log.d("demo","inside on complete listener ");
                            Uri downloadUri = task.getResult();
                            image_url = downloadUri.toString();
                            expense.setImage_url(image_url);
                            Log.d("demo","URL" + downloadUri);
                            myRef.child(expense.getKey()).setValue(expense);
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_activity,mainScreenFragment,"main").commit();
                        }else{
                            Log.d("demo"," Error in Downloading the image");
                        }
                    }
                });
            }
        });
    }

    @Override
    public void display(Expense e) {
        Log.d("demo","Expense object in for display: "+e);
        displayExpenseFragment = new DisplayExpenseFragment();
        Bundle b = new Bundle();
        Log.d("demo","Expenses before expense going to Display Fragment "+e);
        b.putSerializable("expense",e);
        displayExpenseFragment.setArguments(b);
        setTitle("Expense Manager");
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity,displayExpenseFragment,"display").commit();
    }

    @Override
    public void goToMainScreen() {
        setTitle("Expense Manager");
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity,mainScreenFragment,"main").commit();
    }

}
