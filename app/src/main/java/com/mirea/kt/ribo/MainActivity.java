package com.mirea.kt.ribo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private String login;
    private String password;
    private String group;

    private boolean loadTheme() {
        SharedPreferences preferences = getSharedPreferences("ThemeSwitcher", MODE_PRIVATE);
        return preferences.getBoolean("isDarkTheme", false);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (loadTheme()) {
            setTheme(R.style.Theme_ThemeSwitcher_Dark);
        } else {
            setTheme(R.style.Theme_ThemeSwitcher_Light);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String server = "https://android-for-students.ru";
        String serverPath = "/coursework/login.php";
        //login = "Student84953";
        //password = "iZF8k4n";

        Button enterBtn = findViewById(R.id.enterBtn);
        TextView tvError = findViewById(R.id.logError);
        TextInputEditText etLogin = findViewById(R.id.enter_login);
        TextInputEditText etPassword = findViewById(R.id.enter_pass);
        enterBtn.setOnClickListener(v -> {
            try {
                login = etLogin.getText().toString();
                password = etPassword.getText().toString();
                Log.i("my_task", login);
                Log.i("my_task", password);
                group = "RIBO-04-22";
                HashMap<String, String> map = new HashMap<>();
                HTTPRunnable httpRunnable = new HTTPRunnable(server + serverPath, map);
                Thread th = new Thread(httpRunnable);
                map.put("lgn", login);
                map.put("pwd", password);
                map.put("g", group);
                th.start();
                try {
                    th.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        JSONObject jsonObject = new JSONObject(httpRunnable.getResponseBody());
                        Log.i("my_task", "Title: " + jsonObject.getString("title"));
                        Log.i("my_task", "Task: " + jsonObject.getString("task"));
                        Log.i("my_task", "Variant: " + jsonObject.getString("variant"));
                        System.out.println(jsonObject);
                        tvError.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        Intent loadingPageIntent = new Intent(this, LoadingActivity.class);
                        startActivity(loadingPageIntent);
                    }
                }
            }catch (RuntimeException e){
                Log.i("my_task", "Error, invalid login or pass");
                tvError.setVisibility(View.VISIBLE);
            }
        });
    }
}