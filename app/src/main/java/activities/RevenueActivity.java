package activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import config.FirebaseConfig;
import helper.Base64Custom;
import models.Moviment;
import models.User;
import pedroadmn.example.organizzeclone.R;

import static helper.DateCustom.currentDate;

public class RevenueActivity extends AppCompatActivity {
    private TextInputEditText etDate;
    private TextInputEditText etCategory;
    private TextInputEditText etDescription;
    private EditText etTotalRevenue;
    private FloatingActionButton fabSaveRevenue;
    private Moviment moviment;
    private Double totalRevenue;
    private Double generatedRevenue;

    private DatabaseReference firebaseRef = FirebaseConfig.getFirebaseDatabase();
    private FirebaseAuth firebaseAuth = FirebaseConfig.getFirebaseAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);

        etDate = findViewById(R.id.etDate);
        etCategory = findViewById(R.id.etCategory);
        etDescription = findViewById(R.id.etDescription);
        etTotalRevenue = findViewById(R.id.etTotalRevenue);
        fabSaveRevenue = findViewById(R.id.fabSaveRevenue);

        etDate.setText(currentDate());

        fabSaveRevenue.setOnClickListener(v -> saveRevenue());

        recoveryTotalRevenue();
    }

    private void saveRevenue() {
        if (validateRevenueFields()) {
            Double recoveredValue = Double.parseDouble(etTotalRevenue.getText().toString());
            moviment = new Moviment();
            moviment.setValue(recoveredValue);
            moviment.setCategory(etCategory.getText().toString());
            moviment.setDescription(etDescription.getText().toString());
            moviment.setDate(etDate.getText().toString());
            moviment.setType("r");

            generatedRevenue = recoveredValue;
            Double updatedRevenue = totalRevenue + generatedRevenue;
            updateExpenses(updatedRevenue);

            moviment.save();
        }
    }

    private boolean validateRevenueFields() {
        String value = etTotalRevenue.getText().toString();
        String date = etDate.getText().toString();
        String category = etCategory.getText().toString();
        String description = etDescription.getText().toString();

        if (!value.isEmpty()) {
            if (!date.isEmpty()) {
                if (!category.isEmpty()) {
                    if (!description.isEmpty()) {
                        return true;
                    } else {
                        Toast.makeText(this, "Description is empty", Toast.LENGTH_LONG).show();
                        return false;
                    }
                } else {
                    Toast.makeText(this, "Category is empty", Toast.LENGTH_LONG).show();
                    return false;
                }
            } else {
                Toast.makeText(this, "Date is empty", Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            Toast.makeText(this, "Value is empty", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void recoveryTotalRevenue() {
        String userEmail = firebaseAuth.getCurrentUser().getEmail();
        String userId = Base64Custom.encondeBase64(userEmail);
        DatabaseReference usersRef = firebaseRef.child("users").child(userId);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                totalRevenue = user.getTotalRevenue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateExpenses(Double expense) {
        String userEmail = firebaseAuth.getCurrentUser().getEmail();
        String userId = Base64Custom.encondeBase64(userEmail);
        DatabaseReference usersRef = firebaseRef.child("users").child(userId);

        usersRef.child("totalRevenue").setValue(expense);
    }
}