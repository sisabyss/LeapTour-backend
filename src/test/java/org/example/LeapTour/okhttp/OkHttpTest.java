package org.example.LeapTour.okhttp;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class OkHttpTest {
    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://travel-advisor.p.rapidapi.com/restaurants/list-by-latlng?latitude=12.91285&longitude=100.87808&limit=30&currency=USD&distance=2&open_now=false&lunit=km&lang=en_US")
                .get()
                .addHeader("x-rapidapi-key", "d283acd8ebmsh86e67a15d2cd3dfp1ec54ajsn3ba92b3cff73")
                .addHeader("x-rapidapi-host", "travel-advisor.p.rapidapi.com")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }
}
