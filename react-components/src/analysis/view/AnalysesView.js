/**
 *  @author sriram
 *
 **/

import React, { Component, Fragment } from "react";
import PropTypes from "prop-types";
import { injectIntl } from "react-intl";

import intlData from "../messages";
import ids from "../ids";
import exStyles from "../style";
import appType from "../model/appType";
import viewFilter from "../model/viewFilterOptions";

import DotMenu from "./DotMenu";
import AnalysisParametersDialog from "./dialogs/AnalysisParametersDialog";
import AnalysisInfoDialog from "./dialogs/AnalysisInfoDialog";
import analysisStatus from "../model/analysisStatus";
import AnalysesToolbar from "./AnalysesToolbar";
import ShareWithSupportDialog from "./dialogs/ShareWithSupportDialog";

import {
    build,
    DEHyperlink,
    DEConfirmationDialog,
    DEPromptDialog,
    EnhancedTableHead,
    formatDate,
    formatMessage,
    LoadingMask,
    getMessage,
    TablePaginationActions,
    withI18N,
} from "@cyverse-de/ui-lib";

import LaunchIcon from "@material-ui/icons/Launch";
import analysesExpandIcon from "../../resources/images/analyses-expandList.svg";
import analysesCollapseIcon from "../../resources/images/analyses-collapseList.svg";
import altAnalysesExpandIcon from "../../resources/images/analyses-expandList.png";
import altAnalysesCollapseIcon from "../../resources/images/analyses-collapseList.png";

import {
    Checkbox,
    Table,
    TableBody,
    TableCell,
    TablePagination,
    TableRow,
    Tooltip as ToolTip,
    Typography,
    withStyles,
} from "@material-ui/core";

function AnalysisName(props) {
    const name = props.analysis.name;
    const isBatch = props.analysis.batch;
    const className = props.classes.analysisName;
    const interactiveStyle = props.classes.interactiveButton;
    const handleGoToOutputFolder = props.handleGoToOutputFolder;
    const handleBatchIconClick = props.handleBatchIconClick;
    const handleViewAllIconClick = props.handleViewAllIconClick;
    const interactiveUrls = props.analysis.interactive_urls;
    const handleInteractiveUrlClick = props.handleInteractiveUrlClick;
    const status = props.analysis.status;
    const intl = props.intl;
    const analysis = props.analysis;
    const parentId = props.parentId;
    const baseId = props.baseId;

    if (isBatch) {
        return (
            <span
                title={formatMessage(intl, "goOutputFolderOf") + " " + name}
                className={className}
                onClick={() => handleGoToOutputFolder(analysis)}
            >
                <ToolTip title={getMessage("htDetails")}>
                    <img
                        src={analysesExpandIcon}
                        alt={altAnalysesExpandIcon}
                        onClick={handleBatchIconClick}
                        style={{ height: 16 }}
                        id={build(baseId, ids.ICONS.BATCH)}
                    />
                </ToolTip>
                <sup>{name}</sup>
            </span>
        );
    } else if (
        (status === analysisStatus.SUBMITTED ||
            status === analysisStatus.RUNNING) &&
        interactiveUrls &&
        interactiveUrls.length > 0
    ) {
        return (
            <Fragment>
                <LaunchIcon
                    onClick={() =>
                        handleInteractiveUrlClick(interactiveUrls[0])
                    }
                    id={build(baseId, ids.ICONS.INTERACTIVE)}
                    className={interactiveStyle}
                />
                <span
                    title={formatMessage(intl, "goOutputFolderOf") + " " + name}
                    className={className}
                    onClick={() => handleGoToOutputFolder(analysis)}
                >
                    <sup>{name}</sup>
                </span>
            </Fragment>
        );
    } else if (parentId) {
        return (
            <span
                title={formatMessage(intl, "goOutputFolderOf") + " " + name}
                className={className}
                onClick={() => handleGoToOutputFolder(analysis)}
            >
                <ToolTip title={getMessage("viewAll")}>
                    <img
                        src={analysesCollapseIcon}
                        alt={altAnalysesCollapseIcon}
                        onClick={handleViewAllIconClick}
                        id={build(baseId, ids.ICONS.COLLAPSE)}
                        style={{ height: 16 }}
                    />
                </ToolTip>
                <sup>{name}</sup>
            </span>
        );
    } else {
        return (
            <span
                title={formatMessage(intl, "goOutputFolderOf") + " " + name}
                className={className}
                onClick={() => handleGoToOutputFolder(analysis)}
            >
                {name}
            </span>
        );
    }
}

