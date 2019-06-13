import React, { Fragment } from "react";

import Clauses from "./Clauses";
import ids from "../../search/ids";

import { build, FormSelectField } from "@cyverse-de/ui-lib";
import { Field, FieldArray, getIn } from "formik";
import MenuItem from "@material-ui/core/MenuItem";

function ClauseSelector(props) {
    const {
        field: { name },
        form: { values },
        root,
        collaboratorsUtil,
        presenter,
        parentId,
    } = props;

    let typeFieldName = name ? `${name}.type` : "type";
    let argsFieldName = name ? `${name}.args` : "args";

    let selectedClauseName = getIn(values, typeFieldName);

    return (
        <Fragment>
            <ClauseType
                typeFieldName={typeFieldName}
                argsFieldName={argsFieldName}
                root={root}
                parentId={parentId}
            />
            <ClauseArgs
                selectedClauseName={selectedClauseName}
                argsFieldName={argsFieldName}
                root={root}
                collaboratorsUtil={collaboratorsUtil}
                presenter={presenter}
                parentId={parentId}
            />
        </Fragment>
    );
}

function ClauseType(props) {
    const { typeFieldName, argsFieldName, root, parentId } = props;

    return (
        <Field
            name={typeFieldName}
            render={({ field: { onChange, ...field }, ...props }) => {
                return (
                    <FormSelectField
                        {...props}
                        id={build(parentId, ids.conditionSelector)}
                        fullWidth={false}
                        field={field}
                        onChange={(event) => {
                            resetOnClauseChange(
                                event,
                                typeFieldName,
                                argsFieldName,
                                props.form
                            );
                            onChange(event);
                        }}
                    >
                        {ClauseOptions(root, parentId)}
                    </FormSelectField>
                );
            }}
        />
    );
}

function ClauseArgs(props) {
    const {
        selectedClauseName,
        argsFieldName,
        root,
        collaboratorsUtil,
        presenter,
        parentId,
    } = props;

    let clauseObj = Clauses.componentMap[selectedClauseName];
    let isGroupType = isGroup(selectedClauseName);

    let ClauseComponent = clauseObj && clauseObj.component;

    if (ClauseComponent) {
        let FieldType = isGroupType ? FieldArray : Field;

        return (
            <FieldType
                name={argsFieldName}
                render={(props) => (
                    <ClauseComponent
                        {...props}
                        parentId={parentId}
                        collaboratorsUtil={collaboratorsUtil}
                        presenter={presenter}
                        root={root}
                    />
                )}
            />
        );
    } else {
        return null;
    }
}

function resetOnClauseChange(event, typeField, argsField, form) {
    let formValues = form.values;
    let currentClauseName = getIn(formValues, typeField);
    let nextClauseName = event.target.value;

    if (!isGroup(currentClauseName) || !isGroup(nextClauseName)) {
        form.setFieldValue(argsField, getDefaultArgVal(nextClauseName));
    }
}

function ClauseOptions(root, parentId) {
    let clauses = root
        ? Clauses.labels.filter((item) => {
              return isGroup(item.value);
          })
        : Clauses.labels;

    return clauses.map((item, index) => {
        return (
            <MenuItem
                key={index}
                value={item.value}
                id={build(parentId, item.id)}
            >
                {item.label}
            </MenuItem>
        );
    });
}

function getDefaultArgVal(clauseName) {
    let clauseObj = getClauseObj(clauseName);
    return clauseObj.defaultArgs;
}

function isGroup(clauseName) {
    return clauseName ? getClauseObj(clauseName).isGroup : false;
}

function getClauseObj(clauseName) {
    return clauseName ? Clauses.componentMap[clauseName] : null;
}

export default ClauseSelector;
