import build from "../../../util/DebugIDUtil";
import ids from "../ids";
import { options } from "./Operators";
import SelectOperator from "./SelectOperator";
import SubjectSearchField from "../../../collaborators/SubjectSearchField";
import UserPanel from "./UserPanel";

import { Field } from "redux-form";
import React, { Fragment } from "react";
import Grid from "@material-ui/core/Grid";

/**
 * A component which allows users to specify an owner in QueryBuilder
 */

function Owner(props) {
    const operators = [
        options.Is,
        options.IsNot
    ];

    const {
        parentId,
        helperProps: {
            presenter,
            collaboratorsUtil,
            classes
        }
    } = props;

    return (
        <Fragment>
            <SelectOperator operators={operators}
                            parentId={parentId}/>
            <Field name='owner'
                   operators={operators}
                   presenter={presenter}
                   collaboratorsUtil={collaboratorsUtil}
                   classes={classes}
                   parentId={parentId}
                   validate={[]}
                   component={renderSubjectSearch}/>
        </Fragment>
    )
}

function renderSubjectSearch(props) {
    const {
        presenter,
        collaboratorsUtil,
        input,
        parentId,
        classes
    } = props;

    let collaborator = input.value;

    return (
        <Grid item className={classes.autocompleteField}>
            <SubjectSearchField presenter={presenter}
                                collaboratorsUtil={collaboratorsUtil}
                                parentId={parentId}
                                onSelect={(collaborator) => input.onChange(collaborator)}/>
            {collaborator && <UserPanel users={collaborator ? [collaborator] : null}
                                        id={build(parentId, ids.userList)}
                                        collaboratorsUtil={collaboratorsUtil}
                                        onDelete={() => input.onChange(null)}/>}
        </Grid>
    )
}

export default Owner;