function AppName(props) {
    const analysis = props.analysis;
    const name = analysis.app_name;
    const isDisabled = analysis.app_disabled;
    const className = props.classes.analysisName;
    const handleRelaunch = props.handleRelaunch;

    if (!isDisabled) {
        return (
            <span
                className={className}
                onClick={() => handleRelaunch(analysis)}
            >
                {name}
            </span>
        );
    } else {
        return <span>{name}</span>;
    }
}

function Status(props) {
    const { analysis, onClick, username } = props;
    if (
        username === analysis.username &&
        analysis.status !== analysisStatus.CANCELED
    ) {
        return (
            <DEHyperlink
                onClick={(analysis) => onClick(analysis)}
                text={analysis.status}
            />
        );
    } else {
        return (
            <span style={{ textAlign: "left", fontSize: "11px" }}>
                {analysis.status}
            </span>
        );
    }
}

const columnData = [
    {
        id: ids.NAME,
        name: "Name",
        numeric: false,
        enableSorting: true,
        key: "name",
    },
    {
        id: ids.OWNER,
        name: "Owner",
        numeric: false,
        enableSorting: false,
        key: "owner",
    },
    {
        id: ids.APP,
        name: "App",
        numeric: false,
        enableSorting: false,
        key: "app",
    },
    {
        id: ids.START_DATE,
        name: "Start Date",
        numeric: false,
        enableSorting: true,
        key: "startdate",
    },
    {
        id: ids.END_DATE,
        name: "End Date",
        numeric: false,
        enableSorting: true,
        key: "enddate",
    },
    {
        id: ids.STATUS,
        name: "Status",
        numeric: false,
        enableSorting: true,
        key: "status",
    },
    {
        name: "",
        numeric: false,
        enableSorting: false,
    },
];

const IPLANT = "iplantcollaborative";

const filter = {
    field: "",
    value: "",
};

const filterList = {
    filters: [],
};

const ALL = "all";

const MINE = "mine";

const THEIRS = "theirs";

const APP_NAME = "app_name";

const NAME = "name";

const ID = "id";

class AnalysesView extends Component {
    constructor(props) {
        super(props);
        const { analysesList } = props;
        this.state = {
            data: analysesList ? analysesList.analyses : [],
            loading: true,
            total: analysesList && analysesList.total ? analysesList.total : 0,
            offset: 0,
            page: 0,
            rowsPerPage: 100,
            selected: [],
            order: "desc",
            orderBy: "startdate",
            parameters: [],
            info: null,
            infoDialogOpen: false,
            renameDialogOpen: false,
            commentsDialogOpen: false,
            viewParamsDialogOpen: false,
            shareWithSupportDialogOpen: false,
            confirmDeleteDialogOpen: false,
        };
        this.handleChangePage = this.handleChangePage.bind(this);
        this.handleChangeRowsPerPage = this.handleChangeRowsPerPage.bind(this);
        this.handleSelectAllClick = this.handleSelectAllClick.bind(this);
        this.isSelected = this.isSelected.bind(this);
        this.handleGoToOutputFolder = this.handleGoToOutputFolder.bind(this);
        this.handleViewParams = this.handleViewParams.bind(this);
        this.handleRelaunch = this.handleRelaunch.bind(this);
        this.handleRelaunchFromMenu = this.handleRelaunchFromMenu.bind(this);
        this.handleViewInfo = this.handleViewInfo.bind(this);
        this.handleShare = this.handleShare.bind(this);
        this.handleCancel = this.handleCancel.bind(this);
        this.handleDelete = this.handleDelete.bind(this);
        this.handleRename = this.handleRename.bind(this);
        this.handleUpdateComments = this.handleUpdateComments.bind(this);
        this.shouldDisableCancel = this.shouldDisableCancel.bind(this);
        this.isOwner = this.isOwner.bind(this);
        this.isSharable = this.isSharable.bind(this);
        this.fetchAnalyses = this.fetchAnalyses.bind(this);
        this.getParentIdFilter = this.getParentIdFilter.bind(this);
        this.getTypeFilter = this.getTypeFilter.bind(this);
        this.getViewFilter = this.getViewFilter.bind(this);
        this.doRename = this.doRename.bind(this);
        this.doComments = this.doComments.bind(this);
        this.update = this.update.bind(this);
        this.onShareWithSupport = this.onShareWithSupport.bind(this);
        this.statusClick = this.statusClick.bind(this);
        this.handleBatchIconClick = this.handleBatchIconClick.bind(this);
        this.onTypeFilterChange = this.onTypeFilterChange.bind(this);
        this.onViewFilterChange = this.onViewFilterChange.bind(this);
        this.handleParamValueClick = this.handleParamValueClick.bind(this);
        this.handleSaveParamsToFileClick = this.handleSaveParamsToFileClick.bind(
            this
        );
        this.handleSearch = this.handleSearch.bind(this);
        this.getSearchFilter = this.getSearchFilter.bind(this);
        this.handleInteractiveUrlClick = this.handleInteractiveUrlClick.bind(
            this
        );
        this.handleSaveAndComplete = this.handleSaveAndComplete.bind(this);
        this.handleDeleteClick = this.handleDeleteClick.bind(this);
        this.handleRequestSort = this.handleRequestSort.bind(this);
        this.handleViewAllIconClick = this.handleViewAllIconClick.bind(this);
    }

