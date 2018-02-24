package com.guards.attendance;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.guards.attendance.dialog.SimpleDialog;
import com.guards.attendance.fragments.AdminHomeFragment;
import com.guards.attendance.fragments.AlarmFragment;
import com.guards.attendance.fragments.GuardHomeFragment;
import com.guards.attendance.fragments.HomeFragment;
import com.guards.attendance.fragments.LoginFragment;
import com.guards.attendance.fragments.SignupFragment;
import com.guards.attendance.fragments.SupervisorHomeFragment;
import com.guards.attendance.toolbox.ToolbarListener;
import com.guards.attendance.utils.ActivityUtils;
import com.guards.attendance.utils.Constants;
import com.guards.attendance.utils.LoginUtils;

public class FrameActivity extends AppCompatActivity implements ToolbarListener {

    private Toolbar mToolbar;
    private SimpleDialog mSimpleDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);
        toolbarSetup();
        String fragmentName = getIntent().getStringExtra(Constants.FRAGMENT_NAME);
        Bundle bundle = getIntent().getBundleExtra(Constants.DATA);
        if (!TextUtils.isEmpty(fragmentName)) {
            Fragment fragment = Fragment.instantiate(this, fragmentName);
            if (bundle != null)
                fragment.setArguments(bundle);
            addFragment(fragment);
        } else {
            if(LoginUtils.isAdminUserLogin(this)){
                addFragment(new AdminHomeFragment());
            }
            else if(LoginUtils.isSupervisorUserLogin(this)){
                // change it
                addFragment(new SupervisorHomeFragment());
            }
            else if (LoginUtils.isGuardUserLogin(this)){
                addFragment(new GuardHomeFragment());
            }
            else {
                addFragment(new LoginFragment());
            }
        }

    }
    public void addFragment(final Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
    public void toolbarSetup() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(" ");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public void onBackPressed() {

        if (isTaskRoot()) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
            if(fragment instanceof AlarmFragment){
//                FrameActivity.this.finish();
//                ActivityUtils.startHomeActivity(this, FrameActivity.class,null);
            }else {
                mSimpleDialog = new SimpleDialog(this, null, getString(R.string.msg_exit),
                        getString(R.string.button_cancel), getString(R.string.button_ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.button_positive:
                                mSimpleDialog.dismiss();
                                FrameActivity.this.finish();
                                break;
                            case R.id.button_negative:
                                mSimpleDialog.dismiss();
                                break;
                        }
                    }
                });
                mSimpleDialog.show();
            }
        }
        else {
            FrameActivity.this.finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                onBackPressed();
                return true;
            case R.id.action_dummy:
                break;
            default:
                // ...
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setTitle(String title, boolean isHome) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            if (isHome) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                ActivityUtils.centerToolbarTitle(mToolbar,false);
            }
            else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ActivityUtils.centerToolbarTitle(mToolbar,true);
            }
        }
    }

    @Override
    public void setTitleAdmin(String title, boolean isHome) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            if (isHome) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                ActivityUtils.centerToolbarTitleAdmin(mToolbar,false);
            }
            else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ActivityUtils.centerToolbarTitleAdmin(mToolbar,true);
            }
        }
    }
}
