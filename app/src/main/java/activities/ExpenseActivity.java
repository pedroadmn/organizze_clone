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

public class ExpenseActivity extends AppCompatActivity {

    private TextInputEditText etDate;
    private TextInputEditText etCategory;
    private TextInputEditText etDescription;
    private EditText etTotalExpense;
    private FloatingActionButton fabSaveExpense;
    private Moviment moviment;
    private Double totalExpense;
    private Double generatedExpense;

    private DatabaseReference firebaseRef = FirebaseConfig.getFirebaseDatabase();
    private FirebaseAuth firebaseAuth = FirebaseConfig.getFirebaseAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        etDate = findViewById(R.id.etDate);
        etCategory = findViewById(R.id.etCategory);
        etDescription = findViewById(R.id.etDescription);
        etTotalExpense = findViewById(R.id.etTotalExpense);
        fabSaveExpense = findViewById(R.id.fabSaveExpense);

        etDate.setText(currentDate());

        fabSaveExpense.setOnClickListener(v -> saveExpense());

        recoveryTotalExpenses();
    }

    private void saveExpense() {
        if (validateExpenseFields()) {
            Double recoveredValue = Double.parseDouble(etTotalExpense.getText().toString());
            moviment = new Moviment();
            moviment.setValue(recoveredValue);
            moviment.setCategory(etCategory.getText().toString());
            moviment.setDescription(etDescription.getText().toString());
            moviment.setDate(etDate.getText().toString());
            moviment.setType("d");

            generatedExpense = recoveredValue;
            Double updatedExpense = totalExpense + generatedExpense;
            updateExpenses(updatedExpense);

            moviment.save();
        }
    }

    private boolean validateExpenseFields() {
        String value = etTotalExpense.getText().toString();
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

    private void recoveryTotalExpenses() {
        String userEmail = firebaseAuth.getCurrentUser().getEmail();
        String userId = Base64Custom.encondeBase64(userEmail);
        DatabaseReference usersRef = firebaseRef.child("users").child(userId);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                totalExpense = user.getTotalExpense();
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

        usersRef.child("totalExpense").setValue(expense);
    }
}