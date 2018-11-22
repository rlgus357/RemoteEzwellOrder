package com.hanbada.graves.main.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import com.hanbada.graves.Contants.Contants;
import com.hanbada.graves.common.RequestHttpURLConnection;
import com.hanbada.graves.main.Main;
import com.hanbada.graves.main.model.MainModel;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

public class MainPresenter implements Main.ProvidedPresenterOps, Main.RequiredPresenterOps {

    protected  final String TAG = getClass().getSimpleName();

    private WeakReference<Main.RequiredViewOps> mView;
    private Main.ProvidedModelOps mModel;

    public MainPresenter (Main.RequiredViewOps view){
        mView = new WeakReference<Main.RequiredViewOps>(view);
    }

    private Main.RequiredViewOps getView() throws NullPointerException{
        if(mView != null){
            return mView.get();
        }else{
            throw new NullPointerException("View is unavailable");
        }
    }

    @Override
    public void clickLoginEzwell(Button btn, String phoneNumber) {

        Log.i(TAG,"clickLogBtn");

        String strConnectionUrl = "http://" + Contants.CONNECTION_IP + ":8080/Graves/Ezwell/requestAuthCode.do";

        // Async Task => HttpURLConnection
        ContentValues phoneNumContent = new ContentValues();
        phoneNumContent.put("phoneNumber", phoneNumber);
        NetworkTask networkTask = new NetworkTask(strConnectionUrl,phoneNumContent);
        String result = "";
        try {
            result = networkTask.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG,result);
    }

    @Override
    public void setView(Main.RequiredViewOps view) {
        mView = new WeakReference<Main.RequiredViewOps>(view);
    }

    @Override
    public void sendAuthCode(String strAuthCode) {

        Log.d(TAG,strAuthCode);

        String strConnectionUrl = "Http://" + Contants.CONNECTION_IP + ":8080/Graves/SendAuthCode.do";

        ContentValues authCodeContent = new ContentValues();
        authCodeContent.put("authCode",strAuthCode);
        NetworkTask networkTask = new NetworkTask(strConnectionUrl, authCodeContent);
        String result = "";
        try {
            result = networkTask.execute().get();
        }catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d(TAG,result);

    }

    public void setModel(MainModel model) {
        this.mModel = model;
    }


    @Override
    public Context getAppContext() {
        try {
            return getView().getAppContext();
        }catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Context getActivityContext() {
        try {
            return getView().getActivityContext();
        }catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }


    public class NetworkTask extends AsyncTask<Void, Void, String>{

        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values){
            this.url = url;
            this.values = values;
        }
        @Override
        protected String doInBackground(Void... params) {

            Log.d(TAG, "doInBackground Exec");
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();

            return requestHttpURLConnection.request(url,values);

        }
    }
}
