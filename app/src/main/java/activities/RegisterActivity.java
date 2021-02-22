package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import models.User;
import pedroadmn.example.organizzeclone.R;

import static config.FirebaseConfig.getFirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnRegister;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(view -> {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if(!name.isEmpty()) {
                if(!email.isEmpty()) {
                    if(!password.isEmpty()) {
                        User user = new User();
                        user.setName(name);
                        user.setEmail(email);
                        user.setPassword(password);
                        registerUser(user);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Fill the password", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Fill the email", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "Fill the name", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void registerUser(User user) {
        getFirebaseAuth().createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, task -> {
                   if (task.isSuccessful()) {
                       Toast.makeText(RegisterActivity.this, "Success on register user", Toast.LENGTH_LONG).show();
                   } else {
                       Toast.makeText(RegisterActivity.this, "Error on register user", Toast.LENGTH_LONG).show();
                   }
                });
    }
}