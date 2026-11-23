package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Schedule extends AppCompatActivity {

    private RecyclerView rvSchedule;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);

        rvSchedule = findViewById(R.id.rvSchedule);
        btnBack = findViewById(R.id.btnBack);

        List<String[]> events = new ArrayList<>();
        events.add(new String[]{"19:30", "קבלת פנים ועמדות שף", "מוזיקה חיה ודרינק ראשון בגן"});
        events.add(new String[]{"20:30", "חופה וקידושין", "התכנסות ברחבת החופה המרכזית"});
        events.add(new String[]{"21:15", "מנות שף ועיקריות", "הגשה לשולחנות וברכות"});
        events.add(new String[]{"22:30", "מסיבת ריקודים וקינוחים", "פתיחת רחבת הריקודים ובר מתוקים"});
        events.add(new String[]{"00:00", "סיום האירוע ומשוב", "קבלת התראה לדירוג החוויה"});

        rvSchedule.setLayoutManager(new LinearLayoutManager(this));
        rvSchedule.setAdapter(new ScheduleAdapter(events));

        btnBack.setOnClickListener(v -> finish());
    }

    private static class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
        private final List<String[]> scheduleList;

        ScheduleAdapter(List<String[]> scheduleList) {
            this.scheduleList = scheduleList;
        }

        @NonNull
        @Override
        public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu, parent, false);
            return new ScheduleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
            String[] row = scheduleList.get(position);
            holder.tvTime.setText(row[0] + " - " + row[1]);
            holder.tvDesc.setText(row[2]);
            holder.tvTag.setText("לו\"ז אירוע");
        }

        @Override
        public int getItemCount() {
            return scheduleList.size();
        }

        static class ScheduleViewHolder extends RecyclerView.ViewHolder {
            TextView tvTime, tvDesc, tvTag;

            ScheduleViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTime = itemView.findViewById(R.id.tvItemTitle);
                tvDesc = itemView.findViewById(R.id.tvItemDesc);
                tvTag = itemView.findViewById(R.id.tvItemCategory);
            }
        }
    }
}