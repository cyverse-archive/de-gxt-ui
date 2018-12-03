/**
 *  @author sriram
 *
 **/

import React, { Component } from 'react';
import { injectIntl } from "react-intl";

import moment from "moment";

import constants from "../../constants";
import DotMenu from "./DotMenu";
import intlData from "../messages";
import ids from "../ids";
import exStyles from "../style";
import appType from "../model/appType";
import permission from "../model/permission";
import Color from "../../util/CyVersePalette";
import build from "../../util/DebugIDUtil";
import DEHyperLink from "../../util/hyperlink/DEHyperLink";
import withI18N, { formatMessage, getMessage } from "../../util/I18NWrapper";

import AnalysisParametersDialog from "./dialogs/AnalysisParametersDialog";
import AnalysisInfoDialog from "./dialogs/AnalysisInfoDialog";
import analysisStatus from "../model/analysisStatus";
import AnalysesToolbar from "./AnalysesToolbar";
import DEPromptDialog from "../../util/dialog/DEPromptDialog";
import ShareWithSupportDialog from "./dialogs/ShareWithSupportDialog";
import DEConfirmationDialog from "../../util/dialog/DEConfirmationDialog";
import EnhancedTableHead from "../../util/table/EnhancedTableHead";
import TablePaginationActions from "../../util/table/TablePaginationActions";

import Checkbox from "@material-ui/core/Checkbox";
import CircularProgress from "@material-ui/core/CircularProgress";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TablePagination from "@material-ui/core/TablePagination";
import TableRow from "@material-ui/core/TableRow";
import { withStyles } from "@material-ui/core/styles";

import LaunchIcon from "@material-ui/icons/Launch";
import ListAltIcon from "@material-ui/icons/ListAlt";