    componentDidMount() {
        this.fetchAnalyses();
    }

    componentDidUpdate(prevProps) {
        if (
            prevProps.viewFilter !== this.props.viewFilter ||
            prevProps.appTypeFilter !== this.props.appTypeFilter ||
            prevProps.appNameFilter !== this.props.appNameFilter ||
            prevProps.nameFilter !== this.props.nameFilter ||
            prevProps.parentId !== this.props.parentId ||
            prevProps.idFilter !== this.props.idFilter
        ) {
            this.fetchAnalyses();
        }
    }

    /**
     * Fetch analyses for the given configuration from state.
     *
     */
    fetchAnalyses() {
        const { rowsPerPage, offset, order, orderBy } = this.state;
        this.setState({ loading: true });

        const parentFilter = this.getParentIdFilter();
        const typeFilter = this.getTypeFilter();
        const viewFilter = this.getViewFilter();
        const searchFilter = this.getSearchFilter();

        if (viewFilter && viewFilter.value) {
            parentFilter.value = "";
        }

        const filtersObj = Object.create(filterList);
        if (searchFilter && searchFilter.length > 0) {
            filtersObj.filters = searchFilter;
        } else {
            filtersObj.filters = [parentFilter, typeFilter, viewFilter];
        }

        this.props.presenter.getAnalyses(
            rowsPerPage,
            offset,
            filtersObj,
            orderBy,
            order.toUpperCase(),
            (analysesList) => {
                this.setState({
                    loading: false,
                    data: analysesList.analyses,
                    total: analysesList.total ? analysesList.total : 0,
                });
            },
            (errorCode, errorMessage) => {
                this.setState({
                    loading: false,
                });
            }
        );
    }

    handleBatchIconClick(event, parentId, analysesName) {
        event.stopPropagation();
        this.props.presenter.handleBatchIconClick(parentId, analysesName);
    }

    handleViewAllIconClick(event) {
        event.stopPropagation();
        this.props.presenter.handleViewAllIconClick();
    }

    handleRequestSort(event, property) {
        const orderBy = property;
        let order = "desc";

        if (this.state.orderBy === property && this.state.order === order) {
            order = "asc";
        }

        this.setState(
            {
                order,
                orderBy,
            },
            () => this.fetchAnalyses(false)
        );
    }

    getParentIdFilter() {
        const idParentFilter = Object.create(filter);
        idParentFilter.field = "parent_id";
        idParentFilter.value = this.props.parentId;
        return idParentFilter;
    }

    getTypeFilter() {
        const typeFilter1 = this.props.appTypeFilter;

        if (!typeFilter1 || typeFilter1 === appType.all) {
            return null;
        }

        const typeFilter = Object.create(filter);
        typeFilter.field = "type";
        typeFilter.value = typeFilter1;
        return typeFilter;
    }

    getViewFilter() {
        const viewFilter1 = this.props.viewFilter;
        let val = "";

        if (!viewFilter1) {
            return null;
        }

        switch (viewFilter1) {
            case viewFilter.all:
                val = ALL;
                break;
            case viewFilter.mine:
                val = MINE;
                break;
            case viewFilter.theirs:
                val = THEIRS;
                break;
            default:
                val = ALL;
        }

        const vf = Object.create(filter);
        vf.field = "ownership";
        vf.value = val;
        return vf;
    }

    getSearchFilter() {
        const { nameFilter, appNameFilter, idFilter } = this.props;
        const searchFilters = [];
        if (nameFilter) {
            const nameFilterObj = Object.create(filter);
            nameFilterObj.field = NAME;
            nameFilterObj.value = nameFilter;
            searchFilters.push(nameFilterObj);
        }

        if (appNameFilter) {
            const appNameFilterObj = Object.create(filter);
            appNameFilterObj.field = APP_NAME;
            appNameFilterObj.value = appNameFilter;
            searchFilters.push(appNameFilterObj);
        }

        if (idFilter) {
            const idFilterObj = Object.create(filter);
            idFilterObj.field = ID;
            idFilterObj.value = idFilter;
            searchFilters.push(idFilterObj);
        }

        return searchFilters;
    }

