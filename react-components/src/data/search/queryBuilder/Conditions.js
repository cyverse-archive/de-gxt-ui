import Date from "./Date";
import FileSize from "./FileSize";
import { getMessage } from "../../../util/I18NWrapper";
import Group from "./Group";
import ids from "../ids";
import Label from "./Label";
import Metadata from "./Metadata";
import Owner from "./Owner";
import Path from "./Path";
import Permissions from "./Permissions";
import Tags from "./Tags";

/**
 * All of the conditions available to the user for the QueryBuilder
 *
 * The labels array is used to populate the dropdown menu in Condition
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
    },
    owner: {
        isGroup: false,
        component: Owner,
    },
    path: {
        isGroup: false,
        component: Path,
    },
    created: {
        isGroup: false,
        component: Date,
    },
    modified: {
        isGroup: false,
        component: Date,
    },
    tag: {
        isGroup: false,
        component: Tags,
    },
    size: {
        isGroup: false,
        component: FileSize,
    },
    metadata: {
        isGroup: false,
        component: Metadata,
    },
    permissions: {
        isGroup: false,
        component: Permissions,
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
