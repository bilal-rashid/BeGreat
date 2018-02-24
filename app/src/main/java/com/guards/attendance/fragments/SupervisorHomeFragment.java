package com.guards.attendance.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.guards.attendance.FrameActivity;
import com.guards.attendance.R;
import com.guards.attendance.dialog.SimpleDialog;
import com.guards.attendance.models.User;
import com.guards.attendance.toolbox.ToolbarListener;
import com.guards.attendance.utils.ActivityUtils;
import com.guards.attendance.utils.AppUtils;
import com.guards.attendance.utils.AttendanceUtils;
import com.guards.attendance.utils.LoginUtils;

/**
 * Created by Bilal Rashid on 2/24/2018.
 */

public class SupervisorHomeFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private ViewHolder mHolder;
    private User mUser;
    private SimpleDialog mSimpleDialog;
    private static final int MY_SMS_REQ_CODE_EMERGENCY = 3;
    private static final int MY_SMS_REQ_CODE_CHECKIN = 4;
    private static final int MY_SMS_REQ_CODE_CHECKOUT = 5;
    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Supervisor", true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_supervisor_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUser = LoginUtils.getUser(getContext());
        mHolder = new ViewHolder(view);
        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(AppUtils.getImageOrientation(mUser.image_path));
            Bitmap bitmap = BitmapFactory.decodeFile(mUser.image_path);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
            mHolder.profileImage.setImageBitmap(rotatedBitmap);
        } catch (Exception e) {
            mHolder.profileImage.setImageResource(R.drawable.user);
        }
        mHolder.empCodeText.setText(mUser.employee_code);
        mHolder.usernameText.setText(mUser.username);
        mHolder.checkinCard.setOnTouchListener(this);
        mHolder.checkoutCard.setOnTouchListener(this);
        mHolder.logoutCard.setOnTouchListener(this);

        mHolder.checkinCard.setOnClickListener(this);
        mHolder.checkoutCard.setOnClickListener(this);
        mHolder.logoutCard.setOnClickListener(this);
        if (AttendanceUtils.isSupervisorCheckin(getContext())) {
            mHolder.checkinCard.setEnabled(false);
            mHolder.checkinCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grey));
            mHolder.checkoutCard.setEnabled(true);
            mHolder.checkoutCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.card_checkout_color));
            mHolder.inputLayoutComment.setEnabled(true);
            mHolder.commentEditText.setEnabled(true);
            mHolder.inputLayoutComment.setHintEnabled(true);
        } else {
            mHolder.checkinCard.setEnabled(true);
            mHolder.checkinCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.card_checkin_color));
            mHolder.checkoutCard.setEnabled(false);
            mHolder.checkoutCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grey));
            mHolder.inputLayoutComment.setEnabled(false);
            mHolder.commentEditText.setEnabled(false);
            mHolder.inputLayoutComment.setHintEnabled(false);
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Your GPS seems to be disabled, please enable it to proceed")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onClick(View view) {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        switch (view.getId()) {
            case R.id.card_logout:
                mSimpleDialog = new SimpleDialog(getContext(), null, getString(R.string.msg_logout),
                        getString(R.string.button_cancel), getString(R.string.button_ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.button_positive:
                                LoginUtils.logout(getContext());
                                ActivityUtils.startHomeActivity(getContext(), FrameActivity.class, null);
                                mSimpleDialog.dismiss();
                                break;
                            case R.id.button_negative:
                                mSimpleDialog.dismiss();
                                break;
                        }
                    }
                });
                mSimpleDialog.show();
                break;
            case R.id.card_checkin:
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                } else {

                    mSimpleDialog = new SimpleDialog(getContext(), null, getString(R.string.msg_checkin),
                            getString(R.string.button_cancel), getString(R.string.button_ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switch (view.getId()) {
                                case R.id.button_positive:
                                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                                        mSimpleDialog.dismiss();
                                        requestPermissions(
                                                new String[]{Manifest.permission.SEND_SMS},
                                                MY_SMS_REQ_CODE_CHECKIN);
                                    } else {
                                        mHolder.inputLayoutComment.setEnabled(true);
                                        mHolder.commentEditText.setEnabled(true);
                                        mHolder.inputLayoutComment.setHintEnabled(true);
                                        mHolder.checkinCard.setEnabled(false);
                                        mHolder.checkinCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grey));
                                        mHolder.checkoutCard.setEnabled(true);
                                        mHolder.checkoutCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.card_checkout_color));
                                        AttendanceUtils.checkinSupervisor(getContext());
                                        startLocationUpdates();
                                        mSimpleDialog.dismiss();
                                    }
                                    break;
                                case R.id.button_negative:
                                    mSimpleDialog.dismiss();
                                    break;
                            }
                        }
                    });
                    mSimpleDialog.show();
                }
                break;
            case R.id.card_checkout:
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                } else {
                    mSimpleDialog = new SimpleDialog(getContext(), null, getString(R.string.msg_checkout),
                            getString(R.string.button_cancel), getString(R.string.button_ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switch (view.getId()) {
                                case R.id.button_positive:
                                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                                        mSimpleDialog.dismiss();
                                        requestPermissions(
                                                new String[]{Manifest.permission.SEND_SMS},
                                                MY_SMS_REQ_CODE_CHECKOUT);
                                    } else {
                                        mHolder.inputLayoutComment.setEnabled(false);
                                        mHolder.commentEditText.setEnabled(false);
                                        mHolder.inputLayoutComment.setHintEnabled(false);
                                        mHolder.checkinCard.setEnabled(true);
                                        mHolder.checkinCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.card_checkin_color));
                                        mHolder.checkoutCard.setEnabled(false);
                                        mHolder.checkoutCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grey));
                                        AttendanceUtils.checkoutSupervisor(getContext());
                                        startLocationUpdates();
                                        mSimpleDialog.dismiss();
                                    }
                                    break;
                                case R.id.button_negative:
                                    mSimpleDialog.dismiss();
                                    break;
                            }
                        }
                    });
                    mSimpleDialog.show();
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.card_logout:
                animate(view, motionEvent, ContextCompat.getColor(getActivity(), R.color.card_logout_color),
                        ContextCompat.getColor(getActivity(), R.color.card_logout_color_pressed));
                break;
            case R.id.card_checkin:
                animate(view, motionEvent, ContextCompat.getColor(getActivity(), R.color.card_checkin_color),
                        ContextCompat.getColor(getActivity(), R.color.card_checkin_color_pressed));
                break;
            case R.id.card_checkout:
                animate(view, motionEvent, ContextCompat.getColor(getActivity(), R.color.card_checkout_color),
                        ContextCompat.getColor(getActivity(), R.color.card_checkout_color_pressed));
                break;
        }
        return false;
    }

    private void animate(View view, MotionEvent motionEvent, int color, int colorPressed) {
        CardView cardView = (CardView) view;
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP:
                cardView.setCardBackgroundColor(color);
                break;
            case MotionEvent.ACTION_CANCEL:
                cardView.setCardBackgroundColor(color);
                break;
            case MotionEvent.ACTION_DOWN:
                cardView.setCardBackgroundColor(colorPressed);
                break;
            case MotionEvent.ACTION_MOVE:
                cardView.setCardBackgroundColor(colorPressed);
                break;

        }
    }

    public static class ViewHolder {
        ImageView profileImage;
        TextView usernameText, empCodeText;
        CardView checkinCard, checkoutCard, logoutCard;
        TextInputEditText commentEditText;
        TextInputLayout inputLayoutComment;

        public ViewHolder(View view) {
            profileImage = (ImageView) view.findViewById(R.id.image_profile);
            usernameText = (TextView) view.findViewById(R.id.text_user_name);
            empCodeText = (TextView) view.findViewById(R.id.text_emp_id);

            checkinCard = (CardView) view.findViewById(R.id.card_checkin);
            checkoutCard = (CardView) view.findViewById(R.id.card_checkout);
            logoutCard = (CardView) view.findViewById(R.id.card_logout);
            commentEditText = (TextInputEditText) view.findViewById(R.id.edit_text_comment);
            inputLayoutComment = (TextInputLayout) view.findViewById(R.id.input_layout_comment);
        }

    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_SMS_REQ_CODE_CHECKIN: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mHolder.checkinCard.setEnabled(false);
                    mHolder.checkinCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grey));
                    AttendanceUtils.checkinGuard(getContext());
                    AttendanceUtils.sendCheckin(getContext());
                    AppUtils.startPulse(getContext());
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    AppUtils.showSnackBar(getView(), getString(R.string.err_permission_not_granted));
                }
                return;
            }
            case MY_SMS_REQ_CODE_CHECKOUT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mHolder.checkoutCard.setEnabled(false);
                    mHolder.checkoutCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grey));
                    AttendanceUtils.checkoutGuard(getContext());
                    AttendanceUtils.sendCheckout(getContext());
                    AppUtils.stopPulse(getContext());
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    AppUtils.showSnackBar(getView(), getString(R.string.err_permission_not_granted));
                }
                return;
            }
            case MY_SMS_REQ_CODE_EMERGENCY: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    AttendanceUtils.sendEmergency(getContext());
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

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg;
        if (mHolder.commentEditText.getText().toString().length() > 0) {
            msg = mHolder.commentEditText.getText().toString() + "-" +
                    Double.toString(location.getLatitude()) + "-" +
                    Double.toString(location.getLongitude());
        } else {
            msg = "n/a" + "-" +
                    Double.toString(location.getLatitude()) + "-" +
                    Double.toString(location.getLongitude());
        }
        Log.d("TAAAG", "" + msg);
        if (AttendanceUtils.isSupervisorCheckin(getContext())) {
            AttendanceUtils.sendSupervisorCheckin(getContext(), msg);
        } else {
            AttendanceUtils.sendSupervisorCheckout(getContext(), msg);
        }
        mHolder.commentEditText.setText("");

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        final LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(getActivity()).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                        LocationServices.getFusedLocationProviderClient(getActivity()).removeLocationUpdates(this);
                    }
                },
                Looper.myLooper());
    }
}