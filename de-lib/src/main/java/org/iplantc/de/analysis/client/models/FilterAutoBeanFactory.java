package org.iplantc.de.analysis.client.models;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface FilterAutoBeanFactory extends AutoBeanFactory {

    AutoBean<FilterBean> filterBean();

    AutoBean<FilterBeanList> filterBeanList();

}
