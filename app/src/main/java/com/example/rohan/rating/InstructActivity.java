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
import android.widget.BaseAdapter;
import android.widget.Button;
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
import java.util.HashMap;

public class InstructActivity extends Activity {
    private String TAG = this.getClass().getSimpleName();
    private ListView lstView;
    private RequestQueue RequestQueueObj;
    private ArrayList<InfoModel> arList ;

    private LayoutInflater lf;
    private VolleyAdapter vollyAdapterObj;

    private ProgressDialog pd;
    private Button postButton;
    HashMap<String, String> params = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruct);
        lf = LayoutInflater.from(this);

        Intent i=getIntent();
        final int idValue=i.getIntExtra("position",10);

        arList = new ArrayList<InfoModel>();
        vollyAdapterObj = new VolleyAdapter();

        lstView = (ListView) findViewById(R.id.listView2);
        lstView.setAdapter(vollyAdapterObj);

        postButton = (Button)findViewById(R.id.button);
        postButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
            Intent postIntent = new Intent(getApplicationContext() ,PostActivity.class);
                postIntent.putExtra("nvalue", (idValue+1));
                startActivity(postIntent);
            }

        });
        RequestQueueObj =  Volley.newRequestQueue(this);

        String url = "http://bismarck.sdsu.edu/rateme/instructor/" + (idValue+1);
        String urlcomment = "http://bismarck.sdsu.edu/rateme/comments/"+ (idValue + 1);

        pd = ProgressDialog.show(this,"Please Wait...","Please Wait...");
        try{
            Thread.sleep(2000);
        }catch(Exception e){

        }

        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                           Log.i(TAG,response.toString());
                             try {

                                InfoModel nm = new InfoModel();
                                nm.setId(response.getString("id"));
                                nm.setfirstName(response.optString("firstName"));
                                nm.setlastName(response.optString("lastName"));

                                nm.setOffice(response.optString("office"));
                                nm.setPhone(response.optString("phone"));
                                nm.setEmail(response.optString("email"));
                                JSONObject obj = response.getJSONObject("rating");

                                nm.setAverage(obj.optString("average"));
                                nm.setTotalRatings(obj.optString("totalRatings"));
                                arList.add(nm);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        vollyAdapterObj.notifyDataSetChanged();
                        pd.dismiss();
                        ;            }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,error.getMessage());
            }
        });
        RequestQueueObj.add(jr);

        JsonArrayRequest jr1 = new JsonArrayRequest(urlcomment,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG,response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                InfoModel nm = new InfoModel();
                                nm.setTextcomment(obj.optString("text"));
                                arList.add(nm);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        vollyAdapterObj.notifyDataSetChanged();
                        pd.dismiss();
                        ;            }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,error.getMessage());
            }
        });
        RequestQueueObj.add(jr1);



    }


    class InfoModel{
        private String id;
        private String firstName;
        private String lastName;

        private String office;
        private String phone;
        private String email;
        private String rating;
        private String average;
        private String totalRatings;

        private String textcomment;

        public String getTextcomment() {
            return textcomment;
        }

        public void setTextcomment(String textcomment) {
            this.textcomment = textcomment;
        }

        public String getOffice() {
            return office;
        }

        public void setOffice(String office) {
            this.office = office;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getAverage() {
            return average;
        }

        public void setAverage(String average) {
            this.average = average;
        }


        public String getTotalRatings() {
            return totalRatings;
        }

        public void setTotalRatings(String totalRatings) {
            this.totalRatings = totalRatings;
        }


//        private String pubDate;

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
                view = lf.inflate(R.layout.ilist,null);
                vh.tvTitle = (TextView) view.findViewById(R.id.idTextView);
                vh.firstNametv = (TextView) view.findViewById(R.id.fnameTextView);
                vh.lastNametv = (TextView) view.findViewById(R.id.lnameTextView);

                vh.office = (TextView) view.findViewById(R.id.officeTextView);
                vh.phone = (TextView) view.findViewById(R.id.phoneTextView);
                vh.email = (TextView) view.findViewById(R.id.emailTextView);

                vh.rating = (TextView) view.findViewById(R.id.ratingTextView);
                vh.average = (TextView) view.findViewById(R.id.averageTextView);
                vh.totalratings = (TextView) view.findViewById(R.id.totalratingsTextView);
                vh.datetv = (TextView)view.findViewById(R.id.dateTextView);
                view.setTag(vh);
            }
            else{
                vh = (ViewHolder) view.getTag();
            }

            InfoModel nm = arList.get(i);
            if(nm.getfirstName()== null)
            {

                vh.datetv.setText("Comment : " +nm.getTextcomment());
                vh.tvTitle.setText(" ");
                vh.firstNametv.setText(" ");
                vh.lastNametv.setText(" ");

                vh.office.setText(" ");
                vh.phone.setText(" ");
                vh.email.setText(" ");
//
                vh.rating.setText(" ");
                vh.average.setText(" ");
                vh.totalratings.setText(" ");
            }
            else {


                vh.tvTitle.setText(" ");
                vh.firstNametv.setText("First Name : " + nm.getfirstName());
                vh.lastNametv.setText("Last Name  : " + nm.getLastName());

                vh.office.setText("Office     : " + nm.getOffice());
                vh.phone.setText("Phone     : " + nm.getPhone());
                vh.email.setText("Email      : " + nm.getEmail());
//
                vh.rating.setText("         Ratings And Average");
                vh.average.setText("Average : " + nm.getAverage());
                vh.totalratings.setText("Ratings  : " + nm.getTotalRatings());
                vh.datetv.setText(" ");


            }
            return view;
        }

        class  ViewHolder{
            TextView tvTitle;
            TextView firstNametv;
            TextView lastNametv;
            TextView office;
            TextView phone;
            TextView email;
            TextView rating;
            TextView average;
            TextView totalratings;
            TextView datetv;

        }

    }
}
