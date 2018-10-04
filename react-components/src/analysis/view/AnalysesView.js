/**
 *  @author sriram
 *
 **/

import React, { Component } from 'react';
import EnhancedTableHead from "../../util/table/EnhancedTableHead";
import TablePaginationActions from "../../util/table/TablePaginationActions";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TablePagination from "@material-ui/core/TablePagination";
import TableRow from "@material-ui/core/TableRow";
import withI18N, { formatMessage, getMessage } from "../../util/I18NWrapper";
import Checkbox from "@material-ui/core/Checkbox";
import moment from "moment";
import constants from "../../constants";
import ids from "../ids";
import { withStyles } from "@material-ui/core/styles";
import exStyles from "../style";
import intlData from "../messages";
import AnalysesToolbar from "./AnalysesToolbar";
import CircularProgress from "@material-ui/core/CircularProgress";
import DEHyperLink from "../../util/hyperlink/DEHyperLink";
import DotMenu from "./DotMenu";
import analysisStatus from "../model/analysisStatus";
import ListAltIcon from "@material-ui/icons/ListAlt";
import Color from "../../util/CyVersePalette";
import appType from "../model/appType";
import permission from "../model/permission";
import DEPromptDialog from "../../util/dialog/DEPromptDialog";
import { injectIntl } from "react-intl";
import ShareWithSupportDialog from "./dialogs/ShareWithSupportDialog";

function AnalysisName(props) {
    let name = props.analysis.name;
    let isBatch = props.analysis.batch;
    let className = props.classes.analysisName;
    let presenter = props.presenter;
    let resultFolderId = props.analysis.resultfolderid;
    let handleGoToOutputFolder = props.handleGoToOutputFolder;

    if (isBatch) {
        return (
            <span className={className} onClick={handleGoToOutputFolder}>
                <ListAltIcon style={{color: Color.darkGreen}}/><sup>{name}</sup>
            </span>
        );
    } else {
        return (
            <span className={className} onClick={handleGoToOutputFolder}>
                {name}
            </span>
        );
    }
}

function AppName(props) {
    let analysis = props.analysis;
    let name = analysis.app_name;
    let isDisabled = analysis.app_disabled;
    let className = props.classes.analysisName;
    let presenter = props.presenter;
    let handleRelaunch = props.handleRelaunch;

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
    const {analysis, onClick, user} = props;
    if (user === analysis.username && analysis.status !== analysisStatus.CANCELED) {
        return (<DEHyperLink
            onClick={(analysis) => onClick(analysis)}
            text={analysis.status}/>);
    } else {
        return (<span>{analysis.status}</span>);
    }
}

