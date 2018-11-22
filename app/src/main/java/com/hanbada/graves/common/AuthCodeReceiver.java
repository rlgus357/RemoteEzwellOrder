package com.hanbada.graves.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.hanbada.graves.Contants.Contants;
import com.hanbada.graves.main.Main;
import com.hanbada.graves.main.view.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AuthCodeReceiver extends BroadcastReceiver {

    protected  final String TAG = getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG,"OnReceive 호출!!");
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            {
                StringBuilder stringBuilder = new StringBuilder();
                Bundle bundle = intent.getExtras();

                if(bundle != null){

                    Object[] pdusObj = (Object[])bundle.get("pdus");
                    SmsMessage[] messages = new SmsMessage[pdusObj.length]; // SMS를 받아올 SmsMessage 배열을 만든다

                    String strSendor = "";
                    String strContent = "";
                    for (int i = 0; i < pdusObj.length; i++) {
                        // SmsMessage의 static메서드인 createFromPdu로 pdusObj의 데이터를 message에 담는다 이 때 pdusObj는 byte배열로 형변환을 해줘야 함
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);

                        strSendor = messages[i].getOriginatingAddress(); // 발신자 번호
                        strContent = messages[i].getMessageBody(); // 보낸내용

                        Log.i(TAG,"발신자번호>" + strSendor );
                        Log.i(TAG,"보낸내용>" + strContent);

                    }

                    // SmsMessage배열에 담긴 데이터를 append메서드로 sms에 저장
                    // getMessageBody메서드는 문자 본문을 받아오는 메서드
                    for (SmsMessage smsMessage : messages) {
                        stringBuilder.append(smsMessage.getMessageBody());
                    }

                    String strSmsContent = stringBuilder.toString();// StringBuilder 객체 sms를 String으로 변환

                    if(TextUtils.isEmpty(strSendor) && TextUtils.isEmpty(strSmsContent)){

                    }else{
                        String strEzwellSmsNumber  = Contants.EZWELL_SENDOR_NUMBER;
                        if(strSendor.equals(strEzwellSmsNumber)){
                            // 이지웰에서 보낸 문자일경우..
                            String strAuthCode = strSmsContent.substring(strSmsContent.lastIndexOf("[") + 1 , strSmsContent.lastIndexOf("]"));
                            Log.d(TAG,strAuthCode);

//                            ((MainActivity) context.sendAuthCode(strAuthCode);
                            Intent in = new Intent("SmsMessage.intent.MAIN").putExtra("authCode",strAuthCode);
                            context.sendBroadcast(in);

                        }
                    }
                }
            }
        }


    }
}
