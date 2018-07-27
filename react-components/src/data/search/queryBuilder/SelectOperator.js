import { operatorMap } from "./Operators";
import styles from "../styles";

import { Fields } from "redux-form";
import injectSheet from "react-jss";
import Input from "@material-ui/core/Input";
import MenuItem from "@material-ui/core/MenuItem";
import React, { Component } from 'react';
import Select from '@material-ui/core/Select';

/**
 * A Select which prompts the user to choose between different Operators
 */
class SelectOperator extends Component {
    render() {
        let {
            operators,
            classes
        } = this.props;

        return (
            <Fields names={['opLabel', 'exact', 'negated', 'permission_recurse']}
                    classes={classes}
                    operators={operators}
                    component={renderSelect}/>
        )
    }
}

function renderSelect(props) {
    let {
        opLabel,
        exact,
        negated,
        permission_recurse,
        operators,
        classes
    } = props;

    return (
        <Select input={<Input className={classes.selectField}/>}
                value={opLabel.input.value}
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

export default injectSheet(styles)(SelectOperator);