    handleChangePage(event, page) {
        const { rowsPerPage } = this.state;
        //reset selection between pages
        this.setState(
            { page: page, offset: rowsPerPage * page, selected: [] },
            () => this.fetchAnalyses()
        );
    }

    handleChangeRowsPerPage(event) {
        this.setState({ rowsPerPage: event.target.value }, () =>
            this.fetchAnalyses()
        );
    }

    handleSelectAllClick(event, checked) {
        if (checked) {
            this.setState((state) => ({
                selected: state.data.map((n) => n.id),
            }));
            return;
        }
        this.setState({ selected: [] });
    }

    isSelected(id) {
        return this.state.selected.indexOf(id) !== -1;
    }

    statusClick(analysis) {
        this.handleRowClick(analysis.id);
        this.setState((prevState, props) => {
            return { shareWithSupportDialogOpen: true };
        });
    }

    onShareWithSupport(analysis, comment, supportRequested) {
        this.setState({ loading: true });
        if (supportRequested) {
            this.setState({ loading: true, shareWithSupportDialogOpen: false });
            this.props.presenter.onUserSupportRequested(
                analysis,
                comment,
                () => {
                    this.setState({
                        loading: false,
                    });
                },
                (errorCode, errorMessage) => {
                    this.setState({
                        loading: false,
                    });
                }
            );
        } else {
            this.setState({
                loading: false,
                shareWithSupportDialogOpen: false,
            });
        }
    }

    handleRowClick(id) {
        this.setState((prevState, props) => {
            const { selected } = prevState;
            if (selected.indexOf(id) < 0) {
                return { selected: [id] };
            }
        });
    }

    handleCheckBoxClick(event, id) {
        this.setState((prevState, props) => {
            const { selected } = prevState;
            const selectedIndex = selected.indexOf(id);
            let newSelected = [];

            if (selectedIndex === -1) {
                newSelected = newSelected.concat(selected, id);
            } else if (selectedIndex === 0) {
                newSelected = newSelected.concat(selected.slice(1));
            } else if (selectedIndex === selected.length - 1) {
                newSelected = newSelected.concat(selected.slice(0, -1));
            } else if (selectedIndex > 0) {
                newSelected = newSelected.concat(
                    selected.slice(0, selectedIndex),
                    selected.slice(selectedIndex + 1)
                );
            }
            return { selected: newSelected };
        });
        event.stopPropagation();
    }

    handleGoToOutputFolder(analysis) {
        if (analysis) {
            this.props.presenter.onAnalysisNameSelected(
                analysis.resultfolderid
            );
        } else {
            this.props.presenter.onAnalysisNameSelected(
                this.findAnalysis(this.state.selected[0]).resultfolderid
            );
        }
    }

    handleInteractiveUrlClick(url) {
        window.open(url, "_blank");
    }

    handleViewParams() {
        let selected = this.state.selected[0];
        this.setState({ loading: true });
        this.props.paramPresenter.fetchAnalysisParameters(
            selected,
            (params) => {
                this.setState({
                    loading: false,
                    parameters: params.parameters,
                    viewParamsDialogOpen: true,
                });
            },
            (errorCode, errorMessage) => {
                this.setState({
                    loading: false,
                });
            }
        );
    }
    handleParamValueClick(parameter) {
        //have to close viewFilter params otherwise file viewer wont show up front
        this.setState({ viewParamsDialogOpen: false });
        this.props.paramPresenter.onAnalysisParamValueSelected(parameter);
    }

    handleSaveParamsToFileClick(parameters) {
        //close viewFilter params temporarily so that save as dialog opens on top of the screen
        this.setState({ loading: true, viewParamsDialogOpen: false });

        const csvRow = (...args) => args.join("\t");
        if (parameters && parameters.length > 0) {
            let contents = [csvRow("Name", "Type", "Value")];
            parameters.forEach(({ param_name, param_type, displayValue }) => {
                contents = [
                    ...contents,
                    csvRow(param_name, param_type, displayValue),
                ];
            });

            this.props.paramPresenter.saveParamsToFile(
                contents.join("\n"),
                () => {
                    this.setState({
                        loading: false,
                        viewParamsDialogOpen: true,
                    });
                },
                (errorCode, errorMessage) => {
                    this.setState({
                        loading: false,
                        viewParamsDialogOpen: true,
                    });
                }
            );
        }
    }

    handleRelaunch(analysis) {
        let selected = analysis
            ? analysis
            : this.findAnalysis(this.state.selected[0]);
        this.props.presenter.onAnalysisAppSelected(
            selected.id,
            selected.system_id,
            selected.app_id
        );
    }

