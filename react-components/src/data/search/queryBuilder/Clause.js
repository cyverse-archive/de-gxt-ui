/**
 * @author aramsey
 *
 * A Clause is basically a component meant to mimic the building blocks of a search, which at its
 * most basic is {type: "someType", args: someObjectOrArrayOfObjects}
 *
 * The ClauseType is a Select field which allows you to choose which clause type you want to use.
 * For example the type could be "all" (a group) or the type could be "label".
 *
 * ClauseArgs changes based on the type selected. When a type is selected, ClauseArgs is responsible
 * for looking up and rendering the corresponding component which contains the "args" for that type.
 * For example, if the type is "label", then ClauseArgs will render a text field for the user to enter
 * a file name.  If the type is then switched to "modified", then ClauseArgs will render date pickers
 * for the user to create a date range.
 *
 * More specifically, ClauseType's Select field is populated by the "labels" array in the Clauses file.
 * Each value in the "labels" array is a key in the "componentMap" object in the Clauses file.
 * Each key maps to the corresponding component that needs to be rendered.  This is what allows the
 * Clause component to be dynamic and change based on a Select field's value.
 */

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

/**
 * This function is necessary to preset the default values for each clause.  Without this, formik's
 * validations don't seem to trigger properly and also cause an error about an uncontrolled component
 * switching to controlled.
 *
 * The default values are defined within each clause's component file, towards the top.
 */
function resetOnClauseChange(event, typeField, argsField, form) {
    let formValues = form.values;
    let currentClauseName = getIn(formValues, typeField);
    let nextClauseName = event.target.value;

    if (!isGroup(currentClauseName) || !isGroup(nextClauseName)) {
        form.setFieldValue(argsField, getDefaultArgVal(nextClauseName));
    }
}

/**
 * This function makes sure the very first clause in any search form is always a grouping of some sort.
 *
 * In the UI, we thought it would be easier and more intuitive if every search query began with a
 * grouping (any or all). Technically this is not needed, but it's confusing if the form is filled out
 * with two non-grouping clauses (for example, Label and Owner) with no explicit understanding of
 * whether the user intends both clauses to be true or if either can be true.
 */
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
