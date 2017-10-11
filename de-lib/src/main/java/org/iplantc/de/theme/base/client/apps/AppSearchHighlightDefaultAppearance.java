package org.iplantc.de.theme.base.client.apps;

import com.google.common.base.Strings;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * @author jstroot
 */
public class AppSearchHighlightDefaultAppearance implements AppSearchHighlightAppearance {

    public final String REPLACEMENT_START = "<font style='background: #FF0'>";
    public final String REPLACEMENT_END = "</font>";
    private RegExp regExp;

    @Override
    public SafeHtml highlightText(String name, String pattern) {
        if(Strings.isNullOrEmpty(pattern)){
            regExp = null;
            return SafeHtmlUtils.fromTrustedString(name);
        }

        // XXX JDS Keep an eye on performance.

        // Initialize or recompile regExp if necessary
        if(regExp == null
            || !regExp.getSource().equals(pattern)) {
            try {
                regExp = RegExp.compile(pattern, "ig");
            } catch (Exception exception) {
                regExp = RegExp.compile(RegExp.quote(pattern), "ig");
            }
        }
        return SafeHtmlUtils.fromTrustedString(regExp.replace(name, REPLACEMENT_START + "$1" + REPLACEMENT_END));
    }
}
