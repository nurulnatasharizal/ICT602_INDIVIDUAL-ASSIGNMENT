package com.example.electricbillestimator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ListView listView;
    DBHelper db;
    ArrayList<String> records = new ArrayList<>();
    ArrayList<Integer> ids = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Saved Bills");
        setContentView(R.layout.activity_history);

        listView = findViewById(R.id.listViewHistory);
        db = new DBHelper(this);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, records);
        listView.setAdapter(adapter);

        loadData();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(this, DetailActivity.class);
            i.putExtra("bill_id", ids.get(position));
            startActivity(i);
        });
    }

    void loadData() {
        Cursor c = db.getAllBills();
        if (c.moveToFirst()) {
            do {
                ids.add(c.getInt(0));
                String month = c.getString(1);
                double finalCost = c.getDouble(5);
                records.add(month + ": RM " + String.format("%.2f", finalCost));
            } while (c.moveToNext());
        }
        c.close();
        adapter.notifyDataSetChanged();
    }
}