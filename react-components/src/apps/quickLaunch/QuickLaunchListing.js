/**
 *
 * @author sriram
 *
 */

import React, { useEffect, useState } from "react";
import Paper from "@material-ui/core/Paper";
import { QuickLaunch } from "@cyverse-de/de-components";
import Code from "@material-ui/icons/Code";
import Play from "@material-ui/icons/PlayArrow";
import Share from "@material-ui/icons/Share";

import IconButton from "@material-ui/core/IconButton";
import withI18N, { formatMessage, getMessage } from "../../util/I18NWrapper";
import { injectIntl } from "react-intl";
import intlData from "../messages";
import constants from "../../constants";
import DEDialogHeader from "../../util/dialog/DEDialogHeader";
import CopyTextArea from "../../util/CopyTextArea";
import Grid from "@material-ui/core/Grid";
import Tooltip from "@material-ui/core/Tooltip";
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";
import DEConfirmationDialog from "../../util/dialog/DEConfirmationDialog";
import Popover from "@material-ui/core/Popover";
import Typography from "@material-ui/core/Typography";

function ActionsPopper(props) {
    const {
        qLaunch,
        anchorEl,
        intl,
        useQuickLaunchClickHandler,
        embedCodeClickHandler,
        shareClickHandler,
        onActionPopperClose,
    } = props;
    return (
        <Popover
            open={Boolean(anchorEl)}
            anchorEl={anchorEl}
            onClose={onActionPopperClose}
            anchorOrigin={{
                vertical: "bottom",
                horizontal: "center",
            }}
            transformOrigin={{
                vertical: "top",
                horizontal: "center",
            }}
        >
            <Paper>
                <Tooltip title={formatMessage(intl, "qLaunchToolTip")}>
                    <IconButton
                        fontSize="small"
                        onClick={() => useQuickLaunchClickHandler(qLaunch)}
                    >
                        <Play color="primary" />
                    </IconButton>
                </Tooltip>
                <Tooltip title={formatMessage(intl, "qLaunchEmbedToolTip")}>
                    <IconButton
                        fontSize="small"
                        onClick={embedCodeClickHandler}
                    >
                        <Code color="primary" />
                    </IconButton>
                </Tooltip>
                <Tooltip title={formatMessage(intl, "qLaunchShareToolTip")}>
                    <IconButton fontSize="small" onClick={shareClickHandler}>
                        <Share color="primary" />
                    </IconButton>
                </Tooltip>
            </Paper>
        </Popover>
    );
}

