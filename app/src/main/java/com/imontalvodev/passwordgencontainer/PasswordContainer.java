package com.imontalvodev.passwordgencontainer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Map;

public class PasswordContainer extends AppCompatActivity {

    private ListView lvPasswords;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> data;

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
        data = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, data);
        lvPasswords.setAdapter(adapter);

        refreshListView();

        lvPasswords.setOnItemClickListener((parent, view, position, id) -> {
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.getMenuInflater().inflate(R.menu.menu_listview, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                String selectedItem = data.get(position);
                int itemId = item.getItemId();
                if (itemId == R.id.editPassword) {
                    editPassword(selectedItem);
                    return true;
                } else if (itemId == R.id.deletePassword) {
                    deletePassword(selectedItem);
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    private void refreshListView() {
        SharedPreferences sharedPreferences = getSharedPreferences("passwords", MODE_PRIVATE);
        Map<String, ?> allData = sharedPreferences.getAll();
        data.clear();

        for (Map.Entry<String, ?> entry : allData.entrySet()) {
            StringBuilder passwordMask = new StringBuilder();
            for (int i = 0; i < entry.getValue().toString().length(); i++) {
                passwordMask.append("*");
            }
            data.add(passwordMask + ":\t" + entry.getKey());
        }

        adapter.notifyDataSetChanged();

        if (data.isEmpty()) {
            Toast.makeText(this, "No existen contraseñas registradas", Toast.LENGTH_LONG).show();
        }
    }

    public void editPassword(String selectedItem) {
        String[] parts = selectedItem.split(":\t");
        if (parts.length < 2) return;
        final String alias = parts[1];

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar contraseña");
        View view = getLayoutInflater().inflate(R.layout.edit_password, null);
        builder.setView(view);

        final TextView tvOldPassword = view.findViewById(R.id.tvOldPassword);
        final EditText etNewPassword = view.findViewById(R.id.etNewPassword);
        tvOldPassword.setText("Alias: " + alias);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String newPassword = etNewPassword.getText().toString();
            if (newPassword.trim().isEmpty()) {
                Toast.makeText(this, "La contraseña no puede estar vacía.", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sharedPreferences = getSharedPreferences("passwords", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(alias, newPassword);
            editor.apply();

            refreshListView();
            Toast.makeText(this, "Contraseña editada correctamente.", Toast.LENGTH_LONG).show();
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    public void deletePassword(String selectedItem) {
        String[] parts = selectedItem.split(":\t");
        if (parts.length < 2) return;
        final String alias = parts[1];

        new AlertDialog.Builder(this)
                .setTitle("Eliminar Contraseña")
                .setMessage("¿Estás seguro de que quieres eliminar la contraseña para \"" + alias + "\"?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    SharedPreferences sharedPreferences = getSharedPreferences("passwords", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(alias);
                    editor.apply();
                    refreshListView();
                    Toast.makeText(this, "Contraseña eliminada", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
