package salam.ziswaf.ziswafsalam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.android.caching.FileCacher;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends Activity implements OnClickListener{
    private String username,password;
    private Button ok;
    private EditText editTextUsername,editTextPassword;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    String cUser="";
    FileCacher<String> stringCacher;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ok = (Button)findViewById(R.id.login);
        ok.setOnClickListener(this);
        editTextUsername = (EditText)findViewById(R.id.uname);
        editTextPassword = (EditText)findViewById(R.id.pwd);
        saveLoginCheckBox = (CheckBox)findViewById(R.id.ingat);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            editTextUsername.setText(loginPreferences.getString("username", ""));
            editTextPassword.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
            //doSomethingElse();
        }

    }

    public void onClick(View view) {
        if (view == ok) {

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editTextUsername.getWindowToken(), 0);

            username = editTextUsername.getText().toString();
            password = editTextPassword.getText().toString();

            String uri = "http://keuangan.sekolahalambogor.id/json/json_user/"+username+'/'+password;
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(uri, new JsonHttpResponseHandler(){
                @Override
                public void onStart() {
                    super.onStart();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    try {

                        stringCacher= new FileCacher<>(LoginActivity.this, "datauser.txt");
                        stringCacher.writeCache(response.toString());

                        JSONObject jsonObject = new JSONObject(response.toString());
                        String status = jsonObject.getString("response");
                        Log.d("GETJENIS", "onSuccess: " + '[' + response.toString() + ']');
                        cUser=response.toString();
                        if(status.equals("success")) {


                            if (saveLoginCheckBox.isChecked()) {
                                loginPrefsEditor.putBoolean("saveLogin", true);
                                loginPrefsEditor.putString("username", username);
                                loginPrefsEditor.putString("password", password);
                                loginPrefsEditor.commit();
                            } else {
                                loginPrefsEditor.clear();
                                loginPrefsEditor.commit();
                            }

                            doSomethingElse();

                        }
                        else {
                            Toast toast = Toast.makeText(
                                    getApplicationContext(), "Login Gagal, Email/Password Anda Masih Salah", Toast.LENGTH_LONG
                            );
                            toast.setGravity(Gravity.BOTTOM,0,0);
                            toast.show();
                        }

                    }
                    catch (JSONException e)
                    {
                        Toast toast = Toast.makeText(
                                getApplicationContext(), e.toString(), Toast.LENGTH_LONG
                        );
                        toast.setGravity(Gravity.BOTTOM,0,0);
                        toast.show();
                    }
                    catch (IOException e) {
                        Toast toast = Toast.makeText(
                                getApplicationContext(), e.toString(), Toast.LENGTH_LONG
                        );
                        toast.setGravity(Gravity.BOTTOM,0,0);
                        toast.show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.d("GETJENIS", "onFailure: "+responseString);
                    Toast toast = Toast.makeText(
                            getApplicationContext(), responseString, Toast.LENGTH_LONG
                    );
                    toast.setGravity(Gravity.BOTTOM,0,0);
                    toast.show();
                }
            });

        }
    }
    public void doSomethingElse() {
        //gettransaksi();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        LoginActivity.this.finish();
    }
    private void gettransaksi(){
        String user_login,c_user="";
        try
        {
                JSONObject objUser=new JSONObject(cUser);
                JSONObject d_user = objUser.getJSONObject("data");
                c_user = d_user.getString("nama");


        }
        catch (JSONException e)
        {
            Toast toast = Toast.makeText(
                    getApplicationContext(), e.toString(), Toast.LENGTH_LONG
            );
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.show();
        }

        String uri ="http://keuangan.sekolahalambogor.id/json/json_penerimaan/";
        String url=uri+c_user;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {

                    stringCacher= new FileCacher<>(LoginActivity.this, "datatransaksi.txt");
                    stringCacher.writeCache(response.toString());
                    Log.d("TRANSAKSI", "onSuccess: " +response.toString());

                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("GETJENIS", "onFailure: "+responseString);
            }
        });
    }
}
