package com.ingic.saeedni.helpers;

import android.content.Context;
import android.util.Log;

import com.ingic.saeedni.entities.ResponseWrapper;
import com.ingic.saeedni.global.WebServiceConstants;
import com.ingic.saeedni.retrofit.WebService;
import com.ingic.saeedni.retrofit.WebServiceFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created on 5/15/2017.
 */

public class TokenUpdater {
    private static final TokenUpdater tokenUpdater = new TokenUpdater();
    private WebService webservice;

    private TokenUpdater() {
    }

    public static TokenUpdater getInstance() {
        return tokenUpdater;
    }

    public void UpdateToken(Context context, final String userid, String DeviceType, String Token) {
        if (Token.isEmpty()) {
            Log.e("Token Updater", "Token is Empty");
        }
        webservice = WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(context,
                WebServiceConstants.SERVICE_URL);
        Call<ResponseWrapper> call = webservice.updateToken(userid, DeviceType, Token);
        call.enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                if (response.body() != null)
                    Log.i("UPDATETOKEN", response.body().getResponse() + "" + userid);
            }

            @Override
            public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                Log.e("UPDATETOKEN", t.toString());
            }
        });
       // UpdateLanguage(context, userid);
    }

    public  void UpdateLanguage(Context context, String userid) {
        Call<ResponseWrapper> languageCall = webservice.updateUserLanguage(userid,new BasePreferenceHelper(context).getLang());
        languageCall.enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                if (response.body() != null)
                    Log.i("UPDATE LANGUAGE", response.body().getResponse() + "" + userid);
            }

            @Override
            public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                Log.e("UPDATE LANGUAGE", t.toString());
            }
        });
    }

}
