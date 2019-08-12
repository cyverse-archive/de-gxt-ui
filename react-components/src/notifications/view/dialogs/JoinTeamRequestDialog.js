/**
 * A dialog that shows request details for joining a team with option to approve / deny request.
 *
 * @author Sriram
 *
 **/
import React, { Component } from "react";
import { injectIntl } from "react-intl";

import ids from "../../ids";
import intlData from "../../messages";
import privilegeType from "../../model/privilegeType";

import {
    build,
    formatMessage,
    getMessage,
    LoadingMask,
    palette,
    withI18N,
} from "@cyverse-de/ui-lib";

import {
    Button,
    Card,
    CardActions,
    CardContent,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    Grid,
    FormControlLabel,
    FormControl,
    Select,
    MenuItem,
    Radio,
    RadioGroup,
    TextField,
    Typography,
    withStyles,
} from "@material-ui/core";

const styles = (theme) => ({
    formControl: {
        margin: theme.spacing.unit * 1,
    },
    group: {
        margin: `${theme.spacing.unit}px 0`,
    },
    grid: {
        border: "1px solid",
        margin: 1,
    },
});

const APPROVE = "approve";
const DENY = "deny";

function TeamDetails(props) {
    const {
        requester_name,
        team_name,
        requester_email,
        requester_message,
    } = props.request;
    const { intl, classes } = props;
    return (
        <Grid container component={"span"} spacing={8} className={classes.grid}>
            <Grid component={"span"} item xs={12}>
                {formatMessage(intl, "teamLabel")}: {team_name}
            </Grid>
            <Grid component={"span"} item xs={12}>
                {formatMessage(intl, "name")}: {requester_name}
            </Grid>
            <Grid component={"span"} item xs={12}>
                {formatMessage(intl, "email")}: {requester_email}
            </Grid>
            <Grid component={"span"} item xs={12}>
                {formatMessage(intl, "message")}: {requester_message}
            </Grid>
        </Grid>
    );
}

function RequestOptions(props) {
    const { classes, action, onChange } = props;
    return (
        <FormControl
            component="fieldset"
            required
            className={classes.formControl}
        >
            <RadioGroup
                aria-label="action"
                name="action"
                className={classes.group}
                value={action}
                onChange={onChange}
            >
                <FormControlLabel
                    id={build(ids.JOIN_REQUEST_DLG, ids.APPROVE_BTN)}
                    value={APPROVE}
                    control={<Radio />}
                    label={getMessage("approveBtnText")}
                />
                <FormControlLabel
                    id={build(ids.JOIN_REQUEST_DLG, ids.DENY_BTN)}
                    value={DENY}
                    control={<Radio />}
                    label={getMessage("denyBtnText")}
                />
            </RadioGroup>
        </FormControl>
    );
}

function ApproveRequest(props) {
    const {
        classes,
        action,
        onChange,
        requester_name,
        team_name,
        privilege,
    } = props;
    return (
        <div style={{ display: action === APPROVE ? "block" : "none" }}>
            <Card className={classes.card}>
                <Typography component={"span"} variant="h6">
                    {getMessage("setPrivilegesHeading")}
                </Typography>
                <CardContent>
                    <Typography component={"span"}>
                        {getMessage("setPrivilegesText", {
                            values: {
                                name: requester_name,
                                team: team_name,
                            },
                        })}
                    </Typography>
                </CardContent>
                <CardActions>
                    <form className={classes.container}>
                        <FormControl className={classes.formControl}>
                            <Select
                                value={privilege}
                                onChange={onChange}
                                inputProps={{
                                    name: "privilege",
                                    id: "privilege-simple",
                                }}
                            >
                                <MenuItem value="admin">
                                    {privilegeType.admin}
                                </MenuItem>
                                <MenuItem value="readOptin">
                                    {privilegeType.readOptin}
                                </MenuItem>
                                <MenuItem value="read">
                                    {privilegeType.read}
                                </MenuItem>
                            </Select>
                        </FormControl>
                    </form>
                </CardActions>
            </Card>
        </div>
    );
}