    handleRelaunchFromMenu() {
        this.handleRelaunch(this.findAnalysis(this.state.selected[0]));
    }

    handleViewInfo() {
        let id = this.state.selected[0];
        this.setState({ loading: true });
        this.props.presenter.onAnalysisJobInfoSelected(
            id,
            (info) => {
                this.setState({
                    loading: false,
                    info: info,
                    infoDialogOpen: true,
                });
            },
            (errorCode, errorMessage) => {
                this.setState({
                    loading: false,
                });
            }
        );
    }

    handleShare() {
        const selectedAnalyses = this.state.selected.map((id) =>
            this.findAnalysis(id)
        );
        this.props.presenter.onShareAnalysisSelected(selectedAnalyses);
    }

    handleCancel() {
        this.setState({ loading: true });
        const selectedAnalyses = this.state.selected.map((id) =>
            this.findAnalysis(id)
        );
        const presenter = this.props.presenter;
        let promises = [];

        if (selectedAnalyses && selectedAnalyses.length > 0) {
            selectedAnalyses.forEach(function(analysis) {
                let p = new Promise((resolve, reject) => {
                    presenter.onCancelAnalysisSelected(
                        analysis.id,
                        analysis.name,
                        () => {
                            resolve("");
                        },
                        (errorCode, errorMessage) => {
                            reject(errorMessage);
                        }
                    );
                });
                promises.push(p);
            });

            Promise.all(promises)
                .then((value) => {
                    this.setState({ loading: false });
                    this.fetchAnalyses();
                })
                .catch((error) => {
                    this.setState({ loading: false });
                    this.fetchAnalyses();
                });
        }
    }

    handleSaveAndComplete() {
        this.setState({ loading: true });
        const selectedAnalyses = this.state.selected.map((id) =>
            this.findAnalysis(id)
        );
        const presenter = this.props.presenter;
        let promises = [];

        if (selectedAnalyses && selectedAnalyses.length > 0) {
            selectedAnalyses.forEach(function(analysis) {
                let p = new Promise((resolve, reject) => {
                    presenter.onCompleteAnalysisSelected(
                        analysis.id,
                        analysis.name,
                        () => {
                            resolve("");
                        },
                        (errorCode, errorMessage) => {
                            reject(errorMessage);
                        }
                    );
                });
                promises.push(p);
            });

            Promise.all(promises)
                .then((value) => {
                    this.setState({ loading: false });
                    this.fetchAnalyses();
                })
                .catch((error) => {
                    this.setState({ loading: false });
                    this.fetchAnalyses();
                });
        }
    }

    handleDeleteClick() {
        this.setState({ confirmDeleteDialogOpen: true });
    }
    handleDelete() {
        this.setState({ loading: true, confirmDeleteDialogOpen: false });
        this.props.presenter.deleteAnalyses(
            this.state.selected,
            () => {
                this.setState(
                    {
                        loading: false,
                        selected: [],
                    },
                    () => this.fetchAnalyses()
                );
            },
            (errorCode, errorMessage) => {
                this.setState({
                    loading: false,
                });
            }
        );
    }

    handleRename() {
        this.setState({ renameDialogOpen: true });
    }

    handleUpdateComments() {
        this.setState({ commentsDialogOpen: true });
    }

    doRename(newName) {
        this.setState({ loading: true, renameDialogOpen: false });
        this.props.presenter.renameAnalysis(
            this.state.selected[0],
            newName,
            () => {
                this.setState({
                    loading: false,
                });
                this.update(this.state.selected[0], newName);
            },
            (errorCode, errorMessage) => {
                this.setState({
                    loading: false,
                });
            }
        );
    }

    doComments(comments) {
        this.setState({ loading: true, commentsDialogOpen: false });
        this.props.presenter.updateAnalysisComments(
            this.state.selected[0],
            comments,
            () => {
                this.setState({
                    loading: false,
                });
                let analysis = this.findAnalysis(this.state.selected[0]);
                analysis.description = comments;
            },
            (errorCode, errorMessage) => {
                this.setState({
                    loading: false,
                });
            }
        );
    }

