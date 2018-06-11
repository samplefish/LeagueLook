package edu.csulb.LeagueLook;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.Response;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by William on 4/25/2017.
 */

public class StoreSummonerRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://leaguelook.000webhostapp.com/SummonerRequest.php";
    private Map<String, String> params;

    public StoreSummonerRequest(String summonerName) {
        super(Method.POST, REGISTER_REQUEST_URL, null, null);
        params = new HashMap<>();
        params.put("name", summonerName);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
