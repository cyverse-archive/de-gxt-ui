package org.iplantc.de.diskResource.client.views.toolbar;

import org.iplantc.de.client.models.HasPath;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.PathListRequest;
import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.diskResource.client.PathListAutomationView;
import org.iplantc.de.diskResource.client.gin.factory.DiskResourceSelectorFieldFactory;
import org.iplantc.de.diskResource.client.views.dialogs.SaveAsDialog;
import org.iplantc.de.diskResource.client.views.widgets.MultiFileSelectorField;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

import java.util.List;
import java.util.stream.Collectors;

public class PathListAutomationViewImpl extends Composite implements PathListAutomationView {

    interface PathListAutomationUiBinder extends UiBinder<Widget, PathListAutomationViewImpl> {}
    private static final PathListAutomationUiBinder BINDER = GWT.create(PathListAutomationUiBinder.class);

    @UiField(provided = true)
    PathListAutomationAppearance appearance;
    @UiField(provided = true) MultiFileSelectorField multiFolderSelector;
    @UiField CheckBox foldersOnlyCbx;
    @UiField TextField regexField;
    @UiField ListView<InfoType, String> infoTypeList;
    @UiField ListStore<InfoType> infoTypeStore;
    @UiField TextButton destSelector;
    @UiField TextField destField;
    @UiField HTML inputLbl, destLbl;
    @UiField FieldLabel patternLbl;

    @Inject AsyncProviderWrapper<SaveAsDialog> saveAsDialogProvider;
    @Inject DiskResourceAutoBeanFactory drFactory;

    @Inject
    public PathListAutomationViewImpl(PathListAutomationAppearance appearance,
                                      DiskResourceSelectorFieldFactory selectionFieldFactory) {
        this.appearance = appearance;
        multiFolderSelector = selectionFieldFactory.createMultiFileSelector(true, appearance.selectorEmptyText());

        initWidget(BINDER.createAndBindUi(this));
        infoTypeList.setHeight(appearance.listHeight());
        inputLbl.setHTML(appearance.formatRequiredFieldLbl(inputLbl.getHTML()));
        destLbl.setHTML(appearance.formatRequiredFieldLbl(destLbl.getHTML()));
        patternLbl.setHTML(appearance.patternMatchLbl());
    }

    @UiFactory
    ListView<InfoType, String> createListView() {
        return new ListView<>(infoTypeStore, new ValueProvider<InfoType, String>() {
            @Override
            public String getValue(InfoType object) {
                return object.getTypeString();
            }

            @Override
            public void setValue(InfoType object, String value) {
                // Do nothing
            }

            @Override
            public String getPath() {
                return null;
            }
        });
    }

    @UiFactory
    ListStore<InfoType> createListStore() {
        return new ListStore<>(InfoType::getTypeString);
    }

    @UiHandler("destSelector")
    void onSelect(SelectEvent event) {
        saveAsDialogProvider.get(new AsyncCallback<SaveAsDialog>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(final SaveAsDialog dialog) {
                dialog.addOkButtonSelectHandler(event1 -> {
                    dialog.mask(appearance.loading());
                    String destination =
                            dialog.getSelectedFolder().getPath() + "/" + dialog.getFileName();
                    destField.setValue(destination);
                    dialog.hide();
                });
                dialog.addCancelButtonSelectHandler(event1 -> {
                    dialog.hide();
                });
                dialog.show(null);
                dialog.toFront();
            }
        });
    }

    public PathListRequest getRequest() {
        PathListRequest request = drFactory.pathListRequest().as();
        request.setDest(destField.getValue());
        request.setPattern(regexField.getValue());
        request.setInfoTypes(getSelectedInfoTypes());
        request.setFoldersOnly(foldersOnlyCbx.getValue());
        request.setPaths(getPathList());
        return request;
    }

    private List<String> getSelectedInfoTypes() {
        List<InfoType> selectedItems = infoTypeList.getSelectionModel().getSelectedItems();
        if (selectedItems != null) {
            return selectedItems.stream().map(InfoType::getTypeString).collect(Collectors.toList());
        } else {
            return null;
        }
    }

    public boolean isValid() {
        return multiFolderSelector.getValue() != null && multiFolderSelector.getValue().size() != 0
               && !Strings.isNullOrEmpty(destField.getValue());
    }

    @Override
    public void addInfoTypes(List<InfoType> infoTypes) {
        infoTypeStore.addAll(infoTypes);
    }

    private List<String> getPathList() {
        List<HasPath> paths = multiFolderSelector.getValue();
        if (paths != null) {
            return paths.stream().map(HasPath::getPath).collect(Collectors.toList());
        } else {
            return null;
        }

    }
}
