package com.imontalvodev.passwordgencontainer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PasswordGen extends AppCompatActivity {

    private EditText etName, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_password_gen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnGeneratePasswd = findViewById(R.id.btnGeneratePasswd);

        btnGeneratePasswd.setOnClickListener(v -> {
            StringBuilder password = new StringBuilder();
            int leng = 16;
            for (int i = 0; i < leng; i++) {
                int random = (int) (Math.random() * 4 + 1);
                password.append(randomChars(random));
            }
            etPassword.setText(password.toString());
        });

        btnSave.setOnClickListener(v -> {
            if (etName.getText().toString().isEmpty()) {
                Toast.makeText(this, "Ingrese una pista para almacenar la contraseña", Toast.LENGTH_LONG).show();
                return;
            }

            if (etPassword.getText().toString().isEmpty()) {
                Toast.makeText(this, "Ingrese o genere una contraseña", Toast.LENGTH_LONG).show();
                return;
            }

            SharedPreferences sharedPreferences = getSharedPreferences("passwords", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(etName.getText().toString(), etPassword.getText().toString());
            editor.apply();
            Toast.makeText(this, "Contraseña guardada correctamente", Toast.LENGTH_LONG).show();
        });
    }

    public static String randomChars(int random) {

        String lower = "abcdefghijklmnñopqrstuvwxyz";
        String upper = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
        String nums = "0123456789";
        String specials = ".-*/!ªº?¿'¡´ç¨{}_;:@#~";
        String password = "";
        char character;

        switch (random) {
            case 1:
                random = (int) (Math.random() * lower.length());
                character = lower.charAt(random);
                password += character;
                break;

            case 2:
                random = (int) (Math.random() * upper.length());
                character = upper.charAt(random);
                password += character;
                break;

            case 3:
                random = (int) (Math.random() * nums.length());
                character = nums.charAt(random);
                password += character;
                break;

            case 4:
                random = (int) (Math.random() * specials.length());
                character = specials.charAt(random);
                password += character;
                break;

            default:
                throw new AssertionError();
        }
        return password;
    }
}