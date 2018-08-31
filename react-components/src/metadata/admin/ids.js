import metadata_ids from "../ids";

const { BUTTONS } = metadata_ids;

export default {
    ...metadata_ids,

    METADATA_TEMPLATE_FORM: "metadataTemplateDialog",

    ATTR_GRID: "attrGrid",
    ATTR_NAME: "name",
    ATTR_DESCRIPTION: "description",
    ATTR_TYPE: "type",
    ATTR_REQUIRED: "required",
    BUTTONS: {
        ...BUTTONS,
        MOVE_DOWN: "moveDownBtn",
        MOVE_UP: "moveUpBtn",
    },
    CHECK_DELETED: "checkDeleted",
    ENUM_VALUES_GRID: "enumValuesGrid",
    ENUM_VALUE: "value",
    ENUM_VALUE_DEFAULT: "is_default",
    OLS_PARAMS_EDIT_DIALOG: "olsParamsEditDialog",
    ONTOLOGIES: "ontologies",
    ONTOLOGY_CHILDREN: "children",
    ONTOLOGY_ALL_CHILDREN: "allChildren",
    ONTOLOGY_ENTITY_TYPE: "entityType",
    TEMPLATE_NAME: "templateName",
    TEMPLATE_DESCRIPTION: "description",
    TITLE_ERR: "errTitle",
    TITLE_SUB: "subTitle",
};
