/**
 * A dialog that shows request details for joining a team with option to approve / deny request.
 *
 * @author Sriram
 *
 **/
import React, { Component } from "react";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import DialogTitle from "@material-ui/core/DialogTitle";
import Paper from "@material-ui/core/Paper";
import Grid from "@material-ui/core/Grid";
import { withStyles } from "@material-ui/core/styles";
import withI18N, { getMessage } from "../../../util/I18NWrapper";
import Button from "@material-ui/core/Button";
import intlData from "../../messages";
import Color from "../../../util/CyVersePalette";
import Typography from "@material-ui/core/Typography";
import Radio from "@material-ui/core/Radio";
import RadioGroup from "@material-ui/core/RadioGroup";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import FormControl from "@material-ui/core/FormControl";
import Card from "@material-ui/core/Card";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
import Select from "@material-ui/core/Select";
import MenuItem from "@material-ui/core/MenuItem";
import CardHeader from "@material-ui/core/CardHeader";
import TextField from "@material-ui/core/TextField";
import CircularProgress from "@material-ui/core/CircularProgress";
import privilegeType from "../../model/privilegeType";
import build from "../../../util/DebugIDUtil";
import ids from "../../ids";

const styles = theme => ({
    paper: {
        padding: theme.spacing.unit * 1,
        textAlign: 'inherit',
        color: theme.palette.text.secondary,
    },
    formControl: {
        margin: theme.spacing.unit * 1,
    },
    group: {
        margin: `${theme.spacing.unit}px 0`,
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
    const classes = props.classes;
    return (
        <div className={classes.root}>
            <Grid container spacing={12}>
                <Grid item xs={12}>
                    <Paper
                        className={classes.paper}>{getMessage("teamLabel")}: {team_name}</Paper>
                </Grid>
                <Grid item xs={12}>
                    <Paper
                        className={classes.paper}>{getMessage("name")}: {requester_name}</Paper>
                </Grid>
                <Grid item xs={12}>
                    <Paper
                        className={classes.paper}>{getMessage("email")}: {requester_email}</Paper>
                </Grid>
                <Grid item xs={12}>
                    <Paper
                        className={classes.paper}>{getMessage("message")}: {requester_message}</Paper>
                </Grid>
            </Grid>
        </div>
    );
}

function RequestOptions(props) {
    const {classes, action, onChange} = props;
    return (
        <FormControl component="fieldset" required className={classes.formControl}>
            <RadioGroup
                aria-label="action"
                name="action"
                className={classes.group}
                value={action}
                onChange={onChange}
            >
                <FormControlLabel id={build(ids.JOIN_REQUEST_DLG, ids.APPROVE_BTN)}
                                  value={APPROVE} control={<Radio/>}
                                  label={getMessage("approveBtnText")}/>
                <FormControlLabel id={build(ids.JOIN_REQUEST_DLG, ids.DENY_BTN)}
                                  value={DENY} control={<Radio/>}
                                  label={getMessage("denyBtnText")}/>
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
        privilege
    } = props;
    return (
        <div style={{display: action === APPROVE ? "block" : "none"}}>
            <Card className={classes.card} raised={true}>
                <CardHeader title={getMessage("setPrivilegesHeading")}/>
                <CardContent>
                    <Typography paragraph>
                        {getMessage("setPrivilegesText", {
                            values: {
                                name: requester_name,
                                team: team_name
                            }
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
                                    name: 'privilege',
                                    id: 'privilege-simple',
                                }}
                            >
                                <MenuItem value="admin">{privilegeType.admin}</MenuItem>
                                <MenuItem
                                    value="readOptin">{privilegeType.readOptin}</MenuItem>
                                <MenuItem value="read">{privilegeType.read}</MenuItem>
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
        team_name
    } = props;
    return (
        <div style={{display: action === DENY ? "block" : "none"}}>
            <Card className={classes.card} raised={true}>
                <CardHeader title={getMessage("denyRequestHeader")}/>
                <CardContent>
                    <Typography paragraph>
                        {getMessage("denyRequestMessage", {
                            values: {
                                name: requester_name,
                                team: team_name
                            }
                        })}
                    </Typography>
                </CardContent>
                <CardActions>
                    <TextField
                        InputLabelProps={{
                            shrink: true,
                        }}
                        placeholder={getMessage("message")}
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
            privilege:"read",
            message: "",
            loading: false,
        };
        this.handleCancelClick = this.handleCancelClick.bind(this);
        this.handleOkClick = this.handleOkClick.bind(this);
    }

    handleCancelClick() {
        this.setState({dialogOpen: false});
    }

    handleOkClick() {
        this.setState({loading: true});
        if (this.state.action === APPROVE) {
            this.props.presenter.addMemberWithPrivilege(this.state.privilege, (result) => {
                this.setState({dialogOpen: false});
            }, (errorCode, errorMessage) => {
                this.setState({
                    dialogOpen: false,
                    loading: false
                });
            });
        } else {
            this.props.presenter.denyRequest(this.state.message, (result) => {
                this.setState({dialogOpen: false});
            }, (errorCode, errorMessage) => {
                this.setState({
                    dialogOpen: false,
                    loading: false
                });
            });
        }
    }
    render() {
        const classes = this.props.classes;
        const {
            requester_name,
            team_name,
        } = this.props.request;
        const {dialogOpen} = this.state;
        return (
            <React.Fragment>
                <Dialog
                    id={ids.JOIN_REQUEST_DLG}
                    open={dialogOpen}
                    onClose={this.props.handleJoinTeamRequestDialogClose}
                >
                    <DialogTitle style={{backgroundColor: Color.blue}}>
                        <Typography
                            style={{color: Color.white}}>{getMessage("joinTeamRequestHeader")}
                        </Typography>
                    </DialogTitle>
                    <DialogContent>
                        <DialogContentText>
                            <Typography paragraph>
                                {getMessage("joinRequestIntro")}
                            </Typography>
                            {this.state.loading &&
                            <CircularProgress
                                size={30}
                                className={classes.loadingStyle}
                                thickness={7}/>
                            }
                            <TeamDetails {...this.props}/>
                        </DialogContentText>
                        <RequestOptions classes={classes}
                                        action={this.state.action}
                                        onChange={(e) => {
                                            this.setState({action: e.target.value})
                                        }}/>
                        <ApproveRequest classes={classes}
                                        action={this.state.action}
                                        requester_name={requester_name}
                                        team_name={team_name}
                                        privilege={this.state.privilege}
                                        onChange={(e) => {
                                            this.setState({privilege: e.target.value})
                                        }}/>
                        <DenyRequest classes={classes}
                                     action={this.state.action}
                                     requester_name={requester_name}
                                     team_name={team_name}
                                     onChange={(e) => {
                                         this.setState({message: e.target.value})
                                     }}/>
                    </DialogContent>
                    <DialogActions>
                        <Button id={build(ids.JOIN_REQUEST_DLG, ids.OK_BTN)}
                                onClick={this.handleOkClick}
                                color="primary">
                            {getMessage("okBtnText")}
                        </Button>
                        <Button id={build(ids.JOIN_REQUEST_DLG, ids.CANCEL_BTN)}
                                onClick={this.handleCancelClick}
                                color="primary"
                                autoFocus>
                            {getMessage("cancelBtnText")}
                        </Button>
                    </DialogActions>
                </Dialog>
            </React.Fragment>
        );
    }
}

export default withStyles(styles)(withI18N(JoinTeamRequestDialog, intlData));