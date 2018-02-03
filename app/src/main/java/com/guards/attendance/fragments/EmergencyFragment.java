package com.guards.attendance.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guards.attendance.R;
import com.guards.attendance.models.Packet;
import com.guards.attendance.toolbox.ToolbarListener;
import com.guards.attendance.utils.AppUtils;
import com.guards.attendance.utils.Constants;
import com.guards.attendance.utils.GsonUtils;

import in.shadowfax.proswipebutton.ProSwipeButton;

/**
 * Created by Bilal Rashid on 2/3/2018.
 */

public class EmergencyFragment extends Fragment implements ProSwipeButton.OnSwipeListener{

    private ViewHolder mHolder;
    MediaPlayer mediaPlayer;
    Packet mPacket;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Emergency",true);
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_emergency, container, false);
    }

    private void manipulateBundle() {
        if (getArguments() != null) {
            mPacket = GsonUtils.fromJson(getArguments().getString(Constants.GUARD_DATA),Packet.class);
        }
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHolder = new ViewHolder(view);
        manipulateBundle();
        mHolder.emp_id_text.setText(""+mPacket.emp_id);
        mHolder.proSwipeBtn.setOnSwipeListener(this);
        AppUtils.IncreaseSound(getContext());
        mediaPlayer = MediaPlayer.create(getContext(), Settings.System.DEFAULT_ALARM_ALERT_URI);
        mediaPlayer.setVolume(100,100);
        mediaPlayer.start();
    }
    @Override
    public void onSwipeConfirm() {
        mediaPlayer.stop();
        getActivity().finish();
    }

    public static class ViewHolder {

        ProSwipeButton proSwipeBtn;
        TextView emp_id_text;

        public ViewHolder(View view) {
            proSwipeBtn = (ProSwipeButton) view.findViewById(R.id.proswipebutton_main);
            emp_id_text = (TextView) view.findViewById(R.id.text_emp_id);

        }

    }
}
