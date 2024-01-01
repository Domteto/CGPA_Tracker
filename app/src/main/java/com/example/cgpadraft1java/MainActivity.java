package com.example.cgpadraft1java;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // declaration
    EditText gradeEditText, creditPointsEditText, moduleNameEditText;
    Button addModuleButton, deleteModuleButton, forecastButton, clearForecastButton, viewAllModulesButton;
    TextView cgpaTextView, totalModulesTextView, forecastedModulesTextView,moduledetailsTextView;

    SQLiteDatabase tdb;
    List<Module> forecastedModules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gradeEditText = findViewById(R.id.gradeEditText);
        creditPointsEditText = findViewById(R.id.creditPointsEditText);
        moduleNameEditText = findViewById(R.id.moduleNameEditText);
        addModuleButton = findViewById(R.id.addModuleButton);
        deleteModuleButton = findViewById(R.id.deleteModuleButton);
        forecastButton = findViewById(R.id.forecastButton);
        clearForecastButton = findViewById(R.id.clearForecastButton);
        viewAllModulesButton = findViewById(R.id.viewAllModulesButton);
        cgpaTextView = findViewById(R.id.cgpaTextView);
        totalModulesTextView = findViewById(R.id.totalModulesTextView);
        forecastedModulesTextView = findViewById(R.id.forecastedModulesTextView);
        moduledetailsTextView = findViewById(R.id.moduledetailsTextView);

        // To open or create the database
        tdb = openOrCreateDatabase("CGPADatabase", MODE_PRIVATE, null);

        // Create the "modules" table incase it doesn't exist
        tdb.execSQL("CREATE TABLE IF NOT EXISTS modules (id INTEGER PRIMARY KEY AUTOINCREMENT, module_name TEXT, grade REAL, credit_points REAL)");

        // creatiing array list to store forecasted modules as an object of module class.
        forecastedModules = new ArrayList<>();

        addModuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addModule();
                calculateAndDisplayCGPA();
                calculateModulesCount();
                // update only happens in case the view module was already on
                updateViewedModules();
            }
        });

        deleteModuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteModule();
                calculateAndDisplayCGPA();
                calculateModulesCount();
                updateViewedModules();
            }
        });

        forecastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forecastCGPA();
                calculateModulesCount();
            }
        });

        clearForecastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearForecastedModules();
                calculateAndDisplayCGPA();
                calculateModulesCount();
                // to remove the forecast module count in the button of the page as
                // there will be no more forecasts
                // this was added after testing and making sure that everything worked as expected
                forecastedModulesTextView.setText("");
            }
        });

        viewAllModulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAllModules();
                calculateModulesCount();
            }
        });

        // to view the cpga and the number of modules in the page even
        // before pressing any buttons as this acts like a dash board
        calculateAndDisplayCGPA();
        calculateModulesCount();

    }

    private void addModule() {
        // Get input values
        String moduleName = moduleNameEditText.getText().toString().trim();
        if (moduleName.isEmpty()) {
            // Set a default name if the user doesn't provide one
            moduleName = "Module " + (ModulesCount() + 1);
            calculateModulesCount();
        }


        String gradecheck = gradeEditText.getText().toString();
        String creditcheck =creditPointsEditText.getText().toString();
        if(!gradecheck.isEmpty() && !creditcheck.isEmpty()){
            double grade = Double.parseDouble(gradeEditText.getText().toString());
            double creditPoints = Double.parseDouble(creditPointsEditText.getText().toString());

            // Insert module into the "modules" table
            tdb.execSQL("INSERT INTO modules (module_name, grade, credit_points) VALUES (?, ?, ?)", new Object[]{moduleName, grade, creditPoints});

            clearinputs();
        }
        else {
            Toast.makeText(MainActivity.this, "Please Fill Module Details", Toast.LENGTH_SHORT).show();
        }


    }
    private void clearinputs(){
        // Clear input fields after adding a module
        gradeEditText.setText("");
        creditPointsEditText.setText("");
        moduleNameEditText.setText("");
    }

    private void deleteModule() {
        // Get the module name to delete
        String moduleNameToDelete = moduleNameEditText.getText().toString().trim();
        if (!moduleNameToDelete.isEmpty()){
            // Delete the module from the database
            tdb.execSQL("DELETE FROM modules WHERE module_name = ?", new Object[]{moduleNameToDelete});

            // Clear the deleteModuleNameEditText after deletion
            moduleNameEditText.setText("");

            clearinputs();
        }else {
            Toast.makeText(MainActivity.this, "Please enter the name of the module you want to be deleted", Toast.LENGTH_SHORT).show();

        }


    }

    private void calculateAndDisplayCGPA() {
        // Calculate CGPA based on stored modules
        double cgpa = calculateCGPA();
        // Display CGPA in the TextView
        String cgpas = Double.toString(cgpa);
        cgpaTextView.setText(String.format("CGPA: "+ cgpa));
    }

    private double calculateCGPA() {
        // Calculate CGPA based on stored modules in the database
        Cursor cursor = tdb.rawQuery("SELECT SUM(grade * credit_points), SUM(credit_points) FROM modules", null);
        double totalGradeCreditProduct = 0;
        double totalCreditPoints = 0;

        if (cursor.moveToFirst()) {
            totalGradeCreditProduct = cursor.getDouble(0);
            totalCreditPoints = cursor.getDouble(1);
        }

        cursor.close();

        // Include the effect of forecasted modules
        for (Module module : forecastedModules) {
            totalGradeCreditProduct += (module.getGrade() * module.getCreditPoints());
            totalCreditPoints += module.getCreditPoints();
        }

        double cgpa = (totalGradeCreditProduct / totalCreditPoints);
        // to make the cpga 2dp as previously it would show a long number bec of the decimal places
        // as well as it rounds up to the 2nd decimal place
        double roundedValue = Math.round(cgpa * 100.0) / 100.0;
        return roundedValue;
    }


    private void forecastCGPA() {
        //getting the module name and removing any spaces given by mistake
        String moduleName = moduleNameEditText.getText().toString().trim();
        if (moduleName.isEmpty()) {
            // providing a default name to the module incase the user didnt give any
            // as module name is not compulsory for this function
            moduleName = "Forecasted " + (forecastedModules.size() + 1);
        }
        String gradecheck = gradeEditText.getText().toString();
        String creditcheck =creditPointsEditText.getText().toString();

        //checking if the required fields are filled and if not a toast will appear asking to fill them
        if(!gradecheck.isEmpty() && !creditcheck.isEmpty()){
            double grade = Double.parseDouble(gradeEditText.getText().toString());
            double creditPoints = Double.parseDouble(creditPointsEditText.getText().toString());

            // Adding the forecast module to the list
            forecastedModules.add(new Module(moduleName, grade, creditPoints));

            // recalulate the cgpa after taking into account the new forecast module
            calculateAndDisplayCGPA();

            // Calculate the number of forecasted modules by checking the size of the list
            forecastedModulesTextView.setText(String.format("Forecasted Modules: "+ Integer.toString(forecastedModules.size())));

            clearinputs();
        }
        else {
            // error message to fill the required details to complete the process
            Toast.makeText(MainActivity.this, "Please Fill Module Details", Toast.LENGTH_LONG).show();
        }

    }

    private void clearForecastedModules() {
        // Clear the list of forecasted modules
        forecastedModules.clear();

        // Update the forecastedModulesTextView
        forecastedModulesTextView.setText(String.format("Forecasted Modules: %d", forecastedModules.size()));
        calculateAndDisplayCGPA();
    }

    private double calculateTotalCreditPoints() {
        // Calculate total credit points from stored modules
        Cursor cursor = tdb.rawQuery("SELECT SUM(credit_points) FROM modules", null);
        double totalCreditPoints = 0;
        if (cursor.moveToFirst()) {
            totalCreditPoints = cursor.getDouble(0);
        }
        cursor.close();
        return totalCreditPoints;
    }

    private void displayAllModules() {
        if(viewAllModulesButton.getText().toString().equals("View All Modules")){
            // Retrieve all modules from the database
            Cursor cursor = tdb.rawQuery("SELECT * FROM modules", null);

            // Display modules in the TextView
            StringBuilder modulesInfo = new StringBuilder("All Modules:\n");
            int totalModules = 0;
            while (cursor.moveToNext()) {
                String moduleName = cursor.getString(cursor.getColumnIndex("module_name"));
                double grade = cursor.getDouble(cursor.getColumnIndex("grade"));
                double creditPoints = cursor.getDouble(cursor.getColumnIndex("credit_points"));

                modulesInfo.append("Module Name: ").append(moduleName)
                        .append(", Grade: ").append(grade)
                        .append(", Credit Points: ").append(creditPoints)
                        .append("\n");

                totalModules++;
            }
            cursor.close();

            moduledetailsTextView.setText(modulesInfo.toString());
            viewAllModulesButton.setText("Hide All Modules");
        }
        else {
            viewAllModulesButton.setText("View All Modules");
            moduledetailsTextView.setText("");
        }
    }
    private void updateViewedModules(){
        if(viewAllModulesButton.getText().toString().equals("Hide All Modules")){
            // Retrieve all modules from the database
            Cursor cursor = tdb.rawQuery("SELECT * FROM modules", null);

            // Display modules in the TextView
            StringBuilder modulesInfo = new StringBuilder("All Modules:\n");
            int totalModules = 0;
            while (cursor.moveToNext()) {
                String moduleName = cursor.getString(cursor.getColumnIndex("module_name"));
                double grade = cursor.getDouble(cursor.getColumnIndex("grade"));
                double creditPoints = cursor.getDouble(cursor.getColumnIndex("credit_points"));

                modulesInfo.append("Module Name: ").append(moduleName)
                        .append(", Grade: ").append(grade)
                        .append(", Credit Points: ").append(creditPoints)
                        .append("\n");

                totalModules++;
            }
            cursor.close();

            moduledetailsTextView.setText(modulesInfo.toString());
            viewAllModulesButton.setText("Hide All Modules");
        }
    }
    private void calculateModulesCount() {
        // Calculate the number of modules in the database
        Cursor cursor = tdb.rawQuery("SELECT COUNT(*) FROM modules", null);
        int moduleCount = 0;
        if (cursor.moveToFirst()) {
            moduleCount = cursor.getInt(0);
        }
        cursor.close();

        // Display the total number of modules
        totalModulesTextView.setText(String.format("Total Modules: %d", moduleCount));
    }
    private int ModulesCount(){
        Cursor cursor = tdb.rawQuery("SELECT COUNT(*) FROM modules", null);
        int moduleCount = 0;
        if (cursor.moveToFirst()) {
            moduleCount = cursor.getInt(0);
        }
        cursor.close();
        return moduleCount;
    }

    @Override
    protected void onDestroy() {
        // Close the database when the activity is destroyed
        if (tdb != null) {
            tdb.close();
        }
        super.onDestroy();
    }
}
