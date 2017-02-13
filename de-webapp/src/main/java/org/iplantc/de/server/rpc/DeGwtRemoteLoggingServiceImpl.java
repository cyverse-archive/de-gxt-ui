package org.iplantc.de.server.rpc;

import com.google.gwt.core.server.StackTraceDeobfuscator;
import com.google.gwt.logging.server.RemoteLoggingServiceUtil;
import com.google.gwt.logging.shared.RemoteLoggingService;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.logging.LogRecord;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

/**
 * This class is a light copy of {@link com.google.gwt.logging.server.RemoteLoggingServiceImpl}.
 * This class is necessary for integration of remote logging with Spring MVC.
 *
 * @author jstroot
 * @see org.iplantc.de.conf.GwtRpcConfig
 */
public class DeGwtRemoteLoggingServiceImpl implements RemoteLoggingService {
    private static Logger logger = LoggerFactory.getLogger(RemoteLoggingService.class);
    private StackTraceDeobfuscator deobfuscator;
    private String loggerNameOverride = null;
    private HttpServletRequest request;


    @Override
    public String logOnServer(LogRecord record) {
        String strongName = request.getHeader(RpcRequestBuilder.STRONG_NAME_HEADER);
        String contextPath = request.getServletPath();
        String moduleName = getModuleName(contextPath);
        String path = "WEB-INF/deploy/" + moduleName + "/symbolMaps/";
        deobfuscator = StackTraceDeobfuscator.fromResource(path);

        try {
            RemoteLoggingServiceUtil.logOnServer(record, strongName, deobfuscator, loggerNameOverride);
        } catch (RemoteLoggingServiceUtil.RemoteLoggingException e) {
            logger.error("Remote logging failed", e);
            return "Remote logging failed, check stack trace for details.";
        }
        return null;
    }

    public void setRequest(HttpServletRequest strongName) {
        this.request = strongName;
    }

    String getModuleName(String path) {
        if (path != null) {
            String pattern = "(.*?)([a-z]+)/remote_logging";
            Pattern regex = Pattern.compile(pattern);
            Matcher matcher = regex.matcher(path);
            return matcher.find() ? matcher.group(2) : "";
        }
        return "";
    }
}
