import build from "../../../util/DebugIDUtil";
import ids from "../ids";
import { operatorMap } from "./Operators";

import { Fields } from "redux-form";
import Grid from "@material-ui/core/Grid";
import MenuItem from "@material-ui/core/MenuItem";
import React from "react";
import Select from "@material-ui/core/Select";

/**
 * A Select which prompts the user to choose between different Operators
 */
function SelectOperator(props) {
    const {
        operators,
        classes,
        parentId
    } = props;

    return (
        <Grid item>
            <Fields names={['opLabel', 'exact', 'negated', 'permission_recurse']}
                    classes={classes}
                    parentId={parentId}
                    operators={operators}
                    component={renderSelect}/>
        </Grid>
    )
}

function renderSelect(props) {
    const {
        opLabel,
        exact,
        negated,
        permission_recurse,
        operators,
        parentId
    } = props;

    if (opLabel.input.value === "") {
        opLabel.input.onChange(operators[0].value)
    }

    return (
        <Select value={opLabel.input.value}
                id={build(parentId, ids.selectOperator)}
                onChange={(event) => handleChange(event, opLabel, exact, negated, permission_recurse)}>
            {operators && operators.map((operator, index) => {
                return <MenuItem key={index} value={operator.value}>{operator.label}</MenuItem>
            })}
        </Select>
    )
}

function handleChange(event, opLabel, exact, negated, permission_recurse) {
    let operator = event.target.value;
    opLabel.input.onChange(operator);
    if (exact) exact.input.onChange(operatorMap[operator].exact);
    if (negated) negated.input.onChange(operatorMap[operator].negated);
    if (permission_recurse) permission_recurse.input.onChange(operatorMap[operator].permission_recurse);
}

export default SelectOperator;