package org.iplantc.de.diskResource.client.presenters.details;

import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.de.client.models.search.SearchAutoBeanFactory;
import org.iplantc.de.client.models.tags.IplantTagAutoBeanFactory;
import org.iplantc.de.client.models.tags.IplantTagList;
import org.iplantc.de.client.models.tags.Tag;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.services.FileSystemMetadataServiceFacade;
import org.iplantc.de.client.services.TagsServiceFacade;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.diskResource.client.DetailsView;
import org.iplantc.de.diskResource.client.events.search.SubmitDiskResourceQueryEvent;
import org.iplantc.de.diskResource.client.events.selection.RemoveResourceTagSelected;
import org.iplantc.de.diskResource.client.events.selection.UpdateResourceTagSelected;
import org.iplantc.de.diskResource.client.presenters.callbacks.TagAttachCallback;
import org.iplantc.de.diskResource.client.presenters.callbacks.TagDetachCallback;
import org.iplantc.de.diskResource.client.presenters.callbacks.TagsFetchCallback;
import org.iplantc.de.diskResource.client.presenters.callbacks.TagsSearchCallback;
import org.iplantc.de.resources.client.messages.I18N;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jstroot
 */
public class DetailsViewPresenterImpl implements DetailsView.Presenter,
                                                 RemoveResourceTagSelected.RemoveResourceTagSelectedHandler,
                                                 UpdateResourceTagSelected.UpdateResourceTagSelectedHandler {

    @Inject IplantAnnouncer announcer;
    @Inject FileSystemMetadataServiceFacade metadataService;
    @Inject DetailsView.Presenter.Appearance appearance;
    @Inject
    TagsServiceFacade tagsService;
    @Inject
    DiskResourceServiceFacade diskResourceService;
    @Inject
    SearchAutoBeanFactory sabFactory;

    private List<Tag> tags = new ArrayList<>();

    private final DetailsView view;

    private HandlerManager handlerManager;

    IplantTagAutoBeanFactory factory;

    @Inject
    DetailsViewPresenterImpl(final DetailsView view, final IplantTagAutoBeanFactory factory) {
        this.view = view;
        this.factory = factory;

        view.setPresenter(this);
    }

    @Override
    public HandlerRegistration addSubmitDiskResourceQueryEventHandler(SubmitDiskResourceQueryEvent.SubmitDiskResourceQueryEventHandler handler) {
        return ensureHandlers().addHandler(SubmitDiskResourceQueryEvent.TYPE, handler);
    }

    @Override
    public void onUpdateResourceTagSelected(UpdateResourceTagSelected event) {
        DiskResource resource = event.getDiskResource();
        Tag tag = event.getTag();
        attachTag(tag.getId(), tag.getValue(), resource.getId(), null);

    }

    @Override
    public void onRemoveResourceTagSelected(RemoveResourceTagSelected event) {
        Tag tag = event.getTag();
        DiskResource resource = event.getResource();
        detachTag(tag.getId(), tag.getValue(), resource.getId(), null);
    }

    @Override
    public void fetchTagsForResource(String diskResourceId, TagsFetchCallback callback) {
        tags.clear();
        metadataService.getTags(diskResourceId, new AsyncCallback<List<Tag>>() {
            @Override
            public void onFailure(Throwable caught) {
                // FIXME Move to appearance
                ErrorHandler.post("Unable to retrieve tags!", caught);
            }

            @Override
            public void onSuccess(List<Tag> result) {
                tags.addAll(result);
                callback.onTagsFetched(tagsToSplittableArray(tags));
            }
        });
    }

    @Override
    public void searchTags(String searchVal, TagsSearchCallback callback) {
        tagsService.suggestTag(searchVal, 10, new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.ERROR.tagRetrieveError(), caught);
            }

            @SuppressWarnings("serial")
            @Override
            public void onSuccess(final String result) {
                AutoBean<IplantTagList> tagListBean =
                        AutoBeanCodex.decode(factory, IplantTagList.class, result);
                List<Tag> tagList = tagListBean.as().getTagList();

                callback.searchComplete(tagsToSplittableArray(tagList));
            }
        });
    }


    @Override
    public void attachTag(String tagId,
                          String tagValue,
                          String diskResourceId,
                          TagAttachCallback callback) {
        metadataService.attachTags(wrapInList(tagId), diskResourceId, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(appearance.tagAttachError(), caught);
            }

            @Override
            public void onSuccess(Void result) {
                Tag tag = factory.getTag().as();
                tag.setId(tagId);
                tag.setValue(tagValue);
                List<Tag> found = tags.stream()
                                      .filter(t -> t.getId().equals(tag.getId()))
                                      .collect(Collectors.toList());
                if (found.size() == 0) {
                    tags.add(tag);
                }
                callback.onAttach(tagsToSplittableArray(tags));
            }
        });
    }

    @Override
    public void detachTag(String tagId,
                          String tagValue,
                          String diskResourceId,
                          TagDetachCallback callback) {
        metadataService.detachTags(wrapInList(tagId), diskResourceId, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(appearance.tagDetachError(), caught);
            }

            @Override
            public void onSuccess(Void result) {
                Tag tag = factory.getTag().as();
                tag.setId(tagId);
                tag.setValue(tagValue);
                List<Tag> toRemove = tags.stream()
                                         .filter(t -> t.getId().equals(tag.getId()))
                                         .collect(Collectors.toList());
                tags.removeAll(toRemove);
                callback.onDetach(tagsToSplittableArray(tags));
            }
        });
    }

    @Override
    public void createTag(String tagValue, String diskResourceId, TagAttachCallback callback) {
        tagsService.createTag(tagValue.trim(), new AsyncCallback<Tag>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.ERROR.tagCreateError(), caught);
            }

            @Override
            public void onSuccess(Tag result) {
                attachTag(result.getId(), result.getValue(), diskResourceId, callback);
            }
        });
    }

    @Override
    public DetailsView getView() {
        return view;
    }

    @Override
    public void onTagSelection(String tagId, String tagValue) {
        Tag tag = factory.getTag().as();
        tag.setId(tagId);
        tag.setValue(tagValue);
        DiskResourceQueryTemplate queryTemplate = sabFactory.dataSearchFilter().as();
        queryTemplate.setTagQuery(Sets.newHashSet(tag));
        ensureHandlers().fireEvent(new SubmitDiskResourceQueryEvent(queryTemplate));
    }

    List<String> wrapInList(String tagId) {
        return Lists.newArrayList(tagId);
    }

    private Splittable[] tagsToSplittableArray(List<Tag> tagList) {
        Splittable[] tags = new Splittable[tagList.size()];
        for (int i = 0; i < tagList.size(); i++) {
            tags[i] = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(tagList.get(i)));
        }
        return tags;
    }

    HandlerManager createHandlerManager() {
        return new HandlerManager(this);
    }

    HandlerManager ensureHandlers() {
        return handlerManager == null ? handlerManager = createHandlerManager() : handlerManager;
    }
}
