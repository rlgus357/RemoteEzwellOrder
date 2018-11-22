package com.hanbada.graves.main;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Button;
import android.widget.Toast;

public interface Main {

    // Presenter 에서 필요한 View에서 제공되는 작업
    interface RequiredViewOps{
        Context getAppContext();
        Context getActivityContext();

        void showToast(Toast toast);
        void hideToast();

        void showAlert(AlertDialog dialog);

    }

    // View와 통신하기 위해서 Presenter에서 제공되는 작업
    interface ProvidedPresenterOps{

        void clickLoginEzwell(Button btn, String phoneNumber);
        void setView(RequiredViewOps view);
        void sendAuthCode(String strAuthCode);

    }


    //Model 에서 필요한 Presenter에서 제공되는 작업
    interface RequiredPresenterOps{
        Context getAppContext();
        Context getActivityContext();
    }

    //Presenter와 통신을 위해 Model에서 필요한 작업
    interface ProvidedModelOps{

    }
}
