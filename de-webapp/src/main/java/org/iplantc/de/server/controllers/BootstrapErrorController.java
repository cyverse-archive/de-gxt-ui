package org.iplantc.de.server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

/**
 * @author aramsey
 */
@Controller
public class BootstrapErrorController {

    @Value("${org.iplantc.discoveryenvironment.cas.app-name}") private String appName;
    @Value("${org.iplantc.discoveryenvironment.twitter-url}") private String twitterUrl;
    @Value("${org.iplantc.discoveryenvironment.facebook-url}") private String facebookUrl;
    @Value("${org.iplantc.discoveryenvironment.newsletter-url}") private String newsletterUrl;
    @Value("${org.iplantc.discoveryenvironment.ask-url}") private String askUrl;
    @Value("${org.iplantc.discoveryenvironment.maintenance-cal-url}") private String maintCalUrl;

    private final Logger LOG = LoggerFactory.getLogger(BootstrapErrorController.class);

    @RequestMapping("/de/error-{statusCode:\\d+}")
    public ModelAndView deError(@PathVariable Integer statusCode,
                                HttpServletRequest request) {
        if (statusCode >= 300 && statusCode < 600) {
            return showError("/de", statusCode, request);
        }
        return showError("/de", null, request);
    }

    @RequestMapping("/de/error")
    public ModelAndView deError(HttpServletRequest request) {
        return showError("/de", null, request);
    }

    public ModelAndView showError(final String appNamePathPart,
                                  Integer statusCode,
                                  HttpServletRequest request) {

        Map<String, String[]> parameterMap = request.getParameterMap();
        String errorMessage = parameterMap.keySet().stream()
                                          .map(key -> key.toUpperCase() + ": " + decodeUrl(Arrays.toString(parameterMap.get(key))))
                                          .collect(Collectors.joining("<br>"));
        ModelAndView modelAndView = new ModelAndView("bootstrapError");
        modelAndView.addObject("app_name", appName);
        modelAndView.addObject("login_url", appNamePathPart);
        modelAndView.addObject("logout_url", appNamePathPart + "/logout");
        modelAndView.addObject("twitter_url", twitterUrl);
        modelAndView.addObject("facebook_url", facebookUrl);
        modelAndView.addObject("newsletter_url", newsletterUrl);
        modelAndView.addObject("ask_url", askUrl);
        modelAndView.addObject("maintenance_cal_url", maintCalUrl);
        modelAndView.addObject("status_code", statusCode);
        modelAndView.addObject("error_message", errorMessage);
        modelAndView.addObject("request_url", request.getRequestURL());
        modelAndView.addObject("username", request.getRemoteUser());
        modelAndView.addObject("user_agent", request.getHeader("user-agent"));
        modelAndView.addObject("date", new Date().toString());

        return modelAndView;
    }

    String decodeUrl(String str) {
        String decoded = "";
        try {
            decoded = URLDecoder.decode(str, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            LOG.error(e.getMessage());
        }
        return decoded;
    }

}
