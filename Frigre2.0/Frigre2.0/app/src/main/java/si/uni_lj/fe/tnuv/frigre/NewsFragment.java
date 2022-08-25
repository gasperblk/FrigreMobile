package si.uni_lj.fe.tnuv.frigre;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class NewsFragment extends Fragment {

    RequestQueue queue;
    ArrayList<NewsObject> newsObjects;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(getActivity());
        newsObjects = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        getNews();
    }

    public void getNews() {
        View view = getView();
        //LinearLayout linearLayoutNews = view.findViewById(R.id.linearLayoutNews);

        try {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    "https://frigre.herokuapp.com/api/novice",
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray responseNews) {
                            TextView title;
                            TextView content;
                            View line;

                            for (int i = 0; i < responseNews.length(); i++) {
                                try {
                                    newsObjects.add(new NewsObject(responseNews.getJSONObject(i).getString("title"), responseNews.getJSONObject(i).getString("content")));
                                    /*title = new TextView(getActivity());
                                    title.setText(responseNews.getJSONObject(i).getString("title"));
                                    title.setTextSize(18);
                                    title.setGravity(Gravity.CENTER_HORIZONTAL);
                                    title.setTextColor(Color.rgb(232, 172, 74));
                                    title.setShadowLayer(1.6f, 1.5f, 1.3f, Color.BLACK);
                                    title.setPadding(0,20, 0, 10);
                                    LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    //titleParams.setMargins(0,20, 0, 10);
                                    title.setLayoutParams(titleParams);
                                    linearLayoutNews.addView(title);

                                    content = new TextView(getActivity());
                                    content.setText(responseNews.getJSONObject(i).getString("content"));
                                    content.setTextSize(12);
                                    content.setGravity(Gravity.CENTER_HORIZONTAL);
                                    content.setTextColor(Color.WHITE);
                                    content.setPadding(0,0, 0, 30);
                                    LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    //contentParams.setMargins(0,0, 0, 30);
                                    content.setLayoutParams(contentParams);
                                    linearLayoutNews.addView(content);


                                    line = new View(getActivity());
                                    line.setBackgroundColor(Color.WHITE);
                                    LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
                                    line.setLayoutParams(lineParams);
                                    linearLayoutNews.addView(line);*/

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            MyAdapter adapter = new MyAdapter(requireContext(), R.layout.list_item, newsObjects);
                            ListView listView = view.findViewById(R.id.listView);
                            listView.setAdapter(adapter);

                            View footerView = ((LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.news_footer, null, false);
                            listView.addFooterView(footerView);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError errorPlayers) {
                            Log.e("Error", "fuck");
                        }
                    }
            );
            queue.add(jsonArrayRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}