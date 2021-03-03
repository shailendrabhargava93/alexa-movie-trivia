package com.shailendra.alexa.handlers;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.RequestHelper;
import com.shailendra.alexa.properties.MovieSearchUtils;

public class MovieIntentHandler implements IntentRequestHandler {

	@Override
	public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {
		return intentRequest.getIntent().getName().equals("MovieIntentHandler");
	}

	@Override
	public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

		RequestHelper requestHelper = RequestHelper.forHandlerInput(handlerInput);
		Optional<String> slotValue = requestHelper.getSlotValue("movie_name");
		String movieName = slotValue.map(t -> t).orElse(null);

		String response = null;
		if (movieName == null)
			response = "Please say movie name";
		else
			response = MovieSearchUtils.getMovieInfo(movieName);

		return handlerInput.getResponseBuilder().withSpeech(response).withShouldEndSession(false).build();
	}

}
