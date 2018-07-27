import Conditions from "./Conditions";
import DeleteBtn from "./DeleteBtn";
import Group from "./Group";

import { FieldArray, Fields, FormSection } from 'redux-form';
import Input from "@material-ui/core/Input";
import MenuItem from '@material-ui/core/MenuItem';
import React, { Component, Fragment } from 'react';
import Select from '@material-ui/core/Select';

class Condition extends Component {

    render() {
        let {
            field,
            root,
            array,
            onRemove,
            classes,
            messages,
            resetSection,
            presenter
        } = this.props;

        let selectOptions = root ? Conditions.labels.filter((item) => {
            return isGroup(item.value);
        }) : Conditions.labels;

        let fieldType = root ? 'type' : `${field}.type`;
        let fieldArgs = root ? 'args' : `${field}.args`;

        return (
            <Fields names={[fieldType, fieldArgs]}
                    field={field}
                    array={array}
                    classes={classes}
                    root={root}
                    onRemove={onRemove}
                    messages={messages}
                    resetSection={resetSection}
                    selectOptions={selectOptions}
                    presenter={presenter}
                    component={renderCondition}/>
        )
    }
}

function renderCondition(props) {
    let {
        field,
        array,
        root,
        selectOptions,
        onRemove,
        classes,
        messages,
        resetSection,
        presenter
    } = props;

    let fieldTypeVal = root ? 'type' : `${field}.type`;

    let fieldType = root ? props['type'] : getTargetProp(props, fieldTypeVal);
    let fieldArgs = root ? 'args' : `${field}.args`;

    let selection = fieldType.input.value;

    return (
        <Fragment>
            <div className={isGroup(selection) ? classes.group : classes.condition}>
                <Select input={<Input className={classes.selectField}/>}
                        value={selection}
                        onChange={(event) => handleUpdateCondition(fieldType, fieldArgs, event, resetSection)}>
                    {selectOptions && selectOptions.map((item, index) => {
                        return <MenuItem key={index} value={item.value}>{item.label}</MenuItem>
                    })}
                </Select>
                {isGroup(selection)
                    ? <FieldArray name={fieldArgs}
                                  array={array}
                                  root={root}
                                  classes={classes}
                                  onRemove={onRemove}
                                  selectOptions={selectOptions}
                                  messages={messages}
                                  resetSection={resetSection}
                                  presenter={presenter}
                                  component={Group}/>
                    : <Fragment>
                        <FormSection name={fieldArgs}
                                     array={array}
                                     classes={classes}
                                     messages={messages}
                                     presenter={presenter}
                                     component={ConditionComponent(selection)}/>
                        {!root && <DeleteBtn onClick={onRemove}/>}
                    </Fragment>
                }
            </div>
        </Fragment>
    )
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

// target will be something like 'args[0].type' - props[args[0].type] doesn't work
// This converts to props[args][0][type]
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
    if (condition === "") return false;
    return Conditions.componentMap[condition].isGroup
}

function ConditionComponent(condition) {
    if (condition === "") return 'undefined';
    return Conditions.componentMap[condition].component;
}

export default Condition;