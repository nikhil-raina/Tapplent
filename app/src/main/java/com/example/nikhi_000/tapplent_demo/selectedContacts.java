package com.example.nikhi_000.tapplent_demo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 *
 * Created by nikhi_000 on 1/3/2018.
 */

public class selectedContacts extends AppCompatActivity{

    List<DataPerson> list_Mock;
    String message;
    boolean bilateralConversation;
    String feedbackGroupName;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.display_group_contacts);

        Intent i = getIntent();
        List<DataPerson> list = (List<DataPerson>) i.getSerializableExtra("LIST");
        list_Mock = list;
        bilateralConversation = (list_Mock.size() == 1);

        if(bilateralConversation) {
            feedbackGroupName = null;
            new AsyncronusTask_groupData(feedbackGroupName).execute();
        }
        else{
            groupName();
            //feedbackGroupName = message;
        }

       //
    }

    public void groupName(){

        Intent intent = new Intent(this, editText_Screen.class);
        startActivityForResult(intent, 2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2){
         //   feedbackGroupName = data.getStringExtra("Message");
            if(!TextUtils.isEmpty(data.getStringExtra("Message")))
                new AsyncronusTask_groupData(data.getStringExtra("Message")).execute();

        }
    }


    private class AsyncronusTask_groupData extends AsyncTask<String , String, String>{

        HttpURLConnection connection;
        URL url = null;
        private String feedGrpName;

        public AsyncronusTask_groupData(String feedbackGroupName) {
            this.feedGrpName = feedbackGroupName;
        }

        /**
         *
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         *
         * @param params
         * @return
         */
        @Override
        protected String doInBackground(String... params) {

            JSONObject requestBody = new JSONObject();
            JSONArray personDetailList = new JSONArray();

            String urlName = "https://mytapplentdev.com/tapplent-core-app/tapp/conversation/v1/createFeedbackGroup/t/interplex-dev/u/0x48A6B145BCDA84DEA1597DE4E5DF5AD7/e/?timeZone=Asia/Kolkata&personName=user";
            //boolean bilateralConversation = (list_Mock.size() == 1);

            for(int i = 0; i < list_Mock.size(); i++){
                try {
                    JSONObject personID_obj = new JSONObject();
                    personID_obj.put("personID", list_Mock.get(i).personID);
                    personDetailList.put(personID_obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            String feedbackGroupPkId = null;

            String mTPECode = "Person";
            String objectId = "0x48A6B145BCDA84DEA1597DE4E5DF5AD7";
            String feedbackGroupImageId = null;

            OutputStream dataOut;

            // Data for the requestBody.
            try {
                requestBody.put("personDetailList", personDetailList);
                requestBody.put("bilateralConversation", bilateralConversation);
                requestBody.put("feedbackGroupPkId", feedbackGroupPkId);
                if(!TextUtils.isEmpty(this.feedGrpName))
                    requestBody.put("feedbackGroupName", feedGrpName);
                requestBody.put("mTPECode", mTPECode);
                requestBody.put("objectId", objectId);
                requestBody.put("feedbackGroupImageId", feedbackGroupImageId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // To show the information in the LOG form.
            // Will appear in RED.
            try {
                Log.e("url and JSON data: ", urlName + requestBody.toString());
                url = new URL(urlName);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            // Creating the connection with the URL.
            try {
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type","application/json");
                connection.setRequestProperty("access_token","abc");

                dataOut = new BufferedOutputStream(connection.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(dataOut, "UTF-8"));
                writer.write(requestBody.toString());
                writer.flush();
                writer.close();

                dataOut.close();

                connection.connect();
//                Toast.makeText(selectedContacts.this, "Connection Made", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }


            // Gets the response and stores in RESULT.
            try {
                int feedBack_ID = connection.getResponseCode();
                Log.e("feedBack_ID:"," "+feedBack_ID);
                if(feedBack_ID == HttpURLConnection.HTTP_OK){
                    InputStream is = connection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);

                    StringBuilder result = new StringBuilder();
                    String line;

                    while((line = br.readLine())!= null){
                        result.append(line);
                    }

                    return (result.toString());
                }
                else return ("UNSUCCESSFUL");
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                connection.disconnect();
            }

            return null;
        }

        /**
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            final String title ;
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject j_obj = jsonObject.getJSONObject("feedbackGroup");
                title = j_obj.getString("feedbackGroupName");
                Intent intent = new Intent(selectedContacts.this, displayGroup.class);
                intent.putExtra("title", title);
                intent.putExtra("LIST", (Serializable)list_Mock);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