function ListQuickLaunches(props) {
    const { presenter, appId, intl, userName, onQuickLaunch } = props;
    const [embedCode, setEmbedCode] = useState("");
    const [qLaunchUrl, setQLaunchUrl] = useState("");
    const [embedDialogOpen, setEmbedDialogOpen] = useState(false);
    const [shareDialogOpen, setShareDialogOpen] = useState(false);
    const [anchorEl, setAnchorEl] = useState("");
    const [deleteConfirmOpen, setDeleteConfirmOpen] = useState(false);
    const [selected, setSelected] = useState(null);
    const [quickLaunches, setQuickLaunches] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        function handleSuccess(qLaunches) {
            setLoading(false);
            setQuickLaunches(qLaunches);
        }

        function handleFailure(statusCode, message) {
            setLoading(false);
        }

        setLoading(true);
        presenter.getQuickLaunches(appId, handleSuccess, handleFailure);
    }, []);

    const quickLaunchClickHandler = (event, qLaunch) => {
        setSelected(qLaunch);
        if (qLaunch.is_public) {
            setAnchorEl(event.currentTarget);
        } else {
            useQuickLaunchClickHandler(qLaunch);
        }
    };

    const useQuickLaunchClickHandler = (qLaunch) => {
        console.log("Quick launch use with id: " + qLaunch.id);
        presenter.getAppInfoForQuickLaunch(qLaunch.id, qLaunch.app_id);
        onQuickLaunch();
    };

    const embedCodeClickHandler = () => {
        console.log("Embed code with id: " + selected.id);
        let img_src =
            "http//:" +
            window.location.host +
            "/" +
            constants.QUICK_LAUNCH_EMBED_ICON;
        const embed =
            "<a href='" +
            getShareUrl(selected.id) +
            "' target='_blank'><img src='" +
            img_src +
            "'></a>";

        setEmbedCode(embed);
        setEmbedDialogOpen(true);
    };

    const shareClickHandler = () => {
        console.log("Share code with id: " + selected.id);
        if (qLaunchUrl) {
            setShareDialogOpen(true);
        } else {
            setQLaunchUrl(getShareUrl(selected.id));
            setShareDialogOpen(true);
        }
    };

    const getShareUrl = () => {
        let host =
            window.location.protocol +
            "//" +
            window.location.host +
            window.location.pathname;
        const url =
            host +
            "?" +
            constants.TYPE +
            "=" +
            constants.QUICK_LAUNCH +
            "&" +
            constants.QUICK_LAUNCH_ID +
            "=" +
            selected.id;
        return url;
    };

    const deleteQuickLaunchHandler = (event, qLaunch) => {
        console.log("Delete quick launch with id: " + qLaunch.id);
        setSelected(qLaunch);
        setDeleteConfirmOpen(true);
    };

    const doDelete = () => {
        console.log("deleting quick launch with id: " + selected.id);
        presenter.deleteQuickLaunch(
            selected.id,
            () => {
                let pos = quickLaunches.indexOf(selected);
                if (pos !== -1) {
                    //must copy into new array for re-render
                    quickLaunches.splice(pos, 1);
                    setSelected(null);
                    const newQLaunches = [...quickLaunches];
                    setQuickLaunches(newQLaunches);
                }
            },
            (statusCode, errorMessage) => {}
        );
    };

    if (!quickLaunches || quickLaunches.length === 0) {
        return (
            <Typography variant="subtitle2" style={{ margin: 5 }}>
                {getMessage("noQuickLaunches")}
            </Typography>
        );
    } else {
        return (
            <React.Fragment>
                <Paper style={{ padding: 5 }}>
                    <Grid container spacing={24}>
                        {quickLaunches.map((qLaunch) => {
                            const id = qLaunch.id;
                            const is_public = qLaunch.is_public;
                            const onDelete =
                                userName === qLaunch.creator
                                    ? (event) =>
                                          deleteQuickLaunchHandler(
                                              event,
                                              qLaunch
                                          )
                                    : undefined;
                            return (
                                <Grid item key={id}>
                                    <QuickLaunch
                                        label={qLaunch.name}
                                        isPublic={is_public}
                                        handleClick={(event) =>
                                            quickLaunchClickHandler(
                                                event,
                                                qLaunch
                                            )
                                        }
                                        handleDelete={
                                            onDelete
                                                ? (event) =>
                                                      onDelete(event, qLaunch)
                                                : undefined
                                        }
                                    />
                                    <ActionsPopper
                                        qLaunch={qLaunch}
                                        anchorEl={anchorEl}
                                        intl={intl}
                                        useQuickLaunchClickHandler={
                                            useQuickLaunchClickHandler
                                        }
                                        embedCodeClickHandler={
                                            embedCodeClickHandler
                                        }
                                        shareClickHandler={shareClickHandler}
                                        onActionPopperClose={() =>
                                            setAnchorEl(null)
                                        }
                                    />
                                </Grid>
                            );
                        })}
                    </Grid>
                </Paper>
                <Dialog open={embedDialogOpen} maxWidth="sm" fullWidth={true}>
                    <DEDialogHeader
                        heading={formatMessage(intl, "embedLbl")}
                        onClose={() => setEmbedDialogOpen(false)}
                    />
                    <DialogContent>
                        <CopyTextArea text={embedCode} multiline={true} />
                    </DialogContent>
                </Dialog>
                <Dialog open={shareDialogOpen} maxWidth="sm" fullWidth={true}>
                    <DEDialogHeader
                        heading={formatMessage(intl, "shareLbl")}
                        onClose={() => setShareDialogOpen(false)}
                    />
                    <DialogContent>
                        <CopyTextArea text={qLaunchUrl} multiline={true} />
                    </DialogContent>
                </Dialog>
                <DEConfirmationDialog
                    heading={formatMessage(intl, "deleteLbl")}
                    okLabel={formatMessage(intl, "deleteLbl")}
                    message={formatMessage(
                        intl,
                        "quickLaunchDeleteConfirmation"
                    )}
                    dialogOpen={deleteConfirmOpen}
                    onCancelBtnClick={() => setDeleteConfirmOpen(false)}
                    onOkBtnClick={() => {
                        setDeleteConfirmOpen(false);
                        doDelete();
                    }}
                />
            </React.Fragment>
        );
    }
}

export default withI18N(injectIntl(ListQuickLaunches), intlData);
