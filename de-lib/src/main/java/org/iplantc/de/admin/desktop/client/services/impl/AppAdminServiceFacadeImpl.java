package org.iplantc.de.admin.desktop.client.services.impl;

import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.DELETE;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.GET;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.PATCH;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.POST;

import org.iplantc.de.admin.desktop.client.services.AppAdminServiceFacade;
import org.iplantc.de.admin.desktop.client.services.model.AppCategorizeRequest;
import org.iplantc.de.client.models.HasQualifiedId;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.apps.AppDoc;
import org.iplantc.de.client.models.apps.proxy.AppListLoadResult;
import org.iplantc.de.client.services.AppServiceFacade;
import org.iplantc.de.client.services.converters.AppCategoryListCallbackConverter;
import org.iplantc.de.client.services.converters.AsyncCallbackConverter;
import org.iplantc.de.client.services.converters.DECallbackConverter;
import org.iplantc.de.client.services.converters.StringToVoidCallbackConverter;
import org.iplantc.de.shared.DECallback;
import org.iplantc.de.shared.services.BaseServiceCallWrapper;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.common.base.Strings;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import java.util.List;

/**
 * @author jstroot
 */
public class AppAdminServiceFacadeImpl implements AppAdminServiceFacade {

    private final String APPS = "org.iplantc.services.apps";
    private final String APPS_ADMIN = "org.iplantc.services.admin.apps";
    private final String CATEGORIES_ADMIN = "org.iplantc.services.admin.apps.categories";

    @Inject private DiscEnvApiService deService;
    @Inject private AdminServiceAutoBeanFactory factory;
    @Inject
    AppServiceFacade.AppServiceAutoBeanFactory svcFactory;

    @Inject
    AppAdminServiceFacadeImpl() { }

