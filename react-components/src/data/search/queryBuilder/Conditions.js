import Date from "./Date";
import FileSize from "./FileSize";
import { getMessage } from "../../../util/I18NWrapper";
import Group from "./Group";
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
        value: 'all',
        label: getMessage('all'),
    },
    {
        value: 'any',
        label: getMessage('any'),
    },
    {
        value: 'label',
        label: getMessage('label'),
    },
    {
        value: 'owner',
        label: getMessage('ownedBy'),
    },
    {
        value: 'path',
        label: getMessage('path'),
    },
    {
        value: 'created',
        label: getMessage('created'),
    },
    {
        value: 'modified',
        label: getMessage('modified'),
    },
    {
        value: 'tag',
        label: getMessage('tags'),
    },
    {
        value: 'size',
        label: getMessage('fileSize'),
    },
    {
        value: 'metadata',
        label: getMessage('metadata'),
    },
    {
        value: 'permissions',
        label: getMessage('permissions'),
    },
];

let componentMap = {
    label: {
        isGroup: false,
        component: Label
    },
    owner: {
        isGroup: false,
        component: Owner
    },
    path: {
        isGroup: false,
        component: Path
    },
    created: {
        isGroup: false,
        component: Date
    },
    modified: {
        isGroup: false,
        component: Date
    },
    tag: {
        isGroup: false,
        component: Tags
    },
    size: {
        isGroup: false,
        component: FileSize
    },
    metadata: {
        isGroup: false,
        component: Metadata
    },
    permissions: {
        isGroup: false,
        component: Permissions
    },
    all: {
        isGroup: true,
        component: Group
    },
    any: {
        isGroup: true,
        component: Group
    }
};

export default {
    labels,
    componentMap
};