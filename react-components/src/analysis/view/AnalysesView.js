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

const columnData = [
    {name: "Name", numeric: false, enableSorting: true},
    {name: "Owner", numeric: false, enableSorting: true},
    {name: "App", numeric: false, enableSorting: true},
    {name: "Start Date", numeric: false, enableSorting: true},
    {name: "End Date", numeric: false, enableSorting: true},
    {name: "Status", numeric: false, enableSorting: true},
    {name: "", numeric: false, enableSorting: false},
];

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
            orderBy: 'End Date',
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
            newSelected = newSelected.concat(selected, id);
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
                this.setState({selected: newSelected});
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
                        />
                        <TableBody>
                            {data.map(n => {
                                const id = n.id;
                                const isSelected = this.isSelected(id);
                                return (
                                    <TableRow onClick={event => this.handleRowClick(event, id)}
                                              role="checkbox"
                                              aria-checked={isSelected}
                                              tabIndex={-1}
                                              selected={isSelected}
                                              hover
                                              key={id}>
                                        <TableCell padding="checkbox">
                                            <Checkbox
                                                onClick={(event, n) => this.handleCheckBoxClick(event, id)}
                                                checked={isSelected}/>
                                        </TableCell>
                                        <TableCell>{n.name} </TableCell>
                                        <TableCell>{n.username} </TableCell>
                                        <TableCell>{n.app_name} </TableCell>
                                        <TableCell>{parseInt(n.startdate, 10) ? moment(parseInt(n.startdate, 10), "x").format(
                                            constants.DATE_FORMAT) :
                                            getMessage("emptyValue")} </TableCell>
                                        <TableCell>{parseInt(n.enddate, 10) ? moment(parseInt(n.enddate, 10), "x").format(
                                            constants.DATE_FORMAT) :
                                            getMessage("emptyValue")}</TableCell>
                                        <TableCell><DEHyperLink onClick={(n) => this.statusClick(n)}
                                                                text={n.status}/></TableCell>
                                        <TableCell>
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