    @Override
    public void addCategory(final String systemId,
                            final String newCategoryName,
                            final HasQualifiedId parentCategory,
                            final AsyncCallback<AppCategory> callback) {
        String address = CATEGORIES_ADMIN;

        Splittable payload = StringQuoter.createSplittable();
        StringQuoter.create(parentCategory.getSystemId()).assign(payload,"system_id");
        StringQuoter.create(parentCategory.getId()).assign(payload, "parent_id");
        StringQuoter.create(newCategoryName).assign(payload, "name");

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, payload.getPayload());
        deService.getServiceData(wrapper, new AsyncCallbackConverter<String, AppCategory>(callback) {
            @Override
            protected AppCategory convertFrom(String object) {
                AutoBean<AppCategory> appCategoryAutoBean = AutoBeanCodex.decode(factory, AppCategory.class, object);
                return appCategoryAutoBean.as();
            }
        });
    }

    @Override
    public void getPublicAppCategories(final DECallback<List<AppCategory>> callback,
                                       final boolean loadHpc) {
        String address = CATEGORIES_ADMIN + "?public=true&hpc=" + loadHpc;
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        deService.getServiceData(wrapper, new AppCategoryListCallbackConverter(callback));
    }

    @Override
    public void categorizeApp(AppCategorizeRequest request, AsyncCallback<String> callback) {
        String address = APPS_ADMIN;
        String body = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(request)).getPayload();

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, body);
        deService.getServiceData(wrapper, callback);
    }

    @Override
    public void deleteAppCategory(HasQualifiedId category, AsyncCallback<Void> callback) {
        String address = CATEGORIES_ADMIN + "/" + category.getSystemId() + "/" + category.getId();

        ServiceCallWrapper wrapper = new ServiceCallWrapper(DELETE, address);
        deService.getServiceData(wrapper, new StringToVoidCallbackConverter(callback));
    }

    @Override
    public void deleteApp(final HasQualifiedId app,
                          final AsyncCallback<Void> callback) {
        String address = APPS_ADMIN + "/" + app.getSystemId() + "/" + app.getId();

        ServiceCallWrapper wrapper = new ServiceCallWrapper(DELETE, address);
        deService.getServiceData(wrapper, new StringToVoidCallbackConverter(callback));
    }

    @Override
    public void getAppDetails(final HasQualifiedId app,
                              final AsyncCallback<App> callback) {
        String address = APPS_ADMIN + "/" + app.getSystemId() + "/" + app.getId() + "/details";

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new AsyncCallbackConverter<String, App>(callback) {
            @Override
            protected App convertFrom(String object) {
                AutoBean<App> appAutoBean = AutoBeanCodex.decode(factory, App.class, object);
                return appAutoBean.as();
            }
        });
    }

    @Override
    public void moveCategory(HasQualifiedId category, HasQualifiedId parent,
                             AsyncCallback<String> callback) {
        String address = CATEGORIES_ADMIN + "/" + category.getSystemId() + "/" + category.getId();

        JSONObject body = new JSONObject();
        body.put("system_id", new JSONString(parent.getSystemId()));
        body.put("parent_id", new JSONString(parent.getId()));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(PATCH, address,
                                                            body.toString());
        deService.getServiceData(wrapper, callback);
    }

    @Override
    public void renameAppCategory(final HasQualifiedId category,
                                  final String newCategoryName,
                                  final AsyncCallback<AppCategory> callback) {
        String address = CATEGORIES_ADMIN + "/" + category.getSystemId() + "/" + category.getId();

        Splittable payload = StringQuoter.createSplittable();
        StringQuoter.create(newCategoryName).assign(payload, "name");

        ServiceCallWrapper wrapper = new ServiceCallWrapper(PATCH, address,
                                                            payload.getPayload());
        deService.getServiceData(wrapper, new AsyncCallbackConverter<String, AppCategory>(callback) {
            @Override
            protected AppCategory convertFrom(String object) {
                AutoBean<AppCategory> appCategoryAutoBean = AutoBeanCodex.decode(factory, AppCategory.class, object);
                return appCategoryAutoBean.as();
            }
        });
    }

    @Override
    public void restoreApp(final HasQualifiedId app,
                           final AsyncCallback<App> callback) {
        String address = APPS_ADMIN + "/" + app.getSystemId() + "/" + app.getId();

        Splittable payload = StringQuoter.createSplittable();
        StringQuoter.create(false).assign(payload, "deleted");

        ServiceCallWrapper wrapper = new ServiceCallWrapper(PATCH, address,
                                                            payload.getPayload());
        deService.getServiceData(wrapper, new AsyncCallbackConverter<String, App>(callback) {
            @Override
            protected App convertFrom(String object) {
                AutoBean<App> appAutoBean = AutoBeanCodex.decode(factory, App.class, object);
                return appAutoBean.as();
            }
        });
    }

    @Override
    public void updateApp(final App app, AsyncCallback<App> callback) {

        String address = APPS_ADMIN + "/" + app.getSystemId() + "/" + app.getId();

        final App appClone = AutoBeanCodex.decode(factory, App.class, "{}").as();

        // do not send these field to service
        appClone.setEditedDate(null);
        appClone.setIntegrationDate(null);
        appClone.setPipelineEligibility(null);
        appClone.setStepCount(null);
        appClone.setIntegratorName(null);
        appClone.setIntegratorEmail(null);
        appClone.setAppType(null);
        appClone.setRating(null);

        // copy rest of the fields
        appClone.setId(app.getId());
        appClone.setName(app.getName());
        appClone.setDescription(app.getDescription());
        appClone.setDeleted(app.isDeleted());
        appClone.setDisabled(app.isDisabled());
        appClone.setWikiUrl(app.getWikiUrl());

        Splittable payload = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(appClone));
        ServiceCallWrapper wrapper = new ServiceCallWrapper(PATCH,
                                                            address,
                                                            payload.getPayload());
         deService.getServiceData(wrapper, new AsyncCallbackConverter<String, App>(callback) {
            @Override
            protected App convertFrom(String object) {
                AutoBean<App> appAutoBean = AutoBeanCodex.decode(factory, App.class, object);
                return appAutoBean.as();
            }
        });
    }

    @Override
    public void getAppDoc(final HasQualifiedId app,
                          final AsyncCallback<AppDoc> callback) {
        String address = APPS + "/" + app.getSystemId() + "/" + app.getId() + "/documentation";
        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new AsyncCallbackConverter<String, AppDoc>(callback) {
            @Override
            protected AppDoc convertFrom(String object) {
                AutoBean<AppDoc> appDocAutoBean = AutoBeanCodex.decode(factory, AppDoc.class, object);
                return appDocAutoBean.as();
            }
        });
    }

    @Override
    public void saveAppDoc(final HasQualifiedId app,
                           final AppDoc doc,
                           final AsyncCallback<AppDoc> callback) {
        sendAppDocUpdate(app, doc, callback, POST);
    }

    @Override
    public void updateAppDoc(final HasQualifiedId app,
                             final AppDoc doc,
                             final AsyncCallback<AppDoc> callback) {
        sendAppDocUpdate(app, doc, callback, PATCH);
    }

    private void sendAppDocUpdate(HasQualifiedId app,
                                  AppDoc doc,
                                  final AsyncCallback<AppDoc> callback,
                                  BaseServiceCallWrapper.Type method) {
        String address = APPS_ADMIN + "/" + app.getSystemId() + "/" + app.getId() + "/documentation";

        Splittable payload = StringQuoter.createSplittable();
        StringQuoter.create(doc.getDocumentation()).assign(payload, "documentation");
        ServiceCallWrapper wrapper = new ServiceCallWrapper(method, address, payload.getPayload());
        deService.getServiceData(wrapper, new AsyncCallbackConverter<String, AppDoc>(callback) {
            @Override
            protected AppDoc convertFrom(String object) {
                AutoBean<AppDoc> appDocAutoBean = AutoBeanCodex.decode(factory, AppDoc.class, object);
                return appDocAutoBean.as();
            }
        });
    }

    @Override
    public void searchApp(String term,
                          String startDate,
                          String endDate,
                          DECallback<AppListLoadResult> callback) {
        StringBuilder address = new StringBuilder(APPS_ADMIN + "?");
        if(!Strings.isNullOrEmpty(term)) {
            address.append("search=" + URL.encodeQueryString(term));
        }
        if (!Strings.isNullOrEmpty(startDate)) {
            address.append("&start_date=" + startDate);
        }
        if(!Strings.isNullOrEmpty(endDate)) {
            address.append("&end_date=" + endDate);
        }

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address.toString());
        deService.getServiceData(wrapper,  new DECallbackConverter<String, AppListLoadResult>(callback) {
            @Override
            protected AppListLoadResult convertFrom(String object) {
                AppListLoadResult loadResult =
                        AutoBeanCodex.decode(svcFactory, AppListLoadResult.class, object).as();
                return loadResult;
            }
        });
    }

}
