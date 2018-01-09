package org.iplantc.de.theme.base.client.desktop.window;

import org.iplantc.de.client.models.AboutApplicationData;
import org.iplantc.de.desktop.client.views.windows.AboutApplicationWindow;
import org.iplantc.de.resources.client.messages.I18N;

import com.google.common.base.Strings;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Window;

import java.time.Year;
import java.util.Date;

/**
 * The Default production Appearance for the About Window's contents and layout.
 * 
 * @author psarando
 * 
 */
public class AboutApplicationDefaultAppearance implements AboutApplicationWindow.AboutApplicationAppearance {
    interface AboutApplicationWindowStyles extends CssResource {
        String iplantAboutPadText();
    }

    interface AboutApplicationWindowResources extends ClientBundle {
        @Source("org/iplantc/de/theme/base/client/desktop/window/AboutApplicationWindowStyles.gss")
        AboutApplicationWindowStyles css();

        @Source("org/iplantc/de/theme/base/client/desktop/window/cyverse_about.png")
        ImageResource cyverseAbout();
    }

    interface Templates extends SafeHtmlTemplates {
        @SafeHtmlTemplates.Template("<p style='font-style:italic;'> {4} </p>"
                + "<p>Release: {0}</p>"
                + "<p>Build: {1}</p>"
                + "<p>User Agent: {2}</p>"
                + "<p style='font-weight:700'> {3} </p>")
        SafeHtml about(String release, String build, String userAgent, SafeHtml copyright,
                SafeHtml nsfProject);
    }

    private final AboutApplicationWindowResources resources;
    private final Templates templates;


    public AboutApplicationDefaultAppearance(){
        this(GWT.<AboutApplicationWindowResources> create(AboutApplicationWindowResources.class),
             GWT.<Templates> create(Templates.class));
    }

    public AboutApplicationDefaultAppearance(final AboutApplicationWindowResources resources,
                                             final Templates templates){
        this.resources = resources;
        this.templates = templates;
        this.resources.css().ensureInjected();
    }
    @Override
    public SafeHtml about(AboutApplicationData data) {
        return templates.about(Strings.nullToEmpty(data.getReleaseVersion()),
                Strings.nullToEmpty(data.getBuild()),
                Window.Navigator.getUserAgent(),
                I18N.DISPLAY.projectCopyrightStatement(DateTimeFormat.getFormat("yyyy").format(new Date())),
                I18N.DISPLAY.nsfProjectText());
    }

    @Override
    public String headingText() {
        return "About Discovery Environment";
    }

    @Override
    public ImageResource iplantAbout() {
        return resources.cyverseAbout();
    }

    @Override
    public String iplantcAboutPadText() {
        return resources.css().iplantAboutPadText();
    }

    @Override
    public String windowWidth() {
        return "320";
    }

    @Override
    public String windowHeight() {
        return "260";
    }

}
