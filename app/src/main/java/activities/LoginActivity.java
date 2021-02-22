package activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import models.User;
import pedroadmn.example.organizzeclone.R;

import static config.FirebaseConfig.getFirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(onLogin());
    }

    private View.OnClickListener onLogin() {
        return view -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if(!email.isEmpty()) {
                if(!password.isEmpty()) {
                    User user = new User();
                    user.setEmail(email);
                    user.setPassword(password);
                    login(user);
                } else {
                    Toast.makeText(LoginActivity.this, "Fill the password", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Fill the email", Toast.LENGTH_LONG).show();
            }
        };
    }

    private void login(User user) {
        getFirebaseAuth().signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        goToHomeActivity();
                        Toast.makeText(LoginActivity.this, "Success on login", Toast.LENGTH_LONG).show();
                    } else {
                        String exceptionMessage = "";
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidCredentialsException exception) {
                            exceptionMessage = "Email and password do not correspond to registered user";
                        } catch (FirebaseAuthInvalidUserException exception) {
                            exceptionMessage = "User is not registered";
                        } catch (Exception exception) {
                            exceptionMessage = "Error on register user";
                            exception.printStackTrace();
                        }
                        Toast.makeText(LoginActivity.this, exceptionMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void goToHomeActivity() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}