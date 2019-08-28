/**
 * @author aramsey
 * A component for showing the teams a user has access to view
 */
import React, { Fragment, useEffect, useState } from "react";

import ids from "./ids";
import messages from "./messages";
import styles from "./styles";

import {
    build,
    DEHyperlink,
    EmptyTable,
    EnhancedTableHead,
    formatMessage,
    getMessage,
    getSorting,
    LoadingMask,
    SearchField,
    stableSort,
    withI18N,
} from "@cyverse-de/ui-lib";
import {
    Button,
    MenuItem,
    Select,
    Table,
    TableBody,
    TableCell,
    TableRow,
    Toolbar,
    withStyles,
} from "@material-ui/core";
import { Add } from "@material-ui/icons";
import PropTypes from "prop-types";
import { injectIntl } from "react-intl";
import Checkbox from "@material-ui/core/Checkbox";

const TEAM_FILTER = {
    MY_TEAMS: "MY_TEAMS",
    ALL_TEAMS: "ALL",
};

function Teams(props) {
    const {
        parentId,
        presenter,
        loading,
        teamListing,
        collaboratorsUtil,
        isSelectable,
        selectedTeams,
        intl,
    } = props;

    const [teamFilter, setTeamFilter] = useState(TEAM_FILTER.MY_TEAMS);
    const [searchTerm, setSearchTerm] = useState("");
    const [sortConfig, setSortConfig] = useState({
        order: "asc",
        orderBy: "name",
    });

    useEffect(() => {
        presenter.getMyTeams();
    }, []);

    const handleTeamFilterChange = (value) => {
        setSearchTerm("");
        setTeamFilter(value);
        if (value === TEAM_FILTER.MY_TEAMS) {
            presenter.getMyTeams();
        } else {
            presenter.getAllTeams();
        }
    };

    const handleSearch = (searchTerm) => {
        setTeamFilter("");
        setSearchTerm(searchTerm);
        presenter.searchTeams(searchTerm);
    };

    const onRequestSort = (event, property) => {
        const orderBy = property;
        let order = "desc";

        if (sortConfig.orderBy === property && sortConfig.order === "desc") {
            order = "asc";
        }

        setSortConfig({
            order: order,
            orderBy: orderBy,
        });
    };

    // copied from MUI tables example
    const handleTeamSelectionChange = (selectedTeam) => {
        const selectedTeamIdsIndex = selectedTeams.indexOf(selectedTeam);
        let newSelectedTeams = [];

        if (selectedTeamIdsIndex === -1) {
            newSelectedTeams = newSelectedTeams.concat(
                selectedTeams,
                selectedTeam
            );
        } else if (selectedTeamIdsIndex === 0) {
            newSelectedTeams = newSelectedTeams.concat(selectedTeams.slice(1));
        } else if (selectedTeamIdsIndex === selectedTeams.length - 1) {
            newSelectedTeams = newSelectedTeams.concat(
                selectedTeams.slice(0, -1)
            );
        } else if (selectedTeamIdsIndex > 0) {
            newSelectedTeams = newSelectedTeams.concat(
                selectedTeam.slice(0, selectedTeamIdsIndex),
                selectedTeam.slice(selectedTeamIdsIndex + 1)
            );
        }

        presenter.onTeamSelectionChanged(newSelectedTeams);
    };

    const onSelectAllClick = (checked) => {
        if (checked) {
            presenter.onTeamSelectionChanged(teamListing);
            return;
        }
        presenter.onTeamSelectionChanged([]);
    };

    return (
        <Fragment>
            <StyledToolbar
                parentId={build(parentId, ids.TEAMS.TOOLBAR)}
                presenter={presenter}
                teamFilter={teamFilter}
                handleTeamFilterChange={handleTeamFilterChange}
                searchTerm={searchTerm}
                handleSearch={handleSearch}
                intl={intl}
            />
            <StyledTeamListing
                parentId={parentId}
                loading={loading}
                presenter={presenter}
                collaboratorsUtil={collaboratorsUtil}
                teamListing={teamListing}
                sortConfig={sortConfig}
                onRequestSort={onRequestSort}
                isSelectable={isSelectable}
                selectedTeams={selectedTeams}
                handleTeamSelectionChange={handleTeamSelectionChange}
                onSelectAllClick={onSelectAllClick}
            />
        </Fragment>
    );
}

const StyledToolbar = withStyles(styles)(TeamsToolbar);

