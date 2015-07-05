package com.akshaykavthekar.taskr;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;



public class DateActivity extends AppCompatActivity {
    public static String name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        name = i.getExtras().getString("name");
        setContentView(R.layout.activity_date);
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.parseColor("#2196F3"));

    }
    public void complete(){
        DatePicker myDatePicker = (DatePicker) findViewById(R.id.mydatepicker);
        int day = myDatePicker.getDayOfMonth();
        int month = myDatePicker.getMonth();
        int year = myDatePicker.getYear();
        TimePicker myTimePicker = (TimePicker) findViewById(R.id.mytimePicker);
        int hour = myTimePicker.getCurrentHour();
        int min = myTimePicker.getCurrentMinute();
        Intent data = new Intent();
        data.putExtra("mon", month);
        data.putExtra("day", day);
        data.putExtra("yr", year);
        data.putExtra("hr", hour);
        data.putExtra("min", min);
        data.putExtra("name", name);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_date, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            complete();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
