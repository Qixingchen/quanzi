package com.tizi.quanzi;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.tool.GetPassword;

import java.util.Map;
import java.util.TreeMap;


public class LoginActivity extends AppCompatActivity {

    private android.widget.EditText phoneNumberEditText;
    private android.widget.EditText passwordEditText;
    private android.widget.Button LoginButton;
    private Activity mActivity;
    private android.support.design.widget.TextInputLayout phoneNumberInputLayout;
    private android.support.design.widget.TextInputLayout passwordInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mActivity = this;


        this.passwordInputLayout = (TextInputLayout) findViewById(R.id.passwordInputLayout);
        this.phoneNumberInputLayout = (TextInputLayout) findViewById(R.id.phoneNumberInputLayout);
        phoneNumberInputLayout.setError("长度为11位");
        this.LoginButton = (Button) findViewById(R.id.LoginButton);
        this.passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        this.phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);

        final Response.Listener<String> mOKListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //todo 成功
            }
        };
        final Response.ErrorListener mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(findViewById(R.id.LoginLayout), "网络错误", Snackbar.LENGTH_LONG).show();
            }
        };

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phonenumber = phoneNumberEditText.getText().toString();
                String password = GetPassword.hashans(passwordEditText.getText().toString());
                Map<String, String> loginPara = new TreeMap<>();
                loginPara.put("phonenumber", phonenumber);
                loginPara.put("password", password);
                GetVolley.getmInstance(mActivity).setOKListener(mOKListener).
                        setErrorListener(mErrorListener)
                        .addRequestNoSign(Request.Method.GET, "http://123", loginPara);
            }
        });
        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkPhoneNumber();
            }
        });
    }

    private boolean checkPhoneNumber() {
        String phoneNumber = phoneNumberEditText.getText().toString();
        if (phoneNumber.length() != 11) {
            phoneNumberInputLayout.setErrorEnabled(true);
            return false;
        } else {
            phoneNumberInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
