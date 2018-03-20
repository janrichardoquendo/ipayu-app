package ipayuv1.source.cfts.ipayu_v1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Reg_personal extends AppCompatActivity {

    Button btnNext;
    EditText fname,mname,sname,bday;
    Spinner genderSpin;
    int gender=0;

    RadioButton genderM,genderF;
    DatePicker datePicker;
    StringBuilder datePicked;
    Calendar c;
    int year, month, day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_personal);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("REGISTRATION");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#072183")));

        btnNext = (Button)findViewById(R.id.btnNext);
        fname = (EditText)findViewById(R.id.txtFname);
        mname = (EditText)findViewById(R.id.txtMname);
        sname = (EditText)findViewById(R.id.txtSname);
        bday = (EditText)findViewById(R.id.txtDate);
        genderSpin = (Spinner)findViewById(R.id.gender);
//        genderM = (RadioButton)findViewById(R.id.rdoMale);
//        genderF = (RadioButton)findViewById(R.id.rdoFemale);

        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        //datePicker = (DatePicker) findViewById(R.id.datePicker);
//        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
//            @Override
//            public void onDateChanged(DatePicker arg0, int arg1, int arg2, int arg3) {
//                getDatePickerValue(arg1, arg2 + 1, arg3);
//            }
//        });
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, monthOfYear);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        bday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog dialog = new DatePickerDialog(Reg_personal.this, date, year, month,
                        day);
                if (Build.VERSION.SDK_INT >= 11) {
                    dialog.getDatePicker().setCalendarViewShown(false);
                    dialog.getDatePicker().setSpinnersShown(true);
                }
                dialog.setTitle("Birthday");
                dialog.show();
            }
        });

        addItemsOnSpinner();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (genderSpin.getSelectedItem().equals("Male")) {
                    gender = 0;
                } else {
                    gender = 1;
                }

                Toast.makeText(Reg_personal.this, bday.getText().toString()+" "+String.valueOf(gender), Toast.LENGTH_LONG).show();

//                Intent i = new Intent(Reg_personal.this, Reg_contact.class);
//                i.putExtra("fname",fname.getText().toString());
//                i.putExtra("mname",mname.getText().toString());
//                i.putExtra("sname",sname.getText().toString());
//                i.putExtra("bday",String.valueOf(datePicked));
//                i.putExtra("gender",String.valueOf(gender));
//                startActivity(i);
                //Reg_personal.this.finish();
            }
        });
    }

    public void addItemsOnSpinner() {

        List<String> list = new ArrayList<String>();
        list.add("Male");
        list.add("Female");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpin.setAdapter(dataAdapter);
    }

    private void updateLabel() {

        String myFormat = "MMMM dd, yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        bday.setText(sdf.format(c.getTime()));
    }

    public void getDatePickerValue(int year, int month, int day){
        // set current date into textview
        datePicked = new StringBuilder()
                // Month is 0 based, just add 1
                .append(month).append("-").append(day).append("-")
                .append(year).append(" ");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;
        switch(item.getItemId()){
            case android.R.id.home:
                intent = new Intent(this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                this.finish();
                // app icon in action bar clicked; go home
                return true;

            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(this, Login.class));
        this.finish();
    }

}
