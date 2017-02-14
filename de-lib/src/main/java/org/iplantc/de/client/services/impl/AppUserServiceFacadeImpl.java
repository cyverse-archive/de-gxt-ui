package org.iplantc.de.client.services.impl;

import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.DELETE;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.GET;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.PATCH;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.POST;

import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.HasQualifiedId;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppAutoBeanFactory;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.apps.AppDeletionRequest;
import org.iplantc.de.client.models.apps.AppDoc;
import org.iplantc.de.client.models.apps.AppFeedback;
import org.iplantc.de.client.models.apps.AppList;
import org.iplantc.de.client.models.apps.PublishAppRequest;
import org.iplantc.de.client.models.apps.QualifiedAppId;
import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.client.models.apps.integration.AppTemplateAutoBeanFactory;
import org.iplantc.de.client.models.apps.proxy.AppListLoadResult;
import org.iplantc.de.client.models.apps.sharing.AppPermission;
import org.iplantc.de.client.models.apps.sharing.AppPermissionsRequest;
import org.iplantc.de.client.models.apps.sharing.AppSharingAutoBeanFactory;
import org.iplantc.de.client.models.apps.sharing.AppSharingRequestList;
import org.iplantc.de.client.models.apps.sharing.AppUnSharingRequestList;
import org.iplantc.de.client.services.AppUserServiceFacade;
import org.iplantc.de.client.services.converters.AppCategoryListCallbackConverter;
import org.iplantc.de.client.services.converters.AppTemplateCallbackConverter;
import org.iplantc.de.client.services.converters.DECallbackConverter;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.shared.DECallback;
import org.iplantc.de.shared.services.BaseServiceCallWrapper.Type;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.EmailServiceAsync;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import com.sencha.gxt.data.shared.SortDir;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides access to remote services for operations related to analysis submission templates.
 * 
 * @author Dennis Roberts, jstroot
 */
public class AppUserServiceFacadeImpl implements AppUserServiceFacade {

    private class AppDocCallbackConverter extends DECallbackConverter<String, AppDoc> {
        public AppDocCallbackConverter(DECallback<AppDoc> callback) {
            super(callback);
        }

        @Override
        protected AppDoc convertFrom(String object) {
            AutoBean<AppDoc> appDocAutoBean = AutoBeanCodex.decode(factory, AppDoc.class, object);
            return appDocAutoBean.as();
        }
    }

    interface AppUserServiceBeanFactory extends AutoBeanFactory {
        AutoBean<App> app();
        AutoBean<AppDoc> appDoc();
    }

    private final String APPS = "org.iplantc.services.apps";
    private final String CATEGORIES = "org.iplantc.services.apps.categories";
    private final String PIPELINES = "org.iplantc.services.apps.pipelines";
    private final DiscEnvApiService deServiceFacade;
    private final EmailServiceAsync emailService;
    @Inject IplantDisplayStrings displayStrings;
    @Inject DiskResourceUtil diskResourceUtil;
    @Inject AppSharingAutoBeanFactory shareFactory;
    @Inject AppUserServiceBeanFactory factory;
    @Inject AppServiceAutoBeanFactory svcFactory;
    @Inject AppTemplateAutoBeanFactory templateAutoBeanFactory;
    @Inject AppAutoBeanFactory appAutoBeanFactory;

    @Inject
    public AppUserServiceFacadeImpl(final DiscEnvApiService deServiceFacade,
                                    final EmailServiceAsync emailService) {
        this.deServiceFacade = deServiceFacade;
        this.emailService = emailService;
    }

    @Override
    public void getPublicAppCategories(DECallback<List<AppCategory>> callback, boolean loadHpc) {
        String address = CATEGORIES + "?public=true&hpc=" + loadHpc;
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        deServiceFacade.getServiceData(wrapper, new AppCategoryListCallbackConverter(callback));
    }

    @Override
    public void getAppCategories(boolean privateOnly, DECallback<List<AppCategory>> callback) {
        String address = CATEGORIES;
        if (privateOnly) {
            address += "?public=false";
        }
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        deServiceFacade.getServiceData(wrapper, new AppCategoryListCallbackConverter(callback));
    }

