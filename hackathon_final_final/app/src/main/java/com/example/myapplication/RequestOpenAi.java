package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RequestOpenAi {
    private static String openaiKey="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";
    private static String openaiURL="https://api.openai.com/v1/chat/completions";


    public List<ActivitateSkill> chatGPT(String message) {
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "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"; // API key goes here
        String model = "gpt-3.5-turbo"; // current model of chatgpt api
        List<ActivitateSkill>lista=new ArrayList<>();
        try {
            // Create the HTTP POST request
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
            con.setRequestProperty("Content-Type", "application/json");

            // Build the request body
            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}]}";
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Get the response
            /*BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;*/

           /* StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();*/
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line.trim()+"\n");
            }
            br.close();
            sb.toString().replaceAll("/", "");
            String jsonResponse=sb.toString();
            try{
                JSONObject responseJson = new JSONObject(jsonResponse);
                JSONArray assistantMessageContent = new JSONObject(responseJson.getJSONArray("choices").getJSONObject(0)
                        .getJSONObject("message").getString("content")).getJSONArray("activities");
                for(int i=0;i<assistantMessageContent.length();i++){
                    JSONObject activitate=assistantMessageContent.getJSONObject(i);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        ActivitateSkill activitateSkill=new ActivitateSkill(LocalDate.now().toString(), activitate.getString("skill"), activitate.getString("hours"));
                        lista.add(activitateSkill);
                    }
                }
            }catch (JSONException e){

            }



            //con.disconnect();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            return lista;
        }
    }
    public String requestMessage(String message) {
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "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";
        String model = "gpt-3.5-turbo";
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
            con.setRequestProperty("Content-Type", "application/json");

            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}]}";
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;

            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine.trim()+"\n");
            }
            in.close();
            response.toString().replaceAll("\\n", "\n");
            return extractContentFromResponse(response.toString());

            // returns the extracted contents of the response.

        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }


    public static String extractContentFromResponse(String response) {
        int startMarker = response.indexOf("content")+11; // Marker for where the content starts.
        int endMarker = response.indexOf("\"", startMarker); // Marker for where the content ends.
        return response.substring(startMarker, endMarker); // Returns the substring containing only the response.
    }

}
