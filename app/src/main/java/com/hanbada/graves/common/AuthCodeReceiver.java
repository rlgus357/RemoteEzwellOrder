package com.hanbada.graves.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

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

                    // SMS를 받아올 SmsMessage 배열을 만든다
                    SmsMessage[] messages = new SmsMessage[pdusObj.length];
                    for (int i = 0; i < pdusObj.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        // SmsMessage의 static메서드인 createFromPdu로 pdusObj의
                        // 데이터를 message에 담는다
                        // 이 때 pdusObj는 byte배열로 형변환을 해줘야 함

                        String sendor = messages[i].getOriginatingAddress(); // 발신자 번호
                        String content = messages[i].getMessageBody(); // 보낸내용

                        Log.i(TAG,"발신자번호>"+sendor);
                        Log.i(TAG,"보낸내용>"+content);
                        Toast.makeText(context,"발신자번호>"+sendor+"<>"+"보낸내용>"+content, Toast.LENGTH_SHORT).show();
                    }

                    String strEzwellSmsNumber  = "02-3282-0579";
                    // test commit


                    // SmsMessage배열에 담긴 데이터를 append메서드로 sms에 저장
                    for (SmsMessage smsMessage : messages) {
                        // getMessageBody메서드는 문자 본문을 받아오는 메서드
                        stringBuilder.append(smsMessage.getMessageBody());
                    }



                    String toString = stringBuilder.toString();// StringBuilder 객체 sms를 String으로 변환
                    Log.d(TAG,"=>"+toString);
                }
            }
        }


    }
}
