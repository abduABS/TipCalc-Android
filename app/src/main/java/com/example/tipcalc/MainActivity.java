package com.example.tipcalc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener,
        View.OnClickListener, SeekBar.OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    //Set the UI elements
    EditText et_num;
    TextView tv_tipPercent, tv_amount;
    Spinner spinner;
    SeekBar seekBar;
    RadioGroup rb_group;
    RadioButton rb_morning, rb_afternoon, rb_evening;
    CheckBox cb_family, cb_double;
    Button btn_calculate;

    //Set the Java Variables
    float amount = 0;
    int tipPercent = 10;
    float tip = 0;
    float discount = 0;
    float cb_discount = 0;
    int pSplit = 1;
    float total = 0;
    int rb_selected = 0;
    boolean cb_family_checked = false;
    boolean cb_double_checked = false;

    SharedPreferences sharedPreferences;




    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up the Shared Preferences
        sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);

        //Find the IDs
        et_num = findViewById(R.id.et_num);
        tv_tipPercent = findViewById(R.id.tv_tipPercent);
        tv_amount = findViewById(R.id.tv_amount);
        spinner = findViewById(R.id.spinner);
        seekBar = findViewById(R.id.seekBar);
        rb_group = findViewById(R.id.rb_group);
        rb_morning = findViewById(R.id.rb_morning);
        rb_afternoon = findViewById(R.id.rb_afternoon);
        rb_evening = findViewById(R.id.rb_evening);
        cb_family = findViewById(R.id.cb_Family);
        cb_double = findViewById(R.id.cb_Double);
        btn_calculate = findViewById(R.id.btn_calculate);

        //Connect the listeners
        et_num.setOnEditorActionListener(this);
        spinner.setOnItemSelectedListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        rb_group.setOnCheckedChangeListener(this);
        btn_calculate.setOnClickListener(this);
        cb_family.setOnCheckedChangeListener(this);
        cb_double.setOnCheckedChangeListener(this);


        //Setting up the adapter for the Spinner
        ArrayList<String> splits = new ArrayList<>();

        splits.add("Split for 1 person");

        for (int i = 2; i < 5; i++) {
            splits.add("Split for " + i + " people");
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, splits);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //        float amount = 0;
//        int tipPercent = 10;
//        float tip = 0;
//        float discount = 0;
//        float cb_discount = 0;
//        int pSplit = 1;
//        float total = 0;
//        int rb_selected = 0;
//        boolean cb_family_checked = false;
//        boolean cb_double_checked = false;


        amount = sharedPreferences.getFloat("amount", 0.00f);
        et_num.setText(amount + "");

        tipPercent = sharedPreferences.getInt("tipPercent", 10);
        seekBar.setProgress(tipPercent);
        tv_tipPercent.setText(tipPercent + "%");

        tip = sharedPreferences.getFloat("tip", 0.00f);

        discount = sharedPreferences.getFloat("discount", 0.00f);

        cb_discount = sharedPreferences.getFloat("cb_discount", 0.00f);

        pSplit = sharedPreferences.getInt("pSplit", 1);
        spinner.setSelection(pSplit - 1);

        total = sharedPreferences.getFloat("total", 0.00f);

        rb_selected = sharedPreferences.getInt("rb_selected", 0);
        if(rb_selected != 0){
            rb_group.check(rb_selected);
        }

        cb_family_checked = sharedPreferences.getBoolean("cb_family_checked", false);
        cb_family.setChecked(cb_family_checked);

        cb_double_checked = sharedPreferences.getBoolean("cb_double_checked", false);
        cb_double.setChecked(cb_double_checked);


        calculateAndDisplay();




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        myEdit.putFloat("amount", amount);
        myEdit.putFloat("tip", tip);
        myEdit.putFloat("discount", discount);
        myEdit.putFloat("cb_discount", cb_discount);
        myEdit.putFloat("total", total);
        myEdit.putInt("rb_selected", rb_selected);
        myEdit.putInt("tipPercent", tipPercent);
        myEdit.putInt("pSplit", pSplit);
        myEdit.putBoolean("cb_family_checked", cb_family_checked);
        myEdit.putBoolean("cb_double_checked", cb_double_checked);

        myEdit.commit();
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        amount = Float.parseFloat(String.valueOf(textView.getText()));
        Toast.makeText(getApplicationContext(),
                "The amount you have entered is: " + amount, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onClick(View view) {
        calculateAndDisplay();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        tipPercent = i;
        tv_tipPercent.setText(i +  "%");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //pass
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //pass
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        pSplit = i  + 1;
        Toast.makeText(getApplicationContext(),
                pSplit + " people are splitting the tip", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (i == R.id.rb_morning){
            Toast.makeText(getApplicationContext(),
                    "Morning has been selected", Toast.LENGTH_SHORT).show();
            discount = 3;
        }
        else if (i == R.id.rb_afternoon){
            Toast.makeText(getApplicationContext(),
                    "Afternoon has been selected", Toast.LENGTH_SHORT).show();
            discount = 2;
        }
        else if (i == R.id.rb_evening){
            Toast.makeText(getApplicationContext(),
                    "Evening has been selected", Toast.LENGTH_SHORT).show();
            discount = 1;
        }

        rb_selected = i;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == R.id.cb_Family){
            if (compoundButton.isChecked()){
                cb_family_checked = true;
                Toast.makeText(getApplicationContext(),
                        "Family discount has been checked", Toast.LENGTH_SHORT).show();
                cb_discount += 3;
            }
            else{
                cb_family_checked = false;
                Toast.makeText(getApplicationContext(),
                        "Family discount has stopped being checked", Toast.LENGTH_SHORT).show();
                cb_discount -= 3;
            }

        }
        else if (compoundButton.getId() == R.id.cb_Double){
            if (compoundButton.isChecked()){
                cb_double_checked = true;
                Toast.makeText(getApplicationContext(),
                        "Double discount has been checked", Toast.LENGTH_SHORT).show();
                cb_discount += 2;
            }
            else{
                cb_double_checked = false;
                Toast.makeText(getApplicationContext(),
                        "Double discount has stopped being checked", Toast.LENGTH_SHORT).show();
                cb_discount -= 2;
            }
        }
    }

    public void calculateAndDisplay(){
        total = amount - ((amount * ( (discount + cb_discount) / 100f)));
        tip = total * (tipPercent/100f);
        total = total + tip;

        total = total / pSplit;

        tv_amount.setText("$" + String.format("%.2f", total));


    }
}