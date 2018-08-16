package org.iplantc.de.admin.desktop.client.metadata.presenter;

import org.iplantc.de.admin.desktop.client.metadata.events.AddMetadataSelectedEvent;
import org.iplantc.de.admin.desktop.client.metadata.events.DeleteMetadataSelectedEvent;
import org.iplantc.de.admin.desktop.client.metadata.events.EditMetadataSelectedEvent;
import org.iplantc.de.admin.desktop.client.metadata.presenter.callbacks.SaveTemplateError;
import org.iplantc.de.admin.desktop.client.metadata.presenter.callbacks.SaveTemplateSuccess;
import org.iplantc.de.admin.desktop.client.metadata.service.MetadataTemplateAdminServiceFacade;
import org.iplantc.de.admin.desktop.client.metadata.view.EditMetadataTemplateView;
import org.iplantc.de.admin.desktop.client.metadata.view.TemplateListingView;
import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.MetadataTemplate;
import org.iplantc.de.client.models.diskResources.MetadataTemplateInfo;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.base.Strings;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;

import java.util.List;
import java.util.logging.Logger;

public class MetadataTemplatesPresenterImpl implements TemplateListingView.Presenter,
                                                       EditMetadataTemplateView.Presenter,
                                                       EditMetadataSelectedEvent.EditMetadataSelectedEventHandler,
                                                       AddMetadataSelectedEvent.AddMetadataSelectedEventHandler,
                                                       DeleteMetadataSelectedEvent.DeleteMetadataSelectedEventHandler {

    private final TemplateListingView view;
    @Inject AsyncProviderWrapper<DiskResourceServiceFacade> drSvcFacProvider;
    private final MetadataTemplateAdminServiceFacade mdSvcFac;
    private final EditMetadataTemplateView editView;
    private final DiskResourceAutoBeanFactory drFac;
    private final TemplateListingView.Presenter.MetadataPresenterAppearance appearance;

    Logger LOG = Logger.getLogger("MetadataTemplatesPresenterImpl");

    @Inject
    MetadataTemplatesPresenterImpl(final TemplateListingView view,
                                   final EditMetadataTemplateView editView,
                                   final MetadataTemplateAdminServiceFacade mdSvcFac,
                                   final DiskResourceAutoBeanFactory drFac,
                                   final TemplateListingView.Presenter.MetadataPresenterAppearance appearance) {
        this.view = view;
        this.editView = editView;
        this.mdSvcFac = mdSvcFac;
        this.drFac = drFac;
        this.appearance = appearance;

        view.addAddMetadataSelectedEventHandler(this);
        view.addDeleteMetadataSelectedEventHandler(this);
        view.addEditMetadataSelectedEventHandler(this);
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view.asWidget());
        loadTemplates();
    }

    private void loadTemplates() {
        view.mask("loading");
        mdSvcFac.getMetadataTemplateListing(new AsyncCallback<List<MetadataTemplateInfo>>() {

            @Override
            public void onSuccess(List<MetadataTemplateInfo> result) {
                view.unmask();
                view.loadTemplates(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                view.unmask();
                ErrorHandler.post(appearance.templateRetrieveError(), caught);

            }
        });
    }

    @Override
    public void onDeleteMetadataSelectedHandler(DeleteMetadataSelectedEvent event) {
        final MetadataTemplateInfo template = event.getTemplateInfo();
        ConfirmMessageBox cmb = new ConfirmMessageBox("Confirm", appearance.deleteTemplateConfirm());
        cmb.addDialogHideHandler(new DialogHideHandler(){

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(PredefinedButton.OK)
                        || event.getHideButton().equals(PredefinedButton.YES)) {
                    mdSvcFac.deleteTemplate(template.getId(), new AsyncCallback<String>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            ErrorHandler.post(appearance.deleteTemplateError(), caught);

                        }

                        @Override
                        public void onSuccess(String result) {
                            IplantAnnouncer.getInstance()
                                           .schedule(new SuccessAnnouncementConfig(appearance.deleteTemplateSuccess()));
                            loadTemplates();
                        }
                    });
                }

            }
            
        });
        cmb.show();
        setMsgBoxDebugIds(cmb);
    }

    private void setMsgBoxDebugIds(ConfirmMessageBox cmb) {
        cmb.ensureDebugId(Belphegor.MetadataIds.DELETE_MSG_BOX);
        cmb.getButton(PredefinedButton.YES).ensureDebugId(Belphegor.MetadataIds.DELETE_MSG_BOX + Belphegor.MetadataIds.YES);
        cmb.getButton(PredefinedButton.NO).ensureDebugId(Belphegor.MetadataIds.DELETE_MSG_BOX + Belphegor.MetadataIds.NO);
    }

    @Override
    public void onEditMetadataSelected(EditMetadataSelectedEvent event) {
        final MetadataTemplateInfo template = event.getTemplateInfo();
        drSvcFacProvider.get(new AsyncCallback<DiskResourceServiceFacade>() {
            @Override
            public void onFailure (Throwable e) {}

            @Override
            public void onSuccess (DiskResourceServiceFacade drSvcFac) {
              drSvcFac.getMetadataTemplate(template.getId(), new AsyncCallback<MetadataTemplate>() {

                @Override
                public void onSuccess(MetadataTemplate result) {
                    final Splittable metadataTemplate = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(result));

                    editView.edit(MetadataTemplatesPresenterImpl.this, metadataTemplate);
                }

                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(appearance.templateRetrieveError(), caught);
                }
              });
            }
        });

    }

    private void addTemplate(final String template, SaveTemplateSuccess resolve, SaveTemplateError reject) {
        final MetadataTemplatesPresenterImpl presenter = this;

        mdSvcFac.addTemplate(template, new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(appearance.addTemplateError(), caught);
                reject.reject(caught.getMessage());
                presenter.closeTemplateInfoDialog();
            }

            @Override
            public void onSuccess(String result) {
                IplantAnnouncer.getInstance().schedule(new SuccessAnnouncementConfig(appearance.addTemplateSuccess()));

                resolve.resolve(AutoBeanCodex.encode(AutoBeanCodex.decode(drFac, MetadataTemplate.class, result)));
                presenter.closeTemplateInfoDialog();
                loadTemplates();
            }
        });
    }

    private void updateTemplate(final String templateId, final String template, SaveTemplateSuccess resolve, SaveTemplateError reject) {
        final MetadataTemplatesPresenterImpl presenter = this;

        mdSvcFac.updateTemplate(templateId, template, new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(appearance.updateTemplateError(), caught);
                reject.reject(caught.getMessage());
                presenter.closeTemplateInfoDialog();
            }

            @Override
            public void onSuccess(String result) {
                IplantAnnouncer.getInstance()
                               .schedule(new SuccessAnnouncementConfig(appearance.updateTemplateSuccess()));

                resolve.resolve(AutoBeanCodex.encode(AutoBeanCodex.decode(drFac, MetadataTemplate.class, result)));
                presenter.closeTemplateInfoDialog();
                loadTemplates();
            }
        });
    }

    @Override
    public void onAddMetadataSelected(AddMetadataSelectedEvent event) {
        final AutoBean<MetadataTemplate> templateAutoBean = drFac.getTemplate();
        templateAutoBean.as().setName("");
        templateAutoBean.as().setDescription("");

        editView.edit(this, AutoBeanCodex.encode(templateAutoBean));
    }

    @SuppressWarnings("unusable-by-js")
    @Override
    public void onSaveTemplate(Splittable template, SaveTemplateSuccess resolve, SaveTemplateError reject) {
        final MetadataTemplate metadataTemplate = AutoBeanCodex.decode(drFac, MetadataTemplate.class, template).as();
        final String templateId = metadataTemplate.getId();

        final String payload = template.getPayload();

        if (Strings.isNullOrEmpty(templateId)) {
            addTemplate(payload, resolve, reject);
        } else {
            updateTemplate(templateId, payload, resolve, reject);
        }
    }

    @Override
    public void closeTemplateInfoDialog() {
        editView.closeDialog();
    }

    @Override
    public void setViewDebugId(String baseId) {
        view.asWidget().ensureDebugId(baseId + Belphegor.MetadataIds.VIEW);
    }
}
