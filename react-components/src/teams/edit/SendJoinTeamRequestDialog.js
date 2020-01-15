/**
 * @author aramsey
 *
 * A dialog for allowing users to submit a request to join a team
 */

import React, { useEffect, useState } from "react";

import ids from "../ids";
import messages from "../messages";
import styles from "../styles";

import {
    build,
    DEDialogHeader,
    getMessage,
    LoadingMask,
    withI18N,
} from "@cyverse-de/ui-lib";
import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    makeStyles,
    TextField,
} from "@material-ui/core";
import PropTypes from "prop-types";

function SendJoinTeamRequestDialog(props) {
    const { loading, open, onSendRequest, onClose, teamName } = props;
    const classes = makeStyles(styles);

    const [joinMessage, setJoinMessage] = useState("");
    useEffect(() => setJoinMessage(""), [open]);

    let parentId = ids.EDIT_TEAM.SEND_JOIN_REQUEST_DLG;

    return (
        <Dialog open={open} fullWidth={true} maxWidth="lg" id={parentId}>
            <DEDialogHeader
                messages={messages.messages}
                heading={getMessage("sendJoinRequest", {
                    values: { name: teamName },
                })}
                onClose={onClose}
            />
            <DialogContent>
                <LoadingMask loading={loading}>
                    <DialogContentText>
                        {getMessage("sendJoinRequestDetails", {
                            values: { name: teamName },
                        })}
                    </DialogContentText>
                    <div className={classes.requestMessage}>
                        <TextField
                            id={build(
                                parentId,
                                ids.EDIT_TEAM.SEND_JOIN_REQUEST_MSG
                            )}
                            label={getMessage("joinRequestMessage")}
                            multiline={true}
                            rows={3}
                            value={joinMessage}
                            fullWidth={true}
                            variant="outlined"
                            onChange={(event) =>
                                setJoinMessage(event.target.value)
                            }
                        />
                    </div>
                </LoadingMask>
            </DialogContent>
            <DialogActions>
                <Button
                    variant="contained"
                    id={build(parentId, ids.BUTTONS.CANCEL)}
                    onClick={onClose}
                >
                    {getMessage("cancel")}
                </Button>
                <Button
                    variant="contained"
                    id={build(parentId, ids.BUTTONS.SEND_JOIN_REQUEST)}
                    type="submit"
                    color="primary"
                    onClick={() => onSendRequest({ message: joinMessage })}
                >
                    {getMessage("sendRequestBtn")}
                </Button>
            </DialogActions>
        </Dialog>
    );
}

SendJoinTeamRequestDialog.propTypes = {
    loading: PropTypes.bool.isRequired,
    open: PropTypes.bool.isRequired,
    teamName: PropTypes.string.isRequired,
    presenter: PropTypes.shape({
        closeJoinTeamRequestDlg: PropTypes.func.isRequired,
        sendRequestToJoin: PropTypes.func.isRequired,
    }),
};

export default withI18N(SendJoinTeamRequestDialog, messages);
