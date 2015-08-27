package com.tizi.quanzi.ui.NewGroup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

import com.tizi.quanzi.R;

public class NewGroupActivity extends AppCompatActivity {
    NewGroupStep1Fragment newGroupStep1Fragment;
    NewGroupStep1Fragment.NewGroupStep1Ans ans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        newGroupStep1Fragment = new NewGroupStep1Fragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, newGroupStep1Fragment).commit();
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_next_step) {
            NewGroupStep1Fragment.NewGroupStep1Ans temp = newGroupStep1Fragment.getNewGroupAns();
            if (temp.complete) {
                ans = temp;
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        newGroupStep1Fragment.onIntentResult(requestCode, resultCode, data);
    }
}
