import React from "react";

import { DeleteBtn, StyledAddBtn } from "./Buttons";
import ids from "./ids";
import SimpleExpansionPanel from "./SimpleExpansionPanel";
import { nonEmptyMinValue } from "./Validations";

import {
    build,
    EmptyTable,
    EnhancedTableHead,
    FormNumberField,
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
    { name: "Container Port", numeric: false, enableSorting: false },
    {
        name: "Remove?",
        numeric: false,
        enableSorting: false,
        key: "remove",
    },
];

function ContainerPorts(props) {
    const {
        name,
        parentId,
        push,
        remove,
        form: { values, errors },
    } = props;

    let ports = getIn(values, name);
    let hasErrors = !!getIn(errors, name);

    return (
        <SimpleExpansionPanel
            header={getMessage("containerPorts")}
            parentId={parentId}
            defaultExpanded={!!ports && ports.length > 0}
            hasErrors={hasErrors}
        >
            <Toolbar>
                <StyledAddBtn
                    onClick={() => push({ container_port: "" })}
                    parentId={parentId}
                />
                <Typography variant="subtitle1">
                    {getMessage("port")}
                </Typography>
            </Toolbar>
            <Table>
                <TableBody>
                    {(!ports || ports.length === 0) && (
                        <EmptyTable
                            message={getMessage("noContainerPorts")}
                            numColumns={TABLE_COLUMNS.length}
                        />
                    )}
                    {ports &&
                        ports.length > 0 &&
                        ports.map((port, index) => (
                            <TableRow tabIndex={-1} key={index}>
                                <TableCell>
                                    <Field
                                        name={`${name}.${index}.container_port`}
                                        id={build(
                                            parentId,
                                            index,
                                            ids.EDIT_TOOL_DLG.CONTAINER_PORT
                                        )}
                                        label={getMessage("portNumber")}
                                        required
                                        validate={nonEmptyMinValue}
                                        component={FormNumberField}
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
                    rowCount={ports ? ports.length : 0}
                    baseId={parentId}
                    ids={ids.PORTS_TABLE}
                    columnData={TABLE_COLUMNS}
                />
            </Table>
        </SimpleExpansionPanel>
    );
}

ContainerPorts.propTypes = {
    name: PropTypes.string.isRequired,
    parentId: PropTypes.string.isRequired,
    push: PropTypes.func.isRequired,
    remove: PropTypes.func.isRequired,
    form: PropTypes.shape({
        values: PropTypes.object.isRequired,
    }),
};

export default ContainerPorts;
