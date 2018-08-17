import build from "../../../util/DebugIDUtil";
import Conditions from "./Conditions";
import DeleteBtn from "./DeleteBtn";
import Group from "./Group";
import ids from "../ids";

import { FieldArray, Fields, FormSection } from "redux-form";
import Grid from "@material-ui/core/Grid";
import MenuItem from "@material-ui/core/MenuItem";
import React, { Fragment } from "react";
import Select from "@material-ui/core/Select";

/**
 * A condition is a rule that can be constructed to help build a query in the QueryBuilder
 * A condition visually is any row within the QueryBuilder and consists of a dropdown menu
 * where the user can select which type of condition they're adding.  The list of conditions
 * are in the Conditions file.
 * Once a selection has been made, then the row populates with the rest of the values needed to
 * fully fill out that condition.
 */
function Condition(props) {
    const {
        field,
        root,
        onRemove,
        helperProps
    } = props;

    let selectOptions = root ? Conditions.labels.filter((item) => {
        return isGroup(item.value);
    }) : Conditions.labels;

    let fieldType = root ? 'type' : `${field}.type`;
    let fieldArgs = root ? 'args' : `${field}.args`;

    return (
        <Fields names={[fieldType, fieldArgs]}
                field={field}
                root={root}
                selectOptions={selectOptions}
                onRemove={onRemove}
                helperProps={helperProps}
                component={renderCondition}/>
    )
}

function renderCondition(props) {
    const {
        field,
        root,
        selectOptions,
        onRemove,
        helperProps,
        helperProps: {
            resetSection,
            parentId
        }
    } = props;

    let fieldType = root ? 'type' : `${field}.type`;
    let fieldArgs = root ? 'args' : `${field}.args`;
    let fieldTypeObj = getTargetProp(props, fieldType);

    if (!fieldTypeObj.input.value) {
        let defaultIndex = root ? 0 : 2;
        fieldTypeObj.input.onChange(Conditions.labels[defaultIndex].value);
    }
    let baseId = build(parentId, field);
    let selection = fieldTypeObj.input.value;

    let ConditionSelector = () => (
        <Select value={selection}
                id={build(baseId, ids.conditionSelector)}
                onChange={(event) => handleUpdateCondition(fieldTypeObj, fieldArgs, event, resetSection)}>
            {selectOptions && selectOptions.map((item, index) => {
                return <MenuItem key={index} value={item.value}>{item.label}</MenuItem>
            })}
        </Select>
    );

    if (isGroup(selection)) {
        return (
            <Fragment>
                <ConditionSelector/>
                <FieldArray name={fieldArgs}
                            root={root}
                            onRemove={onRemove}
                            parentId={parentId}
                            selectOptions={selectOptions}
                            helperProps={helperProps}
                            component={Group}/>
            </Fragment>
        )
    } else {
        return (
            <Grid container spacing={16}>
                {!root && <DeleteBtn onClick={onRemove}
                                     id={build(baseId, ids.deleteConditionBtn)}/>}
                <Grid item>
                    <ConditionSelector/>
                </Grid>
                <FormSection name={fieldArgs}
                             parentId={baseId}
                             helperProps={helperProps}
                             component={ConditionComponent(selection)}/>
            </Grid>
        )
    }
}

function handleUpdateCondition(fieldType, fieldArgs, event, resetSection) {
    let newCondition = event.target.value;
    let currentCondition = fieldType.input.value;
    let groupSwitch = isGroup(currentCondition) && isGroup(newCondition);

    if (!groupSwitch) {
        resetSection('dataQueryBuilder', true, true, fieldArgs);
    }
    fieldType.input.onChange(newCondition);
}

/**
 * target will be something like 'args[0].type' - props[args[0].type] doesn't work
 * This converts to props[args][0][type] and returns the obj there
 * @param propsObj
 * @param target
 * @returns {*}
 */
function getTargetProp(propsObj, target) {
    let obj = Object.assign('{}', propsObj);
    let targets = target.split(/[[\].]+/);

    for (let i = 0; i < targets.length; i++) {
        if (typeof obj !== "undefined") {
            obj = obj[targets[i]];
        }
    }
    return obj;
}

function isGroup(condition) {
    return condition ? Conditions.componentMap[condition].isGroup : false;
}

function ConditionComponent(condition) {
    if (condition === "") return 'undefined';
    return Conditions.componentMap[condition].component;
}

export default Condition;