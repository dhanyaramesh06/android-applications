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
public class Login_Fragment extends Fragment {


    EditText email,password;
    Button login,signup;
    LoginInterface object;
    public Login_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            object = (LoginInterface) getActivity();
        }
        catch (Exception e)
        {
            Log.d("demo","Exception in Login Fragment" +e);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        email = getActivity().findViewById(R.id.email);
        password = getActivity().findViewById(R.id.password);
        login = getActivity().findViewById(R.id.login);
        signup = getActivity().findViewById(R.id.signup);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty())
                {
                    Toast.makeText(getActivity(), "Enter valid credentials", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    object.onLogin(email.getText().toString(),password.getText().toString());
                }

                //Toast.makeText(getContext(), "Login button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object.onSignUp();
            }
        });
    }
    public  interface LoginInterface{
        void onLogin(String email, String password);
        void onSignUp();

    }
}
