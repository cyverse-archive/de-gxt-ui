import Created from "./Created";
import { getMessage } from "../../../util/I18NWrapper";
import Group from "./Group";
import Label from "./Label";
import Owner from "./Owner";
import Path from "./Path";
import Tags from "./Tags";
import FileSize from "./FileSize";
import Metadata from "./Metadata";
import Permissions from "./Permissions";

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
        component: Created
    },
    modified: {
        isGroup: false,
        component: Created
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