package org.iplantc.de.client.util;

import org.iplantc.de.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.de.client.models.search.SearchAutoBeanFactory;
import org.iplantc.de.client.models.tags.Tag;
import org.iplantc.de.shared.DEProperties;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import java.util.logging.Logger;

/**
 * A utility class for functions used in Data window searches
 */
public class SearchModelUtils {

    private final SearchAutoBeanFactory factory;
    private final DEProperties properties;
    private static SearchModelUtils INSTANCE;
    Logger LOG = Logger.getLogger(SearchModelUtils.class.getName());

    SearchModelUtils(){
        properties = DEProperties.getInstance();
        factory = GWT.create(SearchAutoBeanFactory.class);
    }

    public static SearchModelUtils getInstance(){
        if(INSTANCE == null){
            INSTANCE = new SearchModelUtils();
        }
        return INSTANCE;
    }

    public Splittable createDefaultFilter() {
        String query = "{\"type\":\"all\"}";
        return StringQuoter.split(query);
    }

    public Splittable createDefaultSimpleSearch(String searchTerm) {
        String query = "{\"type\":\"all\",\"args\":[{\"type\":\"path\",\"args\":{\"negated\":true,\"prefix\":\"" + properties.getCommunityDataPath() + "\"}},"
                        + "{\"type\":\"label\",\"args\":{\"exact\":false,\"negated\":false,\"label\":\"" + searchTerm + "\"}}]}";
        return StringQuoter.split(query);
    }

    public Splittable convertTemplateToSplittable(DiskResourceQueryTemplate template) {
        return AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(template));
    }

    public DiskResourceQueryTemplate convertSplittableToTemplate(Splittable splittable) {
        DiskResourceQueryTemplate template = factory.getQueryTemplate().as();
        Splittable label = splittable.get("label");
        String name = null;
        if (label != null) {
            name = label.asString();
        }
        template.setQuery(splittable);
        template.setName(name);
        return template;
    }

    public Splittable getTagQuery(Tag tag) {
        String tagString = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(tag)).getPayload();
        String query = "{\"type\":\"all\",\"args\":[{\"type\":\"tag\",\"args\":{\"negated\":false,\"tags\":[" + tagString + "]}}]}";
        return StringQuoter.split(query);
    }
}
