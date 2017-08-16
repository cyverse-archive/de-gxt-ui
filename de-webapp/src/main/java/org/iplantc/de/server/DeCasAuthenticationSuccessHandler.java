package org.iplantc.de.server;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author dennis
 */
public class DeCasAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private RequestCache requestCache = new HttpSessionRequestCache();
    private String baseUrl = null;

    /**
     * Sets a custom request cache. This should only be required if Spring Security is configured
     * to use a custom request cache elsewhere. This class will attempt to extract a saved request
     * from the user's session by default.
     *
     * @param requestCache The request cache to use.
     */
    public void setRequestCache(RequestCache requestCache) {
        this.requestCache = requestCache;
    }

    /**
     * Sets the default base URL to redirect the user to when authentication succeeds.
     *
     * If this property is set then this class will construct the redirect URL from the configured base
     * URL and the URL path and query parameters from the saved request. If no saved request can be found
     * then the base URL will be used as-is.
     *
     * If this property is not set then the class will use the URL from the saved request if it's
     * available. Otherwise, it will default to {@code "/"}.
     *
     * @param baseUrl The base URL to use.
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Sets the strategy to use when redirecting the user. Spring's {@code DefaultRedirectStrategy}
     * will be used by default.
     *
     * @param redirectStrategy the redirect strategy to use.
     */
    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // Look up the saved request.
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        // Redirect the user.
        redirectStrategy.sendRedirect(request, response, getTargetUrl(savedRequest));
    }

    private String getTargetUrl(SavedRequest savedRequest) throws ServletException {

        // Fall back to the defaults if a saved request wasn't found.
        if (savedRequest == null) {
            return (baseUrl == null) ? "/" : baseUrl;
        }

        // Use the URL from the original request if no base URL was specified.
        if (baseUrl == null) {
            return savedRequest.getRedirectUrl();
        }

        // Parse both the URI from the original request and the configured base URI.
        URI baseUri = parseUri(baseUrl, "base URI");
        URI originalUri = parseUri(savedRequest.getRedirectUrl(), "original URI");

        return buildUri(baseUri, originalUri).toString();
    }

    /**
     * Parses a URI.
     *
     * @param spec the string representation of the URI.
     * @param description a brief description of the URI, used if an error occurs.
     * @return the parsed URI.
     * @throws ServletException if the URI cannot be parsed.
     */
    private URI parseUri(String spec, String description) throws ServletException {
        try {
            return new URI(spec);
        } catch (URISyntaxException e) {
            throw new ServletException("unable to parse " + description, e);
        }
    }

    /**
     * Builds the redirect URI from the base URI and the source URI.
     *
     * @param baseUri The parsed base URI.
     * @param originalUri The parsed URI from the original request.
     * @return the redirect URI.
     * @throws ServletException if the redirect URI cannot be built.
     */
    private URI buildUri(URI baseUri, URI originalUri) throws ServletException {
        try {
            return new URI(
                    baseUri.getScheme(),
                    baseUri.getUserInfo(),
                    baseUri.getHost(),
                    baseUri.getPort(),
                    originalUri.getPath(),
                    originalUri.getQuery(),
                    originalUri.getFragment()
            );
        } catch (URISyntaxException e) {
            throw new ServletException("unable to build redirect URI", e);
        }
    }
}
