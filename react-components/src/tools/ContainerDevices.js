import React from "react";

import { StyledAddBtn, DeleteBtn } from "./Buttons";
import ids from "./ids";
import SimpleExpansionPanel from "./SimpleExpansionPanel";
import { nonEmptyField } from "./Validations";

import {
    build,
    EmptyTable,
    EnhancedTableHead,
    FormTextField,
    getFormError,
    getMessage,
} from "@cyverse-de/ui-lib";
import { Field, getIn } from "formik";
import {
    Table,
    TableBody,
    TableCell,
    TableRow,
    Toolbar,
    Typography,
} from "@material-ui/core";
import PropTypes from "prop-types";

const TABLE_COLUMNS = [
    { name: "Host Path", numeric: false, enableSorting: false },
    { name: "Container Path", numeric: false, enableSorting: false },
    { name: "", numeric: false, enableSorting: false, key: "remove" },
];

function ContainerDevices(props) {
    const {
        name,
        parentId,
        push,
        remove,
        form: { values, errors, touched },
    } = props;

    let devices = getIn(values, name);
    let hasErrors = !!getFormError(name, touched, errors);

    return (
        <SimpleExpansionPanel
            header={getMessage("containerDevices")}
            parentId={parentId}
            defaultExpanded={!!devices && devices.length > 0}
            hasErrors={hasErrors}
        >
            <Toolbar>
                <StyledAddBtn
                    onClick={() => push({ host_path: "", container_path: "" })}
                    parentId={parentId}
                />
                <Typography variant="subtitle1">
                    {getMessage("device")}
                </Typography>
            </Toolbar>
            <Table>
                <TableBody>
                    {(!devices || devices.length === 0) && (
                        <EmptyTable
                            message={getMessage("noContainerDevices")}
                            numColumns={TABLE_COLUMNS.length}
                        />
                    )}
                    {devices &&
                        devices.length > 0 &&
                        devices.map((port, index) => (
                            <TableRow tabIndex={-1} key={index}>
                                <TableCell>
                                    <Field
                                        name={`${name}.${index}.host_path`}
                                        id={build(
                                            parentId,
                                            index,
                                            ids.EDIT_TOOL_DLG.HOST_PATH
                                        )}
                                        fullWidth={false}
                                        label={getMessage("hostPath")}
                                        required
                                        validate={nonEmptyField}
                                        component={FormTextField}
                                    />
                                </TableCell>
                                <TableCell>
                                    <Field
                                        name={`${name}.${index}.container_path`}
                                        id={build(
                                            parentId,
                                            index,
                                            ids.EDIT_TOOL_DLG.CONTAINER_PATH
                                        )}
                                        fullWidth={false}
                                        label={getMessage("containerPath")}
                                        required
                                        validate={nonEmptyField}
                                        component={FormTextField}
                                    />
                                </TableCell>
                                <TableCell>
                                    <DeleteBtn
                                        parentId={build(parentId, index)}
                                        onClick={() => remove(index)}
                                    />
                                </TableCell>
                            </TableRow>
                        ))}
                </TableBody>
                <EnhancedTableHead
                    selectable={false}
                    rowCount={devices ? devices.length : 0}
                    baseId={parentId}
                    ids={ids.PORTS_TABLE}
                    columnData={TABLE_COLUMNS}
                />
            </Table>
        </SimpleExpansionPanel>
    );
}

ContainerDevices.propTypes = {
    name: PropTypes.string.isRequired,
    parentId: PropTypes.string.isRequired,
    push: PropTypes.func.isRequired,
    remove: PropTypes.func.isRequired,
    form: PropTypes.shape({
        values: PropTypes.object.isRequired,
    }),
};

export default ContainerDevices;
