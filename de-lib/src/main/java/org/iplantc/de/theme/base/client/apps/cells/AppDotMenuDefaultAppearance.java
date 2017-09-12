package org.iplantc.de.theme.base.client.apps.cells;

import org.iplantc.de.apps.client.views.list.cells.AppDotMenuCell;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.theme.base.client.apps.AppsMessages;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;

public class AppDotMenuDefaultAppearance implements AppDotMenuCell.AppDotMenuAppearance {

    interface MyCss extends CssResource {
        @ClassName("dot_menu")
        String dotMenu();
    }

    interface Resources extends ClientBundle {
        @Source("AppDotMenu.gss")
        MyCss css();
    }

    interface Templates extends SafeHtmlTemplates {
        @SafeHtmlTemplates.Template("<img class='{0}' src='{1}' id='{2}'/>")
        SafeHtml cell(String imgClassName, SafeUri img, String debugId);
    }

    private Templates templates;
    private Resources resources;
    private IplantResources iplantResources;
    private IplantDisplayStrings iplantDisplayStrings;
    private AppsMessages appsMessages;

    public AppDotMenuDefaultAppearance() {
        this(GWT.create(Templates.class),
             GWT.create(Resources.class),
             GWT.create(IplantResources.class),
             GWT.create(IplantDisplayStrings.class),
             GWT.create(AppsMessages.class));
    }

    public AppDotMenuDefaultAppearance(Templates templates,
                                       Resources resources,
                                       IplantResources iplantResources,
                                       IplantDisplayStrings iplantDisplayStrings,
                                       AppsMessages appsMessages) {
        this.templates = templates;
        this.resources = resources;
        this.iplantResources = iplantResources;
        this.iplantDisplayStrings = iplantDisplayStrings;
        this.appsMessages = appsMessages;

        resources.css().ensureInjected();
    }

    @Override
    public void render(Cell.Context context, SafeHtmlBuilder sb, String debugId) {
        String className = resources.css().dotMenu();
        SafeUri uri = dotMenuIcon().getSafeUri();

        sb.append(templates.cell(className, uri, debugId));
    }

    @Override
    public ImageResource dotMenuIcon() {
        return iplantResources.dotMenu();
    }

    @Override
    public ImageResource infoIcon() {
        return iplantResources.info();
    }

    @Override
    public String infoText() {
        return iplantDisplayStrings.appInfo();
    }

    @Override
    public String favoriteText(App app) {
        if (app.getAppType().equalsIgnoreCase(App.EXTERNAL_APP)) {
            return appsMessages.featureNotSupported();
        } else if (!app.isDisabled() && app.isFavorite()) {
            return iplantDisplayStrings.remAppFromFav();
        } else if (!app.isDisabled() && !app.isFavorite()) {
            return iplantDisplayStrings.addAppToFav();
        } else {
            return iplantDisplayStrings.appUnavailable();
        }
    }

    @Override
    public ImageResource favoriteIcon(App app) {
        if (app.getAppType().equalsIgnoreCase(App.EXTERNAL_APP)) {
            return iplantResources.disabledFavIcon();
        } else if (!app.isDisabled() && app.isFavorite()) {
            return iplantResources.favIconDelete();
        } else if (!app.isDisabled() && !app.isFavorite()) {
            return iplantResources.favIconAdd();
        } else {
            return iplantResources.disabledFavIcon();
        }
    }

    @Override
    public String commentText(App app) {
        if (app.getAppType().equalsIgnoreCase(App.EXTERNAL_APP)) {
            return appsMessages.featureNotSupported();
        } else {
            return iplantDisplayStrings.comments();
        }
    }

    @Override
    public ImageResource commentIcon() {
        return iplantResources.userComment();
    }
}
