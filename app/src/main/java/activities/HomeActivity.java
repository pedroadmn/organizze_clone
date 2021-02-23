package activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

import config.FirebaseConfig;
import helper.Base64Custom;
import models.User;
import pedroadmn.example.organizzeclone.R;

import static config.FirebaseConfig.getFirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private com.github.clans.fab.FloatingActionButton fabExpense;
    private com.github.clans.fab.FloatingActionButton fabRevenue;

    private MaterialCalendarView calendarView;
    private TextView tvGreetings;
    private TextView tvBalance;

    private FirebaseAuth firebaseAuth = FirebaseConfig.getFirebaseAuth();
    private DatabaseReference databaseRef = FirebaseConfig.getFirebaseDatabase();
    private DatabaseReference userRef;
    private ValueEventListener userValueEventListener;

    private Double totalExpenses = 0.0;
    private Double totalRevenue = 0.0;
    private Double balance = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvGreetings = findViewById(R.id.tvGreetings);
        tvBalance = findViewById(R.id.tvBalance);
        calendarView = findViewById(R.id.calendarView);
        setupCalendarView();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze Clone");
        setSupportActionBar(toolbar);

        fabExpense = findViewById(R.id.fabExpense);
        fabExpense.setOnClickListener(v -> goToExpense());

        fabRevenue = findViewById(R.id.fabRevenue);
        fabRevenue.setOnClickListener(v -> goToRevenue());
    }

    private void goToExpense() {
        startActivity(new Intent(this, ExpenseActivity.class));
    }

    private void goToRevenue() {
        startActivity(new Intent(this, RevenueActivity.class));
    }

    private void setupCalendarView() {
        CharSequence months[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        calendarView.setTitleMonths(months);
        calendarView.setOnMonthChangedListener((widget, date) -> {

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Signout:
                firebaseAuth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getUserInfo() {
        String userEmail = firebaseAuth.getCurrentUser().getEmail();
        String userId = Base64Custom.encondeBase64(userEmail);
        userRef = databaseRef.child("users").child(userId);

        userValueEventListener = userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                totalExpenses = user.getTotalExpense();
                totalRevenue = user.getTotalRevenue();
                balance = totalRevenue - totalExpenses;

                DecimalFormat decimalFormat = new DecimalFormat("0.##");
                String resultFormatted = decimalFormat.format(balance);

                tvGreetings.setText("Hello, " + user.getName());
                tvBalance.setText("R$ " + resultFormatted);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUserInfo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (userRef != null) {
            userRef.removeEventListener(userValueEventListener);
        }
    }
}