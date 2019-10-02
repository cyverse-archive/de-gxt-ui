/**
 * @author aramsey
 */
import React, { Fragment, useState } from "react";

import ids from "./ids";
import messages from "./messages";
import PERMISSION from "../models/Permission";
import styles from "./styles";

import {
    build,
    EmptyTable,
    EnhancedTableHead,
    formatMessage,
    getMessage,
    LoadingMask,
    SearchField,
    TablePaginationActions,
    withI18N,
} from "@cyverse-de/ui-lib";
import {
    Button,
    Checkbox,
    IconButton,
    Menu,
    MenuItem,
    Select,
    Table,
    TableBody,
    TableCell,
    TablePagination,
    TableRow,
    Toolbar,
    withStyles,
} from "@material-ui/core";
import {
    InfoOutlined,
    Menu as MenuIcon,
    Refresh,
    Share,
} from "@material-ui/icons";
import PropTypes from "prop-types";
import { injectIntl } from "react-intl";

const TOOL_FILTER_VALUES = {
    ALL: "ALL",
    MY_TOOLS: "MY_TOOLS",
    PUBLIC: "PUBLIC",
};

const PAGING_OPTIONS = [25, 50, 100, 200];

function ManageTools(props) {
    const {
        parentId,
        toolList,
        loading,
        presenter,
        searchTerm,
        order,
        orderBy,
        rowsPerPage,
        page,
        classes,
        intl,
    } = props;

    const [selectedTool, setSelectedTool] = useState(null);
    const [toolFilterValue, setToolFilterValue] = useState(
        TOOL_FILTER_VALUES.ALL
    );
    const [numToolsSelected, setNumToolsSelected] = useState(0);

    const refreshListing = (
        toolFilter,
        searchTerm,
        order,
        orderBy,
        rowsPerPage,
        page
    ) => {
        let isPublic;
        if (toolFilter === TOOL_FILTER_VALUES.MY_TOOLS) {
            isPublic = false;
        }
        if (toolFilter === TOOL_FILTER_VALUES.PUBLIC) {
            isPublic = true;
        }

        presenter.loadTools(
            isPublic,
            searchTerm,
            order,
            orderBy,
            rowsPerPage,
            page
        );
    };

    const onRefreshClicked = () => {
        refreshListing(
            toolFilterValue,
            searchTerm,
            order,
            orderBy,
            rowsPerPage,
            page
        );
    };

    const onToolFilterValueChanged = (event) => {
        let updatedFilter = event.target.value;
        setToolFilterValue(updatedFilter);
        refreshListing(updatedFilter, "", order, orderBy, rowsPerPage, 0);
    };

    const handleSearch = (searchTerm) => {
        setToolFilterValue("");
        refreshListing("", searchTerm, order, orderBy, rowsPerPage, 0);
    };

    const onRequestSort = (event, property) => {
        const isDesc = orderBy === property && order === "desc";
        refreshListing(
            toolFilterValue,
            searchTerm,
            isDesc ? "asc" : "desc",
            property,
            rowsPerPage,
            page
        );
    };

    const onChangePage = (event, page) => {
        refreshListing(
            toolFilterValue,
            searchTerm,
            order,
            orderBy,
            rowsPerPage,
            page
        );
    };

    const onChangeRowsPerPage = (event) => {
        refreshListing(
            toolFilterValue,
            searchTerm,
            order,
            orderBy,
            event.target.value,
            0
        );
    };

    const onToolSelection = (tool) => {
        if (selectedTool === tool) {
            tool = null;
        }
        setSelectedTool(tool);
        setNumToolsSelected(tool ? 1 : 0);
        presenter.onToolSelectionChanged(tool);
    };

    return (
        <Fragment>
            <StyledToolbar
                parentId={parentId}
                presenter={presenter}
                intl={intl}
                searchTerm={searchTerm}
                selectedTool={selectedTool}
                toolFilterValue={toolFilterValue}
                onRefreshClicked={onRefreshClicked}
                onToolFilterValueChanged={onToolFilterValueChanged}
                handleSearch={handleSearch}
            />
            <div className={classes.container}>
                <LoadingMask loading={loading}>
                    <Table size="small">
                        <ToolListing
                            parentId={parentId}
                            toolList={toolList}
                            presenter={presenter}
                            selectedTool={selectedTool}
                            onToolSelection={onToolSelection}
                        />
                        <EnhancedTableHead
                            numSelected={numToolsSelected}
                            rowCount={toolList ? toolList.tools.length : 0}
                            baseId={parentId}
                            columnData={TABLE_COLUMNS}
                            order={order}
                            orderBy={orderBy}
                            onRequestSort={onRequestSort}
                        />
                    </Table>
                </LoadingMask>
                <TablePagination
                    className={classes.tablePagination}
                    colSpan={6}
                    component="div"
                    count={toolList ? toolList.total : 0}
                    rowsPerPage={rowsPerPage}
                    page={page}
                    onChangePage={onChangePage}
                    onChangeRowsPerPage={onChangeRowsPerPage}
                    ActionsComponent={TablePaginationActions}
                    rowsPerPageOptions={PAGING_OPTIONS}
                />
            </div>
        </Fragment>
    );
}

