import React, { Fragment, useState } from "react";
import PropTypes from "prop-types";
import messages from "../messages";
import { injectIntl } from "react-intl";
import {
    build,
    DEDialogHeader,
    DEHyperlink,
    EmptyTable,
    EnhancedTableHead,
    formatMessage,
    getMessage,
    LoadingMask,
    withI18N,
} from "@cyverse-de/ui-lib";
import {
    Dialog,
    DialogContent,
    IconButton,
    Table,
    TableBody,
    TableCell,
    TableRow,
    Tooltip,
    makeStyles,
} from "@material-ui/core";
import PublicIcon from "@material-ui/icons/Public";

/**
 *
 * @author sriram
 *
 * A component that displays a list of app publication requests.
 *
 */

const useStyles = makeStyles((theme) => ({
    container: {
        height:
            "calc(100% - " +
            theme.mixins.toolbar["@media (min-width:600px)"].minHeight +
            "px)",
        overflow: "auto",
    },
}));

const appColumnData = [
    { name: "App Name", enableSorting: false, key: "name" },
    {
        name: "Integrated By",
        enableSorting: false,
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
    const { tools, appName, parentId, intl } = props;
    const [toolsDialogOpen, setToolsDialogOpen] = useState(false);

    return (
        <React.Fragment>
            <DEHyperlink
                text={formatMessage(intl, "viewTools")}
                onClick={() => setToolsDialogOpen(true)}
            />
            <Dialog open={toolsDialogOpen}>
                <DEDialogHeader
                    heading={formatMessage(intl, "toolsUsed", {
                        appName: appName,
                    })}
                    onClose={() => setToolsDialogOpen(false)}
                />
                <DialogContent>
                    <Table>
                        <TableBody>
                            {tools.map((tool) => {
                                return (
                                    <TableRow hover key={tool.id}>
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
        </React.Fragment>
    );
}
function AppPublicationRequests(props) {
    const { requests, presenter, parentId, loading, intl } = props;
    const classes = useStyles();
    const onPublishClicked = (app) => {
        presenter.publishApp(app.id, app.system_id);
    };
    return (
        <div className={classes.container}>
            <LoadingMask loading={loading}>
                <Table size="small" stickyHeader={true}>
                    <TableBody>
                        {(!requests || requests.length === 0) && (
                            <EmptyTable
                                message={getMessage("noRequests")}
                                numColumns={appColumnData.length}
                            />
                        )}
                        {requests &&
                            requests.length > 0 &&
                            requests.map((request) => {
                                return (
                                    <React.Fragment>
                                        <TableRow hover key={request.id}>
                                            <TableCell>
                                                {request.app.name}
                                            </TableCell>
                                            <TableCell>
                                                {request.app.integrator_name}
                                            </TableCell>
                                            <TableCell>
                                                {request.app.integrator_email}
                                            </TableCell>
                                            <TableCell>
                                                <ToolsUsed
                                                    intl={intl}
                                                    tools={request.app.tools}
                                                    appName={request.app.name}
                                                    parentId={build(
                                                        parentId,
                                                        "tools"
                                                    )}
                                                />
                                            </TableCell>
                                            <TableCell>
                                                <Tooltip
                                                    title={formatMessage(
                                                        intl,
                                                        "publishApp",
                                                        {
                                                            appName:
                                                                request.app
                                                                    .name,
                                                        }
                                                    )}
                                                >
                                                    <IconButton
                                                        onClick={() =>
                                                            onPublishClicked(
                                                                request.app
                                                            )
                                                        }
                                                    >
                                                        <PublicIcon fontSize="small" />
                                                    </IconButton>
                                                </Tooltip>
                                            </TableCell>
                                        </TableRow>
                                    </React.Fragment>
                                );
                            })}
                    </TableBody>
                    <EnhancedTableHead
                        selectable={false}
                        numSelected={0}
                        rowCount={requests ? requests.length : 0}
                        baseId={parentId}
                        columnData={appColumnData}
                    />
                </Table>
            </LoadingMask>
        </div>
    );
}

AppPublicationRequests.propTypes = {
    loading: PropTypes.bool.isRequired,
    requests: PropTypes.object,
    presenter: PropTypes.shape({
        publishApp: PropTypes.func.isRequired,
    }),
    parentId: PropTypes.string.isRequired,
};

export default withI18N(injectIntl(AppPublicationRequests), messages);
