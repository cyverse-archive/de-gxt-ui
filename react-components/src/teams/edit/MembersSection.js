/**
 * @author aramsey
 *
 * The Members section is part of the Edit Team dialog.  It allows users
 * to view, add, and remove subjects as members of a team while also simultaneously
 * setting privileges for those subjects for the team.
 *
 * The backend endpoint does not allow a user to modify their own privilege on a team, so their
 * row in the Members table will be read-only.
 */

import React from "react";

import { DeleteBtn } from "../../tools/Buttons";
import { SubjectSearchField } from "../../collaborators/collaborators";
import ids from "../ids";
import { MemberPermissions } from "./PermissionOptions";
import Privilege from "../../models/Privilege";
import styles from "../styles";
import SubjectNameCell from "../../collaborators/SubjectNameCell";

import {
    build,
    DETableRow,
    EnhancedTableHead,
    FormSelectField,
    getMessage,
} from "@cyverse-de/ui-lib";
import {
    makeStyles,
    Table,
    TableBody,
    TableCell,
    Typography,
} from "@material-ui/core";
import { Field, getIn } from "formik";
import PropTypes from "prop-types";
import SimpleExpansionPanel from "../../tools/SimpleExpansionPanel";
import { getUpdateRequest } from "./TeamHelperFunctions";

function getTableColumns(isAdmin) {
    let columns = [
        { name: "Name", align: "left", enableSorting: false, id: "name" },
        {
            name: "Institution / Description",
            align: "left",
            enableSorting: false,
            id: "description",
        },
    ];

    if (isAdmin) {
        columns.push({
            name: "Permissions",
            align: "left",
            enableSorting: false,
            id: "permission",
        });
        columns.push({
            name: "",
            align: "left",
            enableSorting: false,
            id: "remove",
        });
    }
    return columns;
}

const useStyles = makeStyles(styles);

function Members(props) {
    const {
        parentId,
        selfId,
        isAdmin,
        collaboratorsUtil,
        presenter,
        push,
        remove,
        form: { values },
        name,
        team,
    } = props;
    const classes = useStyles();
    let privileges = getIn(values, name);

    const isNewTeam = team === null;

    const memberExists = (subject) => {
        let exists = privileges.filter(
            (privilege) => privilege.subject.id === subject.id
        );
        return exists !== null && exists.length > 0;
    };

    const addMember = (subject) => {
        if (!memberExists(subject)) {
            let defaultPrivilege = {
                name: Privilege.READ.value,
                subject: { ...subject },
            };
            if (isNewTeam) {
                push(defaultPrivilege);
            } else {
                presenter.updatePrivilegesToTeam(
                    team.name,
                    getUpdateRequest([defaultPrivilege]),
                    [defaultPrivilege.subject.id],
                    () => push(defaultPrivilege)
                );
            }
        }
    };

    const removeMember = (privilege, index) => {
        if (isNewTeam) {
            remove(index);
        } else {
            let emptyPrivilege = { ...privilege };
            emptyPrivilege.name = "";
            presenter.removeMemberAndPrivilege(
                team.name,
                privilege.subject.id,
                getUpdateRequest([emptyPrivilege]),
                () => remove(index)
            );
        }
    };

    const modifyPrivilege = (event, onChange, privilege) => {
        if (isNewTeam) {
            onChange(event);
        } else {
            let updatedPrivilege = { ...privilege };
            updatedPrivilege.name = event.target.value;
            presenter.updatePrivilegesToTeam(
                team.name,
                getUpdateRequest([updatedPrivilege]),
                null,
                () => onChange(event)
            );
        }
    };

    let baseId = build(parentId, ids.EDIT_TEAM.MEMBERS);

    return (
        <SimpleExpansionPanel
            header={getMessage("members")}
            defaultExpanded={true}
            parentId={baseId}
        >
            <Typography variant="body2">
                {getMessage("memberHelpMessage")}
            </Typography>
            {isAdmin && (
                <div className={classes.subjectSearch}>
                    <SubjectSearchField
                        parentId={baseId}
                        collaboratorsUtil={collaboratorsUtil}
                        presenter={presenter}
                        onSelect={addMember}
                    />
                </div>
            )}
            <Table>
                <TableBody>
                    {privileges &&
                        privileges.length > 0 &&
                        privileges.map((privilege, index) => {
                            const isSelf =
                                privilege && privilege.subject.id === selfId;
                            const rowId = build(baseId, privilege.subject.id);
                            return (
                                <DETableRow tabIndex={-1} key={index}>
                                    <Field
                                        name={`${name}.${index}.subject`}
                                        render={({ field: { value } }) => {
                                            return (
                                                <TableCell
                                                    id={build(
                                                        rowId,
                                                        ids.EDIT_TEAM
                                                            .SUBJECT_NAME
                                                    )}
                                                >
                                                    <SubjectNameCell
                                                        collaboratorsUtil={
                                                            collaboratorsUtil
                                                        }
                                                        subject={value}
                                                    />
                                                </TableCell>
                                            );
                                        }}
                                    />
                                    <Field
                                        name={`${name}.${index}.subject.institution`}
                                        render={({ field: { value } }) => (
                                            <TableCell
                                                id={build(
                                                    rowId,
                                                    ids.EDIT_TEAM.DESCRIPTION
                                                )}
                                            >
                                                {value}
                                            </TableCell>
                                        )}
                                    />
                                    {isAdmin && (
                                        <Field
                                            name={`${name}.${index}.name`}
                                            render={({
                                                field: { onChange, ...field },
                                                ...props
                                            }) => {
                                                return (
                                                    <TableCell>
                                                        <FormSelectField
                                                            {...props}
                                                            fullWidth={false}
                                                            field={field}
                                                            id={build(
                                                                rowId,
                                                                ids.EDIT_TEAM
                                                                    .PRIVILEGE
                                                            )}
                                                            inputProps={{
                                                                readOnly:
                                                                    !isAdmin ||
                                                                    isSelf,
                                                            }}
                                                            onChange={(event) =>
                                                                modifyPrivilege(
                                                                    event,
                                                                    onChange,
                                                                    privilege
                                                                )
                                                            }
                                                        >
                                                            {MemberPermissions(
                                                                baseId
                                                            )}
                                                        </FormSelectField>
                                                    </TableCell>
                                                );
                                            }}
                                        />
                                    )}
                                    {isAdmin && !isSelf && (
                                        <TableCell>
                                            <DeleteBtn
                                                onClick={() =>
                                                    removeMember(
                                                        privilege,
                                                        index
                                                    )
                                                }
                                                parentId={rowId}
                                            />
                                        </TableCell>
                                    )}
                                </DETableRow>
                            );
                        })}
                </TableBody>
                <EnhancedTableHead
                    selectable={false}
                    rowCount={privileges.length}
                    baseId={baseId}
                    columnData={getTableColumns(isAdmin)}
                />
            </Table>
        </SimpleExpansionPanel>
    );
}

Members.propTypes = {
    parentId: PropTypes.string.isRequired,
    selfId: PropTypes.string.isRequired,
    isAdmin: PropTypes.bool.isRequired,
    team: PropTypes.object.isRequired,
    collaboratorsUtil: PropTypes.object.isRequired,
    presenter: PropTypes.object.isRequired,
};

export default Members;
