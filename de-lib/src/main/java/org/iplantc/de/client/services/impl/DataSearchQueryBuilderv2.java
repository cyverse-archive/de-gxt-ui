package org.iplantc.de.client.services.impl;

import org.iplantc.de.client.models.querydsl.QueryDSLTemplate;
import org.iplantc.de.client.models.search.DateInterval;
import org.iplantc.de.client.models.sharing.PermissionValue;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import java.util.List;
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

    // QUERY BUILDING BLOCKS
    public static final String ALL = "all";
    public static final String ANY = "any";
    public static final String NONE = "none";

    public static final String TYPE = "type";
    public static final String ARGS = "args";
    public static final String QUERY = "query";
    public static final String EXACT = "exact";


    // CLAUSES

    //Label
    public static final String LABEL = "label";
    //Path
    public static final String PATH = "path";
    public static final String PREFIX = "prefix";
    //Owner
    public static final String OWNER = "owner";
    //Permissions
    public static final String PERMISSIONS = "permissions";
    public static final String PERMISSION = "permission";
    public static final String PERMISSION_RECURSE = "permission_recurse";
    public static final String SHARED_WITH = "users";
    //Tag
    public static final String TAG = "tag";
    public static final String TAGS = "tags";
    //Metadata
    public static final String METADATA = "metadata";
    public static final String ATTRIBUTE = "attribute";
    public static final String ATTRIBUTE_EXACT = "attribute_exact";
    public static final String VALUE = "value";
    public static final String VALUE_EXACT = "value_exact";
    public static final String UNIT = "unit";
    public static final String UNIT_EXACT = "unit_exact";
    public static final String METADATA_TYPES = "metadata_types";
    //Date ranges
    public static final String FROM = "from";
    public static final String TO = "to";
    //Modified within
    public static final String MODIFIED = "modified";
    //Created within
    public static final String CREATED = "created";


    private final QueryDSLTemplate template;
    private final Splittable allList;
    private final Splittable anyList;
    private final Splittable noneList;

    Logger LOG = Logger.getLogger(DataSearchQueryBuilderv2.class.getName());

    public DataSearchQueryBuilderv2(QueryDSLTemplate template) {
        this.template = template;
        allList = StringQuoter.createIndexed();
        anyList = StringQuoter.createIndexed();
        noneList = StringQuoter.createIndexed();
    }

    public String buildFullQuery() {
        ownedBy()
                .pathPrefix()
                .sharedWith()
                .modifiedWithin()
                .createdWithin()
                .file()
                .notFile()
                .metadata()
                .tags();

        LOG.fine("search query==>" + toString());
        return toString();
    }

    /**
     * {"type": "owner", "args": {"owner": "ipcdev"}}
     */
    public DataSearchQueryBuilderv2 ownedBy() {
        String queryContent = template.getOwnedBy();
        if (!Strings.isNullOrEmpty(queryContent)) {
            appendArrayItem(allList, createTypeClause(OWNER, OWNER, queryContent));
        }
        return this;
    }

    /**
     * {"type": "label", "args": {"label": "some_random_file_name", "exact": true}}
     */
    public DataSearchQueryBuilderv2 file() {
        String content = template.getNameHas();
        if (!Strings.isNullOrEmpty(content)) {
            Splittable args = StringQuoter.createSplittable();
            assignKeyValue(args, LABEL, content);
            appendArrayItem(allList, createTypeClause(LABEL, args));
        }
        return this;
    }

    /**
     * {"type": "label", "args": {"label": "some_random_file_name", "exact": true}}
     */
    public DataSearchQueryBuilderv2 notFile() {
        String content = template.getNameHasNot();
        if (!Strings.isNullOrEmpty(content)) {
            Splittable args = StringQuoter.createSplittable();
            assignKeyValue(args, LABEL, content);
            appendArrayItem(noneList, createTypeClause(LABEL, args));
        }
        return this;
    }

    /**
     * {"type": "permissions", "args": {"users": ["ipcdev","aramsey"], "permission": "write"}}
     */
    public DataSearchQueryBuilderv2 sharedWith() {
        String sharedWith = template.getSharedWith();
        List<String> content = Lists.newArrayList(sharedWith);
        if (!Strings.isNullOrEmpty(sharedWith)) {
            Splittable users = listToSplittable(content);
            Splittable args = StringQuoter.createSplittable();
            assignKeyValue(args, SHARED_WITH, users);
            if (Strings.isNullOrEmpty(template.getPermission())) {
                template.setPermission(PermissionValue.read.toString());
                template.setPermissionRecurse(true);
            }
            assignKeyValue(args, PERMISSION, template.getPermission());
            assignKeyValue(args, PERMISSION_RECURSE, template.isPermissionRecurse());
            appendArrayItem(allList, createTypeClause(PERMISSIONS, args));
        }
        return this;
    }

    /**
     * {"type": "tag", "args": {"tags": ["all","my","tags"]}
     */
    public DataSearchQueryBuilderv2 tags() {
        List<String> content = template.getTags();
        if (content != null && !content.isEmpty()) {
            Splittable tags = listToSplittable(content);
            Splittable args = StringQuoter.createSplittable();
            assignKeyValue(args, TAGS, tags);
            appendArrayItem(allList, createTypeClause(TAG, args));
        }
        return this;
    }

    /**
     * {"type": "metadata", "args": {"attribute": "some_random_attribute_value", "value": "some_random_value"}}
     */
    public DataSearchQueryBuilderv2 metadata() {
        String attributeContent = template.getMetadataAttributeHas();
        String valueContent = template.getMetadataValueHas();
        boolean hasAttributeSearch = !Strings.isNullOrEmpty(attributeContent);
        boolean hasValueSearch = !Strings.isNullOrEmpty(valueContent);

        if (hasAttributeSearch || hasValueSearch) {
            Splittable args = StringQuoter.createSplittable();

            if (hasAttributeSearch) {
                assignKeyValue(args, ATTRIBUTE, attributeContent);
            }
            if (hasValueSearch) {
                assignKeyValue(args, VALUE, valueContent);
            }
            appendArrayItem(allList, createTypeClause(METADATA, args));
        }

        return this;
    }

    /**
     * {"type": "created", "args": {"from": "some_date", "to": "some_other_date"}}
     */
    public DataSearchQueryBuilderv2 createdWithin() {
        DateInterval content = template.getCreatedWithin();

        return dateInterval(content, CREATED);
    }

    /**
     * {"type": "modified", "args": {"from": "some_date", "to": "some_other_date"}}
     */
    public DataSearchQueryBuilderv2 modifiedWithin() {
        DateInterval content = template.getModifiedWithin();

        return dateInterval(content, MODIFIED);
    }

    /**
     * {"type": "typeClause", "args": {"from": "some_date", "to": "some_other_date"}}
     */
    DataSearchQueryBuilderv2 dateInterval(DateInterval content, String typeClause) {
        if (content != null && !Strings.isNullOrEmpty(content.getLabel())) {
            String from = String.valueOf(content.getFrom().getTime());
            String to = String.valueOf(content.getTo().getTime());

            Splittable args = StringQuoter.createSplittable();

            assignKeyValue(args, FROM, from);
            assignKeyValue(args, TO, to);

            appendArrayItem(allList, createTypeClause(typeClause, args));
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
    public Splittable createTypeClause(String typeVal, String argKey, Boolean argVal) {
        Splittable argKeySpl = StringQuoter.createSplittable();
        assignKeyValue(argKeySpl, argKey, argVal);
        return createTypeClause(typeVal, argKeySpl);
    }

    /**
     * {"type": "typeVal", "args": {"argKey": argVal}}
     */
    public Splittable createTypeClause(String typeVal, String argKey, Splittable argVal) {
        Splittable argKeySpl = StringQuoter.createSplittable();
        assignKeyValue(argKeySpl, argKey, argVal);
        return createTypeClause(typeVal, argKeySpl);
    }

    /**
     * {"type": "typeVal", "args": {"argKey": "argVal"}}
     */
    private Splittable createTypeClause(String typeVal, Splittable args) {
        Splittable type = StringQuoter.createSplittable();

        assignKeyValue(type, TYPE, typeVal);
        assignKeyValue(type, ARGS, args);

        return type;
    }

    private Splittable listToSplittable(List<String> list) {
        Splittable splittable = StringQuoter.createIndexed();
        list.forEach(value -> StringQuoter.create(value).assign(splittable, splittable.size()));
        return splittable;
    }

    /**
     * Takes a splittable, assigns the specified key and value
     * { "key" : "value" }
     * @param keySplittable
     * @param key
     * @param value
     */
    private void assignKeyValue(Splittable keySplittable, String key, String value) {
        StringQuoter.create(value).assign(keySplittable, key);
    }

    /**
     * Takes a splittable, assigns the specified key and value
     * { "key" : { "someKey" : "someValue" } }
     * @param keySplittable
     * @param key
     * @param value
     */
    private void assignKeyValue(Splittable keySplittable, String key, Splittable value) {
        value.assign(keySplittable, key);
    }

    /**
     * Takes a splittable, assigns the specified key and boolean value
     * { "key" : true }
     * @param keySplittable
     * @param key
     * @param value
     */
    private void assignKeyValue(Splittable keySplittable, String key, boolean value) {
        StringQuoter.create(value).assign(keySplittable, key);
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
        Splittable query = StringQuoter.createSplittable();

        Splittable operators = StringQuoter.createSplittable();
        assignKeyValue(operators, NONE, noneList);
        assignKeyValue(operators, ALL, allList);
        assignKeyValue(operators, ANY, anyList);

        assignKeyValue(query, QUERY, operators);

        return query.getPayload();
    }
}