function DenyRequest(props) {
    const {
        classes,
        action,
        onChange,
        requester_name,
        team_name,
        intl,
    } = props;
    return (
        <div style={{ display: action === DENY ? "block" : "none" }}>
            <Card className={classes.card}>
                <Typography component={"span"} variant="h6">
                    {getMessage("denyRequestHeader")}
                </Typography>
                <CardContent>
                    <Typography component={"span"}>
                        {getMessage("denyRequestMessage", {
                            values: {
                                name: requester_name,
                                team: team_name,
                            },
                        })}
                    </Typography>
                </CardContent>
                <CardActions>
                    <TextField
                        InputLabelProps={{
                            shrink: true,
                        }}
                        placeholder={formatMessage(intl, "message")}
                        fullWidth
                        margin="normal"
                        onChange={onChange}
                    />
                </CardActions>
            </Card>
        </div>
    );
}

class JoinTeamRequestDialog extends Component {
    constructor(props) {
        super(props);
        this.state = {
            dialogOpen: props.dialogOpen,
            action: APPROVE,
            privilege: "read",
            message: "",
            loading: false,
        };
        this.handleCancelClick = this.handleCancelClick.bind(this);
        this.handleOkClick = this.handleOkClick.bind(this);
    }

    handleCancelClick() {
        this.setState({ dialogOpen: false });
    }

    handleOkClick() {
        this.setState({ loading: true });
        if (this.state.action === APPROVE) {
            this.props.presenter.addMemberWithPrivilege(
                this.state.privilege,
                (result) => {
                    this.setState({ dialogOpen: false });
                },
                (errorCode, errorMessage) => {
                    this.setState({
                        dialogOpen: false,
                        loading: false,
                    });
                }
            );
        } else {
            this.props.presenter.denyRequest(
                this.state.message,
                (result) => {
                    this.setState({ dialogOpen: false });
                },
                (errorCode, errorMessage) => {
                    this.setState({
                        dialogOpen: false,
                        loading: false,
                    });
                }
            );
        }
    }
    render() {
        const classes = this.props.classes;
        const { requester_name, team_name } = this.props.request;
        const { dialogOpen, loading } = this.state;
        const { intl } = this.props;
        return (
            <React.Fragment>
                <Dialog
                    id={ids.JOIN_REQUEST_DLG}
                    open={dialogOpen}
                    onClose={this.props.handleJoinTeamRequestDialogClose}
                >
                    <LoadingMask loading={loading}>
                        <DialogTitle style={{ backgroundColor: palette.blue }}>
                            <Typography style={{ color: palette.white }}>
                                {formatMessage(intl, "joinTeamRequestHeader")}
                            </Typography>
                        </DialogTitle>
                        <DialogContent>
                            <DialogContentText>
                                <Typography component={"span"}>
                                    {formatMessage(intl, "joinRequestIntro")}
                                </Typography>
                                <TeamDetails {...this.props} />
                            </DialogContentText>
                            <RequestOptions
                                classes={classes}
                                action={this.state.action}
                                onChange={(e) => {
                                    this.setState({ action: e.target.value });
                                }}
                            />
                            <ApproveRequest
                                classes={classes}
                                action={this.state.action}
                                requester_name={requester_name}
                                team_name={team_name}
                                privilege={this.state.privilege}
                                onChange={(e) => {
                                    this.setState({
                                        privilege: e.target.value,
                                    });
                                }}
                            />
                            <DenyRequest
                                classes={classes}
                                intl={intl}
                                action={this.state.action}
                                requester_name={requester_name}
                                team_name={team_name}
                                onChange={(e) => {
                                    this.setState({ message: e.target.value });
                                }}
                            />
                        </DialogContent>
                        <DialogActions>
                            <Button
                                id={build(ids.JOIN_REQUEST_DLG, ids.OK_BTN)}
                                onClick={this.handleOkClick}
                                color="primary"
                                variant="contained"
                            >
                                {getMessage("okBtnText")}
                            </Button>
                            <Button
                                id={build(ids.JOIN_REQUEST_DLG, ids.CANCEL_BTN)}
                                onClick={this.handleCancelClick}
                                color="primary"
                                autoFocus
                            >
                                {getMessage("cancelBtnText")}
                            </Button>
                        </DialogActions>
                    </LoadingMask>
                </Dialog>
            </React.Fragment>
        );
    }
}

export default withStyles(styles)(
    withI18N(injectIntl(JoinTeamRequestDialog), intlData)
);
