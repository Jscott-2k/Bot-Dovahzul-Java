import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Translator {

    private final String URL = "https://api.funtranslations.com/translate/thuum";
    private String secretKey;
    private String text;
    private JSONParser parser;

    public Translator(){
    }
    public void configure(String text, String secretKey) {
        this.text = text;
        this.secretKey = secretKey;
    }

    private String readResponseString(HttpEntity entity) throws IOException {
        InputStream inputStream  = entity.getContent();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while(true)
        {
            line = bufferedReader.readLine();
            if(line == null || line.length() == 0)
                break;
            stringBuilder.append(line);
        }
        bufferedReader.close();
        inputStream.close();

        return stringBuilder.toString();
    }

    public String translate(){

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            HttpPost httpPost = new HttpPost(URL);
            httpPost.addHeader("X-Funtranslations-Api-Secret", secretKey);

            List<NameValuePair> postParameters = new ArrayList<>();
            postParameters.add(new BasicNameValuePair("text", text));
            postParameters.add(new BasicNameValuePair("translation", "thuum"));
            httpPost.setEntity(new UrlEncodedFormEntity(postParameters));

            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
                System.out.println(response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
                HttpEntity entity = response.getEntity();
                String translationJSON = readResponseString(entity);

                parser = new JSONParser();
                System.out.println("Resp String Test: " + translationJSON);
                JSONObject parsedObject = (JSONObject) parser.parse(translationJSON);
                EntityUtils.consume(entity);

                JSONObject content = (JSONObject) parser.parse(parsedObject.get("contents").toString());
                System.out.println(content.get("translated"));
                return content.get("translated").toString();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return "Error";
    }

}
