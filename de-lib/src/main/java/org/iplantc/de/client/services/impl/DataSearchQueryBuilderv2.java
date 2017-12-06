package org.iplantc.de.client.services.impl;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.querydsl.QueryDSLTemplate;
import org.iplantc.de.client.util.SearchModelUtils;

import com.google.common.base.Strings;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import java.util.logging.Logger;

/**
 *
 * This class uses a builder pattern to construct a search query from a given query template.
 *
 * If a field in the given query template is null or empty, the corresponding search term will be omitted
 * from the final query.
 *
 * @author aramsey
 */
@SuppressWarnings("nls")
public class DataSearchQueryBuilderv2 {

    public static final String QUERY = "query";
    public static final String EXACT = "exact";
    public static final String PATH = "path";
    public static final String PREFIX = "prefix";
    public static final String LABEL = "label";
    public static final String OWNER = "owner";
    public static final String ALL = "all";
    public static final String TYPE = "type";
    public static final String ARGS = "args";

    private final QueryDSLTemplate template;
    private final UserInfo userinfo;
    private final Splittable allList;
    private final SearchModelUtils searchModelUtils;

    Splittable query = StringQuoter.createSplittable();
    Splittable all = addChild(query, QUERY);

    Logger LOG = Logger.getLogger(DataSearchQueryBuilderv2.class.getName());

    public DataSearchQueryBuilderv2(QueryDSLTemplate template, UserInfo userinfo) {
        this.template = template;
        this.userinfo = userinfo;
        this.searchModelUtils = SearchModelUtils.getInstance();
        allList = StringQuoter.createIndexed();
    }

    public String buildFullQuery() {
        ownedBy()
                .pathPrefix()
                .file();

        LOG.fine("search query==>" + toString());
        return toString();
    }

    /**
     * {"type": "owner", "args": {"owner": "ipcdev"}}
     */
    public DataSearchQueryBuilderv2 ownedBy() {
        String queryContent = template.getOwner();
        if (!Strings.isNullOrEmpty(queryContent)) {
            appendArrayItem(allList, createTypeClause(OWNER, OWNER, queryContent));
        }
        return this;
    }

    /**
     * {"type": "label", "args": {"label": "some_random_file_name", "exact": true}}
     */
    public DataSearchQueryBuilderv2 file() {
        String content = template.getLabel();
        if (!Strings.isNullOrEmpty(content)) {
            Splittable args = StringQuoter.createSplittable();
            assignKeyValue(args, LABEL, content);
            assignKeyValue(args, EXACT, template.isLabelExact());
            appendArrayItem(allList, createTypeClause(LABEL, args));
        }
        return this;
    }

    /**
     * {"type": "path", "args": {"prefix": "/iplant/home/userName"}}
     */
    public DataSearchQueryBuilderv2 pathPrefix() {
        String queryContent = template.getPathPrefix();
        if (!Strings.isNullOrEmpty(queryContent)) {
            appendArrayItem(allList, createTypeClause(PATH, PREFIX, queryContent));
        }
        return this;
    }

    /**
     * {"type": "typeVal", "args": {"argKey": "argVal"}}
     */
    public Splittable createTypeClause(String typeVal, String argKey, String argVal) {
        Splittable argKeySpl = StringQuoter.createSplittable();
        assignKeyValue(argKeySpl, argKey, argVal);
        return createTypeClause(typeVal, argKeySpl);
    }

    /**
     * {"type": "typeVal", "args": {"argKey": "argVal"}}
     */
    public Splittable createTypeClause(String typeVal, Splittable args) {
        Splittable type = StringQuoter.createSplittable();

        assignKeyValue(type, TYPE, typeVal);
        assignKeyValue(type, ARGS, args);

        return type;
    }

    /**
     * Takes a splittable, assigns the specified key and value
     * { "key" : "value" }
     * @param keySplittable
     * @param key
     * @param value
     */
    public void assignKeyValue(Splittable keySplittable, String key, String value) {
        StringQuoter.create(value).assign(keySplittable, key);
    }

    /**
     * Takes a splittable, assigns the specified key and value
     * { "key" : { "someKey" : "someValue" } }
     * @param keySplittable
     * @param key
     * @param value
     */
    public void assignKeyValue(Splittable keySplittable, String key, Splittable value) {
        value.assign(keySplittable, key);
    }

    /**
     * Takes a splittable, assigns the specified key and boolean value
     * { "key" : true }
     * @param keySplittable
     * @param key
     * @param value
     */
    public void assignKeyValue(Splittable keySplittable, String key, boolean value) {
        StringQuoter.create(value).assign(keySplittable, key);
    }

    private Splittable addChild(Splittable parent, String key) {
        Splittable child = StringQuoter.createSplittable();
        child.assign(parent, key);
        return child;
    }

    /**
     * Takes a splittable array and adds the specified item to it
     * @param array
     * @param item
     */
    private void appendArrayItem(Splittable array, Splittable item) {
        item.assign(array, array.size());
    }

    /**
     * @return the currently constructed query.
     */
    @Override
    public String toString() {
        // {"all":[{"type": "randomValue"}, {"type": "otherRandomValue"}]}

        allList.assign(all, ALL);

        return query.getPayload();
    }
}
