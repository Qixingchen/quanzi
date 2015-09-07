package com.tizi.quanzi.ui.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.tizi.quanzi.R;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.log.Log;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private MainFragmentPagerAdapter mainFragmentPagerAdapter;
    private ViewPager.OnPageChangeListener onPageChangeListener;

    private MainFragment mainFragment;
    private PrivateMessageFragment privateMessageFragment;

    //toolbar
    private Toolbar toolbar;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, mainFragment).commit();

        PrivateMessPairList.getInstance().addOnChangeCallBack(new PrivateMessPairList.OnChangeCallBack() {
            @Override
            public void changed() {
                menu.findItem(R.id.action_private_message).setTitle("私信（" + PrivateMessPairList.getInstance().getUnreadNum() + "）条");
            }
        });


        //Drawable logo = getDrawable(R.drawable.face);
        //toolbar.setLogo(R.drawable.face);
        //        for (int i = 0; i < toolbar.getChildCount(); i++) {
        //            View child = toolbar.getChildAt(i);
        //            if (child != null)
        //                if (child.getClass() == ImageView.class) {
        //                    ImageView iv2 = (ImageView) child;
        //                    if (iv2.getDrawable() == logo) {
        //                        iv2.setAdjustViewBounds(true);
        //                    }
        //                }
        //        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //        mViewPager.removeOnPageChangeListener(getOnPageChangeListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
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
            return true;
        }
        if (id == R.id.action_private_message) {
            privateMessageFragment = new PrivateMessageFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, privateMessageFragment)
                    .addToBackStack("privateMessageFragment").commit();
        }

        return super.onOptionsItemSelected(item);
    }
}
