import build from "../../../util/DebugIDUtil";
import ids from "../ids";
import messages from "../messages";
import { options } from "./Operators";
import SelectOperator from "./SelectOperator";
import styles from "../styles";
import SubjectSearchField from "../../../collaborators/SubjectSearchField";
import UserPanel from "./UserPanel";
import withI18N, { getMessage } from "../../../util/I18NWrapper";

import { Field, FieldArray } from "redux-form";
import Grid from "@material-ui/core/Grid";
import MenuItem from "@material-ui/core/MenuItem";
import React, { Fragment } from "react";
import Select from "@material-ui/core/Select";
import { withStyles } from "@material-ui/core/styles";

/**
 * A component which allows users to specify a list of users which must have certain
 * permissions in QueryBuilder
 */
function Permissions(props) {
    let operators = [
        options.AreAtLeast,
        options.Are,
        options.AreNotAtLeast,
        options.AreNot,
    ];

    let permissions = [
        {
            value: 'read',
            label: getMessage('read')
        },
        {
            value: 'write',
            label: getMessage('write')
        },
        {
            value: 'own',
            label: getMessage('own')
        }
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
            <Field name='permission'
                   permissions={permissions}
                   id={build(parentId, ids.permissionList)}
                   component={renderSelect}/>
            <FieldArray name='users'
                        presenter={presenter}
                        parentId={parentId}
                        component={renderSubjectSearch}/>
        </Fragment>
    )
}

function renderSelect(props) {
    let {
        input,
        permissions,
        id
    } = props;

    if (input.value === "") {
        input.onChange(permissions[0].value)
    }

    return (
        <Grid item>
            <Select value={input.value}
                    id={id}
                    onChange={(event) => input.onChange(event.target.value)}>
                {permissions && permissions.map((permission, index) => {
                    return <MenuItem key={index} value={permission.value}>{permission.label}</MenuItem>
                })}
            </Select>
        </Grid>
    )
}

function renderSubjectSearch(props) {
    let {
        presenter,
        fields,
        parentId
    } = props;

    return (
        <Grid item>
            <SubjectSearchField presenter={presenter}
                                parentId={parentId}
                                onSelect={(collaborator) => fields.push(collaborator)}/>
            {fields.getAll() && <UserPanel users={fields.getAll()}
                                           id={build(parentId, ids.userList)}
                                           onDelete={fields.remove}/>}
        </Grid>
    )
}

export default withStyles(styles)(withI18N(Permissions, messages));