function TeamsToolbar(props) {
    const {
        parentId,
        presenter,
        teamFilter,
        handleTeamFilterChange,
        searchTerm,
        handleSearch,
        intl,
        classes,
    } = props;

    let teamFilterId = build(parentId, ids.TEAMS.TEAM_FILTER);

    return (
        <Toolbar id={parentId} className={classes.toolbar}>
            <Button
                variant="contained"
                id={build(parentId, ids.TEAMS.CREATE_BTN)}
                onClick={() => presenter.onCreateTeamSelected()}
            >
                <Add />
                {getMessage("createTeam")}
            </Button>
            <Select
                value={teamFilter}
                onChange={(event) => {
                    handleTeamFilterChange(event.target.value);
                }}
                id={teamFilterId}
                className={classes.teamFilter}
            >
                <MenuItem
                    value={TEAM_FILTER.MY_TEAMS}
                    id={build(teamFilterId, ids.TEAMS.MY_TEAMS_MI)}
                >
                    {getMessage("myTeams")}
                </MenuItem>
                <MenuItem
                    value={TEAM_FILTER.ALL_TEAMS}
                    id={build(teamFilterId, ids.TEAMS.ALL_TEAMS_MI)}
                >
                    {getMessage("allTeams")}
                </MenuItem>
            </Select>
            <SearchField
                handleSearch={handleSearch}
                value={searchTerm}
                id={build(parentId, ids.TEAMS.SEARCH)}
                placeholder={formatMessage(intl, "searchTeams")}
            />
        </Toolbar>
    );
}

const TABLE_COLUMNS = [
    { name: "Name", numeric: false, enableSorting: true },
    {
        name: "Creator Name",
        numeric: false,
        enableSorting: false,
        key: "creator",
    },
    { name: "Description", numeric: false, enableSorting: false },
];

const StyledTeamListing = withStyles(styles)(TeamsListing);

function TeamsListing(props) {
    const {
        parentId,
        loading,
        presenter,
        collaboratorsUtil,
        teamListing,
        sortConfig,
        onRequestSort,
        isSelectable,
        selectedTeams,
        handleTeamSelectionChange,
        onSelectAllClick,
        classes,
    } = props;

    return (
        <div className={classes.table}>
            <LoadingMask loading={loading}>
                <Table>
                    <TableBody>
                        {(!teamListing || teamListing.length === 0) && (
                            <EmptyTable
                                message={getMessage("noTeams")}
                                numColumns={TABLE_COLUMNS.length}
                            />
                        )}
                        {teamListing &&
                            teamListing.length > 0 &&
                            stableSort(
                                teamListing,
                                getSorting(sortConfig.order, sortConfig.orderBy)
                            ).map((team) => {
                                const isSelected =
                                    isSelectable &&
                                    selectedTeams.includes(team);

                                return (
                                    <TableRow
                                        role="checkbox"
                                        tabIndex={-1}
                                        hover
                                        key={team.id}
                                        selected={isSelected}
                                        onClick={() =>
                                            handleTeamSelectionChange(team)
                                        }
                                    >
                                        {isSelectable && (
                                            <TableCell padding="checkbox">
                                                <Checkbox
                                                    checked={isSelected}
                                                />
                                            </TableCell>
                                        )}
                                        <TableCell>
                                            <DEHyperlink
                                                onClick={() =>
                                                    presenter.onTeamNameSelected(
                                                        team
                                                    )
                                                }
                                                text={collaboratorsUtil.getGroupShortName(
                                                    team
                                                )}
                                            />
                                        </TableCell>
                                        <TableCell>{team.creator}</TableCell>
                                        <TableCell>
                                            {team.description}
                                        </TableCell>
                                    </TableRow>
                                );
                            })}
                    </TableBody>
                    <EnhancedTableHead
                        selectable={isSelectable}
                        numSelected={selectedTeams.length}
                        onSelectAllClick={(event, checked) => {
                            onSelectAllClick(checked);
                        }}
                        rowsInPage={teamListing ? teamListing.length : 0}
                        order={sortConfig.order}
                        orderBy={sortConfig.orderBy}
                        baseId={parentId}
                        columnData={TABLE_COLUMNS}
                        onRequestSort={onRequestSort}
                    />
                </Table>
            </LoadingMask>
        </div>
    );
}

Teams.propTypes = {
    parentId: PropTypes.string.isRequired,
    presenter: PropTypes.shape({
        getMyTeams: PropTypes.func.isRequired,
        getAllTeams: PropTypes.func.isRequired,
        onCreateTeamSelected: PropTypes.func.isRequired,
        searchTeams: PropTypes.func.isRequired,
        onTeamNameSelected: PropTypes.func.isRequired,
        onTeamSelectionChanged: PropTypes.func.isRequired,
    }),
    loading: PropTypes.bool.isRequired,
    teamListing: PropTypes.array.isRequired,
    collaboratorsUtil: PropTypes.shape({
        getGroupShortName: PropTypes.func.isRequired,
    }),
    isSelectable: PropTypes.bool.isRequired,
};

export default withI18N(injectIntl(Teams), messages);
