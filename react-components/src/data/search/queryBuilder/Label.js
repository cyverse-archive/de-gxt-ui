import { options } from "./Operators";
import ReduxTextField from "./ReduxTextField";
import SelectOperator from "./SelectOperator";

import { Field } from "redux-form";
import React, { Component, Fragment } from "react";

class Label extends Component {
    render() {
        let operators = [
            options.Is,
            options.IsNot,
            options.Contains,
            options.ContainsNot
        ];
        return (
            <Fragment>
                <SelectOperator operators={operators}/>
                <Field name='label'
                       operators={operators}
                       component={ReduxTextField}/>
            </Fragment>

        )
    }
}

export default Label;