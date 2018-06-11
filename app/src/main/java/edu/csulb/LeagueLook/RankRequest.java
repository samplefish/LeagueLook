package edu.csulb.LeagueLook;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by William on 6/21/2017.
 */

public class RankRequest extends StringRequest {

    private static final String GET_STATUS_URL = "https://na1.api.riotgames.com/lol/league/v3/positions/by-summoner/";
    private static final String API_KEY = "?api_key=RGAPI-37729fdd-1fbf-46d1-904c-402e18cb9a6c";

    public RankRequest(String id, Response.Listener<String> listener){

        super(Request.Method.GET, GET_STATUS_URL+ id + API_KEY, listener, null);

    }
}
