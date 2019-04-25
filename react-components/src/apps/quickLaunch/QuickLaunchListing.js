/**
 *
 * @author sriram
 *
 */

import React, { useState } from "react";
import Paper from "@material-ui/core/Paper";
import { LoadingMask, QuickLaunch } from "@cyverse-de/de-components";
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
import DEHyperLink from "../../util/hyperlink/DEHyperLink";

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
    const {
        quickLaunches,
        systemId,
        intl,
        userName,
        onDelete,
        useQuickLaunch,
        onCreate,
        onSelection,
        loading,
        selected,
    } = props;

    const [embedCode, setEmbedCode] = useState("");
    const [qLaunchUrl, setQLaunchUrl] = useState("");
    const [embedDialogOpen, setEmbedDialogOpen] = useState(false);
    const [shareDialogOpen, setShareDialogOpen] = useState(false);
    const [anchorEl, setAnchorEl] = useState("");
    const [deleteConfirmOpen, setDeleteConfirmOpen] = useState(false);

    const quickLaunchClickHandler = (event, qLaunch) => {
        onSelection(qLaunch);
        if (qLaunch.is_public) {
            setAnchorEl(event.currentTarget);
        } else {
            useQuickLaunch(qLaunch);
        }
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
            selected.id +
            "&" +
            constants.APP_ID +
            "=" +
            selected.app_id;
        return url;
    };

    const deleteQuickLaunchHandler = (event, qLaunch) => {
        onSelection(qLaunch);
        setDeleteConfirmOpen(true);
    };

    if (!quickLaunches || quickLaunches.length === 0) {
        if (systemId !== constants.AGAVE) {
            return (
                <React.Fragment>
                    <Typography variant="subtitle2">
                        {getMessage("noQuickLaunches")}
                    </Typography>
                    <DEHyperLink
                        text={getMessage("createQuickLaunchLabel")}
                        onClick={onCreate}
                    />
                </React.Fragment>
            );
        } else {
            return (
                <Typography variant="subtitle2">
                    {getMessage("quickLaunchNotSupportedMessage")}
                </Typography>
            );
        }
    } else {
        return (
            <React.Fragment>
                <Paper style={{ padding: 5 }}>
                    <LoadingMask loading={loading}>
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
                                            id={id}
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
                                                          onDelete(
                                                              event,
                                                              qLaunch
                                                          )
                                                    : undefined
                                            }
                                        />
                                        <ActionsPopper
                                            qLaunch={qLaunch}
                                            anchorEl={anchorEl}
                                            intl={intl}
                                            useQuickLaunchClickHandler={
                                                useQuickLaunch
                                            }
                                            embedCodeClickHandler={
                                                embedCodeClickHandler
                                            }
                                            shareClickHandler={
                                                shareClickHandler
                                            }
                                            onActionPopperClose={() =>
                                                setAnchorEl(null)
                                            }
                                        />
                                    </Grid>
                                );
                            })}
                        </Grid>
                    </LoadingMask>
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
                        onDelete(selected);
                    }}
                />
            </React.Fragment>
        );
    }
}

export default withI18N(injectIntl(ListQuickLaunches), intlData);
