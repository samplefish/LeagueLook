package edu.csulb.LeagueLook;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.csulb.spycam.R;

public class CustomSearchActivity extends AppCompatActivity {

    Button searchButton;
    EditText editName;
    TextView details, status, ranking;
    String searchName, concatRank;
    Map<String,String> idRank;
    InputMethodManager imm;
    List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_custom_search);
        searchButton = (Button) findViewById(R.id.button5);
        editName = (EditText) findViewById(R.id.editText);
        editName.setGravity(Gravity.CENTER_HORIZONTAL);
        details = (TextView) findViewById((R.id.textView3));
        status = (TextView) findViewById(R.id.textView5);
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        ranking = (TextView) findViewById((R.id.textView6));
        idRank = new HashMap<String, String>();
        editName.requestFocus();
        list = new ArrayList<String>();
        imm.showSoftInput(editName, InputMethodManager.SHOW_IMPLICIT);
        searchButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                idRank.clear();
                list.clear();

                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                searchName = editName.getText().toString().replaceAll("\\s+","");

                details.setText(searchName + " is not in game!");
                ranking.setText("loading...");

                Response.Listener<String> idListener = new Response.Listener<String>(){
                    public void onResponse(String response){
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                                    String summonerId = jsonResponse.getString("id");
                            Response.Listener<String> responseListener = new Response.Listener<String>(){
                                public void onResponse(String response){
                                    try{
                                        JSONObject detailResponse = new JSONObject(response);
                                        if(detailResponse.has("gameId"))
                                        {
                                            status.setText("In a " + detailResponse.getString("gameMode"));

                                            JSONArray participantsArray = detailResponse.getJSONArray("participants");

                                            String concat = "Players: \n";
                                            for(int i = 0 ; i < participantsArray.length() ; i++){
                                                concat = concat + participantsArray.getJSONObject(i).getString("summonerName") + "\n";
                                                list.add(i, participantsArray.getJSONObject(i).getString("summonerId"));

                                                if(i==4)
                                                {
                                                    concat = concat + "---\n";
                                                }
                                            }

                                            int raw = Integer.parseInt(detailResponse.getString("gameLength"));
                                            int minutes = raw/60;
                                            int seconds = raw%60;
                                            concat = concat + "\nTime of game: " + minutes + "m" + seconds + "s" ;
                                            concatRank = "Ranks: \n";

                                            for(int i = 0; i < list.size(); i++)
                                            {
                                                final String currentId = list.get(i);

                                                Response.Listener<String> rankListener = new Response.Listener<String>(){

                                                    public void onResponse(String response){

                                                        try{
                                                            JSONArray rankResponse = new JSONArray(response);
                                                            if(rankResponse.isNull(0)){
                                                                idRank.put(currentId,"Unranked");
                                                            }
                                                            else{
                                                                //int totalGames = Integer.parseInt(rankResponse.getJSONObject(0).getString("wins")) + Integer.parseInt(rankResponse.getJSONObject(0).getString("losses"));
                                                                idRank.put(currentId,rankResponse.getJSONObject(0).getString("tier") + " " + rankResponse.getJSONObject(0).getString("rank")
                                                                /*+ " " + totalGames + "(" + Integer.parseInt(rankResponse.getJSONObject(0).getString("wins")) + "/" + Integer.parseInt(rankResponse.getJSONObject(0).getString("losses")) + ")"*/);
                                                            }

                                                            //concatRank = concatRank + rankResponse.getJSONObject(0).getString("tier") + " " + rankResponse.getJSONObject(0).getString("rank")+ "\n";
                                                            //ranking.setText(concatRank);
                                                            if(idRank.size() == 10){
                                                                for(int i = 0; i < list.size(); i++){
                                                                    concatRank = concatRank + idRank.get(list.get(i)) + "\n";

                                                                    if(i == 4){
                                                                        concatRank = concatRank + "---\n";
                                                                    }
                                                                    //Log.i(""+i,list.get(i));
                                                                }
                                                                ranking.setText(concatRank);

                                                            }

                                                            //Log.i(currentId, rankResponse.getJSONObject(0).getString("tier") + " " + rankResponse.getJSONObject(0).getString("rank"));

                                                        }
                                                        catch(JSONException e){
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                };
                                                RankRequest request = new RankRequest(currentId, rankListener);
                                                RequestQueue queue = Volley.newRequestQueue(CustomSearchActivity.this);
                                                queue.add(request);


                                            }
                                            Iterator it = idRank.entrySet().iterator();
                                            while(it.hasNext()){
                                                Map.Entry pair = (Map.Entry) it.next();

                                            }




                                            details.setText(concat);

                                        }
                                    }
                                    catch(JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                            };
                            CustomRequest request = new CustomRequest(summonerId, responseListener);
                            RequestQueue queue = Volley.newRequestQueue(CustomSearchActivity.this);
                            queue.add(request);
                        }
                        catch(JSONException e){
                            e.printStackTrace();

                        }
                    }
                };
                NameRequest request = new NameRequest(searchName, idListener);
                RequestQueue queue = Volley.newRequestQueue(CustomSearchActivity.this);
                queue.add(request);

            }
        });


    }
}
