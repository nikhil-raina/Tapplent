package com.example.nikhi_000.tapplent_demo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView displayItems;
    private Adapter adapter;
    private Button groupButton;
    private RelativeLayout perContact;
    private SwipeRefreshLayout swipeRefresh;
    private LinearLayout selectContact;
    private List<DataPerson> longPress_Contacts;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        groupButton = (Button) findViewById(R.id.btn);

        perContact = (RelativeLayout) findViewById(R.id.perContact_fullBody);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        selectContact = (LinearLayout) findViewById(R.id.selectContact);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncronusTask().execute();
                Toast.makeText(MainActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();

            }
        });
        new AsyncronusTask().execute();
    }

    /**
     *
     * @param view
     */
    public void groupClickHandler(View view) {
       // Toast.makeText(MainActivity.this, "Button Pressed", Toast.LENGTH_SHORT).show();
        if(adapter.getSelectedDatList() != null && adapter.getSelectedDatList().size() >= 1) {
            Intent intent = new Intent(MainActivity.this, selectedContacts.class);
            longPress_Contacts = adapter.getSelectedDatList();
            intent.putExtra("LIST", (Serializable) longPress_Contacts);

            startActivity(intent);
        }
    }


    /**
     *
     */
    private class AsyncronusTask extends AsyncTask<String, String, String>{
        HttpURLConnection connection;
        URL url = null;

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

            String urlName = "https://www.mytapplentdev.com/tapplent-core-app/tapp/conversation/v1/contactDetails/t/interplex-dev/u/0x48A6B145BCDA84DEA1597DE4E5DF5AD7/e/?timeZone=Aa/Kolkata&personName=user";
            JSONObject post = new JSONObject();
            String objID = "0x48A6B145BCDA84DEA1597DE4E5DF5AD7";
            int limit = 20;
            int offset = 0;
            String keyword = null;

            OutputStream dataOut;
            try {
                post.put("mTPECode","Person");
                post.put("objectId",objID);
                post.put("limit",limit);
                post.put("offset", offset);
                post.put("keyword", keyword);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Log.e("url and JSON data:", urlName + post.toString());
                url = new URL(urlName);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type","application/json");
                connection.setRequestProperty("access_token","abc");

                dataOut = new BufferedOutputStream(connection.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(dataOut, "UTF-8"));
                writer.write(post.toString());
                writer.flush();
                writer.close();

                dataOut.close();

                connection.connect();
                Toast.makeText(MainActivity.this, "Connection Made", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Message:", e.getMessage());
            }

            try {
                int feedBack_ID = connection.getResponseCode();
                Log.e("feedBack_ID",""+feedBack_ID);
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
        protected void onPostExecute(String result){

            List<DataPerson> data = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("personDetails");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    DataPerson person = new DataPerson();
                    person.personName = obj.getString("personFullName");
                    person.personInfo = obj.getString("personDesignation") + ", " + obj.getString("location");
                    person.personInitialName = obj.getString("nameInitials");
                    person.personID = obj.getString("personId");
                    data.add(person);
                    Log.e("person id: ", ""+person.personID);
                }

                displayItems = (RecyclerView) findViewById(R.id.display_items);
                displayItems.setHasFixedSize(true);
                adapter = new Adapter(MainActivity.this, data);
                displayItems.setAdapter(adapter);
                displayItems.setLayoutManager(new LinearLayoutManager(MainActivity.this));


            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
            swipeRefresh.setRefreshing(false);
        }
    }
}
