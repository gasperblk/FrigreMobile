package si.uni_lj.fe.tnuv.frigre;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        queue = Volley.newRequestQueue(this);

        EditText email = findViewById(R.id.editTextLoginEmail);
        EditText password = findViewById(R.id.editTextLoginPassword);

        SharedPreferences preferences = getSharedPreferences("rememberMe", MODE_PRIVATE);
        email.setText(preferences.getString("email", ""));
        password.setText(preferences.getString("password", ""));
    }

    public void login(View view) {
        TextView textView = findViewById(R.id.textViewLogin);
        EditText email = findViewById(R.id.editTextLoginEmail);
        EditText password = findViewById(R.id.editTextLoginPassword);
        CheckBox remember = findViewById(R.id.checkBoxRememberMe);

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("elektronskiNaslov", email.getText());
            jsonBody.put("geslo", password.getText());

            // Request a string response from the provided URL.
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                    "https://frigre.herokuapp.com/api/prijava",
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responseUser) {
                        try {

                            JSONObject user = new JSONObject(decode(responseUser.toString()));
                            String username = user.getString("ime");

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
                                                    JSONArray players = college.getJSONArray("players");
                                                    for (int j = 0; j < players.length(); j++) {
                                                        JSONObject player = null;
                                                        try {
                                                            player = players.getJSONObject(j);
                                                            if (player.getString("name").equals(username)) {
                                                                player.put("idFakultete", college.getString("_id"));

                                                                if (remember.isChecked()) {
                                                                    SharedPreferences preferences = getSharedPreferences("rememberMe", MODE_PRIVATE);
                                                                    SharedPreferences.Editor editor = preferences.edit();
                                                                    editor.putString("email", email.getText().toString());
                                                                    editor.putString("password", password.getText().toString());
                                                                    editor.apply();
                                                                }

                                                                Intent intentLogin = new Intent(getBaseContext(), MainActivity.class);
                                                                intentLogin.putExtra("player", player.toString());
                                                                intentLogin.putExtra("token", responseUser.getString("žeton"));
                                                                startActivity(intentLogin);
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError errorPlayers) {
                                            textView.setText("error");
                                        }
                                    }
                            );
                            queue.add(jsonArrayRequest);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.i("VOLLEY", responseUser.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError errorToken) {
                        TextView tooBad = findViewById(R.id.tooBad);
                        String userError = "Napačen elektronski naslov ali geslo";
                        tooBad.setText(userError);
                        tooBad.setVisibility(View.VISIBLE);
                        Log.e("VOLLEY", errorToken.toString());
                    }
                }
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };
            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void forgotPassword(View view) {
        TextView tooBad = findViewById(R.id.tooBad);
        String ohno = "That's too bad :(";
        tooBad.setText(ohno);
        tooBad.setVisibility(View.VISIBLE);
    }

    public void goToRegister(View view) {
        Intent intentGoToRegister = new Intent(this, RegisterActivity.class);
        startActivity(intentGoToRegister);
    }

    public static String decode(String JWTEncoded) throws Exception {
        String decodedStr = "";
        try {
            String[] split = JWTEncoded.split("\\.");
            Log.d("JWT_DECODED", "Header: " + getJson(split[0]));
            Log.d("JWT_DECODED", "Body: " + getJson(split[1]));
            decodedStr = getJson(split[1]);
        } catch (UnsupportedEncodingException e) {
            Log.e("JWT_DECODED", "Error: " + e);
            decodedStr = e.toString();
        }
        return decodedStr;
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes);
    }

}