const StyledToolbar = withStyles(styles)(ToolsToolbar);

function ToolsToolbar(props) {
    const {
        parentId,
        presenter,
        selectedTool,
        toolFilterValue,
        searchTerm,
        onRefreshClicked,
        onToolFilterValueChanged,
        handleSearch,
        intl,
        classes,
    } = props;

    const [toolMenuEl, setToolMenuEl] = useState(null);
    const [shareMenuEl, setShareMenuEl] = useState(null);

    const openToolMenu = (event) => {
        setToolMenuEl(event.currentTarget);
    };
    const closeToolMenu = () => {
        setToolMenuEl(null);
    };
    const openShareMenu = (event) => {
        setShareMenuEl(event.currentTarget);
    };
    const closeShareMenu = () => {
        setShareMenuEl(null);
    };
    const onNewToolSelected = () => {
        closeToolMenu();
        presenter.onNewToolSelected();
    };
    const onRequestToolSelected = () => {
        closeToolMenu();
        presenter.onRequestToolSelected();
    };
    const onEditToolSelected = (toolId) => {
        closeToolMenu();
        presenter.onEditToolSelected(toolId);
    };
    const onDeleteToolSelected = (toolId, toolName) => {
        closeToolMenu();
        presenter.onDeleteToolsSelected(toolId, toolName);
    };
    const onUseToolInApp = (tool) => {
        closeToolMenu();
        presenter.useToolInNewApp(tool);
    };
    const onShareToolsSelected = (tool) => {
        closeShareMenu();
        presenter.onShareToolsSelected(tool);
    };
    const onMakeToolPublicSelected = (tool) => {
        closeShareMenu();
        presenter.onRequestToMakeToolPublicSelected(tool);
    };

    const hasWritePermission =
        selectedTool && selectedTool.permission === PERMISSION.WRITE;
    const isOwner = selectedTool && selectedTool.permission === PERMISSION.OWN;
    const isEditable =
        selectedTool &&
        !selectedTool.is_public &&
        (isOwner || hasWritePermission);

    const toolFilterId = build(parentId, ids.MANAGE_TOOLS.TOOL_FILTER);

    return (
        <Toolbar
            id={build(parentId, ids.MANAGE_TOOLS.TOOLBAR)}
            classes={{ root: classes.toolbar }}
        >
            <Button
                onClick={openToolMenu}
                id={build(parentId, ids.MANAGE_TOOLS.TOOLS_MENU)}
                variant="contained"
            >
                <MenuIcon />
                {getMessage("tools")}
            </Button>
            <Menu
                anchorEl={toolMenuEl}
                open={Boolean(toolMenuEl)}
                onClose={closeToolMenu}
            >
                <MenuItem
                    onClick={onNewToolSelected}
                    id={build(parentId, ids.MANAGE_TOOLS.ADD_TOOL_MI)}
                >
                    {getMessage("addTool")}
                </MenuItem>
                <MenuItem
                    onClick={onRequestToolSelected}
                    id={build(parentId, ids.MANAGE_TOOLS.REQUEST_TOOL_MI)}
                >
                    {getMessage("requestToolMI")}
                </MenuItem>
                <MenuItem
                    disabled={!isEditable}
                    onClick={() => onEditToolSelected(selectedTool.id)}
                    id={build(parentId, ids.MANAGE_TOOLS.EDIT_TOOL_MI)}
                >
                    {getMessage("edit")}
                </MenuItem>
                <MenuItem
                    disabled={!isOwner}
                    onClick={() =>
                        onDeleteToolSelected(selectedTool.id, selectedTool.name)
                    }
                    id={build(parentId, ids.MANAGE_TOOLS.DELETE_TOOL_MI)}
                >
                    {getMessage("delete")}
                </MenuItem>
                <MenuItem
                    disabled={!selectedTool}
                    onClick={() => onUseToolInApp(selectedTool)}
                    id={build(parentId, ids.MANAGE_TOOLS.USE_IN_APP_MI)}
                >
                    {getMessage("useToolInApp")}
                </MenuItem>
            </Menu>
            <Button
                disabled={!isOwner}
                onClick={openShareMenu}
                id={build(parentId, ids.MANAGE_TOOLS.SHARE_MENU)}
                variant="contained"
            >
                <Share />
                {getMessage("share")}
            </Button>
            <Menu
                anchorEl={shareMenuEl}
                open={Boolean(shareMenuEl)}
                onClose={closeShareMenu}
            >
                <MenuItem
                    disabled={!isOwner}
                    onClick={() => onShareToolsSelected(selectedTool)}
                    id={build(parentId, ids.MANAGE_TOOLS.SHARE_MI)}
                >
                    {getMessage("shareWithCollaborators")}
                </MenuItem>
                <MenuItem
                    disabled={!isOwner}
                    onClick={() => onMakeToolPublicSelected(selectedTool)}
                    id={build(parentId, ids.MANAGE_TOOLS.MAKE_PUBLIC_MI)}
                >
                    {getMessage("makePublic")}
                </MenuItem>
            </Menu>
            <Button
                onClick={onRefreshClicked}
                id={build(parentId, ids.MANAGE_TOOLS.REFRESH)}
                variant="contained"
            >
                <Refresh />
                {getMessage("refresh")}
            </Button>
            <Select
                value={toolFilterValue}
                className={classes.toolTypeSelector}
                onChange={onToolFilterValueChanged}
                id={toolFilterId}
            >
                <MenuItem
                    value={TOOL_FILTER_VALUES.ALL}
                    id={build(toolFilterId, ids.MANAGE_TOOLS.ALL_MI)}
                >
                    {getMessage("allTools")}
                </MenuItem>
                <MenuItem
                    value={TOOL_FILTER_VALUES.MY_TOOLS}
                    id={build(toolFilterId, ids.MANAGE_TOOLS.MY_TOOLS_MI)}
                >
                    {getMessage("myTools")}
                </MenuItem>
                <MenuItem
                    value={TOOL_FILTER_VALUES.PUBLIC}
                    id={build(toolFilterId, ids.MANAGE_TOOLS.PUBLIC_MI)}
                >
                    {getMessage("publicTools")}
                </MenuItem>
            </Select>
            <SearchField
                handleSearch={handleSearch}
                value={searchTerm}
                id={build(parentId, ids.MANAGE_TOOLS.SEARCH)}
                placeholder={formatMessage(intl, "searchTools")}
            />
        </Toolbar>
    );
}

