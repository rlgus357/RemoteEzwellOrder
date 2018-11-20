package com.hanbada.graves.main.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hanbada.graves.R;
import com.hanbada.graves.common.StateMaintainer;
import com.hanbada.graves.main.Main;
import com.hanbada.graves.main.model.MainModel;
import com.hanbada.graves.main.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity implements Main.RequiredViewOps, View.OnClickListener {

    private Button mLoginBtn;

    private final StateMaintainer mStateMaintainer = new StateMaintainer( getSupportFragmentManager(), MainActivity.class.getName());
    private Main.ProvidedPresenterOps mPresenter;

    private static final int REQUEST_READ_PHONE_NUMBER = 1;
    private static final int REQUEST_READ_PHONE_STATE = 2;

    protected  final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setupView();
        this.setupConfiguration();

        this.checkAndRequestPermissions();
    }

    private void setupView(){
        mLoginBtn = (Button)findViewById(R.id.EzwellLoginBtn);

        mLoginBtn.setOnClickListener(this);

    }

    private void setupConfiguration(){
        if(mStateMaintainer.firstTimein()){
            MainPresenter presenter = new MainPresenter(this);
            MainModel model = new MainModel(presenter);
            presenter.setModel(model);
            mStateMaintainer.put(presenter);
            mStateMaintainer.put(model);

            mPresenter = presenter;
        }else{
            mPresenter = mStateMaintainer.get(MainPresenter.class.getName());
            mPresenter.setView(this);
        }
    }

    private void checkAndRequestPermissions(){

        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_PHONE_STATE }, REQUEST_READ_PHONE_STATE);

        }else{

        }
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public void showToast(Toast toast) {
        toast.show();
    }

    @Override
    public void hideToast() {

    }

    @Override
    public void showAlert(AlertDialog dialog) {

    }

    @Override
    public void onClick(View v) {
        Log.d(TAG,"onClick Method 호출");
        switch (v.getId()){
            case R.id.EzwellLoginBtn:
                mPresenter.clickLoginEzwell(mLoginBtn, this.getPhoneNumber());
        }
    }

    public String getPhoneNumber(){

        Log.d(TAG,"getPhoneNumber start");

        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED){

            Log.d(TAG, "READ_PHONE_NUMBER PERMISSION NOT GRANTED");
            return "not permission";

        }else{
            TelephonyManager phoneMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String phoneNumber = phoneMgr.getLine1Number();
            Log.d(TAG, "Read phone Number =>" + phoneNumber);
            return phoneMgr.getLine1Number();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch(requestCode){

            case REQUEST_READ_PHONE_NUMBER : {
                // If request is cancelled, the result arrays are empty.
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"권한 승인", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"기능 사용을 위한 권한 동의가 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            }

            case REQUEST_READ_PHONE_STATE : {
                // If request is cancelled, the result arrays are empty.
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"권한 승인", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"기능 사용을 위한 권한 동의가 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }



}
