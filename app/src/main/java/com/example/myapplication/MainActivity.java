package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView tvWelcomeGuest, tvTableInfo, tvCurrentCategory;
    private Button btnMenuYamma, btnSchedule, btnRateEvent;
    private Button btnReception, btnChef, btnMainDish;
    private LinearLayout llMenuSection;
    private RecyclerView rvMenu;

    private final List<Menu> allMenuItems = new ArrayList<>();
    private final List<Menu> displayedMenuItems = new ArrayList<>();
    private MenuAdapter adapter;
    private DatabaseReference dbRef;
    private String guestPhone = "unknown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // קישור רכיבי כותרת
        tvWelcomeGuest = findViewById(R.id.tvWelcomeGuest);
        tvTableInfo = findViewById(R.id.tvTableInfo);

        // 3 הכפתורים הראשיים במסך
        btnMenuYamma = findViewById(R.id.btnMenuYamma);
        btnSchedule = findViewById(R.id.btnSchedule);
        btnRateEvent = findViewById(R.id.btnRateEvent);

        // אזור התפריט שנפתח בלחיצה על תפריט "ימה"
        llMenuSection = findViewById(R.id.llMenuSection);
        btnReception = findViewById(R.id.btnReception);
        btnChef = findViewById(R.id.btnChef);
        btnMainDish = findViewById(R.id.btnMainDish);
        tvCurrentCategory = findViewById(R.id.tvCurrentCategory);
        rvMenu = findViewById(R.id.rvMenu);

        dbRef = FirebaseDatabase.getInstance().getReference();

        // קבלת נתוני האורח ממסך Wellcome
        String guestName = getIntent().getStringExtra("GUEST_NAME");
        String phoneExtra = getIntent().getStringExtra("GUEST_PHONE");
        int tableNumber = getIntent().getIntExtra("TABLE_NUMBER", 12);

        if (phoneExtra != null) guestPhone = phoneExtra;
        tvWelcomeGuest.setText("שלום " + (guestName != null ? guestName : "אורח יקר") + "!");
        tvTableInfo.setText("הושבת בשולחן מספר " + tableNumber + " באולם \"ימה\"");

        // הגדרת RecyclerView לתצוגת התפריט (ללא לחיצות/בחירה)
        rvMenu.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MenuAdapter(displayedMenuItems);
        rvMenu.setAdapter(adapter);

        // טעינת ההיצע הקולינרי של אולם "ימה"
        initYammaMenuData();

        // 1. לחיצה על כפתור תפריט "ימה" פותחת את 3 כפתורי הקטגוריות
        btnMenuYamma.setOnClickListener(v -> {
            if (llMenuSection.getVisibility() == View.GONE) {
                llMenuSection.setVisibility(View.VISIBLE);
                filterMenuByType(ReceptionStation.class, "תפריט \"ימה\" - עמדות קבלת פנים");
            } else {
                llMenuSection.setVisibility(View.GONE);
            }
        });

        // 2. לחיצה על כפתור לו"ז עוברת למסך Schedule
        btnSchedule.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Schedule.class)));

        // 3. לחיצה על כפתור דירוג האירוע פותחת את חלונית הדירוג
        btnRateEvent.setOnClickListener(v -> showRatingDialog());

        // פתיחה אוטומטית של הדירוג אם המשתמש לחץ על ה-Notification של 00:00
        if (getIntent().getBooleanExtra("OPEN_RATING_DIALOG", false)) {
            showRatingDialog();
        }

        // 3 כפתורי המשנה בתוך תפריט "ימה"
        btnReception.setOnClickListener(v ->
                filterMenuByType(ReceptionStation.class, "תפריט \"ימה\" - עמדות קבלת פנים"));

        btnChef.setOnClickListener(v ->
                filterMenuByType(ChefDish.class, "תפריט \"ימה\" - מנות שף"));

        btnMainDish.setOnClickListener(v ->
                filterMenuByType(MainDish.class, "תפריט \"ימה\" - מנות עיקריות"));
    }

    private void initYammaMenuData() {
        allMenuItems.clear();

        // עמדות קבלת פנים של "ימה"
        allMenuItems.add(new ReceptionStation("r1", "עמדת אסאדו מעושן",
                "נתחי אסאדו בבישול איטי על גחלי עץ אלון לצד ירקות מדורה", 1, "מרפסת הים הצפונית"));
        allMenuItems.add(new ReceptionStation("r2", "סושי בר פרונטלי",
                "מבחר רולים טריים, ניגירי וסשימי מדגי ים טריים המוכנים במקום", 2, "רחבת קבלת הפנים"));
        allMenuItems.add(new ReceptionStation("r3", "טאבון אבן פתוח",
                "פוקאצ'ות חמות מהטאבון עם שמן זית כתית, בלסמי מצומצם ומטבלי הבית", 3, "הכניסה לגן"));

        // מנות שף של "ימה"
        allMenuItems.add(new ChefDish("c1", "סביצ'ה מוסר ים",
                "קוביות מוסר ים טרי, כוסברה, צ'ילי אדום, בצל סגול, קרם אבוקדו וליים", true));
        allMenuItems.add(new ChefDish("c2", "קרפצ'יו פילה בקר",
                "פרוסות פילה דקות עם איולי כמהין, עלי רוקט, צנוברים קלויים ובלסמי מיושן", true));
        allMenuItems.add(new ChefDish("c3", "ריזוטו פטריות יער",
                "אורז ארבוריו מבושל בציר פורצ'יני עשיר, ערמונים קלויים ושמן זית", false));

        // מנות עיקריות של "ימה"
        allMenuItems.add(new MainDish("m1", "אנטריקוט מיושן על הגריל",
                "נתח אנטריקוט עסיסי בצלייה מדויקת עם ציר בקר ויין אדום", false, "תפוחי אדמה מדורה"));
        allMenuItems.add(new MainDish("m2", "פילה לברק ים תיכוני",
                "פילה לברק צרוב על הפלנצ'ה ברוטב לימון כבוש, שום קונפי ועשבי תיבול", false, "ירקות שורש צלויים"));
        allMenuItems.add(new MainDish("m3", "סטייק כרובית וטחינה גולמית",
                "כרובית שלמה צלויה בתנור אבן עם סילאן טבעי, שקדים קלויים ורימונים", true, "אורז בר ועשבי תיבול"));

        // שמירת התפריט ב-Firebase
        dbRef.child("YammaMenu").setValue(allMenuItems);
    }

    private void filterMenuByType(Class<?> categoryClass, String title) {
        tvCurrentCategory.setText(title);
        displayedMenuItems.clear();
        for (Menu item : allMenuItems) {
            if (categoryClass.isInstance(item)) {
                displayedMenuItems.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void showRatingDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.rating, null);
        RatingBar rbFood = dialogView.findViewById(R.id.rbFood);
        RatingBar rbLocation = dialogView.findViewById(R.id.rbLocation);
        RatingBar rbService = dialogView.findViewById(R.id.rbService);

        new AlertDialog.Builder(this)
                .setTitle("דירוג האירוע באולם \"ימה\"")
                .setView(dialogView)
                .setPositiveButton("שלח דירוג", (dialog, which) -> {
                    Map<String, Object> ratingData = new HashMap<>();
                    ratingData.put("guestPhone", guestPhone);
                    ratingData.put("foodStars", rbFood.getRating());
                    ratingData.put("locationStars", rbLocation.getRating());
                    ratingData.put("serviceStars", rbService.getRating());

                    dbRef.child("Ratings").push().setValue(ratingData)
                            .addOnSuccessListener(unused ->
                                    Toast.makeText(MainActivity.this, "תודה! הדירוג שלך נשמר", Toast.LENGTH_LONG).show()
                            );
                })
                .setNegativeButton("ביטול", null)
                .show();
    }

    // מתאם לתצוגה בלבד (ללא אינטראקציית הזמנה/בקשה)
    private static class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
        private final List<Menu> items;

        MenuAdapter(List<Menu> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu, parent, false);
            return new MenuViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
            Menu item = items.get(position);
            holder.tvTitle.setText(item.getName());
            holder.tvDesc.setText(item.getDescription());
            holder.tvCategory.setText(item.getCategoryDetails());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class MenuViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDesc, tvCategory;

            MenuViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvItemTitle);
                tvDesc = itemView.findViewById(R.id.tvItemDesc);
                tvCategory = itemView.findViewById(R.id.tvItemCategory);
            }
        }
    }
}