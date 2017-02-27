package org.iplantc.de.apps.client.views.details.doc;

import org.iplantc.de.apps.client.events.selection.SaveMarkdownSelected;
import org.iplantc.de.apps.client.gin.factory.AppDocEditViewFactory;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppDoc;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;

import java.util.List;

/**
 * Created by jstroot on 3/4/15.
 * @author jstroot
 */
public class AppDocMarkdownDialog extends IPlantDialog implements SaveMarkdownSelected.SaveMarkdownSelectedHandler,
                                                                  SaveMarkdownSelected.HasSaveMarkdownSelectedHandlers {

    public interface AppDocMarkdownDialogAppearance {

        SafeHtml createDocumentMarkdown(String mdRenderedDoc, List<String> appDocReferences);
    }

    @UiTemplate("AppDocMarkdownDialog.ui.xml")
    interface AppDocMarkdownWindowUiBinder extends UiBinder<TabPanel, AppDocMarkdownDialog> {
    }

    private static final AppDocMarkdownWindowUiBinder ourUiBinder = GWT.create(AppDocMarkdownWindowUiBinder.class);
    private final AppDocMarkdownDialogAppearance appearance;
    private AppDocEditViewFactory docEditViewFactory;
    @UiField InlineHTML documentation;
    @UiField TabItemConfig editTabConfig;
    @UiField TabPanel tabPanel;
    private AppDocEditViewImpl appDocEditView;

    @Inject
    public AppDocMarkdownDialog(AppDocMarkdownDialogAppearance appearance,
                                AppDocEditViewFactory docEditViewFactory) {
        this.appearance = appearance;
        this.docEditViewFactory = docEditViewFactory;
        setModal(false);
        setResizable(false);
        setSize("700px", "500px");
        getButtonBar().clear();
        ourUiBinder.createAndBindUi(this);
    }


    public static native String render(String val) /*-{
        var markdown = $wnd.Markdown.getSanitizingConverter();
        return markdown.makeHtml(val);
    }-*/;

    @Override
    public HandlerRegistration addSaveMarkdownSelectedHandler(SaveMarkdownSelected.SaveMarkdownSelectedHandler handler) {
        try {
            return appDocEditView.addHandler(handler, SaveMarkdownSelected.TYPE);
        } catch (NullPointerException ex) {
            GWT.log("Handler added for non markdown documentation", ex);
            return null;
        }
    }

    @Override
    public void onSaveMarkdownSelected(final SaveMarkdownSelected event) {
        // Refresh the other
        final String editorContent = event.getEditorContent();
        documentation.setHTML(editorContent);
    }

    public void show(App app, AppDoc appDoc, UserInfo userInfo) {
        setHeading(app.getName());
        // Build refLink html
        SafeHtml docHtml = appearance.createDocumentMarkdown(render(appDoc.getDocumentation()), appDoc.getReferences());
        documentation.setHTML(docHtml);


        if(userInfo.getEmail().equals(app.getIntegratorEmail())){
            // If current user is the app integrator, add edit tab
            appDocEditView = docEditViewFactory.create(app, appDoc);
            // Add handler to forward event
            appDocEditView.addSaveMarkdownSelectedHandler(this);
            tabPanel.add(appDocEditView, editTabConfig);
        }


        // Add uiBinder tab panel to dialog
        add(tabPanel);
        super.show();
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This method is not supported. Use show(App, AppDoc) method instead.");
    }

}
