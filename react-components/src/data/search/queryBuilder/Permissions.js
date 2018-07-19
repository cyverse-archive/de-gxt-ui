import DeleteBtn from "./DeleteBtn";
import messages from "../messages";
import { options } from "./Operators";
import SelectOperator from "./SelectOperator";
import styles from "../styles";
import SubjectSearchField from "../../../collaborators/SubjectSearchField";
import withI18N, { getMessage } from "../../../util/I18NWrapper";

import { Field, FieldArray } from "redux-form";
import Grid from "@material-ui/core/Grid";
import injectSheet from "react-jss";
import Input from "@material-ui/core/Input";
import MenuItem from "@material-ui/core/MenuItem";
import React, { Component, Fragment } from "react";
import Select from "@material-ui/core/Select";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";

class Permissions extends Component {
    render() {
        let operators = [
            options.Are,
            options.AreNot,
            options.AreAtLeast,
            options.AreNotAtLeast
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
            helperProps: {
                presenter,
                classes
            }
        } = this.props;

        return (
            <Fragment>
                <SelectOperator operators={operators}/>
                <Field name='permission'
                       permissions={permissions}
                       component={renderSelect}/>
                <FieldArray name='users'
                            presenter={presenter}
                            classes={classes}
                            component={renderSubjectSearch}/>
            </Fragment>
        )
    }
}

function renderSelect(props) {
    let {
        input,
        permissions
    } = props;

    if (input.value === "") {
        input.onChange(permissions[0].value)
    }

    return (
        <Grid item>
            <Select value={input.value}
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
        classes,
        fields
    } = props;

    return (
        <Grid item>
            <SubjectSearchField presenter={presenter}
                                onSelect={(collaborator) => fields.push(collaborator)}/>
            <CollaboratorTable classes={classes}
                               fields={fields}/>
        </Grid>
    )
}

function CollaboratorTable(props) {

    let columnData = [
        {name: 'name',          numeric: false, label: getMessage('name')},
        {name: 'description',   numeric: false, label: getMessage('collabDescription')},
        {name: 'delete',        numeric: false, label: null}
    ];

    let {
        classes,
        fields
    } = props;

    let data = fields.getAll();

    return (
        <div className={classes.collabTable}>
            <Table>
                <TableHead>
                    <TableRow hover>
                        {columnData.map(column => (
                            <TableCell className={classes.collabTableHead}
                                       key={column.name}
                                       numeric={column.numeric}>
                                {column.label}
                            </TableCell>
                        ))}
                    </TableRow>
                </TableHead>
                <TableBody>
                    {data && data.map((n, index) => {
                        return (
                            <TableRow hover key={n.id}>
                                <TableCell className={classes.collabCell}>{n.name}</TableCell>
                                <TableCell className={classes.collabCell}>{n.institution ? n.institution : n.description}</TableCell>
                                <TableCell className={classes.collabCell}><DeleteBtn onClick={() => fields.remove(index)}/></TableCell>
                            </TableRow>
                        );
                    })}
                </TableBody>
            </Table>
        </div>
    )
}

export default injectSheet(styles)(withI18N(Permissions, messages));