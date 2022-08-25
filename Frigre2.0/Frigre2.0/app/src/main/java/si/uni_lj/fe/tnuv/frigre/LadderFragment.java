package si.uni_lj.fe.tnuv.frigre;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.lang.String.valueOf;

public class LadderFragment extends Fragment {

    RequestQueue queue;

    public LadderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ladder, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        getLadder();
    }

    public void getLadder() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
            Request.Method.GET,
            "https://frigre.herokuapp.com/api/fakultete",
            null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray responseColleges) {
                    View view = getView();
                    TableLayout tableLayoutLadder = view.findViewById(R.id.table_ladder);
                    responseColleges = sortJsonInt(responseColleges, "points");

                    for (int i = 0; i < responseColleges.length(); i++) {
                        try {
                            JSONObject college = responseColleges.getJSONObject(i);
                            TableRow row = new TableRow(getActivity());

                            TextView collegeName = new TextView(getActivity());
                            String shortName;
                            if (college.getString("name").length() > 35) {
                                shortName = college.getString("name").substring(0, 35) + "...";
                            } else {
                                shortName = college.getString("name");
                            }
                            collegeName.setText(shortName);
                            collegeName.setGravity(Gravity.START);
                            collegeName.setTextColor(Color.WHITE);

                            TextView collegePoints = new TextView(getActivity());
                            collegePoints.setText(college.getString("points"));
                            collegePoints.setGravity(Gravity.END);
                            collegePoints.setTextColor(Color.rgb(224, 175, 217));

                            row.addView(collegeName);
                            row.addView(collegePoints);

                            tableLayoutLadder.addView(row);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }
        );
        queue.add(jsonArrayRequest);
    }

    public JSONArray sortJsonString(JSONArray myJSON, String query) {
        JSONArray sortedJSONArray = new JSONArray();
        List<JSONObject> jsonValues = new ArrayList<JSONObject>();

        for (int i = 0; i < myJSON.length(); i++) {
            try {
                jsonValues.add(myJSON.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(jsonValues, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject a, JSONObject b) {
                String valA = new String();
                String valB = new String();

                try {
                    valA = a.getString(query);
                    valB = b.getString(query);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return valA.compareToIgnoreCase(valB);
            }
        });

        for (int i = 0; i < myJSON.length(); i++) {
            sortedJSONArray.put(jsonValues.get(i));
        }

        return sortedJSONArray;
    }

    public JSONArray sortJsonInt(JSONArray myJSON, String query) {
        JSONArray sortedJSONArray = new JSONArray();
        List<JSONObject> jsonValues = new ArrayList<JSONObject>();

        for (int i = 0; i < myJSON.length(); i++) {
            try {
                jsonValues.add(myJSON.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(jsonValues, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject a, JSONObject b) {
                Integer valA = 0;
                Integer valB = 0;

                try {
                    valA = a.getInt(query);
                    valB = b.getInt(query);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return -valA.compareTo(valB);
            }
        });

        for (int i = 0; i < myJSON.length(); i++) {
            sortedJSONArray.put(jsonValues.get(i));
        }

        return sortedJSONArray;
    }
}