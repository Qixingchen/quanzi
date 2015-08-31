package com.tizi.quanzi.ui.QuanziZone;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.tizi.quanzi.R;
import com.tizi.quanzi.ui.BaseActivity;

public class QuanziZoneActivity extends BaseActivity {
    private Toolbar toolbar;
    private QuanziIntroduceFragment quanziIntroduceFragment;
    private QuanziSetFragment quanziSetFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanzi_zone);


    }

    @Override
    protected void findView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    protected void initView() {
        toolbar.setTitle("Detail");
        setSupportActionBar(toolbar);
        quanziIntroduceFragment = new QuanziIntroduceFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, quanziIntroduceFragment).commit();
    }

    @Override
    protected void setOnClick() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quanzi_zone, menu);
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
            quanziSetFragment = new QuanziSetFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, quanziSetFragment).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
