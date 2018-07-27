import { options } from "./Operators";
import ReduxTextField from "./ReduxTextField";
import SelectOperator from "./SelectOperator";

import { Field } from "redux-form";
import React, { Component, Fragment } from "react";


class Created extends Component {
    render() {
        let operators = [
            options.Between,
            options.BetweenNot,
        ];
        return (
            <Fragment>
                <SelectOperator operators={operators}/>
                <Field name='from'
                       type='date'
                       component={ReduxTextField}/>
                <Field name='to'
                       type='date'
                       component={ReduxTextField}/>
            </Fragment>
        )
    }
}

export default Created;