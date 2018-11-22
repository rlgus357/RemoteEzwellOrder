package com.hanbada.graves.main.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.hanbada.graves.R;
import com.hanbada.graves.common.StateMaintainer;
import com.hanbada.graves.main.Main;
import com.hanbada.graves.main.model.MainModel;
import com.hanbada.graves.main.presenter.MainPresenter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Main.RequiredViewOps, View.OnClickListener {

    private Button mLoginBtn;

    private final StateMaintainer mStateMaintainer = new StateMaintainer( getSupportFragmentManager(), MainActivity.class.getName());

    private Main.ProvidedPresenterOps mPresenter;
    private BroadcastReceiver mIntentReceiver;

    protected  final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setupView();
        this.setupConfiguration();

//        Intent i=new Intent("any string");
//        i.setClass(this, AuthCodeReceiver.class);
//        this.sendBroadcast(i);

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("사용을 위해서 다음과 같은 권한이 필요합니다.")
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.READ_SMS,Manifest.permission.READ_PHONE_STATE)
                .check();
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

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter("SmsMessage.intent.MAIN");
        mIntentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String authCode = intent.getStringExtra("authCode");
                mPresenter.sendAuthCode(authCode);

            }
        };
        this.registerReceiver(mIntentReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.mIntentReceiver);
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

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(MainActivity.this, "권한 허가", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(MainActivity.this, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };



}
