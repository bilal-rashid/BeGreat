package com.guards.attendance.adapters;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guards.attendance.R;
import com.guards.attendance.enumerations.StatusEnum;
import com.guards.attendance.models.Packet;
import com.guards.attendance.toolbox.OnItemClickListener;
import com.guards.attendance.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Bilal Rashid on 1/28/2018.
 */

public class PacketsAdapter extends RecyclerView.Adapter<PacketsAdapter.ViewHolder> {

    private List<Packet> mItems = new ArrayList<>();
    SimpleDateFormat format;
    OnItemClickListener mItemclickListener;

    public PacketsAdapter(OnItemClickListener onItemClickListener) {
        format = new SimpleDateFormat(Constants.DATE_FORMAT);
        this.mItemclickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_packets, parent, false)) {
        };
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        Date date;
        try {
            date = format.parse(mItems.get(position).date_time);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            SimpleDateFormat time_format = new SimpleDateFormat("hh:mm a");
            SimpleDateFormat date_format = new SimpleDateFormat("dd-MMM-yyyy");
            holder.time_text.setText(time_format.format(cal.getTime()));
            holder.date_text.setText(date_format.format(cal.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.status_text.setText(getNameOfStatus(mItems.get(position).status));
        if(mItems.get(position).status.equals(StatusEnum.CHECKIN.getValue())){
            holder.layout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.color_checkin));

        }else if (mItems.get(position).status.equals(StatusEnum.CHECKOUT.getValue())){
            holder.layout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.color_checkout));
        }else if (mItems.get(position).status.equals(StatusEnum.RESPONSE.getValue())){
            holder.layout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.color_responded));
        }else if (mItems.get(position).status.equals(StatusEnum.NO_RESPONSE.getValue())){
            holder.layout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.color_not_responded));
        }else if (mItems.get(position).status.equals(StatusEnum.EMERGENCY.getValue())){
            holder.layout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.color_emergency));
        }else if (mItems.get(position).status.equals(StatusEnum.LOCATION.getValue())){
            holder.layout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.color_location));
        }
        else {
            holder.layout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.color_no_gps));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemclickListener.onItemClick(view,mItems.get(position),position);
            }
        });

    }

    private String getNameOfStatus(String status) {
        if(status.equals(StatusEnum.CHECKIN.getValue())){
            return StatusEnum.CHECKIN.getName();
        }
        else if(status.equals(StatusEnum.CHECKOUT.getValue())){
            return StatusEnum.CHECKOUT.getName();
        }
        else if(status.equals(StatusEnum.EMERGENCY.getValue())){
            return StatusEnum.EMERGENCY.getName();
        }
        else if(status.equals(StatusEnum.RESPONSE.getValue())){
            return StatusEnum.RESPONSE.getName();
        }
        else if(status.equals(StatusEnum.NO_RESPONSE.getValue())){
            return StatusEnum.NO_RESPONSE.getName();
        }
        else if(status.equals(StatusEnum.LOCATION.getValue())){
            return StatusEnum.LOCATION.getName();
        }
        else {
            return StatusEnum.NO_LOCATION.getName();
        }
    }

    @Override
    public int getItemCount() {
        return (mItems != null ? mItems.size() : 0);
    }

    public void addAll(List<Packet> collection) {
        mItems.clear();
        if (collection != null)
            mItems.addAll(collection);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView status_text;
        TextView time_text;
        TextView date_text;
        LinearLayout layout;

        public ViewHolder(View view) {
            super(view);
            status_text = (TextView) view.findViewById(R.id.text_status);
            time_text = (TextView) view.findViewById(R.id.text_time);
            date_text = (TextView) view.findViewById(R.id.text_date);
            layout = (LinearLayout) view.findViewById(R.id.layout_card);

        }

    }
}
