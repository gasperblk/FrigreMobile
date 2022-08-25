package si.uni_lj.fe.tnuv.frigre;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    HashMap<String, String> colleges = new HashMap<String, String>();
    List<String> collegesList = new ArrayList<String>();
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Spinner chooseCollege = findViewById(R.id.spinnerRegisterCollege);
        queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
            Request.Method.GET,
            "https://frigre.herokuapp.com/api/fakultete",
            null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray responseColleges) {
                    for (int i = 0; i < responseColleges.length(); i++) {
                        try {
                            JSONObject college = responseColleges.getJSONObject(i);
                            colleges.put(college.getString("name"), college.getString("_id"));
                            collegesList.add(college.getString("name"));
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getBaseContext(), R.layout.spinner_item, collegesList);
                            chooseCollege.setAdapter(adapter);
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

    public void register(View view) {
        EditText email = findViewById(R.id.editTextRegisterEmail);
        EditText nickname = findViewById(R.id.editTextRegisterNickname);
        Spinner chooseCollege = findViewById(R.id.spinnerRegisterCollege);
        String college = chooseCollege.getSelectedItem().toString();
        EditText password = findViewById(R.id.editTextRegisterPassword);
        EditText passwordAgain = findViewById(R.id.editTextRegisterPasswordAgain);

        if (password.getText().toString().equals(passwordAgain.getText().toString())) {
            try {

                JSONObject jsonBodyUser = new JSONObject();
                jsonBodyUser.put("ime", nickname.getText().toString());
                jsonBodyUser.put("elektronskiNaslov", email.getText().toString());
                jsonBodyUser.put("geslo", password.getText().toString());
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    "https://frigre.herokuapp.com/api/registracija",
                    jsonBodyUser,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject responseUser) {
                            String url = "https://frigre.herokuapp.com/api/fakultete/" + colleges.get(college) + "/igralci";
                            try {
                                JSONObject jsonBodyPlayer = new JSONObject();
                                jsonBodyPlayer.put("name", nickname.getText());
                                JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(
                                    Request.Method.POST,
                                    url,
                                    jsonBodyPlayer,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject responsePlayer) {
                                            try {
                                                responsePlayer.put("idFakultete", college);
                                                Intent intentRegister = new Intent(getBaseContext(), MainActivity.class);
                                                intentRegister.putExtra("player", responsePlayer.toString());
                                                startActivity(intentRegister);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("VOLLEY", error.toString());
                                        }
                                    }
                                );
                                queue.add(jsonObjectRequest1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEY", error.toString());
                        }
                    }
                );
                queue.add(jsonObjectRequest);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            TextView textView = findViewById(R.id.textViewRegister);
            textView.setText("Gesli se morata ujemati");
        }
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}