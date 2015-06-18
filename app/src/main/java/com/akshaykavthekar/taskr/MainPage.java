package com.akshaykavthekar.taskr;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MainPage extends AppCompatActivity {
    List<CheckBox> cbs = new ArrayList<>();
    public static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#2196F3"));
        SharedPreferences settings = this.getSharedPreferences("YourActivityPreferences", Context.MODE_PRIVATE);
        Set<String> myStrings = settings.getStringSet("myStrings", new HashSet<String>());
        for (String s : myStrings) {
            addTask(s);
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
            addTask(message);
        }
        editText.setText(null);
    }
    private void addTask(String task){
        LinearLayout featuresTable = (LinearLayout) findViewById(R.id.failure_reasonslist);
        cbs.add(new CheckBox(this));
        (cbs.get(count)).setText(task);
        (cbs.get(count)).setVisibility(View.VISIBLE);
        featuresTable.addView((cbs.get(count)));
        (cbs.get(count)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCheckboxClicked(v);
            }
        });
        count++;


    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        if(checked){
            ((CheckBox) view).setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            Button clear = (Button) findViewById(R.id.clear);
            clear.setVisibility(View.VISIBLE);
        }
        else{
            ((CheckBox) view).setPaintFlags(((CheckBox) view).getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    public void clearTasks(View view){
        int temp = count - 1;
        boolean checked;
        while(temp >= 0){
            checked = (cbs.get(temp)).isChecked();
            if(checked){
                (cbs.get(temp)).setVisibility(View.GONE);
                cbs.remove(temp);
                count--;
            }
            temp--;
        }

        if(temp < 0){
            view.setVisibility(View.GONE);
        }
    }
    public void clearAll(){
        while(count > 0){
            (cbs.get((count-1))).setVisibility(View.GONE);
            count--;
        }
        cbs.clear();
        Button clear = (Button) findViewById(R.id.clear);
        if(clear.getVisibility() == (View.VISIBLE))
            clear.setVisibility(View.INVISIBLE);
    }
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        SharedPreferences settings = this.getSharedPreferences("YourActivityPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        settings.edit().clear().commit();
        Set<String> myStrings = settings.getStringSet("myStrings", new HashSet<String>());
        myStrings.clear();
        settings.edit().clear().commit();
        String x;
// Add the new value.
        for(int i = 0; i < cbs.size(); ++i){
            x = (String) (cbs.get(i)).getText();
            if(!(x.isEmpty()))
                myStrings.add(x);
        }


// Save the list.
        editor.putStringSet("myStrings", myStrings);
        editor.commit();
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
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