function AnalysisName(props) {
    const name = props.analysis.name;
    const isBatch = props.analysis.batch;
    const className = props.classes.analysisName;
    const presenter = props.presenter;
    const resultFolderId = props.analysis.resultfolderid;
    const handleGoToOutputFolder = props.handleGoToOutputFolder;
    const handleBatchIconClick = props.handleBatchIconClick;
    const interactiveUrls = props.analysis.interactive_urls;
    const handleInteractiveUrlClick = props.handleInteractiveUrlClick;
    const status = props.analysis.status;

    if (isBatch) {
        return (
            <span className={className} onClick={handleGoToOutputFolder}>
                <ListAltIcon onClick={handleBatchIconClick}
                             style={{color: Color.darkGreen}}/>
                <sup>{name}</sup>
            </span>
        );
    } else if (status === analysisStatus.RUNNING && interactiveUrls && interactiveUrls.length > 0) {
        return (
            <span className={className} onClick={handleGoToOutputFolder}>
                <LaunchIcon onClick={() => handleInteractiveUrlClick(interactiveUrls[0])}
                            style={{color: Color.darkBlue}}/>
                <sup>{name}</sup>
            </span>
        )
    } else {
        return (
            <span className={className} onClick={handleGoToOutputFolder}>
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
    const presenter = props.presenter;
    const handleRelaunch = props.handleRelaunch;

    if (!isDisabled) {
        return (
            <span className={className} onClick={() => handleRelaunch(analysis)}>
                {name}
            </span>
        );
    } else {
        return (
            <span>
                {name}
            </span>
        );
    }

}

function Status(props) {
    const {analysis, onClick, username} = props;
    if (username === analysis.username && analysis.status !== analysisStatus.CANCELED) {
        return (<DEHyperLink
            onClick={(analysis) => onClick(analysis)}
            text={analysis.status}/>);
    } else {
        return (<span style={{textAlign: 'left', fontSize: '11px'}}>{analysis.status}</span>);
    }
}

function Prompt(props) {
    const analysis = props.analysis;
    const renameDialogOpen = props.renameDialogOpen;
    const commentsDialogOpen = props.commentsDialogOpen;
    const onRenameOkClicked = props.onRenameOkClicked;
    const onRenameCancelClicked = props.onRenameCancelClicked;
    const onCommentsOkClicked = props.onCommentsOkClicked;
    const onCommentsCancelClicked = props.onCommentsCancelClicked;
    const intl = props.intl;

    if (renameDialogOpen) {
        return (
            <DEPromptDialog multiline={false}
                            initialValue={analysis.name}
                            isRequired={true}
                            heading={formatMessage(intl, "renameDlgHeader")}
                            prompt={formatMessage(intl, "renamePrompt")}
                            onOkBtnClick={onRenameOkClicked}
                            onCancelBtnClick={onRenameCancelClicked}
                            dialogOpen={renameDialogOpen}/>
        );
    } else if (commentsDialogOpen) {
        return (
            <DEPromptDialog multiline={true}
                            initialValue={analysis.description}
                            isRequired={true}
                            heading={formatMessage(intl, "commentsDlgHeader")}
                            prompt={formatMessage(intl, "commentsPrompt")}
                            onOkBtnClick={onCommentsOkClicked}
                            onCancelBtnClick={onCommentsCancelClicked}
                            dialogOpen={commentsDialogOpen}/>
        );
    } else {
        return (null);
    }
}

const columnData = [
    {name: "Name", numeric: false, enableSorting: true},
    {name: "Owner", numeric: false, enableSorting: true},
    {name: "App", numeric: false, enableSorting: true},
    {name: "Start Date", numeric: false, enableSorting: true},
    {name: "End Date", numeric: false, enableSorting: true},
    {name: "Status", numeric: false, enableSorting: true},
    {name: "", numeric: false, enableSorting: false},
];

const IPLANT = "iplantcollaborative";

const filter = {
    field: "",
    value: "",
};

const filterList = {
    filters: []
};

const ALL = "all";

const MINE = "mine";

const THEIRS = "theirs";

const PARENT_ID = "parent_id";

const APP_NAME = "app_name";

const NAME = "name";

const ID = "id";

class AnalysesView extends Component {
    constructor(props) {
        super(props);
        this.state = {
            data: this.props.analysesList ? this.props.analysesList.analyses : [],
            loading: true,
            total: this.props.analysesList && this.props.analysesList.total ?
                this.props.analysesList.total :
                0,
            offset: 0,
            page: 0,
            rowsPerPage: 100,
            selected: [],
            order: 'desc',
            orderBy: 'enddate',
            permFilter: this.props.permFilter,
            typeFilter: this.props.appTypeFilter,
            parentId: "",
            nameFilter: "",
            appNameFilter: "",
            idFilter: this.props.selectedAnalysisId,
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
        this.getPermissionFilter = this.getPermissionFilter.bind(this);
        this.doRename = this.doRename.bind(this);
        this.doComments = this.doComments.bind(this);
        this.update = this.update.bind(this);
        this.onShareWithSupport = this.onShareWithSupport.bind(this);
        this.statusClick = this.statusClick.bind(this);
        this.handleBatchIconClick = this.handleBatchIconClick.bind(this);
        this.onTypeFilterChange = this.onTypeFilterChange.bind(this);
        this.onPermissionsFilterChange = this.onPermissionsFilterChange.bind(this);
        this.handleParamValueClick = this.handleParamValueClick.bind(this);
        this.handleSaveParamsToFileClick = this.handleSaveParamsToFileClick.bind(this);
        this.handleSearch = this.handleSearch.bind(this);
        this.getSearchFilter = this.getSearchFilter.bind(this);
        this.handleInteractiveUrlClick = this.handleInteractiveUrlClick.bind(this);
        this.handleSaveAndComplete = this.handleSaveAndComplete.bind(this);
        this.handleDeleteClick = this.handleDeleteClick.bind(this);
    }

    componentDidMount() {
        this.fetchAnalyses();
    }

    fetchAnalyses() {
        const {rowsPerPage, offset, filter, order, orderBy} = this.state;
        this.setState({loading: true});

        const parentFilter = this.getParentIdFilter();
        const typeFilter = this.getTypeFilter();
        const permFilter = this.getPermissionFilter();
        const searchFilter = this.getSearchFilter();

        if (permFilter && permFilter.value) {
            parentFilter.value = "";
        }

        const filtersObj = Object.create(filterList);
        if (searchFilter && searchFilter.length > 0) {
            filtersObj.filters = searchFilter;
        } else {
            filtersObj.filters = [parentFilter, typeFilter, permFilter];
        }

        this.props.presenter.getAnalyses(rowsPerPage, offset, filtersObj, orderBy, order.toUpperCase(),
            (analysesList) => {
                this.setState({
                    loading: false,
                    data: analysesList.analyses,
                    total: analysesList.total ? analysesList.total : 0,
                })
            },
            (errorCode, errorMessage) => {
                this.setState({
                    loading: false,
                });
            },
        );
    }

    handleBatchIconClick(event, id) {
        event.stopPropagation();
        this.setState({typeFilter: "", permFilter: "", parentId: id}, () => this.fetchAnalyses());
    }

    getParentIdFilter() {
        const idParentFilter = Object.create(filter);
        idParentFilter.field = "parent_id";
        idParentFilter.value = this.state.parentId;
        return idParentFilter;
    }

    getTypeFilter() {
        const typeFilter1 = this.state.typeFilter;

        if (!typeFilter1 || typeFilter1 === appType.all) {
            return null;
        }

        const typeFilter = Object.create(filter);
        typeFilter.field = "type";
        typeFilter.value = typeFilter1;
        return typeFilter;
    }

    getPermissionFilter() {
        const permFilter1 = this.state.permFilter;
        let val = "";

        if (!permFilter1) {
            return null;
        }

        switch (permFilter1) {
            case permission.all :
                val = ALL;
                break;
            case permission.mine :
                val = MINE;
                break;
            case permission.theirs:
                val = THEIRS;
                break;
            default:
                val = ALL;
        }

        const permFilter = Object.create(filter);
        permFilter.field = "ownership";
        permFilter.value = val;
        return permFilter;
    }

    getSearchFilter() {
        const {nameFilter, appNameFilter, idFilter} = this.state;
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
        const {rowsPerPage} = this.state;
        this.setState({page: page, offset: rowsPerPage * page});
    }

    handleChangeRowsPerPage(event) {
        this.setState({rowsPerPage: event.target.value});
    }

    handleSelectAllClick(event, checked) {
        if (checked) {
            this.setState(state => ({selected: state.data.map(n => n.message.id)}));
            return;
        }
        this.setState({selected: []});
    }

    isSelected(id) {
        return this.state.selected.indexOf(id) !== -1
    }

    statusClick(analysis) {
        this.handleRowClick(analysis.id);
        this.setState((prevState, props) => {
            return {shareWithSupportDialogOpen: true}
        });
    }

    onShareWithSupport(analysis, comment, supportRequested) {
        this.setState({loading: true});
        if (supportRequested) {
            this.setState({loading: true, shareWithSupportDialogOpen: false});
            this.props.presenter.onUserSupportRequested(analysis, comment, () => {
                    this.setState({
                        loading: false,
                    });
                },
                (errorCode, errorMessage) => {
                    this.setState({
                        loading: false,
                    });
                });
        } else {
            this.setState({shareWithSupportDialogOpen: false});
        }
    }

    handleRowClick(id) {
        this.setState((prevState, props) => {
            const {selected} = prevState;
            const selectedIndex = selected.indexOf(id);
            let newSelected = [];

            if (selectedIndex === -1) {
                newSelected.push(id);
                return {selected: newSelected};
            }
        });

    }

    handleCheckBoxClick(event, id) {
        this.setState((prevState, props) => {
            const {selected} = prevState;
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
                    selected.slice(selectedIndex + 1),
                );
            }
            return {selected: newSelected};
        });
        event.stopPropagation();
    }

    handleGoToOutputFolder() {
        this.props.presenter.onAnalysisNameSelected(this.findAnalysis(this.state.selected[0]));
    }

    handleInteractiveUrlClick(url) {
        window.open(url, '_blank');
    }

    handleViewParams() {
        let selected = this.state.selected[0];
        this.setState({loading: true});
        this.props.paramPresenter.fetchAnalysisParameters(selected, (params) => {
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
            });
    }
    handleParamValueClick(parameter) {
        this.setState({viewParamsDialogOpen: false}); //have to close view params
        // otherwise file viewer wont show up front
       this.props.paramPresenter.onAnalysisParamValueSelected(parameter);
    }

    handleSaveParamsToFileClick(parameters) {
        this.setState({loading: true, viewParamsDialogOpen: false});   //close view params temporarily
        if (parameters && parameters.length > 0) {                       //so that save as dialog opens on
            let contents = "Name\t" + "Type\t" + "Value\t\n";          // top of the screen
            parameters.forEach(function(param){
                contents =
                    contents.concat(param.param_name + "\t" + param.param_type + "\t" +
                        param.displayValue + "\t\n");
            });
            this.props.paramPresenter.saveParamsToFile(contents, () => {
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
                });
        }
    }

    handleRelaunch(analysis) {
        this.props.presenter.onAnalysisAppSelected(analysis.id, analysis.system_id, analysis.app_id);
    }

    handleViewInfo() {
        let id = this.state.selected[0];
        this.setState({loading: true});
        this.props.presenter.onAnalysisJobInfoSelected(id, (info) => {
                this.setState({
                    loading: false,
                    info: info,
                    infoDialogOpen: true
                });
            },
            (errorCode, errorMessage) => {
                this.setState({
                    loading: false,
                });
            });
    }

    handleShare() {
        const selectedAnalyses = this.state.selected.map(id => this.findAnalysis(id));
        this.props.presenter.onShareAnalysisSelected(selectedAnalyses);
    }

    handleCancel() {
        this.setState({loading: true});
        const selectedAnalyses = this.state.selected.map(id => this.findAnalysis(id));
        const presenter = this.props.presenter;
        let promises = [];

        if (selectedAnalyses && selectedAnalyses.length > 0) {
            selectedAnalyses.forEach(function (analysis) {
                let p = new Promise((resolve, reject) => {
                    presenter.onCancelAnalysisSelected(analysis.id, analysis.name, () => {
                            resolve("");
                        },
                        (errorCode, errorMessage) => {
                            reject("");
                        });
                });
                promises.push(p);
            });

            Promise.all(promises)
                .then(value => {
                    this.setState({loading: false});
                    this.fetchAnalyses();
                })
                .catch(error => {
                    this.setState({loading: false});
                    this.fetchAnalyses();
                });
        }
    }

    handleSaveAndComplete() {
        this.setState({loading: true});
        const selectedAnalyses = this.state.selected.map(id => this.findAnalysis(id));
        const presenter = this.props.presenter;
        let promises = [];

        if (selectedAnalyses && selectedAnalyses.length > 0) {
            selectedAnalyses.forEach(function (analysis) {
                let p = new Promise((resolve, reject) => {
                    presenter.onCompleteAnalysisSelected(analysis.id, analysis.name, () => {
                            resolve("");
                        },
                        (errorCode, errorMessage) => {
                            reject("");
                        });
                });
                promises.push(p);
            });

            Promise.all(promises)
                .then(value => {
                    this.setState({loading: false});
                    this.fetchAnalyses();
                })
                .catch(error => {
                    this.setState({loading: false});
                    this.fetchAnalyses();
                });
        }
    }

    handleDeleteClick() {
        this.setState({confirmDeleteDialogOpen: true});
    }
    handleDelete() {
        this.setState({loading: true, confirmDeleteDialogOpen: false});
        this.props.presenter.deleteAnalyses(this.state.selected, () => {
                this.setState({
                    loading: false,
                    selected: []
                }, () => this.fetchAnalyses());
            },
            (errorCode, errorMessage) => {
                this.setState({
                    loading: false,
                });
            });
    }

    handleRename() {
        this.setState({renameDialogOpen: true, commentsDialogOpen: false});
    }

    handleUpdateComments() {
        this.setState({commentsDialogOpen: true, renameDialogOpen: false});
    }

    doRename(newName) {
        this.setState({loading: true, renameDialogOpen: false});
        this.props.presenter.renameAnalysis(this.state.selected[0], newName, () => {
                this.setState({
                    loading: false,
                });
                this.update(this.state.selected[0], newName);
            },
            (errorCode, errorMessage) => {
                this.setState({
                    loading: false,
                });
            });
    }

    doComments(comments) {
        this.setState({loading: true, commentsDialogOpen: false});
        this.props.presenter.updateAnalysisComments(this.state.selected[0], comments, () => {
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
            });
    }


    isMultiSelect = () => this.state.selected && (this.state.selected.length > 1) ? true : false;

    isDisabled = () => this.state.selected && (this.state.selected.length > 0) ? false : true;

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
        if (this.isDisabled(selection)) {
            return true;
        }
        for (let i = 0; i < selection.length; i++) {
            let found = this.findAnalysis(selection[i]);
            if (found) {
                if (found.status === analysisStatus.RUNNING ||
                    found.status === analysisStatus.IDLE ||
                    found.status === analysisStatus.SUBMITTED) {
                    return false;
                }
                if (found.batch && (found.batch_status.running > 0 || found.batch_status.submitted > 0)) {
                    return false;
                }
            }
        }
        return true;
    }

    findAnalysis(id) {
        return this.state.data.find(function (n) {
            return n.id === id;
        });
    }

    update(id, newName) {
        let analysis = this.findAnalysis(id);
        analysis.name = newName;
        this.state.data.forEach(function (a) {
            if (a.id === analysis.id) {
                a = analysis;
                return;
            }
        });
        this.setState((prevState, props) => {
            return {data: prevState.data};   // make it display the updated name
        });

    }

    onPermissionsFilterChange(permFilterVal) {
        this.setState({permFilter: permFilterVal}, () => this.fetchAnalyses());
    }

    onTypeFilterChange(typeFilterVal) {
        this.setState({typeFilter: typeFilterVal}, () => this.fetchAnalyses());
    }

    handleSearch(searchText) {
        if (searchText) {
            this.setState({
                permFilter: "",
                typeFilter: "",
                parentId: "",
                idFilter:"",
                nameFilter: searchText,
                appNameFilter: searchText,
            }, () => this.fetchAnalyses());
        } else {
            this.setState({
                permFilter: permission.all,
                typeFilter: appType.all,
                idFilter: "",
                parentId: "",
                nameFilter: "",
                appNameFilter: "",
            }, () => this.fetchAnalyses());
        }
    }

    render() {
        const {
            classes,
            intl,
            name,
            email,
            username,
            baseDebugId,
            selectedAnalysisName
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
            permFilter,
            typeFilter,
            parameters,
            info,
            infoDialogOpen,
        } = this.state;
        const selectedAnalysis = this.findAnalysis(selected[0]);
        const baseId = baseDebugId + ids.ANALYSES_VIEW;
        const gridId = baseDebugId + ids.ANALYSES_VIEW + ids.GRID;
        return (
            <React.Fragment>
                <div id={baseId} className={classes.container}>
                    {this.state.loading &&
                    <CircularProgress size={30} className={classes.loadingStyle} thickness={7}/>
                    }
                    <AnalysesToolbar baseDebugId={build(baseId, ids.TOOLBAR)}
                                     selected={selected}
                                     handleGoToOutputFolder={this.handleGoToOutputFolder}
                                     handleViewParams={this.handleViewParams}
                                     handleRelaunch={this.handleRelaunch}
                                     handleViewInfo={this.handleViewInfo}
                                     handleShare={this.handleShare}
                                     handleCancel={this.handleCancel}
                                     handleDeleteClick={this.handleDeleteClick}
                                     handleRename={this.handleRename}
                                     handleUpdateComments={this.handleUpdateComments}
                                     handleSaveAndComplete={this.handleSaveAndComplete}
                                     isDisabled={this.isDisabled}
                                     isMultiSelect={this.isMultiSelect}
                                     shouldDisableCancel={this.shouldDisableCancel}
                                     isOwner={this.isOwner}
                                     isSharable={this.isSharable}
                                     handleRefersh={this.fetchAnalyses}
                                     permFilter={permFilter}
                                     typeFilter={typeFilter}
                                     onPermissionsFilterChange={this.onPermissionsFilterChange}
                                     onTypeFilterChange={this.onTypeFilterChange}
                                     onSearch={this.handleSearch}
                                     searchInputValue={selectedAnalysisName}
                    />
                    <div className={classes.table}>
                        <Table>
                            <EnhancedTableHead
                                selectable={true}
                                numSelected={selected.length}
                                order={order}
                                orderBy={orderBy}
                                onSelectAllClick={this.handleSelectAllClick}
                                onRequestSort={this.handleRequestSort}
                                rowCount={total}
                                columnData={columnData}
                                baseId={baseId}
                                ids={ids}
                                padding="none"
                            />
                            <TableBody>
                                {data.map(n => {
                                    const id = n.id;
                                    const isSelected = this.isSelected(id);
                                    const user = n.username.includes(IPLANT) ?
                                        n.username.split('@')[0] :
                                        n.username;
                                    return (
                                        <TableRow onClick={() => this.handleRowClick(id)}
                                                  role="checkbox"
                                                  aria-checked={isSelected}
                                                  tabIndex={-1}
                                                  selected={isSelected}
                                                  hover
                                                  key={id}
                                        >
                                            <TableCell padding="none">
                                                <Checkbox
                                                    onClick={(event, n) => this.handleCheckBoxClick(
                                                        event,
                                                        id)}
                                                    checked={isSelected}/>
                                            </TableCell>
                                            <TableCell id={build(gridId, id + ids.ANALYSIS_NAME_CELL)}
                                                       padding="none">
                                                <AnalysisName classes={classes}
                                                              analysis={n}
                                                              handleGoToOutputFolder={this.handleGoToOutputFolder}
                                                              handleInteractiveUrlClick={this.handleInteractiveUrlClick}
                                                              handleBatchIconClick={(event) => this.handleBatchIconClick(
                                                                  event,
                                                                  id)}/>
                                            </TableCell>
                                            <TableCell className={classes.cellText}
                                                       padding="none">{user}</TableCell>
                                            <TableCell id={build(gridId, id + ids.APP_NAME_CELL)}
                                                       className={classes.cellText}
                                                       padding="none">
                                                <AppName analysis={n}
                                                         handleRelaunch={this.handleRelaunch}
                                                         classes={classes}/>
                                            </TableCell>
                                            <TableCell className={classes.cellText} padding="none">
                                                {parseInt(n.startdate, 10) ?
                                                    moment(parseInt(n.startdate, 10), "x").format(
                                                        constants.LONG_DATE_FORMAT) :
                                                    getMessage("emptyValue")}
                                            </TableCell>
                                            <TableCell className={classes.cellText} padding="none">
                                                {parseInt(n.enddate, 10) ?
                                                    moment(parseInt(n.enddate, 10), "x").format(
                                                        constants.LONG_DATE_FORMAT) :
                                                    getMessage("emptyValue")}
                                            </TableCell>
                                            <TableCell id={build(gridId, id + ids.SUPPORT_CELL)}
                                                       adding="none">
                                                <Status analysis={n}
                                                        onClick={() => this.statusClick(n)}
                                                        username={username}/>
                                            </TableCell>
                                            <TableCell padding="none"
                                                       id={build(gridId, id + ids.ANALYSIS_DOT_MENU)}>
                                                <DotMenu
                                                    baseDebugId={build(gridId, id + ids.ANALYSIS_DOT_MENU)}
                                                    handleGoToOutputFolder={this.handleGoToOutputFolder}
                                                    handleViewParams={this.handleViewParams}
                                                    handleRelaunch={this.handleRelaunch}
                                                    handleViewInfo={this.handleViewInfo}
                                                    handleShare={this.handleShare}
                                                    handleCancel={this.handleCancel}
                                                    handleDeleteClick={this.handleDeleteClick}
                                                    handleRename={this.handleRename}
                                                    handleUpdateComments={this.handleUpdateComments}
                                                    isDisabled={this.isDisabled}
                                                    isMultiSelect={this.isMultiSelect}
                                                    shouldDisableCancel={this.shouldDisableCancel}
                                                    isOwner={this.isOwner}
                                                    isSharable={this.isSharable}
                                                    handleSaveAndComplete={this.handleSaveAndComplete}
                                                />
                                            </TableCell>
                                        </TableRow>
                                    );
                                })}
                            </TableBody>
                        </Table>
                    </div>
                    <TablePagination style={{height: 50}}
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
                </div>
                {selectedAnalysis &&
                <Prompt analysis={selectedAnalysis}
                        intl={intl}
                        renameDialogOpen={this.state.renameDialogOpen}
                        commentsDialogOpen={this.state.commentsDialogOpen}
                        onRenameOkClicked={this.doRename}
                        onRenameCancelClicked={() => this.setState({renameDialogOpen: false})}
                        onCommentsOkClicked={this.doComments}
                        onCommentsCancelClicked={() => this.setState({commentsDialogOpen: false})}
                />
                }
                {selectedAnalysis &&
                <ShareWithSupportDialog dialogOpen={shareWithSupportDialogOpen}
                                        analysis={selectedAnalysis}
                                        name={name}
                                        email={email}
                                        onShareWithSupport={this.onShareWithSupport}/>
                }
                {selectedAnalysis &&
                <AnalysisParametersDialog dialogOpen={viewParamsDialogOpen}
                                          analysisName={selectedAnalysis.name}
                                          parameters={parameters}
                                          diskResourceUtil={this.props.diskResourceUtil}
                                          onViewParamDialogClose={() => this.setState({viewParamsDialogOpen: false})}
                                          onValueClick={this.handleParamValueClick}
                                          onSaveClick={this.handleSaveParamsToFileClick}
                />
                }
                {info && selectedAnalysis &&
                <AnalysisInfoDialog info={info}
                                    dialogOpen={infoDialogOpen}
                                    onInfoDialogClose={() => this.setState({infoDialogOpen: false})}
                />
                }
                {selectedAnalysis &&
                <DEConfirmationDialog dialogOpen={confirmDeleteDialogOpen}
                                      message={formatMessage(intl, "analysesExecDeleteWarning")}
                                      heading={formatMessage(intl,"delete")}
                                      onOkBtnClick={this.handleDelete}
                                      onCancelBtnClick={() => {
                                          this.setState({confirmDeleteDialogOpen: false})
                                      }}
                />
                }
            </React.Fragment>
        );
    }
}

AnalysesView.propTypes = {};

export default withStyles(exStyles)(withI18N(injectIntl(AnalysesView), intlData));
