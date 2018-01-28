package com.guards.attendance.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guards.attendance.R;
import com.guards.attendance.models.Guard;
import com.guards.attendance.toolbox.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bilal Rashid on 1/27/2018.
 */

public class GuardAdapter extends RecyclerView.Adapter<GuardAdapter.ViewHolder> {
    private List<Guard> mItems = new ArrayList<>();
    OnItemClickListener mItemclickListener;

    public GuardAdapter(OnItemClickListener onItemClickListener){
        this.mItemclickListener = onItemClickListener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_guards, parent, false)) {
        };
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.emp_phoneText.setText(mItems.get(position).number);
        holder.emp_idText.setText(mItems.get(position).emp_id);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemclickListener.onItemClick(view,mItems.get(position).number,position);
            }
        });
        holder.layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_UP:
                        view.setBackgroundColor(view.getContext().getResources().getColor(R.color.colorWhite));
                        mItemclickListener.onItemClick(view,mItems.get(position).number,position);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        view.setBackgroundColor(view.getContext().getResources().getColor(R.color.colorWhite));
                        break;
                    case MotionEvent.ACTION_DOWN:
                        view.setBackgroundColor(view.getContext().getResources().getColor(R.color.colorWhite));
                        break;
                    case MotionEvent.ACTION_MOVE:
                        view.setBackgroundColor(view.getContext().getResources().getColor(R.color.colorAppBG));
                        break;

                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return (mItems != null ? mItems.size() : 0);
    }
    public void addAll(List<Guard> collection) {
        mItems.clear();
        if (collection != null)
            mItems.addAll(collection);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView emp_idText;
        TextView emp_phoneText;
        LinearLayout layout;

        public ViewHolder(View view){
            super(view);
            emp_idText= (TextView)view.findViewById(R.id.text_employee_id);
            emp_phoneText = (TextView)view.findViewById(R.id.text_phone);
            layout = (LinearLayout) view.findViewById(R.id.layout);

        }

    }
}
