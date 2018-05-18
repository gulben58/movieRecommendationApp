package com.example.gulbe.movierecommendation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends Activity {
    Button btn;
    TextView tv;
    String currentQ;
    int qid;
    RadioButton opt1;
    RadioButton opt2;
    RadioButton opt3;
    RadioButton opt4;
    RadioButton opt5;
    RadioButton opt6;
    RadioButton opt7;
    RadioButton opt8;
    ArrayList<String> list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.textView1);
        opt1=(RadioButton) findViewById(R.id.opt1);
        opt2=(RadioButton) findViewById(R.id.opt2);
        opt3=(RadioButton) findViewById(R.id.opt3);
        opt4=(RadioButton) findViewById(R.id.opt4);
        opt5=(RadioButton) findViewById(R.id.opt5);
        opt6=(RadioButton) findViewById(R.id.opt6);
        opt7=(RadioButton) findViewById(R.id.opt7);
        opt8=(RadioButton) findViewById(R.id.opt8);
        list=new ArrayList<>();


        btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Movie().execute();

            }
        });
        new Movie().execute();

    }

    private class Movie extends AsyncTask<Void, Void, String> {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Dark);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... voids) {


            try {
                URL url = new URL("http://192.168.128.86/movie.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                return sb.toString().trim();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            try {
                setQuestionView(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void setQuestionView(String json) throws JSONException {
        RadioGroup grp=(RadioGroup)findViewById(R.id.radioGroup1);
        RadioButton answer=(RadioButton)findViewById(grp.getCheckedRadioButtonId());
        list.add((String) answer.getText());

        if(qid==3){


           Intent intent=new Intent(MainActivity.this,MovieActivity.class);
           intent.putExtra("list of answers",list);

           startActivity(intent);
        }

        JSONArray jsonArray = new JSONArray(json);
        JSONObject obj=jsonArray.getJSONObject(qid);
        tv.setText(obj.getString("question"));
        opt1.setText(obj.getString("opt1"));
        opt2.setText(obj.getString("opt2"));
        opt3.setText(obj.getString("opt3"));
        opt4.setText(obj.getString("opt4"));
        opt5.setText(obj.getString("opt5"));
        opt6.setText(obj.getString("opt6"));
        opt7.setText(obj.getString("opt7"));
        opt8.setText(obj.getString("opt8"));
        qid++;



    }

}