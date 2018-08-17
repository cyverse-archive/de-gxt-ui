import build from "../../../util/DebugIDUtil";
import { getMessage } from "../../../util/I18NWrapper";
import ids from "../ids";
import { options } from "./Operators";
import ReduxTextField from "./ReduxTextField";
import SelectOperator from "./SelectOperator";

import { Field } from "redux-form";
import React, { Fragment } from "react";

/**
 * A component which allows users to specify metadata attribute and values in QueryBuilder
 */
function Metadata(props) {
    const operators = [
        options.Is,
        options.IsNot
    ];

    const {parentId} = props;

    return (
        <Fragment>
            <SelectOperator operators={operators}
                            parentId={parentId}/>
            <Field name='attribute'
                   helperText={getMessage('attribute')}
                   operators={operators}
                   id={build(parentId, ids.metadataAttr)}
                   validate={[]}
                   component={ReduxTextField}/>
            <Field name='value'
                   helperText={getMessage('value')}
                   operators={operators}
                   id={build(parentId, ids.metadataVal)}
                   validate={[]}
                   component={ReduxTextField}/>
        </Fragment>
    )
}

export default Metadata;