package org.iplantc.de.diskResource.client.views.dialogs;

import org.iplantc.de.client.models.HasPath;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.HTPathListRequest;
import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.diskResource.client.gin.factory.DiskResourceSelectorFieldFactory;
import org.iplantc.de.diskResource.client.views.widgets.MultiFileSelectorField;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sriram on 7/26/17.
 */
public class HTPathListAutomationDialog extends IPlantDialog {

    public interface HTPathListAutomationAppearance {
        String inputLbl();

        String folderPathOnlyLbl();

        String selectorEmptyText();

        SafeHtml patternMatchLbl();

        String infoTypeLbl();

        String destLbl();

        String patternMatchEmptyText();

        String heading();

        String loading();

        String processing();

        String requestSuccess();

        String requestFailed();

        SafeHtml formatRequiredFieldLbl(String label);

        String folderPathOnlyPrompt();

        String validationMessage();

        String dialogHeight();

        String dialogWidth();

        String listHeight();

        String destSelectorWidth();

        String select();

    }


    @UiTemplate("HTPathListAutomationDialog.ui.xml")
    interface HTPathListAutomationUiBinder extends UiBinder<Widget, HTPathListAutomationDialog> {

    }

    private static final HTPathListAutomationUiBinder BINDER =
            GWT.create(HTPathListAutomationUiBinder.class);

    @UiField(provided = true)
    MultiFileSelectorField multiFolderSelector;

    @UiField
    CheckBox foldersOnlyCbx;

    @UiField
    TextField regexField;

    @UiField(provided = true)
    ListView<InfoType, String> infoTypeList;

    ListStore<InfoType> infoTypeStore;

    @UiField
    TextButton destSelector;

    @UiField
    TextField destField;

    @UiField
    HTML inputLbl, destLbl;

    @UiField
    FieldLabel patternLbl;

    @Inject
    AsyncProviderWrapper<SaveAsDialog> saveAsDialogProvider;

    @Inject
    DiskResourceAutoBeanFactory drFactory;

    HTPathListAutomationAppearance htAppearance;


    @Inject
    public HTPathListAutomationDialog(DiskResourceSelectorFieldFactory selectionFieldFactory,
                                      HTPathListAutomationAppearance htAppearance,
                                      @Assisted("infoTypes") List<InfoType> infoTypes) {

        this.htAppearance = htAppearance;
        multiFolderSelector =
                selectionFieldFactory.createMultiFileSelector(true, htAppearance.selectorEmptyText());

        infoTypeStore = new ListStore<>(item -> item.getTypeString());
        infoTypeStore.addAll(infoTypes);
        infoTypeList = new ListView<>(infoTypeStore, new ValueProvider<InfoType, String>() {
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
        add(BINDER.createAndBindUi(this));
        infoTypeList.setHeight(htAppearance.listHeight());
        inputLbl.setHTML(htAppearance.formatRequiredFieldLbl(inputLbl.getHTML()));
        destLbl.setHTML(htAppearance.formatRequiredFieldLbl(destLbl.getHTML()));
        patternLbl.setHTML(htAppearance.patternMatchLbl());
        setHideOnButtonClick(false);
        setHeading(htAppearance.heading());
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
                    dialog.mask(htAppearance.loading());
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

    public HTPathListRequest getRequest() {
        HTPathListRequest request = drFactory.htPathListRequest().as();
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
        if (multiFolderSelector.getValue() == null || multiFolderSelector.getValue().size() == 0
            || Strings.isNullOrEmpty(destField.getValue())) {
            return false;
        }
        return true;
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