    isOwner() {
        let selection = this.state.selected;
        if (selection && selection.length) {
            for (let i = 0; i < selection.length; i++) {
                let found = this.findAnalysis(selection[i]);
                if (!found) {
                    return false;
                }
                if (found.username !== this.props.username) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    isSharable() {
        let selection = this.state.selected;
        if (selection && selection.length) {
            for (let i = 0; i < selection.length; i++) {
                let found = this.findAnalysis(selection[i]);
                if (!found) {
                    return false;
                }
                if (!found.can_share) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    shouldDisableCancel() {
        let selection = this.state.selected;
        for (let i = 0; i < selection.length; i++) {
            let found = this.findAnalysis(selection[i]);
            if (found) {
                if (
                    found.status === analysisStatus.RUNNING ||
                    found.status === analysisStatus.IDLE ||
                    found.status === analysisStatus.SUBMITTED
                ) {
                    return false;
                }
                if (
                    found.batch &&
                    (found.batch_status.running > 0 ||
                        found.batch_status.submitted > 0)
                ) {
                    return false;
                }
            }
        }
        return true;
    }

    findAnalysis(id) {
        return this.state.data.find(function(n) {
            return n.id === id;
        });
    }

    update(id, newName) {
        let analysis = this.findAnalysis(id);
        analysis.name = newName;
        this.setState((prevState, props) => {
            return { data: prevState.data }; // make it display the updated name
        });
    }

    onViewFilterChange(viewFilter) {
        const { appTypeFilter, presenter } = this.props;
        this.setState({ selected: [] });
        presenter.handleViewAndTypeFilterChange(viewFilter, appTypeFilter);
    }

    onTypeFilterChange(appTypeFilter) {
        const { viewFilter, presenter } = this.props;
        this.setState({ selected: [] });
        presenter.handleViewAndTypeFilterChange(viewFilter, appTypeFilter);
    }

    handleSearch(searchText) {
        const { presenter } = this.props;
        presenter.handleSearch(searchText);
    }

    render() {
        const {
            classes,
            intl,
            name,
            email,
            username,
            baseDebugId,
            viewFilter,
            appTypeFilter,
            nameFilter,
            parentId,
        } = this.props;
        const {
            rowsPerPage,
            page,
            order,
            orderBy,
            selected,
            total,
            data,
            shareWithSupportDialogOpen,
            viewParamsDialogOpen,
            confirmDeleteDialogOpen,
            parameters,
            info,
            infoDialogOpen,
            loading,
        } = this.state;

        const selectedAnalysis = this.findAnalysis(selected[0]);
        const baseId = baseDebugId + ids.ANALYSES_VIEW;
        const gridId = baseDebugId + ids.ANALYSES_VIEW + ids.GRID;
        const toolbarId = build(baseId, ids.TOOLBAR);

        const selectionCount = selected ? selected.length : 0;
        const owner = this.isOwner(),
            sharable = this.isSharable(),
            disableCancel = this.shouldDisableCancel();

        const hasData = data && data.length;
        return (
            <React.Fragment>
                <div id={baseId} className={classes.container}>
                    <LoadingMask loading={loading}>
                        <AnalysesToolbar
                            baseDebugId={build(
                                toolbarId,
                                ids.MENUITEM_ANALYSES
                            )}
                            baseToolbarId={toolbarId}
                            handleGoToOutputFolder={this.handleGoToOutputFolder}
                            handleViewParams={this.handleViewParams}
                            handleRelaunch={this.handleRelaunchFromMenu}
                            handleViewInfo={this.handleViewInfo}
                            handleShare={this.handleShare}
                            handleCancel={this.handleCancel}
                            handleDeleteClick={this.handleDeleteClick}
                            handleRename={this.handleRename}
                            handleUpdateComments={this.handleUpdateComments}
                            handleSaveAndComplete={this.handleSaveAndComplete}
                            handleRefresh={this.fetchAnalyses}
                            viewFilter={viewFilter}
                            typeFilter={appTypeFilter}
                            onViewFilterChange={this.onViewFilterChange}
                            onTypeFilterChange={this.onTypeFilterChange}
                            onSearch={this.handleSearch}
                            searchInputValue={nameFilter}
                            selectionCount={selectionCount}
                            owner={owner}
                            sharable={sharable}
                            disableCancel={disableCancel}
                        />
                        <div className={classes.table}>
                            <Table>
                                <TableBody>
                                    {data.map((analysis) => {
                                        const id = analysis.id;
                                        const isSelected = this.isSelected(id);
                                        const user =
                                            analysis.username &&
                                            analysis.username.includes(IPLANT)
                                                ? analysis.username.split(
                                                      "@"
                                                  )[0]
                                                : analysis.username;
                                        return (
                                            <TableRow
                                                onClick={() =>
                                                    this.handleRowClick(id)
                                                }
                                                role="checkbox"
                                                aria-checked={isSelected}
                                                tabIndex={-1}
                                                selected={isSelected}
                                                hover
                                                key={id}
                                                title={analysis.name}
                                            >
                                                <TableCell padding="none">
                                                    <Checkbox
                                                        id={build(
                                                            gridId,
                                                            id + ids.CHECKBOX
                                                        )}
                                                        onClick={(event, n) =>
                                                            this.handleCheckBoxClick(
                                                                event,
                                                                id
                                                            )
                                                        }
                                                        checked={isSelected}
                                                    />
                                                </TableCell>
                                                <TableCell
                                                    id={build(
                                                        gridId,
                                                        id +
                                                            ids.ANALYSIS_NAME_CELL
                                                    )}
                                                    padding="none"
                                                >
                                                    <AnalysisName
                                                        classes={classes}
                                                        intl={intl}
                                                        analysis={analysis}
                                                        baseId={build(
                                                            gridId,
                                                            id +
                                                                ids.ANALYSIS_NAME_CELL
                                                        )}
                                                        parentId={parentId}
                                                        handleGoToOutputFolder={
                                                            this
                                                                .handleGoToOutputFolder
                                                        }
                                                        handleInteractiveUrlClick={
                                                            this
                                                                .handleInteractiveUrlClick
                                                        }
                                                        handleBatchIconClick={(
                                                            event
                                                        ) =>
                                                            this.handleBatchIconClick(
                                                                event,
                                                                id,
                                                                analysis.name
                                                            )
                                                        }
                                                        handleViewAllIconClick={(
                                                            event
                                                        ) =>
                                                            this.handleViewAllIconClick(
                                                                event
                                                            )
                                                        }
                                                    />
                                                </TableCell>
                                                <TableCell
                                                    className={classes.cellText}
                                                    padding="none"
                                                >
                                                    {user}
                                                </TableCell>
                                                <TableCell
                                                    id={build(
                                                        gridId,
                                                        id + ids.APP_NAME_CELL
                                                    )}
                                                    className={classes.cellText}
                                                    padding="none"
                                                >
                                                    <AppName
                                                        analysis={analysis}
                                                        handleRelaunch={
                                                            this.handleRelaunch
                                                        }
                                                        classes={classes}
                                                    />
                                                </TableCell>
                                                <TableCell
                                                    className={classes.cellText}
                                                    padding="none"
                                                >
                                                    {formatDate(
                                                        analysis.startdate
                                                    )}
                                                </TableCell>
                                                <TableCell
                                                    className={classes.cellText}
                                                    padding="none"
                                                >
                                                    {formatDate(
                                                        analysis.enddate
                                                    )}
                                                </TableCell>
                                                <TableCell
                                                    id={build(
                                                        gridId,
                                                        id + ids.SUPPORT_CELL
                                                    )}
                                                    padding="none"
                                                >
                                                    <Status
                                                        analysis={analysis}
                                                        onClick={() =>
                                                            this.statusClick(
                                                                analysis
                                                            )
                                                        }
                                                        username={username}
                                                    />
                                                </TableCell>
                                                <TableCell padding="none">
                                                    <DotMenu
                                                        baseDebugId={build(
                                                            gridId,
                                                            id +
                                                                ids.ANALYSIS_DOT_MENU
                                                        )}
                                                        handleGoToOutputFolder={
                                                            this
                                                                .handleGoToOutputFolder
                                                        }
                                                        handleViewParams={
                                                            this
                                                                .handleViewParams
                                                        }
                                                        handleRelaunch={
                                                            this.handleRelaunch
                                                        }
                                                        handleViewInfo={
                                                            this.handleViewInfo
                                                        }
                                                        handleShare={
                                                            this.handleShare
                                                        }
                                                        handleCancel={
                                                            this.handleCancel
                                                        }
                                                        handleDeleteClick={
                                                            this
                                                                .handleDeleteClick
                                                        }
                                                        handleRename={
                                                            this.handleRename
                                                        }
                                                        handleUpdateComments={
                                                            this
                                                                .handleUpdateComments
                                                        }
                                                        isDisabled={
                                                            this.isDisabled
                                                        }
                                                        isMultiSelect={
                                                            this.isMultiSelect
                                                        }
                                                        shouldDisableCancel={
                                                            this
                                                                .shouldDisableCancel
                                                        }
                                                        isOwner={this.isOwner}
                                                        isSharable={
                                                            this.isSharable
                                                        }
                                                        handleSaveAndComplete={
                                                            this
                                                                .handleSaveAndComplete
                                                        }
                                                        selectionCount={
                                                            selectionCount
                                                        }
                                                        owner={owner}
                                                        sharable={sharable}
                                                        disableCancel={
                                                            disableCancel
                                                        }
                                                    />
                                                </TableCell>
                                            </TableRow>
                                        );
                                    })}
                                </TableBody>
                                <EnhancedTableHead
                                    selectable={true}
                                    numSelected={selected.length}
                                    order={order}
                                    orderBy={orderBy}
                                    onSelectAllClick={this.handleSelectAllClick}
                                    onRequestSort={this.handleRequestSort}
                                    columnData={columnData}
                                    baseId={baseId}
                                    padding="none"
                                    rowsInPage={data.length}
                                />
                            </Table>
                            {!hasData && (
                                <Typography
                                    style={{
                                        margin: "0, auto, 0, auto",
                                        width: 600,
                                    }}
                                    align="center"
                                    variant="subtitle1"
                                >
                                    {getMessage("noAnalysis")}
                                </Typography>
                            )}
                        </div>
                        <TablePagination
                            style={{ height: 40 }}
                            colSpan={3}
                            component="div"
                            count={total}
                            rowsPerPage={rowsPerPage}
                            page={page}
                            onChangePage={this.handleChangePage}
                            onChangeRowsPerPage={this.handleChangeRowsPerPage}
                            ActionsComponent={TablePaginationActions}
                            rowsPerPageOptions={[100, 500, 1000]}
                        />
                    </LoadingMask>
                </div>
                {selectedAnalysis && (
                    <DEPromptDialog
                        multiline={false}
                        initialValue={selectedAnalysis.name}
                        isRequired={true}
                        heading={formatMessage(intl, "renameDlgHeader")}
                        prompt={formatMessage(intl, "renamePrompt")}
                        onOkBtnClick={this.doRename}
                        onCancelBtnClick={() =>
                            this.setState({ renameDialogOpen: false })
                        }
                        dialogOpen={this.state.renameDialogOpen}
                    />
                )}
                {selectedAnalysis && (
                    <DEPromptDialog
                        multiline={true}
                        initialValue={selectedAnalysis.description}
                        isRequired={true}
                        heading={formatMessage(intl, "commentsDlgHeader")}
                        prompt={formatMessage(intl, "commentsPrompt")}
                        onOkBtnClick={this.doComments}
                        onCancelBtnClick={() =>
                            this.setState({ commentsDialogOpen: false })
                        }
                        dialogOpen={this.state.commentsDialogOpen}
                    />
                )}
                {selectedAnalysis && (
                    <ShareWithSupportDialog
                        dialogOpen={shareWithSupportDialogOpen}
                        analysis={selectedAnalysis}
                        name={name}
                        email={email}
                        onShareWithSupport={this.onShareWithSupport}
                        baseId={baseId}
                    />
                )}
                {selectedAnalysis && (
                    <AnalysisParametersDialog
                        dialogOpen={viewParamsDialogOpen}
                        analysisName={selectedAnalysis.name}
                        parameters={parameters}
                        diskResourceUtil={this.props.diskResourceUtil}
                        onViewParamDialogClose={() =>
                            this.setState({
                                viewParamsDialogOpen: false,
                            })
                        }
                        onValueClick={this.handleParamValueClick}
                        onSaveClick={this.handleSaveParamsToFileClick}
                    />
                )}
                {info && (
                    <AnalysisInfoDialog
                        baseDebugId={baseDebugId}
                        info={info}
                        dialogOpen={infoDialogOpen}
                        onInfoDialogClose={() =>
                            this.setState({ infoDialogOpen: false })
                        }
                    />
                )}
                <DEConfirmationDialog
                    debugId={build(baseId, ids.MENUITEM_DELETE)}
                    dialogOpen={confirmDeleteDialogOpen}
                    message={formatMessage(intl, "analysesExecDeleteWarning")}
                    heading={formatMessage(intl, "delete")}
                    onOkBtnClick={this.handleDelete}
                    onCancelBtnClick={() => {
                        this.setState({
                            confirmDeleteDialogOpen: false,
                        });
                    }}
                    okLabel={formatMessage(intl, "okBtnText")}
                    cancelLabel={formatMessage(intl, "cancelBtnText")}
                />
            </React.Fragment>
        );
    }
}

AnalysesView.propTypes = {
    presenter: PropTypes.object.isRequired,
    username: PropTypes.string.isRequired,
    email: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
    paramPresenter: PropTypes.object.isRequired,
    diskResourceUtil: PropTypes.object.isRequired,
    baseDebugId: PropTypes.string.isRequired,
    nameFilter: PropTypes.string,
    appNameFilter: PropTypes.string,
    viewFilter: PropTypes.string.isRequired,
    appTypeFilter: PropTypes.string.isRequired,
    idFilter: PropTypes.string,
};

export default withStyles(exStyles)(
    withI18N(injectIntl(AnalysesView), intlData)
);
