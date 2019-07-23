package com.example.inclass09;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUp_Fragment extends Fragment {

    EditText fname,lname,email,pwd,confirm_pwd;
    Button signup,cancel;
    SignUpInterface object;
    public SignUp_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            object =(SignUpInterface) getActivity();
        }
        catch (Exception e)
        {
            Log.d("demo","Exception occurred in SignUp Fragment"+e);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fname = getActivity().findViewById(R.id.fname);
        lname = getActivity().findViewById(R.id.lname);
        pwd = getActivity().findViewById(R.id.password);
        confirm_pwd = getActivity().findViewById(R.id.confirm_password);
        email = getActivity().findViewById(R.id.email);
        signup = getActivity().findViewById(R.id.signup);
        cancel = getActivity().findViewById(R.id.cancel);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty() || pwd.getText().toString().isEmpty() || confirm_pwd.getText().toString().isEmpty() || fname.getText().toString().isEmpty() || lname.getText().toString().isEmpty())
                {
                    Toast.makeText(getActivity(), "Fill in details", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    object.onClickSignUp(email.getText().toString(),pwd.getText().toString(),confirm_pwd.getText().toString(),fname.getText().toString(),lname.getText().toString());
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object.onCancel();
            }
        });
    }
    public interface SignUpInterface{
        void onCancel();
        void onClickSignUp(String email, String password, String confirm_password, String fname, String lname);
    }
}