const TABLE_COLUMNS = [
    { name: "", align: "left", enableSorting: false, key: "info", id: "info" },
    { name: "Name", align: "left", enableSorting: true, id: "name" },
    {
        name: "Image Name",
        align: "left",
        enableSorting: false,
        id: "image_name",
    },
    { name: "Tag", align: "left", enableSorting: false, id: "tag" },
    { name: "Status", align: "left", enableSorting: false, id: "status" },
];

function ToolListing(props) {
    const {
        parentId,
        toolList,
        presenter,
        selectedTool,
        onToolSelection,
    } = props;

    const isSelected = (tool) => {
        return selectedTool ? selectedTool.id === tool.id : false;
    };

    return (
        <TableBody>
            {(!toolList || toolList.tools.length === 0) && (
                <EmptyTable
                    message={getMessage("noTools")}
                    numColumns={TABLE_COLUMNS.length}
                />
            )}
            {toolList &&
                toolList.tools.length > 0 &&
                toolList.tools.map((tool) => {
                    return (
                        <TableRow
                            tabIndex={-1}
                            hover
                            key={tool.id}
                            selected={isSelected(tool)}
                            onClick={() => onToolSelection(tool)}
                        >
                            <TableCell padding="none">
                                <IconButton
                                    onClick={() =>
                                        presenter.onShowToolInfo(tool.id)
                                    }
                                    id={build(
                                        parentId,
                                        tool.id,
                                        ids.MANAGE_TOOLS.TOOL_INFO_BTN
                                    )}
                                >
                                    <InfoOutlined />
                                </IconButton>
                            </TableCell>
                            <TableCell>{tool.name}</TableCell>
                            <TableCell>{tool.container.image.name}</TableCell>
                            <TableCell>{tool.container.image.tag}</TableCell>
                            <TableCell>
                                {tool.is_public
                                    ? getMessage("public")
                                    : tool.permission}
                            </TableCell>
                        </TableRow>
                    );
                })}
        </TableBody>
    );
}

ManageTools.propTypes = {
    presenter: PropTypes.shape({
        onShowToolInfo: PropTypes.func.isRequired,
        onNewToolSelected: PropTypes.func.isRequired,
        onRequestToolSelected: PropTypes.func.isRequired,
        onEditToolSelected: PropTypes.func.isRequired,
        onDeleteToolsSelected: PropTypes.func.isRequired,
        useToolInNewApp: PropTypes.func.isRequired,
        onShareToolsSelected: PropTypes.func.isRequired,
        onRequestToMakeToolPublicSelected: PropTypes.func.isRequired,
        loadTools: PropTypes.func.isRequired,
        onToolSelectionChanged: PropTypes.func.isRequired,
    }),
    loading: PropTypes.bool.isRequired,
    parentId: PropTypes.string.isRequired,
    toolList: PropTypes.object,
    searchTerm: PropTypes.string.isRequired,
    order: PropTypes.string.isRequired,
    orderBy: PropTypes.string.isRequired,
    rowsPerPage: PropTypes.number.isRequired,
    page: PropTypes.number.isRequired,
};

export default withI18N(injectIntl(withStyles(styles)(ManageTools)), messages);
