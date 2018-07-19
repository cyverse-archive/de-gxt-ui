import ids from "../ids";
import { options } from "./Operators";
import SearchFormTagPanel from "../SearchFormTagPanel";
import SelectOperator from "./SelectOperator";

import { Field } from "redux-form";
import Grid from "@material-ui/core/Grid";
import React, { Component, Fragment } from "react";

class Tags extends Component {
    render() {
        let operators = [
            options.Are,
            options.AreNot
        ];

        let {
            helperProps: {
                array,
                presenter
            }
        } = this.props;

        return (
            <Fragment>
                <SelectOperator operators={operators}/>
                <Field name='tags'
                       parentId={ids.form}
                       placeholder={''}
                       array={array}
                       presenter={presenter}
                       component={renderTagSearchField}/>
            </Fragment>

        )
    }
}

function renderTagSearchField(props) {
    let {
        input,
        array,
        parentId,
        placeholder,
        presenter
    } = props;
    return (
        <Grid item>
            <SearchFormTagPanel parentId={parentId}
                                placeholder={placeholder}
                                presenter={presenter}
                                array={array}
                                tagQuery={input}/>
        </Grid>
    )
}

export default Tags;