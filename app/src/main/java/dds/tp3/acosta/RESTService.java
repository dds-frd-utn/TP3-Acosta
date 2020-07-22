package dds.tp3.acosta;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class RESTService {

    public static String makeGetRequest(String restURL){

        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL( restURL );
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();

            BufferedReader bReader = new BufferedReader(
                    new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder sBuilder = new StringBuilder();

            String line = null;
            while ((line = bReader.readLine()) != null) {
                sBuilder.append(line + "\n");
            }
            inputStream.close();

            result = sBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result;

    }
}
