package si.uni_lj.fe.tnuv.frigre;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TournamentFragment extends Fragment {

    RequestQueue queue;
    boolean entered;

    public TournamentFragment() {
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
        return inflater.inflate(R.layout.fragment_tournament, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        getTournaments();
    }

    public void getTournaments() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
            Request.Method.GET,
            "https://frigre.herokuapp.com/api/turnirji",
            null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray responseTournaments) {
                    View view = getView();
                    TableLayout tableLayoutTournaments = view.findViewById(R.id.table_tournaments);
                    for (int i = 0; i < responseTournaments.length(); i++) {
                        try {
                            JSONObject tournament = responseTournaments.getJSONObject(i);
                            TableRow row = new TableRow(getActivity());

                            TextView name = new TextView(getActivity());
                            name.setText(tournament.getString("name"));
                            name.setTextSize(20);
                            name.setTextColor(Color.rgb(245, 230, 181));
                            name.setShadowLayer(1.6f, 1.5f, 1.3f, Color.BLACK);
                            name.setGravity(Gravity.START);
                            TableRow.LayoutParams nameParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                            nameParams.setMargins(0,20, 0, 20);
                            name.setLayoutParams(nameParams);
                            row.addView(name);

                            TextView datetime = new TextView(getActivity());
                            String when = tournament.getString("datetime").substring(8,10) + ". " + tournament.getString("datetime").substring(5,7) + ". " + tournament.getString("datetime").substring(0,4);
                            datetime.setText(when);
                            datetime.setTextSize(15);
                            datetime.setTextColor(Color.WHITE);
                            datetime.setGravity(Gravity.END);
                            TableRow.LayoutParams datetimeParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                            datetimeParams.setMargins(0,20, 0, 20);
                            datetime.setLayoutParams(datetimeParams);
                            row.addView(datetime);

                            row.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        TextView tournamentName = view.findViewById(R.id.tournament_name);
                                        TableLayout tournamentDetails = view.findViewById(R.id.table_tournamentDetails);
                                        TextView clickForDetails = view.findViewById(R.id.clickForDetails);
                                        TextView about = view.findViewById(R.id.tournamentAbout);
                                        Button enter = view.findViewById(R.id.buttonTournamentEnter);
                                        TextView organizerText = view.findViewById(R.id.organizerText);
                                        TextView freeSpotsText = view.findViewById(R.id.freeSpotsText);
                                        TextView dateText = view.findViewById(R.id.dateText);
                                        String dateString = tournament.getString("datetime").substring(8,10) + ". " + tournament.getString("datetime").substring(5,7) + ". " + tournament.getString("datetime").substring(0,4);
                                        TextView timeText = view.findViewById(R.id.clockText);
                                        String timeString =  tournament.getString("datetime").substring(11, 16);
                                        TextView pointsText = view.findViewById(R.id.pointsText);
                                        TextView aboutText = view.findViewById(R.id.tournamentAboutText);
                                        TextView organizer = view.findViewById(R.id.organizer);
                                        TextView freeSpots = view.findViewById(R.id.freeSpots);
                                        TextView date = view.findViewById(R.id.date);
                                        TextView clock = view.findViewById(R.id.clock);
                                        TextView points = view.findViewById(R.id.points);
                                        TextView tournamentAbout = view.findViewById(R.id.tournamentAbout);

                                        int redish = getResources().getColor(R.color.redish);
                                        int greenish = getResources().getColor(R.color.greenish);

                                        tournamentName.setText(tournament.getString("name"));
                                        organizerText.setText(tournament.getString("organizer"));
                                        freeSpotsText.setText(tournament.getString("freeSpots"));
                                        dateText.setText(dateString);
                                        timeText.setText(timeString);
                                        pointsText.setText(tournament.getString("points"));
                                        aboutText.setText(tournament.getString("about"));

                                        tournamentName.setVisibility(View.VISIBLE);
                                        tournamentDetails.setVisibility(View.VISIBLE);
                                        about.setVisibility(View.VISIBLE);
                                        aboutText.setVisibility(View.VISIBLE);
                                        clickForDetails.setVisibility(View.GONE);

                                        Intent intent = requireActivity().getIntent();
                                        try {
                                            JSONObject player = new JSONObject(intent.getStringExtra("player"));

                                            entered = tournament.getString("joinedList").contains(player.getString("_id"));

                                            if (entered) {
                                                // igralec je prijavljen
                                                enter.setText("Izpis");
                                                enter.setBackgroundColor(getResources().getColor(R.color.redish));
                                                organizer.setTextColor(greenish);
                                                freeSpots.setTextColor(greenish);
                                                date.setTextColor(greenish);
                                                clock.setTextColor(greenish);
                                                points.setTextColor(greenish);
                                                tournamentAbout.setTextColor(greenish);
                                                enter.setVisibility(View.VISIBLE);
                                            } else {
                                                // igralec ni prijavljen
                                                enter.setText("Vpis");
                                                enter.setBackgroundColor(getResources().getColor(R.color.greenish));
                                                organizer.setTextColor(redish);
                                                freeSpots.setTextColor(redish);
                                                date.setTextColor(redish);
                                                clock.setTextColor(redish);
                                                points.setTextColor(redish);
                                                tournamentAbout.setTextColor(redish);
                                                enter.setVisibility(View.VISIBLE);
                                            }
                                            enter.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    try {
                                                        String tournamentId = tournament.getString("_id");
                                                        String playerId = player.getString("_id");
                                                        String url;
                                                        int method;
                                                        if (entered) {
                                                            url = "https://frigre.herokuapp.com/api/turnirji/" + tournamentId + "/izbrisi/" + playerId;
                                                            method = Request.Method.DELETE;
                                                        } else {
                                                            url = "https://frigre.herokuapp.com/api/turnirji/" + tournamentId + "/dodaj/" + playerId;
                                                            method = Request.Method.PUT;
                                                        }

                                                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                                                method,
                                                                url,
                                                                null,
                                                                new Response.Listener<JSONObject>() {
                                                                    @Override
                                                                    public void onResponse(JSONObject responseEnter) {
                                                                        try {
                                                                            if (method == Request.Method.DELETE) {
                                                                                JSONArray temp = new JSONArray();
                                                                                JSONArray joinedList = tournament.getJSONArray("joinedList");
                                                                                for (int i = 0; i < joinedList.length(); i++) {
                                                                                    if (!joinedList.getString(i).equals(playerId)) {
                                                                                        temp.put(temp.getString(i));
                                                                                    } else {
                                                                                        Log.i("playerId", joinedList.getString(i));
                                                                                    }
                                                                                }
                                                                                tournament.put("joinedList", temp);
                                                                                enter.setText("Vpis");

                                                                                enter.setBackgroundColor(greenish);
                                                                                organizer.setTextColor(redish);
                                                                                freeSpots.setTextColor(redish);
                                                                                date.setTextColor(redish);
                                                                                clock.setTextColor(redish);
                                                                                points.setTextColor(redish);
                                                                                tournamentAbout.setTextColor(redish);

                                                                            } else if (method == Request.Method.PUT){
                                                                                JSONArray temp = tournament.getJSONArray("joinedList");
                                                                                temp.put(playerId);
                                                                                tournament.put("joinedList", temp);
                                                                                enter.setText("Izpis");

                                                                                enter.setBackgroundColor(redish);
                                                                                organizer.setTextColor(greenish);
                                                                                freeSpots.setTextColor(greenish);
                                                                                date.setTextColor(greenish);
                                                                                clock.setTextColor(greenish);
                                                                                points.setTextColor(greenish);
                                                                                tournamentAbout.setTextColor(greenish);

                                                                            }
                                                                            entered = !entered;
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                Log.e("VOLLEY", error.toString());
                                                            }
                                                        }) {
                                                            @Override
                                                            public Map<String, String> getHeaders() throws AuthFailureError {
                                                                Map<String, String> headers = new HashMap<>();
                                                                String jwtToken = intent.getStringExtra("token");
                                                                Log.i("JWT", jwtToken);
                                                                headers.put("Authorization", "Bearer " + jwtToken);
                                                                return headers;
                                                            }
                                                        };
                                                        queue.add(jsonObjectRequest);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            tableLayoutTournaments.addView(row);
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
}