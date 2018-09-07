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
import withI18N, { getMessage } from "../../util/I18NWrapper";
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

function AnalysisName(props) {
    let name = props.analysis.name;
    let isBatch = props.analysis.batch;
    let className = props.classes.analysisName;

    if (isBatch) {
        return (
            <span className={className}>
                <ListAltIcon style={{color: Color.darkGreen}}/><sup>{name}</sup>
            </span>);
    } else {
        return (
            <span className={className}>
                {name}
            </span>
        );
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
            total: this.props.analysesList ? this.props.analysesList.total : 0,
            offset: 0,
            page: 0,
            rowsPerPage: 100,
            selected: [],
            order: 'desc',
            orderBy: 'enddate',
            permFilter: permission.all,
            typeFilter: appType.all,
            parentId: "",
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

    }

    handleRowClick(event, id) {
        const {selected} = this.state;
        const selectedIndex = selected.indexOf(id);
        let newSelected = [];

        if (selectedIndex === -1) {
            newSelected.push(id);
            this.setState({selected: newSelected});
        }
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
    }

    handleGoToOutputFolder() {

    }

    handleViewParams() {

    }

    handleRelaunch() {

    }

    handleViewInfo() {

    }

    handleShare() {

    }

    handleCancel() {

    }

    handleDelete() {

    }

    handleRename() {

    }

    handleUpdateComments() {

    }


    multiSelect = (selection) => selection && (selection.length > 1) ? true : false;

    disabled = (selection) => selection && (selection.length > 0) ? false : true;

    isOwner(selection) {
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

    isSharable(selection) {
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


    shouldDisableCancel(selection) {
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

    render() {
        const {classes} = this.props;
        const {
            rowsPerPage,
            page,
            order,
            orderBy,
            selected,
            total,
            data,
        } = this.state;
            return (
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
                                     isSharable={this.isSharable}/>
                <div className={classes.table}>
                    <Table style={{padding: 0}}>
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
                        />
                        <TableBody>
                            {data.map(n => {
                                const id = n.id;
                                const isSelected = this.isSelected(id);
                                const username = n.username.includes(IPLANT) ? n.username.split('@')[0] : n.username;
                                return (
                                    <TableRow onClick={event => this.handleRowClick(event, id)}
                                              role="checkbox"
                                              aria-checked={isSelected}
                                              tabIndex={-1}
                                              selected={isSelected}
                                              hover
                                              key={id}
                                    >
                                        <TableCell padding="none">
                                            <Checkbox
                                                onClick={(event, n) => this.handleCheckBoxClick(event, id)}
                                                checked={isSelected}/>
                                        </TableCell>
                                        <TableCell padding="none">
                                            <AnalysisName classes={classes} analysis={n}/>
                                        </TableCell>
                                        <TableCell padding="default">{username}</TableCell>
                                        <TableCell padding="none">{n.app_name} </TableCell>
                                        <TableCell padding="none">
                                            {parseInt(n.startdate, 10) ? moment(parseInt(n.startdate, 10), "x").format(
                                                constants.LONG_DATE_FORMAT) :
                                                getMessage("emptyValue")}
                                        </TableCell>
                                        <TableCell padding="none">
                                            {parseInt(n.enddate, 10) ? moment(parseInt(n.enddate, 10), "x").format(
                                                constants.LONG_DATE_FORMAT) :
                                                getMessage("emptyValue")}
                                        </TableCell>
                                        <TableCell padding="default"><DEHyperLink
                                            onClick={(n) => this.statusClick(n)}
                                            text={n.status}/></TableCell>
                                        <TableCell padding="none">
                                            <DotMenu
                                                selected={selected}
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
        );
    }
}

AnalysesView.propTypes = {};

export default withStyles(exStyles)(withI18N(AnalysesView, intlData));
