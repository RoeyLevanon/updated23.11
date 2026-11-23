package com.example.myapplication;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Wellcome extends AppCompatActivity {

    private EditText etFullName, etPhone;
    private TextView tvNfcStatus;
    private Button btnContinue;
    private NfcAdapter nfcAdapter;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wellcome);

        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        tvNfcStatus = findViewById(R.id.tvNfcStatus);
        btnContinue = findViewById(R.id.btnContinue);

        dbRef = FirebaseDatabase.getInstance().getReference();

        // בקשת הרשאת התראות לאנדרואיד 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 100);
            }
        }

        // בדיקת רכיב NFC במכשיר
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            tvNfcStatus.setText("רכיב NFC אינו נתמך במכשיר זה - ניתן להירשם ידנית");
        } else if (!nfcAdapter.isEnabled()) {
            tvNfcStatus.setText("יש להפעיל NFC בהגדרות או להזין פרטים ידנית");
        } else {
            tvNfcStatus.setText("קרב את הטלפון לתג ה-NFC בכניסה או הזן פרטים:");
        }

        // בדיקה אם המסך נפתח מסריקת תג NFC
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            tvNfcStatus.setText("תג NFC נסרק בהצלחה! הזן שם וטלפון להמשך");
        }

        // שמירת עובד לדוגמה ב-Firebase כדי להדגים שימוש במחלקת Worker
        Worker sampleWorker = new Worker("דני כהן", "0500000000", "מנהל אירוע", 1);
        dbRef.child("Workers").child("0500000000").setValue(sampleWorker);

        btnContinue.setOnClickListener(v -> registerGuestAndProceed());

        // תזמון התראת דירוג לשעה 00:00
        scheduleMidnightRatingNotification();
    }

    private void registerGuestAndProceed() {
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (fullName.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "נא למלא שם מלא ומספר טלפון", Toast.LENGTH_SHORT).show();
            return;
        }

        // הקצאת מספר שולחן (לדוגמה לפי אורך השם או שולחן קבוע 12)
        int tableNumber = (int)(Math.random() * 15); // 0 to 100

        Guest guest = new Guest(fullName, phone, tableNumber, true);

        // שמירה ב-Firebase
        dbRef.child("Guests").child(phone).setValue(guest)
                .addOnSuccessListener(unused -> {
                    Intent intent = new Intent(Wellcome.this, MainActivity.class);
                    intent.putExtra("GUEST_NAME", guest.getFullName());
                    intent.putExtra("GUEST_PHONE", guest.getPhone());
                    intent.putExtra("TABLE_NUMBER", guest.getTableNumber());
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "שגיאה בשמירה: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }

    private void scheduleMidnightRatingNotification() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // אם חצות כבר עבר היום, תזמן לחצות הבא
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Intent intent = new Intent(this, RatingReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    // מחלקה פנימית סטטית לקבלת ההתראה בשעה 00:00
    public static class RatingReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String channelId = "wedding_rating_channel";
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && manager != null) {
                NotificationChannel channel = new NotificationChannel(
                        channelId, "דירוג אירוע", NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
            }

            Intent openAppIntent = new Intent(context, MainActivity.class);
            openAppIntent.putExtra("OPEN_RATING_DIALOG", true);
            PendingIntent contentIntent = PendingIntent.getActivity(
                    context, 1, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(android.R.drawable.btn_star_big_on)
                    .setContentTitle("תודה שהשתתפתם בשמחתנו!")
                    .setContentText("לחץ כאן לדירוג האוכל, המיקום והשירות (5 כוכבים)")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true);

            if (manager != null) {
                manager.notify(2026, builder.build());
            }
        }
    }
}