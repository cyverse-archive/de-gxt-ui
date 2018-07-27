import { options } from "./Operators";
import ReduxTextField from "./ReduxTextField";
import SelectOperator from "./SelectOperator";

import { Field } from "redux-form";
import React, { Component, Fragment } from "react";

class Path extends Component {
    render() {
        let operators = [
            options.Begins,
            options.BeginsNot,
        ];
        return (
            <Fragment>
                <SelectOperator operators={operators}/>
                <Field name='prefix'
                       operators={operators}
                       component={ReduxTextField}/>
            </Fragment>
        )
    }
}

export default Path;