package com.example.agrifysellers.activity.Utils;


import com.example.agrifysellers.activity.order.model.Rating;

import java.util.ArrayList;

public class RatingUtils {
    public static double getAverageRating(ArrayList<Rating> ratings) {
        double sum = 0.0;

        for (Rating rating : ratings) {
            sum += rating.getRating();
        }

        return sum / ratings.size();
    }
}
