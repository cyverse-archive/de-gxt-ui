import { Date, DATE_DEFAULT } from "./Date";
import { FileSize, SIZE_DEFAULT } from "./FileSize";
import Group from "./Group";
import ids from "../ids";
import { Label, LABEL_DEFAULT } from "./Label";
import { Metadata, METADATA_DEFAULT } from "./Metadata";
import { Owner, OWNER_DEFAULT } from "./Owner";
import { Path, PATH_DEFAULT } from "./Path";
import { Permissions, PERMISSION_DEFAULT } from "./Permissions";
import { Tags, TAGS_DEFAULT } from "./Tags";

import { getMessage } from "@cyverse-de/ui-lib";

/**
 * All of the clauses available to the user for the QueryBuilder
 *
 * The labels array is used to populate the dropdown menu in Clause
 *
 * The componentMap object is used to map a dropdown menu selection to which
 * component should be rendered
 */

const labels = [
    {
        value: "all",
        label: getMessage("all"),
        id: ids.conditionAll,
    },
    {
        value: "any",
        label: getMessage("any"),
        id: ids.conditionAny,
    },
    {
        value: "label",
        label: getMessage("label"),
        id: ids.conditionLabel,
    },
    {
        value: "owner",
        label: getMessage("owner"),
        id: ids.conditionOwner,
    },
    {
        value: "path",
        label: getMessage("path"),
        id: ids.conditionPath,
    },
    {
        value: "created",
        label: getMessage("created"),
        id: ids.conditionCreated,
    },
    {
        value: "modified",
        label: getMessage("modified"),
        id: ids.conditionModified,
    },
    {
        value: "tag",
        label: getMessage("tags"),
        id: ids.conditionTags,
    },
    {
        value: "size",
        label: getMessage("fileSize"),
        id: ids.conditionFileSize,
    },
    {
        value: "metadata",
        label: getMessage("metadata"),
        id: ids.conditionMetadata,
    },
    {
        value: "permissions",
        label: getMessage("permissions"),
        id: ids.conditionPermissions,
    },
];

let componentMap = {
    label: {
        isGroup: false,
        component: Label,
        defaultArgs: LABEL_DEFAULT,
    },
    owner: {
        isGroup: false,
        component: Owner,
        defaultArgs: OWNER_DEFAULT,
    },
    path: {
        isGroup: false,
        component: Path,
        defaultArgs: PATH_DEFAULT,
    },
    created: {
        isGroup: false,
        component: Date,
        defaultArgs: DATE_DEFAULT,
    },
    modified: {
        isGroup: false,
        component: Date,
        defaultArgs: DATE_DEFAULT,
    },
    tag: {
        isGroup: false,
        component: Tags,
        defaultArgs: TAGS_DEFAULT,
    },
    size: {
        isGroup: false,
        component: FileSize,
        defaultArgs: SIZE_DEFAULT,
    },
    metadata: {
        isGroup: false,
        component: Metadata,
        defaultArgs: METADATA_DEFAULT,
    },
    permissions: {
        isGroup: false,
        component: Permissions,
        defaultArgs: PERMISSION_DEFAULT,
    },
    all: {
        isGroup: true,
        component: Group,
    },
    any: {
        isGroup: true,
        component: Group,
    },
};

export default {
    labels,
    componentMap,
};
