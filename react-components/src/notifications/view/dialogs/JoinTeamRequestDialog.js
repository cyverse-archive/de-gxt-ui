/**
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
        const {requester_name, team_name, requester_email, requester_message} = this.props.request;
        const {dialogOpen} = this.state;
        return (
            <React.Fragment>
                <Dialog
                    open={dialogOpen}
                    onClose={this.props.handleJoinTeamRequestDialogClose}
                    aria-labelledby="alert-dialog-title"
                    aria-describedby="alert-dialog-description"
                >
                    <DialogTitle style={{backgroundColor: Color.blue}}
                                 id="alert-dialog-title"><Typography
                        style={{color: Color.white}}> {getMessage("joinTeamRequestHeader")}</Typography></DialogTitle>
                    <DialogContent>
                        <DialogContentText id="alert-dialog-description">
                            <Typography paragraph>
                                {getMessage("joinRequestIntro")}
                            </Typography>
                            {this.state.loading &&
                            <CircularProgress size={30} className={classes.loadingStyle} thickness={7}/>
                            }
                            <div className={classes.root}>
                                <Grid container spacing={12}>
                                    <Grid item xs={12}>
                                        <Paper
                                            className={classes.paper}>{getMessage("team")}: {team_name}</Paper>
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
                        </DialogContentText>
                        <FormControl component="fieldset" required className={classes.formControl}>
                            <RadioGroup
                                aria-label="action"
                                name="action"
                                className={classes.group}
                                value={this.state.action}
                                onChange={(e) => {
                                    this.setState({action: e.target.value})
                                }}
                            >
                                <FormControlLabel value={APPROVE} control={<Radio />}
                                                  label={getMessage("approveBtnText")}/>
                                <FormControlLabel value={DENY} control={<Radio />}
                                                  label={getMessage("denyBtnText")}/>
                            </RadioGroup>
                        </FormControl>
                        <div style={{display: this.state.action === APPROVE ? "block" : "none"}}>
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
                                                value={this.state.privilege}
                                                onChange={(e) => {
                                                    this.setState({privilege: e.target.value})
                                                }}
                                                inputProps={{
                                                    name: 'privilege',
                                                    id: 'privilege-simple',
                                                }}
                                            >
                                                <MenuItem value="admin">{getMessage("admin")}</MenuItem>
                                                <MenuItem
                                                    value="readOptin">{getMessage("readOptin")}</MenuItem>
                                                <MenuItem value="read">{getMessage("read")}</MenuItem>
                                            </Select>
                                        </FormControl>
                                    </form>
                                </CardActions>
                            </Card>
                        </div>
                        <div style={{display: this.state.action === DENY ? "block" : "none"}}>
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
                                        id="full-width"
                                        InputLabelProps={{
                                            shrink: true,
                                        }}
                                        placeholder="Message"
                                        fullWidth
                                        margin="normal"
                                        onChange={(e) => {
                                            this.setState({message: e.target.value})
                                        }}
                                    />
                                </CardActions>
                            </Card>
                        </div>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={this.handleOkClick} color="primary" autoFocus>
                            {getMessage("okBtnText")}
                        </Button>
                        <Button onClick={this.handleCancelClick} color="primary" autoFocus>
                            {getMessage("cancelBtnText")}
                        </Button>
                    </DialogActions>
                </Dialog>
            </React.Fragment>
        );
    }
}

export default withStyles(styles)(withI18N(JoinTeamRequestDialog, intlData));