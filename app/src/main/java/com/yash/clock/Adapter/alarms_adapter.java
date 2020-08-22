package com.yash.clock.Adapter;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yash.clock.DatabaseHandler.alarm_data_contract;
import com.yash.clock.R;

import java.util.Currency;
import java.util.List;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class alarms_adapter extends RecyclerView.Adapter<alarms_adapter.alarms_viewHolder> {

    Cursor cursor;

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
