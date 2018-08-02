import build from "../../../util/DebugIDUtil";
import ids from "../ids";
import { options } from "./Operators";
import SelectOperator from "./SelectOperator";
import SubjectSearchField from "../../../collaborators/SubjectSearchField";

import { Field } from 'redux-form';
import React, { Fragment } from 'react';
import Grid from "@material-ui/core/Grid";
import UserPanel from "./UserPanel";

/**
 * A component which allows users to specify an owner in QueryBuilder
 */

function Owner(props) {
    let operators = [
        options.Is,
        options.IsNot
    ];

    let {
        parentId,
        helperProps: {
            presenter
        }
    } = props;

    return (
        <Fragment>
            <SelectOperator operators={operators}
                            parentId={parentId}/>
            <Field name='owner'
                   operators={operators}
                   presenter={presenter}
                   parentId={parentId}
                   component={renderSubjectSearch}/>
        </Fragment>
    )
}

function renderSubjectSearch(props) {
    let {
        presenter,
        input,
        parentId
    } = props;

    let collaborator = input.value;

    return (
        <Grid item>
            <SubjectSearchField presenter={presenter}
                                parentId={parentId}
                                onSelect={(collaborator) => input.onChange(collaborator)}/>
            {collaborator && <UserPanel users={collaborator ? [collaborator] : null}
                                        id={build(parentId, ids.userList)}
                                        onDelete={() => input.onChange(null)}/>}
        </Grid>
    )
}

export default Owner;