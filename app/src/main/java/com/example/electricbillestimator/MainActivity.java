package com.example.electricbillestimator;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerMonth;
    EditText editTextUnits;
    RadioGroup radioGroupRebate;
    TextView textTotal, textFinal;
    DBHelper db;
    Button btnCalculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Electric Bill Estimator");
        setContentView(R.layout.activity_main);

        spinnerMonth = findViewById(R.id.spinnerMonth);
        editTextUnits = findViewById(R.id.editTextUnits);
        radioGroupRebate = findViewById(R.id.radioGroupRebate);
        textTotal = findViewById(R.id.textTotalCharge);
        textFinal = findViewById(R.id.textFinalCost);
        btnCalculate = findViewById(R.id.buttonCalculate);
        db = new DBHelper(this);

        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, months);
        spinnerMonth.setAdapter(adapter);

        btnCalculate.setOnClickListener(v -> calculateAndSave());

        findViewById(R.id.buttonViewHistory).setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));

        btnCalculate.setEnabled(false); // Initially disabled

        // Enable calculate only if units input valid & rebate selected
        editTextUnits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateInputsAndToggleButton();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        radioGroupRebate.setOnCheckedChangeListener((group, checkedId) -> validateInputsAndToggleButton());
    }

    private void validateInputsAndToggleButton() {
        String unitsInput = editTextUnits.getText().toString().trim();
        boolean isUnitsValid = false;

        if (!unitsInput.isEmpty()) {
            try {
                int units = Integer.parseInt(unitsInput);
                if (units >= 0) {
                    isUnitsValid = true;
                    editTextUnits.setError(null);
                } else {
                    editTextUnits.setError("Units cannot be negative");
                }
            } catch (NumberFormatException e) {
                editTextUnits.setError("Please enter a valid number");
            }
        } else {
            editTextUnits.setError(null);
        }

        boolean isRebateSelected = radioGroupRebate.getCheckedRadioButtonId() != -1;

        btnCalculate.setEnabled(isUnitsValid && isRebateSelected);
    }

    private double getRebatePercentage() {
        int checkedId = radioGroupRebate.getCheckedRadioButtonId();
        if (checkedId == R.id.rebate0) return 0.0;
        else if (checkedId == R.id.rebate1) return 0.01;
        else if (checkedId == R.id.rebate2) return 0.02;
        else if (checkedId == R.id.rebate3) return 0.03;
        else if (checkedId == R.id.rebate4) return 0.04;
        else if (checkedId == R.id.rebate5) return 0.05;
        else return 0.0;
    }

    void calculateAndSave() {
        String month = spinnerMonth.getSelectedItem().toString();
        String unitStr = editTextUnits.getText().toString().trim();

        if (unitStr.isEmpty()) {
            editTextUnits.setError("Please enter the number of units used");
            editTextUnits.requestFocus();
            return;
        }

        int units;
        try {
            units = Integer.parseInt(unitStr);
            if (units < 0) {
                editTextUnits.setError("Units cannot be negative");
                editTextUnits.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            editTextUnits.setError("Please enter a valid number");
            editTextUnits.requestFocus();
            return;
        }

        if (radioGroupRebate.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a rebate", Toast.LENGTH_SHORT).show();
            return;
        }

        double rebate = getRebatePercentage();

        double total = calculateCharge(units);
        double finalCost = total - (total * rebate);

        textTotal.setText(getString(R.string.total_charge, total));
        textFinal.setText(getString(R.string.final_cost_after_rebate, finalCost));

        db.insertBill(month, units, rebate, total, finalCost);
    }

    double calculateCharge(int kWh) {
        double total;
        if (kWh <= 200) {
            total = kWh * 0.218;
        } else if (kWh <= 300) {
            total = 200 * 0.218 + (kWh - 200) * 0.334;
        } else if (kWh <= 600) {
            total = 200 * 0.218 + 100 * 0.334 + (kWh - 300) * 0.516;
        } else {
            total = 200 * 0.218 + 100 * 0.334 + 300 * 0.516 + (kWh - 600) * 0.546;
        }
        return total;
    }
}
