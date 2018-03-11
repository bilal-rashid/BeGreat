package com.guards.attendance.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.guards.attendance.R;
import com.guards.attendance.models.User;
import com.guards.attendance.toolbox.ToolbarListener;
import com.guards.attendance.utils.AppUtils;
import com.guards.attendance.utils.GsonUtils;
import com.guards.attendance.utils.LoginUtils;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Bilal Rashid on 1/20/2018.
 */

public class SignupFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private ViewHolder mHolder;
    String[] objects = { "Supervisor","Guard" };
    ArrayAdapter<String> arrayAdapter;
    boolean is_supervisor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Signup", false);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    String dir;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHolder = new ViewHolder(view);
        mHolder.userImage.setOnClickListener(this);
        mHolder.signupButton.setOnClickListener(this);
        arrayAdapter =  new ArrayAdapter(
                getContext(),android.R.layout.simple_list_item_1,objects);
        mHolder.userTypeSpinner.setAdapter(arrayAdapter);
        mHolder.userTypeSpinner.setOnItemSelectedListener(this);
        dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/BeGreat/";
        User user = LoginUtils.getUser(getContext());
        if(user != null){
            mHolder.empEditText.setText(user.employee_code);
//            mHolder.empEditText.setEnabled(false);
            mHolder.usernameEditText.setText(user.username);
//            mHolder.usernameEditText.setEnabled(false);
            if(user.is_supervisor)
                mHolder.userTypeSpinner.setSelection(0);
            else
                mHolder.userTypeSpinner.setSelection(1);
//            mHolder.userTypeSpinner.setEnabled(false);
        }
        is_supervisor = true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_signup:
                Signup();
                break;
            case R.id.image_user:
                mHolder.userImage.setImageResource(R.drawable.user);
                handleCamera();
                break;
        }

    }
    private static final int MY_WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 3;

    public void handleCamera() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
            return;
        }
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.CAMERA},
                    MY_WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
            return;
        }

        File newdir = new File(dir);
        newdir.mkdirs();
        String file = dir + "Guard_Profile.jpg";
        File newfile = new File(file);
        try {
            newfile.createNewFile();
        } catch (IOException e) {
        }

        Uri outputFileUri = Uri.fromFile(newfile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(cameraIntent, 420);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 420 && resultCode == RESULT_OK) {
            String filepath = dir + "Guard_Profile.jpg";
            Matrix matrix = new Matrix();
            matrix.postRotate(AppUtils.getImageOrientation(filepath));
            Bitmap bitmap = BitmapFactory.decodeFile(filepath);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
            mHolder.userImage.setImageBitmap(rotatedBitmap);
        } else {
            AppUtils.showSnackBar(getView(), getString(R.string.err_image_not_selected));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    handleCamera();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    AppUtils.showSnackBar(getView(), getString(R.string.err_permission_not_granted));
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void Signup() {
        if (mHolder.empEditText.getText().toString().length() < 1) {
            mHolder.inputLayoutEmp.setErrorEnabled(true);
            mHolder.inputLayoutEmp.setError("Please Enter Employee code");
            return;
        }
        mHolder.inputLayoutEmp.setError(null);
        mHolder.inputLayoutEmp.setErrorEnabled(false);
        if (mHolder.usernameEditText.getText().toString().length() < 5) {
            mHolder.inputLayoutUsername.setErrorEnabled(true);
            mHolder.inputLayoutUsername.setError("Username must be at least 5 characters long");
            return;
        }
        mHolder.inputLayoutUsername.setError(null);
        mHolder.inputLayoutUsername.setErrorEnabled(false);
        if (mHolder.passwordEditText.getText().toString().length() < 5) {
            mHolder.inputLayoutPassword.setErrorEnabled(true);
            mHolder.inputLayoutPassword.setError("Password must be at least 5 characters long");
            return;
        }
        mHolder.inputLayoutPassword.setError(null);
        mHolder.inputLayoutPassword.setErrorEnabled(false);
        String filepath = dir + "Guard_Profile.jpg";
        User user = new User(mHolder.usernameEditText.getText().toString(),
                mHolder.passwordEditText.getText().toString(),
                mHolder.empEditText.getText().toString(), filepath,is_supervisor);
        LoginUtils.saveUser(getContext(), user);
        if (is_supervisor)
            Toast.makeText(getContext(), "Supervisor Registered", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getContext(), "Guard Registered", Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        try {
            String filepath = dir + "Guard_Profile.jpg";
            Matrix matrix = new Matrix();
            matrix.postRotate(AppUtils.getImageOrientation(filepath));
            Bitmap bitmap = BitmapFactory.decodeFile(filepath);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
            mHolder.userImage.setImageBitmap(rotatedBitmap);
        } catch (Exception e) {

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(i==0){
            is_supervisor = true;
        }else {
            is_supervisor = false;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        is_supervisor = true;
    }

    public static class ViewHolder {

        TextInputEditText usernameEditText;
        TextInputEditText passwordEditText;
        TextInputEditText empEditText;
        Button signupButton;
        TextInputLayout inputLayoutUsername;
        TextInputLayout inputLayoutPassword;
        TextInputLayout inputLayoutEmp;
        ImageView userImage;
        AppCompatSpinner userTypeSpinner;

        public ViewHolder(View view) {
            usernameEditText = (TextInputEditText) view.findViewById(R.id.edit_text_username);
            passwordEditText = (TextInputEditText) view.findViewById(R.id.edit_text_password);
            empEditText = (TextInputEditText) view.findViewById(R.id.edit_text_emp_code);
            inputLayoutUsername = (TextInputLayout) view.findViewById(R.id.input_layout_username);
            inputLayoutPassword = (TextInputLayout) view.findViewById(R.id.input_layout_password);
            inputLayoutEmp = (TextInputLayout) view.findViewById(R.id.input_layout_emp_code);
            signupButton = (Button) view.findViewById(R.id.button_signup);
            userImage = (ImageView) view.findViewById(R.id.image_user);
            userTypeSpinner = (AppCompatSpinner) view.findViewById(R.id.spinner_usertype);
        }

    }
}
