package com.guards.attendance.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.guards.attendance.FrameActivity;
import com.guards.attendance.R;
import com.guards.attendance.toolbox.ToolbarListener;
import com.guards.attendance.utils.ActivityUtils;

/**
 * Created by Bilal Rashid on 1/18/2018.
 */

public class LoginFragment extends Fragment implements View.OnClickListener{
    private ViewHolder mHolder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Login",true);
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHolder = new ViewHolder(view);
        mHolder.loginButton.setOnClickListener(this);
        mHolder.signupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                Login();
                break;
            case R.id.button_signup:
//                signup screen
                break;
        }

    }
    public void Login(){
        if (mHolder.usernameEditText.getText().toString().length() < 5) {
            mHolder.inputLayoutUsername.setErrorEnabled(true);
            mHolder.inputLayoutUsername.setError("Invalid Username");
            return;
        }
        mHolder.inputLayoutUsername.setError(null);
        mHolder.inputLayoutUsername.setErrorEnabled(false);
        if (mHolder.passwordEditText.getText().toString().length() < 5) {
            mHolder.inputLayoutPassword.setErrorEnabled(true);
            mHolder.inputLayoutPassword.setError("Invalid Password");
            return;
        }
        mHolder.inputLayoutPassword.setError(null);
        mHolder.inputLayoutPassword.setErrorEnabled(false);
    }
    public static class ViewHolder {
        TextInputEditText usernameEditText;
        TextInputEditText passwordEditText;
        Button loginButton;
        Button signupButton;
        TextInputLayout inputLayoutUsername;
        TextInputLayout inputLayoutPassword;

        public ViewHolder(View view) {
            usernameEditText = (TextInputEditText) view.findViewById(R.id.edit_text_username);
            passwordEditText = (TextInputEditText) view.findViewById(R.id.edit_text_password);
            inputLayoutUsername= (TextInputLayout) view.findViewById(R.id.input_layout_username);
            inputLayoutPassword = (TextInputLayout) view.findViewById(R.id.input_layout_password);
            loginButton = (Button) view.findViewById(R.id.button_login);
            signupButton = (Button) view.findViewById(R.id.button_signup);
        }

    }
}