    @Override
    public void getApps(HasQualifiedId appCategory, DECallback<List<App>> callback) {
        String address = CATEGORIES + "/" + appCategory.getSystemId() + "/" + appCategory.getId();
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        deServiceFacade.getServiceData(wrapper, new DECallbackConverter<String, List<App>>(callback) {
            @Override
            protected List<App> convertFrom(String object) {
                List<App> apps = AutoBeanCodex.decode(svcFactory, AppList.class, object).as().getApps();
                return apps;
            }
        });
    }

    @Override
    public void getPagedApps(String appCategoryId,
                             int limit,
                             String sortField,
                             int offset,
                             SortDir sortDir,
                             DECallback<String> asyncCallback) {
        String address = CATEGORIES + "/" + appCategoryId + "?limit=" + limit + "&sort-field="
                + sortField + "&sort-dir=" + sortDir.toString() + "&offset=" + offset;
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        deServiceFacade.getServiceData(wrapper, asyncCallback);
    }

    @Override
    public void getDataObjectsForApp(HasQualifiedId app, DECallback<String> callback) {
        String address = APPS + "/" + app.getSystemId() + "/" + app.getId() + "/tasks";

        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        deServiceFacade.getServiceData(wrapper, callback);
    }

    @Override
    public void publishToWorld(PublishAppRequest request, DECallback<Void> callback) {
        String address = APPS + "/" + request.getSystemId() + "/" + request.getId() + "/publish";

        final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(request));
        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, encode.getPayload());

        deServiceFacade.getServiceData(wrapper, new DECallbackConverter<String, Void>(callback) {
            @Override
            protected Void convertFrom(String object) {
                return null;
            }
        });
    }

    @Override
    public void getAppDetails(final App app, DECallback<App> callback) {
        String address = APPS + "/" + app.getSystemId() + "/" + app.getId() + "/details";

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);

        deServiceFacade.getServiceData(wrapper, new DECallbackConverter<String, App>(callback) {
            @Override
            protected App convertFrom(String object) {
                Splittable split = StringQuoter.split(object);
                AutoBean<App> appAutoBean = AutoBeanUtils.getAutoBean(app);
                AutoBeanCodex.decodeInto(split, appAutoBean);
                return appAutoBean.as();
            }
        });
    }



    /**
     * calls /rate-analysis and if that is successful, calls updateDocumentationPage()
     */
    @Override
    public void rateApp(final App app,
                        final int rating,
                        final DECallback<AppFeedback> callback) {
        String address = APPS + "/" + app.getSystemId() + "/" + app.getId() + "/rating";

        Splittable payload = StringQuoter.createSplittable();
        StringQuoter.create(rating).assign(payload, "rating");
        StringQuoter.create(app.getRating().getCommentId()).assign(payload, "comment_id");

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, payload.getPayload());
        deServiceFacade.getServiceData(wrapper, new DECallbackConverter<String, AppFeedback>(callback) {
            @Override
            protected AppFeedback convertFrom(String object) {
                // Send email
                final String appName = parsePageName(app.getWikiUrl());
                if (!Strings.isNullOrEmpty(appName)) {
                    sendRatingEmail(appName, app.getIntegratorEmail());
                }

                return updateAppFeedback(object, app, rating);
            }
        });
    }

    private AppFeedback updateAppFeedback(String object, App app, Integer rating) {
        final AppFeedback appFeedback = app.getRating();
        appFeedback.setUserRating(0);
        appFeedback.setCommentId(0);

        if (rating != null) {
            appFeedback.setUserRating(rating);
        }

        if(Strings.isNullOrEmpty(object)){
            appFeedback.setAverageRating(0);
        } else {
            final Splittable split = StringQuoter.split(object);
            appFeedback.setAverageRating(split.get("average").asNumber());
            appFeedback.setTotal((int)split.get("total").asNumber());
        }
        return appFeedback;
    }

    private void sendRatingEmail(final String appName, final String emailAddress) {
        emailService.sendEmail(displayStrings.ratingEmailSubject(appName),
                               displayStrings.ratingEmailText(appName),
                               "noreply@iplantcollaborative.org", emailAddress, //$NON-NLS-1$
                               new AsyncCallback<Void>() {
                                   @Override
                                   public void onSuccess(Void arg0) {
                                   }

                                   @Override
                                   public void onFailure(Throwable arg0) {
                                       // don't bother the user if email sending fails
                                   }
                               });
    }


    @Override
    public void deleteRating(final App app, final DECallback<AppFeedback> callback) {
        // call rating service, then delete comment from wiki page
        String address = APPS + "/" + app.getSystemId() + "/" + app.getId() + "/rating";

        // KLUDGE Have to send empty JSON body with POST request
        Splittable body = StringQuoter.createSplittable();
        ServiceCallWrapper wrapper = new ServiceCallWrapper(DELETE, address, body.toString());
        deServiceFacade.getServiceData(wrapper, new DECallbackConverter<String, AppFeedback>(callback) {
            @Override
            protected AppFeedback convertFrom(String object) {
                return updateAppFeedback(object, app, null);
            }
        });
    }


    private String parsePageName(String url) {
        if (Strings.isNullOrEmpty(url)) {
            return url;
        }
        return URL.decode(diskResourceUtil.parseNameFromPath(url));
    }

    @Override
    public void favoriteApp(final HasQualifiedId appId,
                            final boolean fav,
                            final DECallback<Void> callback) {
        String address = APPS + "/" + appId.getSystemId() + "/" + appId.getId() + "/favorite";

        JSONObject body = new JSONObject();
        ServiceCallWrapper wrapper;

        if (fav) {
            wrapper = new ServiceCallWrapper(Type.PUT, address, body.toString());
        } else {
            wrapper = new ServiceCallWrapper(DELETE, address, body.toString());
        }
        deServiceFacade.getServiceData(wrapper, new DECallbackConverter<String, Void>(callback) {
            @Override
            protected Void convertFrom(String object) {
                return null;
            }
        });
    }

    @Override
    public void copyApp(final HasQualifiedId app,
                        final DECallback<AppTemplate> callback) {
        String address = APPS + "/" + app.getSystemId() + "/" + app.getId() + "/copy";

        // KLUDGE Have to send empty JSON body with POST request
        Splittable split = StringQuoter.createSplittable();
        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, split.getPayload());
        deServiceFacade.getServiceData(wrapper, new AppTemplateCallbackConverter(templateAutoBeanFactory, callback));
    }

    private AutoBean<AppDeletionRequest> appsToAppDeletionRequest(final List<App> apps) {
        final AutoBean<AppDeletionRequest> request = appAutoBeanFactory.appDeletionRequest();

        final List<QualifiedAppId> appIds = new ArrayList<>();
        for (App app : apps) {
            final QualifiedAppId qualifiedAppId = appAutoBeanFactory.qualifiedAppId().as();
            qualifiedAppId.setAppId(app.getId());
            qualifiedAppId.setSystemId(app.getSystemId());
            appIds.add(qualifiedAppId);
        }
        request.as().setAppIds(appIds);

        return request;
    }

    @Override
    public void deleteAppsFromWorkspace(final List<App> apps,
                                        final DECallback<Void> callback) {
        String address = APPS + "/" + "shredder"; //$NON-NLS-1$

        final AutoBean<AppDeletionRequest> request = appsToAppDeletionRequest(apps);
        final Splittable encode = AutoBeanCodex.encode(request);

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, encode.getPayload());
        deServiceFacade.getServiceData(wrapper, new DECallbackConverter<String, Void>(callback) {
            @Override
            protected Void convertFrom(String object) {
                return null;
            }
        });
    }

    @Override
    public void searchApp(String term, DECallback<AppListLoadResult> callback) {
        StringBuilder address = new StringBuilder(APPS);
        address.append( "?search=" + URL.encodeQueryString(term));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address.toString());
        deServiceFacade.getServiceData(wrapper,  new DECallbackConverter<String, AppListLoadResult>(callback) {
            @Override
            protected AppListLoadResult convertFrom(String object) {
                List<App> apps = AutoBeanCodex.decode(svcFactory, AppList.class, object).as().getApps();
                AutoBean<AppListLoadResult> loadResultAutoBean = svcFactory.loadResult();

                final AppListLoadResult loadResult = loadResultAutoBean.as();
                loadResult.setData(apps);
                return loadResult;
            }
        });
    }

    @Override
    public void publishWorkflow(String workflowId, String body, DECallback<String> callback) {
        String address = PIPELINES + "/" + workflowId;
        ServiceCallWrapper wrapper = new ServiceCallWrapper(Type.PUT, address, body);
        deServiceFacade.getServiceData(wrapper, callback);
    }

    @Override
    public void createWorkflows(String body, DECallback<String> callback) {
        String address = PIPELINES;
        ServiceCallWrapper wrapper = new ServiceCallWrapper(Type.POST, address, body);
        deServiceFacade.getServiceData(wrapper, callback);
    }

    @Override
    public void editWorkflow(HasId workflowId, DECallback<String> callback) {
        String address = PIPELINES + "/" + workflowId.getId() + "/ui";
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        deServiceFacade.getServiceData(wrapper, callback);
    }

    @Override
    public void copyWorkflow(String workflowId, DECallback<String> callback) {
        String address = PIPELINES + "/" + workflowId + "/copy";
        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, "{}");
        deServiceFacade.getServiceData(wrapper, callback);
    }

    @Override
    public void getAppDoc(HasQualifiedId app, DECallback<AppDoc> callback) {
        String address = APPS + "/" + app.getSystemId() + "/" + app.getId() + "/documentation";
        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deServiceFacade.getServiceData(wrapper, new AppDocCallbackConverter(callback));
    }

    @Override
    public void saveAppDoc(final HasQualifiedId app,
                           final String doc,
                           final DECallback<AppDoc> callback) {
        String address = APPS + "/" + app.getSystemId() + "/" + app.getId() + "/documentation";
        Splittable payload = StringQuoter.createSplittable();
        StringQuoter.create(doc).assign(payload, "documentation");
        ServiceCallWrapper wrapper = new ServiceCallWrapper(PATCH, address, payload.getPayload());
        deServiceFacade.getServiceData(wrapper, new AppDocCallbackConverter(callback));

    }

    @Override
    public void getPermissions(List<App> apps, DECallback<String> callback) {
        List<AppPermission> appPermissionList = new ArrayList<>();

        for(App a : apps) {
            final AppPermission appPermission = shareFactory.AppPermission().as();
            appPermission.setId(a.getId());
            appPermission.setSystemId(a.getSystemId());
            appPermissionList.add(appPermission);
        }

        final AutoBean<AppPermissionsRequest> requestAutoBean = shareFactory.AppPermissionsRequest();
        requestAutoBean.as().setApps(appPermissionList);
        final Splittable requestJson = AutoBeanCodex.encode(requestAutoBean);

        String address = APPS + "/" + "permission-lister";
        ServiceCallWrapper wrapper = new ServiceCallWrapper(Type.POST, address, requestJson.getPayload());
        deServiceFacade.getServiceData(wrapper, callback);
     }

    @Override
    public void shareApp(AppSharingRequestList request, DECallback<String> callback) {
        final String payload = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(request)).getPayload();
        GWT.log("app sharing request:" + payload);
        String address = APPS + "/" + "sharing";
        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, payload);
        deServiceFacade.getServiceData(wrapper, callback);
   }

    @Override
    public void unshareApp(AppUnSharingRequestList request, DECallback<String> callback) {
        final String payload = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(request)).getPayload();
        GWT.log("app un-sharing request:" + payload);
        String address = APPS + "/" + "unsharing";
        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, payload);
        deServiceFacade.getServiceData(wrapper, callback);
   }
}
