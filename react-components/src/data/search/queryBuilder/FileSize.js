import build from "../../../util/DebugIDUtil";
import { getMessage } from "../../../util/I18NWrapper";
import ids from "../ids";
import { options } from "./Operators";
import ReduxTextField from "./ReduxTextField";
import SelectOperator from "./SelectOperator";

import { Field } from "redux-form";
import Grid from "@material-ui/core/Grid";
import MenuItem from "@material-ui/core/MenuItem";
import React, { Fragment } from "react";
import Select from "@material-ui/core/Select";

/**
 * A component which allows users to specify a file size range in QueryBuilder
 */
function FileSize(props) {
    let operators = [
        options.Between,
        options.BetweenNot
    ];

    let {
        parentId,
        helperProps: {
            messages
        }
    } = props;

    let sizesList = messages.fileSizes;
    let sizesListChildren = sizesList.map(function (item, index) {
        return <MenuItem key={index} value={item}>{item}</MenuItem>
    });

    return (
        <Fragment>
            <SelectOperator operators={operators}
                            parentId={parentId}/>
            <Field name='from.value'
                   type='number'
                   parse={value => value ? Number(value) : null}
                   min='0'
                   label={getMessage('fileSizeGreater')}
                   id={build(parentId, ids.fileSizeGreaterVal)}
                   component={ReduxTextField}/>
            <Field name='from.unit'
                   id={build(parentId, ids.fileSizeGreaterUnit)}
                   label=' '
                   component={renderDropDown}>
                {sizesListChildren}
            </Field>
            <Field name='to.value'
                   type='number'
                   parse={value => value ? Number(value) : null}
                   min='0'
                   label={getMessage('fileSizeLessThan')}
                   id={build(parentId, ids.fileSizeLessThanVal)}
                   component={ReduxTextField}/>
            <Field name='to.unit'
                   id={build(parentId, ids.fileSizeLessThanUnit)}
                   label=' '
                   component={renderDropDown}>
                {sizesListChildren}
            </Field>
        </Fragment>
    )
}

function renderDropDown(props) {
    let {
        input,
        children,
        id
    } = props;
    return (
        <Grid item>
            <Select value={input.value ? input.value : ''}
                    onChange={(event) => input.onChange(event.target.value)}
                    id={id}>
                {children}
            </Select>
        </Grid>
    )
}

export default FileSize;