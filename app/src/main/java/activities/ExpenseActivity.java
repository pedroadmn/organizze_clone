package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import models.Moviment;
import pedroadmn.example.organizzeclone.R;

import static helper.DateCustom.currentDate;

public class ExpenseActivity extends AppCompatActivity {

    private TextInputEditText etDate;
    private TextInputEditText etCategory;
    private TextInputEditText etDescription;
    private EditText etTotalExpense;
    private FloatingActionButton fabSaveExpense;
    private Moviment moviment;

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
    }

    private void saveExpense() {
        moviment = new Moviment();
        moviment.setValue(Double.parseDouble(etTotalExpense.getText().toString()));
        moviment.setCategory(etCategory.getText().toString());
        moviment.setDescription(etDescription.getText().toString());
        moviment.setDate(etDate.getText().toString());
        moviment.setType("d");
        moviment.save();
    }
}