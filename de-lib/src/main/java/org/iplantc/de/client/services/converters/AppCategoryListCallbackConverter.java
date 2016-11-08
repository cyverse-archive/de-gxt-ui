package org.iplantc.de.client.services.converters;

import org.iplantc.de.client.models.apps.AppAutoBeanFactory;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.apps.AppCategoryList;
import org.iplantc.de.shared.DECallback;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import java.util.List;

/**
 * @author jstroot
 */
public class AppCategoryListCallbackConverter extends DECallbackConverter<String, List<AppCategory>> {

    public class AppCategoryListLoadException extends Exception {
        private static final long serialVersionUID = -9221968252788551910L;

        public AppCategoryListLoadException() { }

        public AppCategoryListLoadException(Throwable caught) {
            super("Failed to load App categories.", caught);
        }
    }

    private final AppAutoBeanFactory factory = GWT.create(AppAutoBeanFactory.class);

    public AppCategoryListCallbackConverter(DECallback<List<AppCategory>> callback) {
        super(callback);
    }

    @Override
    public void onFailure(Integer statusCode, Throwable caught) {
        super.onFailure(statusCode, new AppCategoryListLoadException(caught));
    }

    @Override
    protected List<AppCategory> convertFrom(String object) {
        AutoBean<AppCategoryList> bean = AutoBeanCodex.decode(factory, AppCategoryList.class, object);
        AppCategoryList as = bean.as();
        return as.getCategories();
    }

}
