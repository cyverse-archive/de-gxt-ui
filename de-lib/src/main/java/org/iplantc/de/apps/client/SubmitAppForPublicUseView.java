package org.iplantc.de.apps.client;

import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppRefLink;
import org.iplantc.de.client.models.apps.PublishAppRequest;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.TreeStyle;

import java.util.List;

/**
 * @author jstroot
 */
public interface SubmitAppForPublicUseView extends IsWidget {

    interface SubmitAppAppearance {

        ImageResource categoryIcon();

        ImageResource categoryOpenIcon();

        String links();

        String makePublicFail();

        String makePublicSuccessMessage(String appName);

        String publicNameNote();

        SafeHtml publicNameHTML();

        String publicName();

        String publicDescriptionNote();

        String publicSubmissionForm();

        String publicSubmissionFormAttach();

        String publicSubmissionFormCategories();

        String completeRequiredFieldsError();

        String publishFailureDefaultMessage();

        ImageResource subCategoryIcon();

        String submit();

        String submitForPublicUse();

        String submitForPublicUseIntro();

        String submitRequest();

        String submitting();

        String testDataLabel();

        String inputDescriptionEmptyText();

        String optionalParametersEmptyText();

        String outputDescriptionEmptyText();

        ImageResource addIcon();

        String add();

        String delete();

        ImageResource deleteIcon();

        SafeHtml publicDescription();

        String publicAttach();

        SafeHtml describeInputLbl();

        SafeHtml describeParamLbl();

        SafeHtml describeOutputLbl();

        SafeHtml publicCategories();

        String testDataWarn();

        String warning();

        String communities();

        TreeStyle getTreeStyle();

        String contentPanelHeight();

        String publicSubmissionFormCommunities();
    }

    public interface Presenter extends org.iplantc.de.commons.client.presenter.Presenter {
        void onSubmit();

        void go(HasOneWidget container, App selectedApp, AsyncCallback<String> callback);
    }

    Tree<OntologyHierarchy, String> getCategoryTree();

    String getMarkDownDocs();

    String getAppDescription();

    List<String> getReferenceLinks();

    String getAppName();

    App getSelectedApp();

    boolean validate();

    public void loadReferences(List<AppRefLink> refs);

    void setSelectedApp(App selectedApp);

    Tree<Group, String> getCommunityTree();
}
