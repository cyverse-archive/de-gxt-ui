/**
 * @author aramsey
 */
import React, { Fragment, useEffect, useState } from "react";

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
import { InfoOutlined, Refresh } from "@material-ui/icons";
import PropTypes from "prop-types";
import { injectIntl } from "react-intl";

const TOOL_FILTER_VALUES = {
    ALL: "ALL",
    MY_TOOLS: "MY_TOOLS",
    PUBLIC: "PUBLIC",
};

const PAGING_OPTIONS = [100, 500, 1000];

function ManageTools(props) {
    const { parentId, toolList, loading, presenter, intl } = props;

    const [state, setHookState] = useState({
        selectedTool: null,
        toolFilterValue: TOOL_FILTER_VALUES.ALL,
        toolMenuEl: null,
        shareMenuEl: null,
        numToolsSelected: 0,
        order: "asc",
        orderBy: "name",
        page: 0,
        rowsPerPage: PAGING_OPTIONS[0],
        searchTerm: "",
    });

    const setState = (stateObj) => {
        setHookState({ ...state, ...stateObj });
    };

    useEffect(() => {
        refreshToolList();
    }, [
        state.toolFilterValue,
        state.order,
        state.orderBy,
        state.page,
        state.rowsPerPage,
        state.searchTerm,
    ]);

    useEffect(() => {
        presenter.onToolSelectionChanged(state.selectedTool);
    }, [state.selectedTool]);

    const refreshToolList = () => {
        let isPublic;
        if (state.toolFilterValue === TOOL_FILTER_VALUES.MY_TOOLS) {
            isPublic = false;
        }
        if (state.toolFilterValue === TOOL_FILTER_VALUES.PUBLIC) {
            isPublic = true;
        }

        presenter.loadTools(
            isPublic,
            state.searchTerm,
            state.order,
            state.orderBy,
            state.rowsPerPage,
            state.rowsPerPage * state.page
        );
    };

    const baseId = build(parentId, ids.MANAGE_TOOLS.VIEW);

    return (
        <Fragment>
            <StyledToolbar
                parentId={baseId}
                presenter={presenter}
                intl={intl}
                setState={setState}
                refreshToolList={refreshToolList}
                {...state}
            />
            <StyledToolListing
                loading={loading}
                parentId={baseId}
                toolList={toolList}
                presenter={presenter}
                setState={setState}
                {...state}
            />
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
        toolMenuEl,
        shareMenuEl,
        searchTerm,
        setState,
        refreshToolList,
        intl,
        classes,
    } = props;

    const hasWritePermission = selectedTool
        ? selectedTool.permission === PERMISSION.WRITE
        : false;
    const isOwner = selectedTool
        ? selectedTool.permission === PERMISSION.OWN
        : false;
    const isEditable = selectedTool
        ? !selectedTool.is_public && (isOwner || hasWritePermission)
        : false;

    const toolFilterId = build(parentId, ids.MANAGE_TOOLS.TOOL_FILTER);

    return (
        <Toolbar
            id={build(parentId, ids.MANAGE_TOOLS.TOOLBAR)}
            classes={{ root: classes.toolbar }}
        >
            <Button
                onClick={(event) =>
                    setState({ toolMenuEl: event.currentTarget })
                }
                id={build(parentId, ids.MANAGE_TOOLS.TOOLS_MENU)}
            >
                {getMessage("tools")}
            </Button>
            <Menu
                anchorEl={toolMenuEl}
                open={Boolean(toolMenuEl)}
                onClose={() => setState({ toolMenuEl: null })}
            >
                <MenuItem
                    onClick={() => {
                        setState({ toolMenuEl: null });
                        presenter.onNewToolSelected();
                    }}
                    id={build(parentId, ids.MANAGE_TOOLS.ADD_TOOL_MI)}
                >
                    {getMessage("addTool")}
                </MenuItem>
                <MenuItem
                    onClick={() => {
                        setState({ toolMenuEl: null });
                        presenter.onRequestToolSelected();
                    }}
                    id={build(parentId, ids.MANAGE_TOOLS.REQUEST_TOOL_MI)}
                >
                    {getMessage("requestToolMI")}
                </MenuItem>
                <MenuItem
                    disabled={!isEditable}
                    onClick={() => {
                        setState({ toolMenuEl: null });
                        presenter.onEditToolSelected(selectedTool.id);
                    }}
                    id={build(parentId, ids.MANAGE_TOOLS.EDIT_TOOL_MI)}
                >
                    {getMessage("edit")}
                </MenuItem>
                <MenuItem
                    disabled={!isOwner}
                    onClick={() => {
                        setState({ toolMenuEl: null });
                        presenter.onDeleteToolsSelected(
                            selectedTool.id,
                            selectedTool.name
                        );
                    }}
                    id={build(parentId, ids.MANAGE_TOOLS.DELETE_TOOL_MI)}
                >
                    {getMessage("delete")}
                </MenuItem>
                <MenuItem
                    disabled={selectedTool === null}
                    onClick={() => {
                        setState({ toolMenuEl: null });
                        presenter.useToolInNewApp(selectedTool);
                    }}
                    id={build(parentId, ids.MANAGE_TOOLS.USE_IN_APP_MI)}
                >
                    {getMessage("useToolInApp")}
                </MenuItem>
            </Menu>
            <Button
                disabled={!isOwner}
                onClick={(event) =>
                    setState({ shareMenuEl: event.currentTarget })
                }
                id={build(parentId, ids.MANAGE_TOOLS.SHARE_MENU)}
            >
                {getMessage("share")}
            </Button>
            <Menu
                anchorEl={shareMenuEl}
                open={Boolean(shareMenuEl)}
                onClose={() => setState({ shareMenuEl: null })}
            >
                <MenuItem
                    disabled={!isOwner}
                    onClick={() => {
                        setState({ shareMenuEl: null });
                        presenter.onShareToolsSelected(selectedTool);
                    }}
                    id={build(parentId, ids.MANAGE_TOOLS.SHARE_MI)}
                >
                    {getMessage("shareWithCollaborators")}
                </MenuItem>
                <MenuItem
                    disabled={!isOwner}
                    onClick={() => {
                        setState({ shareMenuEl: null });
                        presenter.onRequestToMakeToolPublicSelected(
                            selectedTool
                        );
                    }}
                    id={build(parentId, ids.MANAGE_TOOLS.MAKE_PUBLIC_MI)}
                >
                    {getMessage("makePublic")}
                </MenuItem>
            </Menu>
            <IconButton
                onClick={refreshToolList}
                id={build(parentId, ids.MANAGE_TOOLS.REFRESH)}
            >
                <Refresh />
            </IconButton>
            <Select
                value={toolFilterValue}
                className={classes.toolTypeSelector}
                onChange={(event) => {
                    let updatedFilter = event.target.value;
                    setState({
                        toolFilterValue: updatedFilter,
                        searchTerm: "",
                    });
                }}
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
                handleSearch={(searchTerm) =>
                    setState({
                        toolFilterValue: "",
                        searchTerm: searchTerm,
                        page: 0,
                    })
                }
                value={searchTerm}
                id={build(parentId, ids.MANAGE_TOOLS.SEARCH)}
                placeholder={formatMessage(intl, "searchTools")}
            />
        </Toolbar>
    );
}

