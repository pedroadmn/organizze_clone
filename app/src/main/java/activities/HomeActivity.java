package activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import pedroadmn.example.organizzeclone.R;

public class HomeActivity extends AppCompatActivity {

    private com.github.clans.fab.FloatingActionButton fabExpense;
    private com.github.clans.fab.FloatingActionButton fabRevenue;

    private MaterialCalendarView calendarView;
    private TextView tvGreetings;
    private TextView tvBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvGreetings = findViewById(R.id.tvGreetings);
        tvBalance = findViewById(R.id.tvBalance);
        calendarView = findViewById(R.id.calendarView);
        setupCalendarView();

        Toolbar toolbar = findViewById(R.id.toolbar);
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
}