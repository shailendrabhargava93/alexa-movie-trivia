package com.shailendra.alexa.handlers;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.RequestHelper;
import com.shailendra.alexa.interceptors.response.LogResponseInterceptor;
import com.shailendra.alexa.localization.LocalizationManager;
import com.shailendra.alexa.properties.MovieSearchUtils;

public class MovieIntentHandler implements IntentRequestHandler {
	
	static final Logger logger = LogManager.getLogger(LogResponseInterceptor.class);
	
	@Override
	public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {
		return intentRequest.getIntent().getName().equals("MovieIntentHandler");
	}

	@Override
	public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
		logger.info("INSIDE MOVIE HANDLER");
		RequestHelper requestHelper = RequestHelper.forHandlerInput(handlerInput);
		Optional<String> slotValue = requestHelper.getSlotValue("movie_name");
		String movieName = slotValue.map(t -> t).orElse(null);

		String response = null;
		if (movieName == null)
			response = "Please say movie name";
		else
			response = MovieSearchUtils.getMovieInfo(movieName);

		if (response == null) {
			String speechText = LocalizationManager.getInstance().getMessage("ERROR_MSG");
			return handlerInput.getResponseBuilder().withSpeech(speechText).withSimpleCard("ERROR", speechText)
					.withReprompt(speechText).build();
		} else
			return handlerInput.getResponseBuilder().withSpeech(response).withSimpleCard("Movie Info", response)
					.withReprompt(response).build();
	}

}
