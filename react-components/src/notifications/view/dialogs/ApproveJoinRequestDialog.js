/**
 *
 * @author sriram
 *
 */
import React, { Component } from "react";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import DialogTitle from "@material-ui/core/DialogTitle";
import { withStyles } from "@material-ui/core/styles";
import withI18N, { getMessage } from "../../../util/I18NWrapper";
import Button from "@material-ui/core/Button";
import intlData from "../../messages";
import FormControl from "@material-ui/core/FormControl";
import Select from "@material-ui/core/Select";
import Color from "../../../util/CyVersePalette";
import Typography from "@material-ui/core/Typography";
import MenuItem from "@material-ui/core/MenuItem";
import InputLabel from "@material-ui/core/InputLabel";

const styles = theme => ({
    container: {
        display: 'flex',
        flexWrap: 'wrap',
    },
    formControl: {
        margin: theme.spacing.unit,
        minWidth: 120,
    },
});

class ApproveJoinRequestDialog extends Component {
    render() {
        const classes = this.props.classes;
        return (
            <Dialog
                open={this.props.open}
                onClose={this.props.handleApproveJoinRequestClose}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
                disableBackdropClick
                disableEscapeKeyDown
            >
                <DialogTitle style={{backgroundColor: Color.blue}}
                             id="alert-dialog-title">
                    <Typography
                        style={{color: Color.white}}> {getMessage("setPrivilegesHeading")}</Typography>
                </DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        {getMessage("setPrivilegesText", {
                            values: {
                                name: this.props.name,
                                team: this.props.team
                            }
                        })}
                        <form className={classes.container}>
                            <FormControl className={classes.formControl}>
                                <InputLabel htmlFor="filter-privilege">Privilege</InputLabel>
                                <Select
                                    value={this.props.privilege}
                                    onChange={this.props.handleChange}
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
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={this.handleClose} color="primary">
                        {getMessage("okBtnText")}
                    </Button>
                    <Button onClick={this.handleClose} color="primary" autoFocus>
                        {getMessage("cancelBtnText")}
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }

}
export default withStyles(styles)(withI18N(ApproveJoinRequestDialog, intlData));