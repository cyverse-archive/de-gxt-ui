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
import ToolbarSeparator from "@material-ui/core/Toolbar";
import Paper from "@material-ui/core/Paper";
import Grid from "@material-ui/core/Grid";
import { withStyles } from "@material-ui/core/styles";
import withI18N, { getMessage } from "../../../util/I18NWrapper";
import Button from "@material-ui/core/Button";
import intlData from "../../messages";
import Color from "../../../util/CyVersePalette";
import Typography from "@material-ui/core/Typography";

const styles = theme => ({
    root: {
        flexGrow: 1,
    },
    paper: {
        padding: theme.spacing.unit * 2,
        textAlign: 'inherit',
        color: theme.palette.text.secondary,
    },
});

class JoinTeamRequestDialog extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        const classes = this.props.classes;
        return (
            <Dialog
                open={this.props.open}
                onClose={this.props.handleJoinTeamRequestDialogClose}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle style={{backgroundColor: Color.blue}}
                             id="alert-dialog-title"><Typography style={{color: Color.white}}> {getMessage("joinTeamRequestHeader")}</Typography></DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        {getMessage("joinRequestIntro")}
                        <div className={classes.root}>
                            <Grid container spacing={24}>
                                <Grid item xs={12}>
                                    <Paper
                                        className={classes.paper}>{getMessage("team")}: {this.props.team}</Paper>
                                </Grid>
                                <Grid item xs={12}>
                                    <Paper
                                        className={classes.paper}>{getMessage("name")}: {this.props.name}</Paper>
                                </Grid>
                                <Grid item xs={12}>
                                    <Paper
                                        className={classes.paper}>{getMessage("email")}: {this.props.email}</Paper>
                                </Grid>
                                <Grid item xs={12}>
                                    <Paper
                                        className={classes.paper}>{getMessage("message")}: {this.props.message}</Paper>
                                </Grid>
                            </Grid>
                        </div>
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={this.handleClose} color="primary">
                        {getMessage("approveBtnText")}
                    </Button>
                    <Button onClick={this.handleClose} color="primary" autoFocus>
                        {getMessage("denyBtnText")}
                    </Button>
                    <ToolbarSeparator/>
                    <Button onClick={this.handleClose} color="primary" autoFocus>
                        {getMessage("cancelBtnText")}
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }
}

export default withStyles(styles)(withI18N(JoinTeamRequestDialog, intlData));