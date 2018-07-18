/**
 *
 * @author Sriram
 *
 **/
import React, { Component } from "react";
import { withStyles } from "@material-ui/core/styles";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TablePagination from "@material-ui/core/TablePagination";
import TableRow from "@material-ui/core/TableRow";
import CircularProgress from "@material-ui/core/CircularProgress";
import moment from "moment";
import NotificationToolbar from "./NotificationToolbar";
import constants from "../../constants";
import withI18N, { getMessage } from "../../util/I18NWrapper";
import Checkbox from "@material-ui/core/Checkbox";
import EnhancedTableHead from "../../util/table/EnhancedTableHead";
import Color from "../../util/CyVersePalette";
import TablePaginationActions from "../../util/table/TablePaginationActions";
import exStyles from "../style";
import intlData from "../messages";

const columnData = [
    {name: "Category", numeric: false, enableSorting: false},
    {name: "Message", numeric: false, enableSorting: false},
    {name: "Date", numeric: false, enableSorting: true},
];


function Message(props) {
    const {message, seen, presenter} = props;
    if (seen) {
        return (
            <TableCell
                style={{textDecoration: 'underline', cursor: 'pointer',}}>
                <div
                    onClick={(event) => presenter.onMessageClicked(message)}> {message.text}</div>
            </TableCell>
        );
    } else {
        return (
            <TableCell
                style={{
                    textDecoration: 'underline',
                    cursor: 'pointer',
                    backgroundColor: Color.lightBlue
                }}>
                <div
                    onClick={(event) => presenter.onMessageClicked(message)}> {message.text}</div>
            </TableCell>
        );
    }
}

class NotificationView extends Component {
    constructor(props) {
        super(props);
        this.state = {
            data: [],
            loading: true,
            total: 0,
            offset: 0,
            page: 0,
            rowsPerPage: 100,
            selected: [],
            order: 'DESC',
            orderBy: 'Date',
            filter: 'All',
        };
        this.fetchNotifications = this.fetchNotifications.bind(this);
        this.handleRefreshClicked = this.handleRefreshClicked.bind(this);
        this.handleMarkSeenClick = this.handleMarkSeenClick.bind(this);
        this.handleDeleteClick = this.handleDeleteClick.bind(this);
        this.shouldDisableMarkSeen = this.shouldDisableMarkSeen.bind(this);
        this.findNotification = this.findNotification.bind(this);
    }

    componentDidMount() {
        this.fetchNotifications();
    }

    fetchNotifications() {
        const {rowsPerPage, offset, filter, order}  = this.state;
        this.setState({loading: true});
        this.props.presenter.getNotifications(rowsPerPage, offset, filter,order, (notifications, total) => {
                this.setState({
                    loading: false,
                    data: notifications.messages,
                    total: total,
                })
            }, (errorCode, errorMessage) => {
                this.setState({
                    loading: false,
                });
            },
        )
    }

    handleRefreshClicked() {
        this.fetchNotifications();
    }

    handleMarkSeenClick() {
        this.setState({loading: true});
        this.props.presenter.onNotificationToolbarMarkAsSeenClicked(this.state.selected, () => {
            this.state.selected.map(id => this.findNotification(id).seen = true);
            this.setState({loading: false});
        }, (errorCode, errorMessage) => {
            this.setState({
                loading: false,
            });
        });
    }

    handleDeleteClick() {
        const {rowsPerPage}  = this.state;
        this.setState({loading: true});
        this.props.presenter.deleteNotifications(this.state.selected, () => {
            this.setState({
                loading: false,
            });
            this.fetchNotifications();
        }, (errorCode, errorMessage) => {
            this.setState({
                loading: false,
            });
        });
    }

    handleChangePage = (event, page) => {
        const {rowsPerPage} = this.state;
        this.setState({page: page, offset: rowsPerPage * page}, () => this.fetchNotifications());
    };

    handleFilterChange = event => {
        this.setState({filter: event.target.value}, () => this.fetchNotifications());
    };


    handleChangeRowsPerPage = event => {
        this.setState({rowsPerPage: event.target.value}, () => this.fetchNotifications());
    };

    handleRowClick = (event, id) => {
        const {selected} = this.state;
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

        this.setState({selected: newSelected});
    };

    shouldDisableMarkSeen() {
        let notifs = [];
        this.state.selected.map(id => {
            let n = this.findNotification(id);
            if (n && (n.seen === false || n.seen == null)) {
                notifs.push(n);
            }
        });
        return notifs.length === 0;
    }

    findNotification(id) {
        return this.state.data.find(function (n) {
            return n.message.id === id;
        });
    }

    handleSelectAllClick = (event, checked) => {
        if (checked) {
            this.setState(state => ({selected: state.data.map(n => n.message.id)}));
            return;
        }
        this.setState({selected: []});
    };

    handleRequestSort = (event, property) => {
        const orderBy = property;
        let order = 'DESC';

        if (this.state.orderBy === property && this.state.order === 'DESC') {
            order = 'ASC';
        }

        this.setState({order, orderBy,}, () => this.fetchNotifications());
    };

    isSelected = id => this.state.selected.indexOf(id) !== -1;

    render() {
        const {classes} = this.props;
        const {data, rowsPerPage, page, order, orderBy, selected, total} = this.state;
        return (
            <div className={classes.container}>
                {this.state.loading &&
                <CircularProgress size={30} className={classes.loadingStyle} thickness={7}/>
                }
                <NotificationToolbar filter={this.state.filter}
                                     onFilterChange={this.handleFilterChange}
                                     onRefreshClicked={this.handleRefreshClicked}
                                     markSeenDisabled={this.state.selected.length === 0 || this.shouldDisableMarkSeen()}
                                     deleteDisabled={this.state.selected.length === 0}
                                     onMarkSeenClicked={this.handleMarkSeenClick}
                                     onDeleteClicked={this.handleDeleteClick}/>
                <div className={classes.table}>
                    <Table>
                        <EnhancedTableHead
                            numSelected={selected.length}
                            order={order}
                            orderBy={orderBy}
                            onSelectAllClick={this.handleSelectAllClick}
                            onRequestSort={this.handleRequestSort}
                            rowCount={total}
                            columnData={columnData}
                        />
                        <TableBody>
                            {data.map(n => {
                                const isSelected = this.isSelected(n.message.id);
                                return (
                                    <TableRow onClick={event => this.handleRowClick(event, n.message.id)}
                                              role="checkbox"
                                              aria-checked={isSelected}
                                              tabIndex={-1}
                                              selected={isSelected}
                                              hover
                                              key={n.message.id}>
                                        <TableCell padding="checkbox">
                                            <Checkbox checked={isSelected}/>
                                        </TableCell>
                                        <TableCell>{n.type}</TableCell>
                                        <Message message={n.message} seen={n.seen}
                                                 presenter={this.props.presenter}/>
                                        <TableCell>{(n.message.timestamp) ? moment(n.message.timestamp, "x").format(
                                                constants.DATE_FORMAT) :
                                            getMessage("emptyValue")} </TableCell>
                                    </TableRow>
                                );
                            })}
                        </TableBody>
                    </Table>
                </div>
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
        )
    }

}
export default withStyles(exStyles)(withI18N(NotificationView, intlData));