package activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import adapters.MovementAdapter;
import config.FirebaseConfig;
import helper.Base64Custom;
import models.Movement;
import models.User;
import pedroadmn.example.organizzeclone.R;

public class HomeActivity extends AppCompatActivity {

    private com.github.clans.fab.FloatingActionButton fabExpense;
    private com.github.clans.fab.FloatingActionButton fabRevenue;

    private MaterialCalendarView calendarView;
    private TextView tvGreetings;
    private TextView tvBalance;
    private RecyclerView rvMovements;
    private MovementAdapter movementAdapter;
    private List<Movement> movementList = new ArrayList<>();

    private FirebaseAuth firebaseAuth = FirebaseConfig.getFirebaseAuth();
    private DatabaseReference databaseRef = FirebaseConfig.getFirebaseDatabase();
    private DatabaseReference userRef;
    private DatabaseReference movementRef;
    private ValueEventListener userValueEventListener;
    private ValueEventListener movementValueEventListener;

    private Double totalExpenses = 0.0;
    private Double totalRevenue = 0.0;
    private Double balance = 0.0;
    private String selectedMonthYear;

    private Movement movement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvGreetings = findViewById(R.id.tvGreetings);
        tvBalance = findViewById(R.id.tvBalance);
        rvMovements = findViewById(R.id.rvMoviments);
        calendarView = findViewById(R.id.calendarView);
        setupCalendarView();
        swipe();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze Clone");
        setSupportActionBar(toolbar);

        fabExpense = findViewById(R.id.fabExpense);
        fabExpense.setOnClickListener(v -> goToExpense());

        fabRevenue = findViewById(R.id.fabRevenue);
        fabRevenue.setOnClickListener(v -> goToRevenue());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        movementAdapter = new MovementAdapter(movementList, this);

        rvMovements.setLayoutManager(layoutManager);
        rvMovements.setHasFixedSize(true);

        rvMovements.setAdapter(movementAdapter);
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

        CalendarDay calendarDate = calendarView.getCurrentDate();
        String selectedMonth = String.format("%02d", calendarDate.getMonth());

        selectedMonthYear = selectedMonth + calendarDate.getYear();

        calendarView.setOnMonthChangedListener((widget, date) -> {
            String chosenMonth = String.format("%02d", date.getMonth());
            selectedMonthYear = chosenMonth + "" + date.getYear();
            movementRef.removeEventListener(movementValueEventListener);
            getMovements();
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

    private void getMovements() {
        String userEmail = firebaseAuth.getCurrentUser().getEmail();
        String userId = Base64Custom.encondeBase64(userEmail);
        movementRef = databaseRef.child("movements")
                                 .child(userId)
                                 .child(selectedMonthYear);

        movementValueEventListener = movementRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                movementList.clear();
                for(DataSnapshot data : snapshot.getChildren()) {
                    Movement movement = data.getValue(Movement.class);
                    movement.setId(data.getKey());
                    movementList.add(movement);
                }

                movementAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

    private void swipe() {
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deleteMovement(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(rvMovements);
    }

    private void deleteMovement(RecyclerView.ViewHolder viewHolder) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Delete Account Movement");
        alertDialog.setMessage("Are you sure that you really wish delete this movement?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirm", (dialogInterface, i) -> {
            int position = viewHolder.getAdapterPosition();
            movement = movementList.get(position);

            String userEmail = firebaseAuth.getCurrentUser().getEmail();
            String userId = Base64Custom.encondeBase64(userEmail);
            movementRef = databaseRef.child("movements")
                    .child(userId)
                    .child(selectedMonthYear)
                    .child(movement.getId());

            movementRef.removeValue();

            movementAdapter.notifyItemRemoved(position);
        });

        alertDialog.setNegativeButton("Cancel", (dialogInterface, i) -> {
            Toast.makeText(HomeActivity.this, "Canceled", Toast.LENGTH_LONG).show();
            movementAdapter.notifyDataSetChanged();
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUserInfo();
        getMovements();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (userRef != null) {
            userRef.removeEventListener(userValueEventListener);
        }
        if (movementRef != null) {
            movementRef.removeEventListener(movementValueEventListener);
        }
    }
}