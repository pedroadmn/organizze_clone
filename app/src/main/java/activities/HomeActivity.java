package activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import pedroadmn.example.organizzeclone.R;

public class HomeActivity extends AppCompatActivity {

    private com.github.clans.fab.FloatingActionButton fabExpense;
    private com.github.clans.fab.FloatingActionButton fabRevenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
}