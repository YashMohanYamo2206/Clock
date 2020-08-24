package com.yash.clock.Adapter;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yash.clock.DatabaseHandler.alarm_data_contract;
import com.yash.clock.R;

import java.util.Currency;
import java.util.List;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class alarms_adapter extends RecyclerView.Adapter<alarms_adapter.alarms_viewHolder> {

    Cursor cursor;
    public onItemClickListener mListener;

    public interface onItemClickListener {
        void onSwitchClick(int position, boolean isChecked, String time, CompoundButton s);

        void onEditClick(int position, String time);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        mListener = listener;
    }

    public alarms_adapter(Cursor cursor) {
        this.cursor = cursor;
    }

    public class alarms_viewHolder extends RecyclerView.ViewHolder {
        TextView alarm_time;
        Switch Switch;
        Button edit;

        public alarms_viewHolder(@NonNull View itemView) {
            super(itemView);
            alarm_time = itemView.findViewById(R.id.alarm_time);
            Switch = itemView.findViewById(R.id.alarm_switch);
            edit = itemView.findViewById(R.id.edit_alarm);
            Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        boolean checked = isChecked;
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onSwitchClick(position, checked, (String) alarm_time.getText(), Switch);
                        }
                    }
                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onEditClick(position, (String) alarm_time.getText());
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public alarms_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_alarm_item, parent, false);
        return new alarms_viewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull alarms_viewHolder holder, int position) {

        if (!cursor.moveToPosition(position)) {
            return;
        }
        String time = cursor.getString(cursor.getColumnIndex(alarm_data_contract.alarm_data_contract_columns.TIME));
        String isAlarmOn = cursor.getString(cursor.getColumnIndex(alarm_data_contract.alarm_data_contract_columns.IsAlarmOn));
        holder.alarm_time.setText(time);
        holder.Switch.setChecked(isAlarmOn.equals("true"));
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
