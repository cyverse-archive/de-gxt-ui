package org.iplantc.de.server.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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

    @RequestMapping("/de/error-{statusCode:\\d+}")
    public ModelAndView deError(@PathVariable Integer statusCode) {
        if (statusCode >= 300 && statusCode < 600) {
            return showError("/de", statusCode);
        }
        return showError("/de", null);
    }

    @RequestMapping("/de/error")
    public ModelAndView deError() {
        return showError("/de", null);
    }


    public ModelAndView showError(final String appNamePathPart, Integer statusCode){
        ModelAndView modelAndView = new ModelAndView("bootstrapError");
        modelAndView.addObject("app_name", appName);
        modelAndView.addObject("login_url", appNamePathPart);
        modelAndView.addObject("logout_url", appNamePathPart + "/logout");
        modelAndView.addObject("twitter_url", twitterUrl);
        modelAndView.addObject("facebook_url", facebookUrl);
        modelAndView.addObject("newsletter_url", newsletterUrl);
        modelAndView.addObject("ask_url", askUrl);
        modelAndView.addObject("status_code", statusCode);

        return modelAndView;
    }

}
