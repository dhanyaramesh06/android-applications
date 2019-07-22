package com.example.inclass03;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView avatar1, avatar2, avatar3,avatar4,avatar5,avatar6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        setTitle("Select Avatar");
         avatar1  = findViewById(R.id.avatar_1);
         avatar2 = findViewById(R.id.avatar_4);
         avatar3 = findViewById(R.id.avatar_2);
         avatar4 = findViewById(R.id.avatar_5);
         avatar5 = findViewById(R.id.avatar_3);
        avatar6 = findViewById(R.id.avatar_6);

        avatar1.setOnClickListener(this);
        avatar2.setOnClickListener(this);
        avatar3.setOnClickListener(this);
        avatar4.setOnClickListener(this);
        avatar5.setOnClickListener(this);
        avatar6.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        switch (v.getId())
        {
            case R.id.avatar_1:
                bundle.putInt("image",R.drawable.avatar_f_1);
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                break;
            case R.id.avatar_2:
                bundle.putInt("image",R.drawable.avatar_f_2);
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                break;

            case R.id.avatar_3:
                bundle.putInt("image",R.drawable.avatar_f_3);
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                break;
            case R.id.avatar_4:
                bundle.putInt("image",R.drawable.avatar_m_1);
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                break;
            case R.id.avatar_5:
                bundle.putInt("image",R.drawable.avatar_m_2);
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                break;
            case R.id.avatar_6:
                bundle.putInt("image",R.drawable.avatar_m_3);
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                break;
        }
        finish();

    }
}
