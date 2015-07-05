package com.akshaykavthekar.taskr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class MainPage extends AppCompatActivity {

    List<CheckBox> cbs = new ArrayList<>();
    ArrayList<Date> dates = new ArrayList<>();
    public static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#2196F3"));

        TextView displayDate = (TextView) findViewById(R.id.date);
        final Calendar c = Calendar.getInstance();

        DateFormat df = new SimpleDateFormat("EEEE MM/dd/yy");
        String reportDate = df.format(c.getTime());
        displayDate.setText("Today's date is: " + reportDate);

        SharedPreferences settings = this.getSharedPreferences("YourActivityPreferences", Context.MODE_PRIVATE);

        int size = settings.getInt("size", 0);
        long date;
        if(size > 0){
            for(int i = 0; i < size; ++i){
                date = settings.getLong(("date" + String.valueOf(i)), 0);
                String x = settings.getString("task" + String.valueOf(i), "");
                Date d = new Date((date));
                addTask(x, d);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_page, menu);
        return true;
    }
    public void sendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.editMessage);
        String message = editText.getText().toString();
        if (!message.isEmpty()) {
            Intent i = new Intent(this, DateActivity.class);
            i.putExtra("name", message);
            startActivityForResult(i, 1);

        }
        editText.setText(null);
    }
    //REDESIGN
    private void addTask(String task, Date date){
        int temp = 0;
        for(int i = 0; i < count; ++i){
            if(date.after(dates.get(i))){
                temp = i + 1;
            }
        }
        if(!task.isEmpty()){
            LinearLayout featuresTable = (LinearLayout) findViewById(R.id.failure_reasonslist);
            cbs.add(temp, new CheckBox(this));
            dates.add(temp, date);
            (cbs.get(temp)).setText(task);
            (cbs.get(temp)).setTextSize(16);
            //(cbs.get(temp)).setVisibility(View.VISIBLE);
            featuresTable.addView((cbs.get(temp)), temp);
            (cbs.get(temp)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onCheckboxClicked(v);
                }
            });
            count++;
        }

    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        boolean uncheck = true;
        if(checked){
            ((CheckBox) view).setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            Button clear = (Button) findViewById(R.id.clear);
            clear.setVisibility(View.VISIBLE);
            uncheck = false;
        }
        else{
            ((CheckBox) view).setPaintFlags(((CheckBox) view).getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            for(int i = 0; i < cbs.size(); ++i){
                if((cbs.get(i)).isChecked()){
                    uncheck = false;
                }
            }
        }
        if(uncheck){
            Button clear = (Button) findViewById(R.id.clear);
            clear.setVisibility(View.INVISIBLE);
        }
    }
    //REDESIGN
    public void clearTasks(View view){
        int temp = count - 1;
        boolean checked;
        while(temp >= 0){
            checked = (cbs.get(temp)).isChecked();
            if(checked){
                LinearLayout featuresTable = (LinearLayout) findViewById(R.id.failure_reasonslist);
                featuresTable.removeViewAt(temp);
                //(cbs.get(temp)).setVisibility(View.GONE);
                cbs.remove(temp);
                dates.remove(temp);
                count--;
            }
            temp--;
        }

        if(temp < 0){
            view.setVisibility(View.GONE);
        }
        if(cbs.isEmpty()){
            dates.clear();
        }
    }
    public void clearAll(){
        while(count > 0){
            (cbs.get((count-1))).setVisibility(View.GONE);
            count--;
        }
        cbs.clear();
        dates.clear();
        Button clear = (Button) findViewById(R.id.clear);
        if(clear.getVisibility() == (View.VISIBLE))
            clear.setVisibility(View.INVISIBLE);
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        int month, day, year, hour, min;
        String name;
        if(requestCode==1)
        {

            if(resultCode==RESULT_OK)
            {
                //get Data
                month =  data.getExtras().getInt("mon");
                day =  data.getExtras().getInt("day");
                year =  data.getExtras().getInt("yr");
                hour =  data.getExtras().getInt("hr");
                min =  data.getExtras().getInt("min");
                name = data.getExtras().getString("name");

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DATE, day);
                calendar.set(Calendar.HOUR, hour);
                calendar.set(Calendar.MINUTE, min);
                calendar.set( Calendar.AM_PM, Calendar.AM );
                Date date = calendar.getTime();
                DateFormat df = new SimpleDateFormat("  (EEE MM/dd/yy h:mm a)");
                String reportDate = df.format(date);
                String message = name + reportDate;
                addTask(message, date);
            }

        }
    }
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        SharedPreferences settings = this.getSharedPreferences("YourActivityPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        settings.edit().clear().apply();
        String x;
        long dateTime;
// Add the new value.
        for(int i = 0; i < cbs.size(); ++i){
            x = (String) (cbs.get(i)).getText();
            dateTime = (dates.get(i)).getTime();
            if(!(x.isEmpty())){
                editor.putString(("task" + String.valueOf(i)), x);
                editor.putLong(("date" + String.valueOf(i)), dateTime);
                editor.apply();
            }
        }

        editor.putInt("size", cbs.size());
        editor.apply();
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onPause(){
        SharedPreferences settings = this.getSharedPreferences("YourActivityPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        settings.edit().clear().apply();
        String x;
// Add the new value.
        long dateTime;
// Add the new value.
        for(int i = 0; i < cbs.size(); ++i){
            x = (String) (cbs.get(i)).getText();
            dateTime = (dates.get(i)).getTime();
            if(!(x.isEmpty())){
                editor.putString(("task" + String.valueOf(i)), x);
                editor.putLong(("date" + String.valueOf(i)), dateTime);
                editor.apply();
            }
        }

        editor.putInt("size", cbs.size());
        editor.apply();
        super.onPause();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            clearAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

