package com.gcos.student_plus;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.util.Log;

//import org.apache.http.params.HttpConnectionParams;
//import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import cz.msebera.android.httpclient.*;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by Mazokie on 7/10/2558.
 */
public class ServerRequests {

    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADDRESS = "http://student-plus.or.ht";

    public ServerRequests(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
    }

    public void storeUserDataInBackground(User user,GetUserCallback userCallback){

        progressDialog.show();
        new StoreUserDataAsyncTask(user,userCallback).execute();

    }

    public void fetchUserDataInBackground(User user,GetUserCallback Callback){

        progressDialog.show();
        new fetchUserDataAsyncTask(user,Callback).execute();

    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void,Void,Void>{

        User user;
        GetUserCallback userCallback;

        public StoreUserDataAsyncTask(User user,GetUserCallback userCallback){

            this.user = user;
            this.userCallback = userCallback;

        }

        @Override
        protected Void doInBackground(Void... params){

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("name", user.name));
            dataToSend.add(new BasicNameValuePair("email", user.email));
            dataToSend.add(new BasicNameValuePair("username", user.username));
            dataToSend.add(new BasicNameValuePair("password", user.password));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams,CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "/Register.php");

            try {

                Log.d(String.valueOf(post.getURI()), "Request URL");
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                Log.d(result, "Response String");


            }catch (Exception e){

                e.printStackTrace();

            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid){
            progressDialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(aVoid);
        }

    }

    public class fetchUserDataAsyncTask extends AsyncTask<Void,Void,User> {

        User user;
        GetUserCallback userCallback;

        public fetchUserDataAsyncTask(User user, GetUserCallback userCallback) {

            this.user = user;
            this.userCallback = userCallback;

        }

        @Override
        protected User doInBackground(Void... voids) {

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", user.username));
            dataToSend.add(new BasicNameValuePair("password", user.password));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "/FetchUserData.php");

            User returnedUser = null;

            try {

                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                if (jObject.length() == 0){

                    returnedUser = null;

                }else {
                    String name = jObject.getString("name");
                    String email = jObject.getString("email");

                    Log.d(name,email);

                    returnedUser = new User(name,email,user.username,user.password);
                }

            }catch (Exception e){

                e.printStackTrace();

            }

            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser){
            progressDialog.dismiss();
            userCallback.done(returnedUser);
            super.onPostExecute(returnedUser);
        }
    }

}
