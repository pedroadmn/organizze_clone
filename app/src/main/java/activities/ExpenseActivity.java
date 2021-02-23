package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

import pedroadmn.example.organizzeclone.R;

import static helper.DateCustom.currentDate;

public class ExpenseActivity extends AppCompatActivity {

    private TextInputEditText etDate;
    private TextInputEditText etCategory;
    private TextInputEditText etDescription;
    private EditText etTotalExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        etDate = findViewById(R.id.tilDate);
        etCategory = findViewById(R.id.tilCategory);
        etDescription = findViewById(R.id.tilDescription);
        etTotalExpense = findViewById(R.id.etTotalExpense);

        etDate.setText(currentDate());
    }
}