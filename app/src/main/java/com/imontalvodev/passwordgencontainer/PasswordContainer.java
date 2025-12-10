package com.imontalvodev.passwordgencontainer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Map;

public class PasswordContainer extends AppCompatActivity {

    private ListView lvPasswords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_password_container);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        lvPasswords = findViewById(R.id.lvPasswords);

        SharedPreferences sharedPreferences = getSharedPreferences("passwords", MODE_PRIVATE);
        Map<String, ?> allData = sharedPreferences.getAll();
        ArrayList<String> data = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allData.entrySet()) {
            data.add(entry.getKey() + ":\t" + entry.getValue());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, data);
        lvPasswords.setAdapter(adapter);

        if (data.isEmpty()) {
            Toast.makeText(this, "No existen contraseñas registradas", Toast.LENGTH_LONG).show();
        }
    }
}