package com.shailendra.alexa.servlet;

import com.amazon.ask.Skill;
import com.amazon.ask.Skills;
import com.amazon.ask.servlet.SkillServlet;
import com.shailendra.alexa.handlers.CancelandStopIntentHandler;
import com.shailendra.alexa.handlers.ErrorHandler;
import com.shailendra.alexa.handlers.FallbackIntentHandler;
import com.shailendra.alexa.handlers.HelpIntentHandler;
import com.shailendra.alexa.handlers.LaunchRequestHandler;
import com.shailendra.alexa.handlers.MyExceptionHandler;
import com.shailendra.alexa.handlers.SessionEndedRequestHandler;
import com.shailendra.alexa.interceptors.request.LocalizationRequestInterceptor;
import com.shailendra.alexa.interceptors.request.LogRequestInterceptor;
import com.shailendra.alexa.interceptors.response.LogResponseInterceptor;

public class AlexaServlet extends SkillServlet {

    public AlexaServlet() {
        super(getSkill());
    }

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new CancelandStopIntentHandler(),
                        new HelpIntentHandler(),
                        new LaunchRequestHandler(),
                        new SessionEndedRequestHandler(),
                        new FallbackIntentHandler(),
                        new ErrorHandler())
                .addExceptionHandler(new MyExceptionHandler())
                .addRequestInterceptors(
                        new LogRequestInterceptor(),
                        new LocalizationRequestInterceptor())
                .addResponseInterceptors(new LogResponseInterceptor())
                // Add your skill id below
                //.withSkillId("[unique-value-here]")
                .build();
    }

}