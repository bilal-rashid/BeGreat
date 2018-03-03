package com.guards.attendance.toolbox;

/**
 * Created by Bilal Rashid on 3/3/2018.
 */

public interface SupervisorSmsListener {
    public void onCheckinSuccess();
    public void onCheckinFailure();
    public void onCheckoutSuccess();
    public void onCheckoutFailure();
}
