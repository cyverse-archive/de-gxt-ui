package org.iplantc.de.server.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * @author jstroot
 */
@Controller
public class BelphegorController {

    @Value("${org.iplantc.discoveryenvironment.session-timeout}") private String sessionTimeout;

    @RequestMapping("/belphegor/")
    public ModelAndView viewBelphegor(final HttpSession session) {
        session.setMaxInactiveInterval(Integer.parseInt(sessionTimeout));
        return new ModelAndView("belphegor");
    }
}
