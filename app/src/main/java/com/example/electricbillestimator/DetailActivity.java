package com.example.electricbillestimator;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    TextView textViewMonth, textViewUnits, textViewRebate, textViewTotal, textViewFinal;
    DBHelper db;
    int billId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Bill Details");
        setContentView(R.layout.activity_detail);

        // Enable back button in the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textViewMonth = findViewById(R.id.textViewMonth);
        textViewUnits = findViewById(R.id.textViewUnits);
        textViewRebate = findViewById(R.id.textViewRebate);
        textViewTotal = findViewById(R.id.textViewTotal);
        textViewFinal = findViewById(R.id.textViewFinal);
        db = new DBHelper(this);

        billId = getIntent().getIntExtra("bill_id", -1);
        if (billId != -1) {
            loadBillDetails(billId);
        } else {
            Toast.makeText(this, "Invalid Bill ID", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadBillDetails(int id) {
        Cursor c = db.getBillById(id);
        if (c != null && c.moveToFirst()) {
            String month = c.getString(c.getColumnIndexOrThrow("month"));
            int units = c.getInt(c.getColumnIndexOrThrow("units"));
            double rebate = c.getDouble(c.getColumnIndexOrThrow("rebate"));
            double total = c.getDouble(c.getColumnIndexOrThrow("total"));
            double finalCost = c.getDouble(c.getColumnIndexOrThrow("final"));

            textViewMonth.setText("Month: " + month);
            textViewUnits.setText("Units Used: " + units + " kWh");
            textViewRebate.setText("Rebate: " + (int) (rebate * 100) + "%");
            textViewTotal.setText("Total Charge: RM " + String.format("%.2f", total));
            textViewFinal.setText("Final Cost after Rebate: RM " + String.format("%.2f", finalCost));

            c.close();
        } else {
            Toast.makeText(this, "No record found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    // Handle ActionBar back button press
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and go back
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
