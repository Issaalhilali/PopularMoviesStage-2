package com.example.popularmovies_stage2.Utils;

import com.example.popularmovies_stage2.model.Movies;
import com.example.popularmovies_stage2.model.Review;
import com.example.popularmovies_stage2.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {
    private static final String TAG = JsonUtils.class.getSimpleName();

    public static ArrayList<Movies> parseMoviesJson(String json){
        try {
            Movies movie;
            JSONObject json_Object = new JSONObject(json);
            JSONArray resultArray = new JSONArray(json_Object.optString("results", "[\"\"]"));

            ArrayList<Movies> movieItems = new ArrayList<>();
            for (int i= 0; i < resultArray.length(); i++ ){
                String thisitem = resultArray.optString(i,"");
                JSONObject movieJson = new JSONObject(thisitem);


                movie = new Movies(
                        movieJson.optString("id","Not Available"),
                        movieJson.optString("original_title","Not Available"),
                        movieJson.optString("release_date","Not Available"),
                        movieJson.optString("vote_average","Not Available"),
                        movieJson.optString("popularity","Not Available"),
                        movieJson.optString("overview","Not Available"),
                        movieJson.optString("poster_path","Not Available"),
                        movieJson.optString("backdrop_path","Not Available")
                );

                movieItems.add(movie);
            }
            return movieItems;
        } catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<Review> ReviewsJson(String json){
        try {

            Review review;
            JSONObject json_object = new JSONObject(json);

            JSONArray resultsArray = new JSONArray(json_object.optString("results","[\"\"]"));

            ArrayList<Review> reviewitems = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                String thisitem = resultsArray.optString(i, "");
                JSONObject movieJson = new JSONObject(thisitem);

                review = new Review(
                        movieJson.optString("author","Not Available"),
                        movieJson.optString("content","Not Available"),
                        movieJson.optString("id","Not Available"),
                        movieJson.optString("url","Not Available")
                );

                reviewitems.add(review);
            }

            return reviewitems;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static ArrayList<Trailer> parseTrailersJson(String json) {
        try {
            Trailer trailer;
            JSONObject json_object = new JSONObject(json);
            JSONArray resultsArray = new JSONArray(json_object.optString("results","[\"\"]"));

            ArrayList<Trailer> traileritems = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                String thisitem = resultsArray.optString(i, "");
                JSONObject movieJson = new JSONObject(thisitem);

                trailer = new Trailer(
                        movieJson.optString("name","Not Available "),
                        movieJson.optString("site","Not Available "),
                        movieJson.optString("key","Not Available ")

                );

                traileritems.add(trailer);
            }
            return traileritems;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