const TABLE_COLUMNS = [
    { name: "", numeric: false, enableSorting: false, key: "info", id: "info" },
    { name: "Name", numeric: false, enableSorting: true, id: "name" },
    {
        name: "Image Name",
        numeric: false,
        enableSorting: false,
        id: "image_name",
    },
    { name: "Tag", numeric: false, enableSorting: false, id: "tag" },
    { name: "Status", numeric: false, enableSorting: false, id: "status" },
];

const StyledToolListing = withStyles(styles)(ToolListing);

function ToolListing(props) {
    const {
        parentId,
        toolList,
        loading,
        presenter,
        selectedTool,
        numToolsSelected,
        order,
        orderBy,
        page,
        rowsPerPage,
        setState,
        classes,
    } = props;

    const onRequestSort = (event, property) => {
        const isDesc = orderBy === property && order === "desc";
        setState({
            order: isDesc ? "asc" : "desc",
            orderBy: property,
        });
    };

    const clearSelectedTool = () => {
        setState({
            selectedTool: null,
            numToolsSelected: 0,
        });
    };

    return (
        <div className={classes.container}>
            <LoadingMask loading={loading}>
                <Table size="small">
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
                                const isSelected = selectedTool
                                    ? selectedTool.id === tool.id
                                    : false;
                                return (
                                    <TableRow
                                        tabIndex={-1}
                                        hover
                                        key={tool.id}
                                        selected={isSelected}
                                        onClick={() => {
                                            selectedTool === tool
                                                ? clearSelectedTool()
                                                : setState({
                                                      selectedTool: tool,
                                                      numToolsSelected: 1,
                                                  });
                                        }}
                                    >
                                        <TableCell padding="checkbox">
                                            <Checkbox
                                                id={build(
                                                    parentId,
                                                    tool.id,
                                                    ids.MANAGE_TOOLS
                                                        .TOOL_CHECKBOX
                                                )}
                                                checked={isSelected}
                                            />
                                        </TableCell>
                                        <TableCell padding="none">
                                            <IconButton
                                                onClick={() =>
                                                    presenter.onShowToolInfo(
                                                        tool.id
                                                    )
                                                }
                                                id={build(
                                                    parentId,
                                                    tool.id,
                                                    ids.MANAGE_TOOLS
                                                        .TOOL_INFO_BTN
                                                )}
                                            >
                                                <InfoOutlined />
                                            </IconButton>
                                        </TableCell>
                                        <TableCell>{tool.name}</TableCell>
                                        <TableCell>
                                            {tool.container.image.name}
                                        </TableCell>
                                        <TableCell>
                                            {tool.container.image.tag}
                                        </TableCell>
                                        <TableCell>
                                            {tool.is_public
                                                ? getMessage("public")
                                                : tool.permission}
                                        </TableCell>
                                    </TableRow>
                                );
                            })}
                    </TableBody>
                    <EnhancedTableHead
                        selectable={true}
                        numSelected={numToolsSelected}
                        rowCount={toolList ? toolList.tools.length : 0}
                        baseId={parentId}
                        columnData={TABLE_COLUMNS}
                        order={order}
                        orderBy={orderBy}
                        onSelectAllClick={clearSelectedTool}
                        onRequestSort={onRequestSort}
                    />
                </Table>
                <TablePagination
                    className={classes.tablePagination}
                    colSpan={6}
                    component="div"
                    count={toolList ? toolList.total : 0}
                    rowsPerPage={rowsPerPage}
                    page={page}
                    onChangePage={(event, page) => {
                        setState({ page: page });
                    }}
                    onChangeRowsPerPage={(event) => {
                        setState({ rowsPerPage: event.target.value });
                    }}
                    ActionsComponent={TablePaginationActions}
                    rowsPerPageOptions={PAGING_OPTIONS}
                />
            </LoadingMask>
        </div>
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
};

export default withI18N(injectIntl(ManageTools), messages);
