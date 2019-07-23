package com.example.inclass10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayNote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_note);
        setTitle("Display Note");

        TextView display_note = findViewById(R.id.note);
        Button close = findViewById(R.id.close_btn);

        if(getIntent() != null && getIntent().getExtras() != null)
        {
            NotePojo obj = (NotePojo) (getIntent().getExtras().getSerializable("display"));
            display_note.setText(obj.note);
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayNote.this,Notes.class);
                startActivity(intent);
            }
        });


    }
}