function Prompt(props) {
    let analysis = props.analysis;
    let renameDialogOpen = props.renameDialogOpen;
    let commentsDialogOpen = props.commentsDialogOpen;
    let onRenameOkClicked = props.onRenameOkClicked;
    let onRenameCancelClicked = props.onRenameCancelClicked;
    let onCommentsOkClicked = props.onCommentsOkClicked;
    let onCommentsCancelClicked = props.onCommentsCancelClicked;
    let intl = props.intl;

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
            permFilter: permission.all,
            typeFilter: appType.all,
            parentId: "",
            renameDialogOpen: false,
            commentsDialogOpen: false,
            shareWithSupportDialogOpen: false,
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

        if (permFilter && permFilter.value) {
            parentFilter.value = "";
        }

        const filtersObj = Object.create(filterList);
        filtersObj.filters = [parentFilter, typeFilter, permFilter];

        this.props.presenter.getAnalyses(rowsPerPage, offset, filtersObj, orderBy, order.toUpperCase(),
            (analysesList) => {
                this.setState({
                    loading: false,
                    data: analysesList.analyses,
                    total: analysesList.total,
                })
            },
            (errorCode, errorMessage) => {
                this.setState({
                    loading: false,
                });
            },
        );
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
        this.setState((prevState, props) => {
            this.handleRowClick(analysis.id)
            return {shareWithSupportDialogOpen: true}
        });
    }

    onShareWithSupport(analysis, comment, supportRequested) {
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

    handleViewParams() {

    }

    handleRelaunch() {
        this.props.presenter.onAnalysisAppSelected(this.findAnalysis(this.state.selected[0]));
    }

    handleViewInfo() {

    }

    handleShare() {
        const selectedAnalyses = this.state.selected.map(id => this.findAnalysis(id));
        this.props.presenter.onShareAnalysisSelected(selectedAnalyses);
    }

    handleCancel() {
        this.setState({loading: true});
        const selectedAnalyses = this.state.selected.map(id => this.findAnalysis(id));
        this.props.presenter.onCancelAnalysisSelected(selectedAnalyses, () => {
                this.setState({
                    loading: false,
                });
                this.fetchAnalyses();
            },
            (errorCode, errorMessage) => {
                this.setState({
                    loading: false,
                });
            });
    }

    handleDelete() {
        this.setState({loading: true});
        const selectedAnalyses = this.state.selected.map(id => this.findAnalysis(id));
        this.props.presenter.deleteAnalyses(selectedAnalyses, () => {
                this.setState({
                    loading: false,
                });
                this.fetchAnalyses();
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


    multiSelect = () => this.state.selected && (this.state.selected.length > 1) ? true : false;

    disabled = () => this.state.selected && (this.state.selected.length > 0) ? false : true;

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
        if (this.disabled(selection)) {
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

    render() {
        const {classes, intl, presenter, name, email, user} = this.props;
        const {
            rowsPerPage,
            page,
            order,
            orderBy,
            selected,
            total,
            data,
            shareWithSupportDialogOpen
        } = this.state;
            return (
                <React.Fragment>
                    <div className={classes.container}>
                        {this.state.loading &&
                        <CircularProgress size={30} className={classes.loadingStyle} thickness={7}/>
                        }
                        <AnalysesToolbar selected={selected}
                                         handleGoToOutputFolder={this.handleGoToOutputFolder}
                                         handleViewParams={this.handleViewParams}
                                         handleRelaunch={this.handleRelaunch}
                                         handleViewInfo={this.handleViewInfo}
                                         handleShare={this.handleShare}
                                         handleCancel={this.handleCancel}
                                         handleDelete={this.handleDelete}
                                         handleRename={this.handleRename}
                                         handleUpdateComments={this.handleUpdateComments}
                                         disabled={this.disabled}
                                         multiSelect={this.multiSelect}
                                         shouldDisableCancel={this.shouldDisableCancel}
                                         isOwner={this.isOwner}
                                         isSharable={this.isSharable}
                                         handleRefersh={this.fetchAnalyses}/>
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
                                    baseId="analysis"
                                    ids={ids}
                                    padding="none"
                                />
                                <TableBody>
                                    {data.map(n => {
                                        const id = n.id;
                                        const isSelected = this.isSelected(id);
                                        const username = n.username.includes(IPLANT) ?
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
                                                <TableCell padding="none">
                                                    <AnalysisName classes={classes}
                                                                  analysis={n}
                                                                  handleGoToOutputFolder={this.handleGoToOutputFolder}/>
                                                </TableCell>
                                                <TableCell className={classes.cellText}
                                                           padding="none">{username}</TableCell>
                                                <TableCell className={classes.cellText}
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
                                                <TableCell padding="none">
                                                    <Status analysis={n}
                                                            onClick={() => this.statusClick(n)}
                                                            user={user}/>
                                                </TableCell>
                                                <TableCell padding="none">
                                                    <DotMenu
                                                        handleGoToOutputFolder={this.handleGoToOutputFolder}
                                                        handleViewParams={this.handleViewParams}
                                                        handleRelaunch={this.handleRelaunch}
                                                        handleViewInfo={this.handleViewInfo}
                                                        handleShare={this.handleShare}
                                                        handleCancel={this.handleCancel}
                                                        handleDelete={this.handleDelete}
                                                        handleRename={this.handleRename}
                                                        handleUpdateComments={this.handleUpdateComments}
                                                        disabled={this.disabled}
                                                        multiSelect={this.multiSelect}
                                                        shouldDisableCancel={this.shouldDisableCancel}
                                                        isOwner={this.isOwner}
                                                        isSharable={this.isSharable}
                                                    />
                                                </TableCell>
                                            </TableRow>
                                        );
                                    })}
                                </TableBody>
                            </Table>
                        </div>
                        <div>
                            <TablePagination
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
                    </div>
                    <Prompt analysis={this.findAnalysis(selected[0])}
                            intl={intl}
                            renameDialogOpen={this.state.renameDialogOpen}
                            commentsDialogOpen={this.state.commentsDialogOpen}
                            onRenameOkClicked={this.doRename}
                            onRenameCancelClicked={() => this.setState({renameDialogOpen: false})}
                            onCommentsOkClicked={this.doComments}
                            onCommentsCancelClicked={() => this.setState({commentsDialogOpen: false})}
                    />
                    {selected[0] &&
                    <ShareWithSupportDialog dialogOpen={shareWithSupportDialogOpen}
                                            analysis={this.findAnalysis(selected[0])}
                                            name={name}
                                            email={email}
                                            onShareWithSupport={this.onShareWithSupport}/>
                    }
                </React.Fragment>
        );
    }
}

AnalysesView.propTypes = {};

export default withStyles(exStyles)(withI18N(injectIntl(AnalysesView), intlData));
