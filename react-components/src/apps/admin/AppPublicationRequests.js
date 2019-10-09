import React, { useState } from "react";
import PropTypes from "prop-types";
import {
    build,
    EnhancedTableHead,
    DEDialogHeader,
    DEHyperlink,
} from "@cyverse-de/ui-lib";
import {
    Dialog,
    TableCell,
    TableRow,
    Table,
    TableBody,
    DialogContent,
    IconButton,
    Tooltip,
} from "@material-ui/core";
import PublicIcon from "@material-ui/icons/Public";

/**
 *
 * @author sriram
 *
 * A component that displays a list of app publication requests.
 *
 */
const appColumnData = [
    { name: "App Name", enableSorting: true, key: "name" },
    {
        name: "Integrated By",
        enableSorting: true,
        key: "integrator_name",
    },
    {
        name: "Integrator Email",
        enableSorting: false,
        key: "Email",
    },
    {
        name: "Tools Used",
        enableSorting: false,
        key: "tools",
    },
    {
        name: "",
        enableSorting: false,
        key: "publish",
    },
];

const toolColumnData = [
    { name: "Name", align: "left", enableSorting: false, id: "name" },
    {
        name: "Image Name",
        align: "left",
        enableSorting: false,
        id: "image_name",
    },
    { name: "Tag", align: "left", enableSorting: false, id: "tag" },
];
function ToolsUsed(props) {
    const { tools, toolsDialogOpen, appName, onClose, parentId } = props;

    return (
        <Dialog open={toolsDialogOpen}>
            <DEDialogHeader
                heading={"Tools used by " + appName}
                onClose={onClose}
            />
            <DialogContent>
                <Table>
                    <TableBody>
                        {tools.map((tool) => {
                            return (
                                <TableRow hover>
                                    <TableCell>{tool.name}</TableCell>
                                    <TableCell>
                                        {tool.container.image.name}
                                    </TableCell>
                                    <TableCell>
                                        {tool.container.image.tag}
                                    </TableCell>
                                </TableRow>
                            );
                        })}
                    </TableBody>
                    <EnhancedTableHead
                        selectable={false}
                        numSelected={0}
                        rowCount={tools ? tools.length : 0}
                        baseId={parentId}
                        columnData={toolColumnData}
                    />
                </Table>
            </DialogContent>
        </Dialog>
    );
}
function AppPublicationRequests(props) {
    const { requests, parentId } = props;
    const [toolsDialogOpen, setToolsDialogOpen] = useState(false);
    const onRequestSort = () => {};
    const onPublishClicked = (app) => {
        console.log(app.name + "is published!");
    };
    return (
        <Table size="small">
            <TableBody>
                {requests &&
                    requests.length > 0 &&
                    requests.map((request) => {
                        return (
                            <React.Fragment>
                                <TableRow hover key={request.id}>
                                    <TableCell>{request.app.name}</TableCell>
                                    <TableCell>
                                        {request.app.integrator_name}
                                    </TableCell>
                                    <TableCell>
                                        {request.app.integrator_email}
                                    </TableCell>
                                    <TableCell>
                                        <DEHyperlink
                                            text="View Tools"
                                            onClick={() =>
                                                setToolsDialogOpen(true)
                                            }
                                        />
                                    </TableCell>
                                    <TableCell>
                                        <Tooltip
                                            title={
                                                "Publish " + request.app.name
                                            }
                                        >
                                            <IconButton
                                                onClick={onPublishClicked}
                                            >
                                                <PublicIcon fontSize="small" />
                                            </IconButton>
                                        </Tooltip>
                                    </TableCell>
                                </TableRow>
                                <ToolsUsed
                                    tools={request.app.tools}
                                    toolsDialogOpen={toolsDialogOpen}
                                    onClose={() => setToolsDialogOpen(false)}
                                    appName={request.app.name}
                                    parentId={build(parentId, "tools")}
                                />
                            </React.Fragment>
                        );
                    })}
            </TableBody>
            <EnhancedTableHead
                selectable={false}
                numSelected={0}
                rowCount={requests ? requests.length : 0}
                order="asc"
                orderBy="App Name"
                baseId={parentId}
                columnData={appColumnData}
                onRequestSort={onRequestSort}
            />
        </Table>
    );
}

AppPublicationRequests.propTypes = {
    loading: PropTypes.bool.isRequired,
};

export default AppPublicationRequests;
