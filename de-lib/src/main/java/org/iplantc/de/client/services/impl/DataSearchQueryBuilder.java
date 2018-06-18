package org.iplantc.de.client.services.impl;

import org.iplantc.de.client.models.search.DateInterval;
import org.iplantc.de.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.de.client.models.search.FileSizeRange;
import org.iplantc.de.client.models.sharing.PermissionValue;
import org.iplantc.de.client.models.tags.Tag;
import org.iplantc.de.shared.DEProperties;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
public class DataSearchQueryBuilder {

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
    public static final String SIZE = "size";
    public static final String FROM = "from";
    public static final String TO = "to";
    //Modified within
    public static final String MODIFIED = "modified";
    //Created within
    public static final String CREATED = "created";


    private final DiskResourceQueryTemplate template;
    private final Splittable allList;
    private final Splittable anyList;
    private final Splittable noneList;

    Logger LOG = Logger.getLogger(DataSearchQueryBuilder.class.getName());

    public DataSearchQueryBuilder(DiskResourceQueryTemplate template) {
        this.template = template;
        allList = StringQuoter.createIndexed();
        anyList = StringQuoter.createIndexed();
        noneList = StringQuoter.createIndexed();
    }

    public Splittable getFullQuery() {
        ownedBy().sharedWith()
                 .modifiedWithin()
                 .createdWithin()
                 .file()
                 .notFile()
                 .fileSizeRange()
                 .metadata()
                 .negatedPathPrefix()
                 .tags()
                 .includeTrash();

        Splittable query = StringQuoter.createSplittable();

        Splittable operators = StringQuoter.createSplittable();
        assignKeyValue(operators, NONE, noneList);
        assignKeyValue(operators, ALL, allList);
        assignKeyValue(operators, ANY, anyList);

        assignKeyValue(query, QUERY, operators);

        return query;
    }

    /**
     * {"type": "owner", "args": {"owner": "ipcdev"}}
     */
    public DataSearchQueryBuilder ownedBy() {
        String queryContent = template.getOwnedBy();
        if (!Strings.isNullOrEmpty(queryContent)) {
            appendArrayItem(allList, createTypeClause(OWNER, OWNER, queryContent));
        }
        return this;
    }

    /**
     * {"type": "label", "args": {"label": "some_random_file_name", "exact": true}}
     */
    public DataSearchQueryBuilder file() {
        String content = template.getFileQuery();
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
    public DataSearchQueryBuilder notFile() {
        String content = template.getNegatedFileQuery();
        if (!Strings.isNullOrEmpty(content)) {
            Splittable args = StringQuoter.createSplittable();
            assignKeyValue(args, LABEL, content);
            appendArrayItem(noneList, createTypeClause(LABEL, args));
        }
        return this;
    }

    /**
     * {"type": "path", "args": {"prefix": "/home/iplant/shared"}}
     */
    public DataSearchQueryBuilder negatedPathPrefix() {
        String content = template.getNegatedPathPrefix();
        if (!Strings.isNullOrEmpty(content)) {
            Splittable args = StringQuoter.createSplittable();
            assignKeyValue(args, PREFIX, content);
            appendArrayItem(noneList, createTypeClause(PATH, args));
        }
        return this;
    }

    /**
     * {"type": "size", "args": {"from": "1KB", "to": "2KB"}}
     */
    public DataSearchQueryBuilder fileSizeRange() {
        FileSizeRange fileSizeRange = template.getFileSizeRange();
        if (fileSizeRange != null) {
            Double min = fileSizeRange.getMin();
            Double max = fileSizeRange.getMax();
            if (min != null || max != null) {
                Splittable args = StringQuoter.createSplittable();
                String from, to;
                if (min != null) {
                    from = min + fileSizeRange.getMinUnit().getLabel();
                    assignKeyValue(args, FROM, from);
                }
                if (max != null) {
                    to = max + fileSizeRange.getMaxUnit().getLabel();
                    assignKeyValue(args, TO, to);
                }
                appendArrayItem(allList, createTypeClause(SIZE, args));
            }
        }
        return this;
    }

    /**
     * {"type": "path", "args": {"prefix": "/iplant/home"}}
     */
    public DataSearchQueryBuilder includeTrash() {
        boolean include = template.isIncludeTrashItems();
        DEProperties props = DEProperties.getInstance();

        Splittable args = StringQuoter.createSplittable();

        if (include) {
            assignKeyValue(args, PREFIX, props.getBaseTrashPath());
            appendArrayItem(anyList, createTypeClause(PATH, args));
        } else {
            assignKeyValue(args, PREFIX, props.getIrodsHomePath());
            appendArrayItem(allList, createTypeClause(PATH, args));
        }
        return this;
    }

    /**
     * {"type": "permissions", "args": {"users": ["ipcdev","aramsey"], "permission": "write"}}
     */
    public DataSearchQueryBuilder sharedWith() {
        String sharedWith = template.getSharedWith();
        List<String> content = Lists.newArrayList(sharedWith);
        if (!Strings.isNullOrEmpty(sharedWith)) {
            Splittable users = listToSplittable(content);
            Splittable args = StringQuoter.createSplittable();
            assignKeyValue(args, SHARED_WITH, users);
            if (template.getPermission() == null) {
                template.setPermission(PermissionValue.read);
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
    public DataSearchQueryBuilder tags() {
        Set<Tag> content = template.getTagQuery();
        if (content != null && !content.isEmpty()) {

            Splittable tags = tagsToSplittable(content);
            Splittable args = StringQuoter.createSplittable();
            assignKeyValue(args, TAGS, tags);
            appendArrayItem(allList, createTypeClause(TAG, args));
        }
        return this;
    }

    private Splittable tagsToSplittable(Set<Tag> tags) {
        List<String> tagValues = tags.stream().map(Tag::getId).collect(Collectors.toList());

        return listToSplittable(tagValues);
    }

    /**
     * {"type": "metadata", "args": {"attribute": "some_random_attribute_value", "value": "some_random_value"}}
     */
    public DataSearchQueryBuilder metadata() {
        String attributeContent = template.getMetadataAttributeQuery();
        String valueContent = template.getMetadataValueQuery();
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
    public DataSearchQueryBuilder createdWithin() {
        DateInterval content = template.getCreatedWithin();

        return dateInterval(content, CREATED);
    }

    /**
     * {"type": "modified", "args": {"from": "some_date", "to": "some_other_date"}}
     */
    public DataSearchQueryBuilder modifiedWithin() {
        DateInterval content = template.getModifiedWithin();

        return dateInterval(content, MODIFIED);
    }

    /**
     * {"type": "typeClause", "args": {"from": "some_date", "to": "some_other_date"}}
     */
    DataSearchQueryBuilder dateInterval(DateInterval content, String typeClause) {
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

    private void assignKeyValue(Splittable keySplittable, String key, PermissionValue value) {
        StringQuoter.create(value.toString()).assign(keySplittable, key);
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
    public String toString(Splittable query) {
        return query.getPayload();
    }
}
