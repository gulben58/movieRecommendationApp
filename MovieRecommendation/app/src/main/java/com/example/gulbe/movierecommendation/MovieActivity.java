package com.example.gulbe.movierecommendation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class MovieActivity extends Activity {
    TextView tv;
    ArrayList <String> alist;
    Object[] arr;
    ImageView image;
    ArrayList<String> listOfNames;
    ArrayList<String> listOfImages;
    ArrayList<String> listOfScores;
    ArrayList<String> listOfYears;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        tv=(TextView) findViewById(R.id.tv);
        alist=new ArrayList<>();
        alist= getIntent().getStringArrayListExtra("list of answers");
        arr=alist.toArray();
        image=(ImageView) findViewById(R.id.img);
        listOfNames= new ArrayList<String>();
        listOfImages= new ArrayList<String>();
        listOfScores= new ArrayList<String>();
        listOfYears= new ArrayList<String>();
        new MovieInfo().execute();



    }
    private class MovieInfo extends AsyncTask<Void, Void, String> {
        final ProgressDialog progressDialog = new ProgressDialog(MovieActivity.this,
                R.style.AppTheme_Dark);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            listOfImages.clear();
            listOfNames.clear();
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... voids) {


            try {
                URL url = new URL("http://192.168.128.86/movieInfo.php");
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
                load(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    private void load(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);


        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            if (obj.getDouble("imdb_score") >= Double.parseDouble(alist.get(2)) && obj.getString("genre").equals(alist.get(1)) && obj.getInt("productionYear") >= (Integer.parseInt(alist.get(3)))) {
                listOfNames.add(obj.getString("m_name"));
                listOfImages.add(obj.getString("image"));
                listOfScores.add(obj.getString("imdb_score"));
                listOfYears.add(obj.getString("productionYear"));

            }



        }
        if (listOfNames.size() != 0) {
            Random random = new Random();
            int x = random.nextInt(listOfNames.size());
            int y=x;
            tv.append(listOfNames.get(y));
            tv.append("\n IMDB Score:");
            tv.append(listOfScores.get(y));
            tv.append("\n Production Year:");
            tv.append(listOfYears.get(y));

            String img_url=listOfImages.get(y);

            if (!img_url.equals("")){
                Picasso.with(getApplicationContext()).load(img_url).into(image);
            }


        }
       else {
            tv.setText("No Movie Found");
        }
    }
}
