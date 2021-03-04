package com.shailendra.alexa.properties;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shailendra.alexa.interceptors.response.LogResponseInterceptor;

public class MovieSearchUtils {
	
	static final Logger logger = LogManager.getLogger(LogResponseInterceptor.class);
	
	public static String moviePoster ="";
	  
	public static String getMovieInfo(String movieName) {
		movieName = movieName.trim();
		movieName = movieName.replaceAll(" ", "+").toLowerCase();
		String apiUrl = "http://www.omdbapi.com/?apikey=e7a9f92b&t=" + movieName;
		String speech = null;
		try {
			URL urlForGetRequest = new URL(apiUrl);
			String readLine = null;
			HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
			conection.setRequestMethod("GET");
			int responseCode = conection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
				StringBuffer response = new StringBuffer();
				while ((readLine = in.readLine()) != null) {
					response.append(readLine);
				}
				in.close();
				logger.info("JSON String Result " + response.toString());
				ObjectMapper mapper = new ObjectMapper();
				JsonNode rootNode = mapper.readTree(response.toString());
				StringBuilder text = new StringBuilder();
				if (response != null) {
					speech = formatResponse(rootNode, text);
				}
			} else {
				logger.info("SEARCH FAILED");
				return null;
			}
		} catch (Exception e) {
			logger.info("SEARCH FAILED :: " + e.getMessage());
			return null;
		}
		return speech;
	}

	private static String formatResponse(JsonNode rootNode, StringBuilder text) {
		String speech;
		moviePoster = rootNode.get("Poster").asText();
		text.append("Wow, I have found your movie");
		text.append("," + rootNode.get("Title").asText());
		text.append(", It's been rated " + rootNode.get("imdbRating").asText() + " on IMDB");
		long amount = Long.valueOf(rootNode.get("BoxOffice").asText().substring(1).replaceAll(",", ""));
		text.append(", Total box office collection is $" + getHumanReadableAmountFromNumber(amount));
		text.append(", It's Directed by " + rootNode.get("Director").asText());
		text.append(", Staring " + rootNode.get("Actors").asText());
		text.append(", Plot is :" + rootNode.get("Plot").asText());
		text.append(" " + rootNode.get("Awards").asText());
		speech = text.toString();
		speech = speech.replace("&", "and");
		return speech;
	}
	
	public static String getMoviePoster() {
		logger.info("image : {}", moviePoster);
		return moviePoster;
	}
	
	public static String getHumanReadableAmountFromNumber(long number){
	    if(number >= 1000000000){
	        return String.format("%.2fB", number/ 1000000000.0);
	    }

	    if(number >= 1000000){
	        return String.format("%.2fM", number/ 1000000.0);
	    }

	    if(number >= 100000){
	        return String.format("%.2fL", number/ 100000.0);
	    }

	    if(number >=1000){
	        return String.format("%.2fK", number/ 1000.0);
	    }
	    return String.valueOf(number);
	}
	
	public static void main(String[] args) {
		getMovieInfo("iron man 2");
	}
}
