package com.example.rohan.rating;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private String TAG = this.getClass().getSimpleName();
    private ListView lstView;
    private RequestQueue requestQueueObj;
    private ArrayList<DataModel> arList ;
    private LayoutInflater lf;
    private VolleyAdapter volleyAdapterObj;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lf = LayoutInflater.from(this);
        arList = new ArrayList<DataModel>();
        volleyAdapterObj      = new VolleyAdapter();

        lstView = (ListView) findViewById(R.id.listView);
        lstView.setAdapter(volleyAdapterObj);
        requestQueueObj =  Volley.newRequestQueue(this);

        String url = "http://bismarck.sdsu.edu/rateme/list";
        pd = ProgressDialog.show(this,"Please Wait...","Please Wait...");
        try{
            Thread.sleep(2000);
        }catch(Exception e){}

        JsonArrayRequest jr = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
        @Override
            public void onResponse(JSONArray response) {
                Log.i(TAG,response.toString());
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj = response.getJSONObject(i);
                    DataModel dataModelObj = new DataModel();
                    dataModelObj.setId(obj.optString("id"));
                    dataModelObj.setfirstName(obj.optString("firstName"));
                    dataModelObj.setlastName(obj.optString("lastName"));
                    arList.add(dataModelObj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
                volleyAdapterObj.notifyDataSetChanged();
                pd.dismiss();
                ;            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,error.getMessage());
            }
        });
        requestQueueObj.add(jr);

        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                    long arg3) {

                Intent i = new Intent(getApplicationContext(),InstructActivity.class);
                i.putExtra("position", pos);
                startActivity(i);
            }
    });

    }



    class DataModel{
        private String id;
        private String firstName;
        private String lastName;

        void setId(String id) {
            this.id = id;
        }

        void setfirstName(String firstName) {
            this.firstName = firstName;
        }

        void setlastName(String lastName) {
            this.lastName = lastName;
        }

        String getId() {
            return id;
        }

        String getfirstName() {
            return firstName;
        }

        String getLastName() {
            return lastName;
        }


    }


    class VolleyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return arList.size();
        }

        @Override
        public Object getItem(int i) {
            return arList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder vh ;
            if(view == null){
                vh = new ViewHolder();
                view = lf.inflate(R.layout.list_item,null);
                vh.nameOfProfessor = (TextView) view.findViewById(R.id.txtTitle);
                view.setTag(vh);
            }
            else{
                vh = (ViewHolder) view.getTag();
            }

            DataModel dataModelObj = arList.get(i);
            vh.nameOfProfessor.setText(dataModelObj.getId() + ". "+ dataModelObj.getfirstName() +" " + dataModelObj.getLastName() );
            return view;
        }

        class  ViewHolder{
            TextView nameOfProfessor;
        }

    }

}
