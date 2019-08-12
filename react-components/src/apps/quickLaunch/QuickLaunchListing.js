/**
 *
 * @author sriram
 *
 */

import React, { useState } from "react";
import { injectIntl } from "react-intl";
import PropTypes from "prop-types";

import constants from "../../constants";
import ids from "../ids";
import intlData from "../messages";

import {
    build,
    CopyTextArea,
    DEConfirmationDialog,
    DEDialogHeader,
    DEHyperlink,
    formatMessage,
    getMessage,
    LoadingMask,
    QuickLaunch,
    withI18N,
} from "@cyverse-de/ui-lib";

import Code from "@material-ui/icons/Code";
import Play from "@material-ui/icons/PlayArrow";
import Share from "@material-ui/icons/Share";

import {
    Dialog,
    DialogContent,
    Grid,
    IconButton,
    Paper,
    Popover,
    Tooltip,
    Typography,
} from "@material-ui/core";

function ActionsPopper(props) {
    const {
        anchorEl,
        intl,
        useQuickLaunchClickHandler,
        embedCodeClickHandler,
        shareClickHandler,
        onActionPopperClose,
        baseDebugId,
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
                        id={build(baseDebugId, ids.QUICK_LAUNCH.useQuickLaunch)}
                        fontSize="small"
                        onClick={useQuickLaunchClickHandler}
                    >
                        <Play color="primary" />
                    </IconButton>
                </Tooltip>
                <Tooltip title={formatMessage(intl, "qLaunchEmbedToolTip")}>
                    <IconButton
                        id={build(
                            baseDebugId,
                            ids.QUICK_LAUNCH.embedQuickLaunch
                        )}
                        fontSize="small"
                        onClick={embedCodeClickHandler}
                    >
                        <Code color="primary" />
                    </IconButton>
                </Tooltip>
                <Tooltip title={formatMessage(intl, "qLaunchShareToolTip")}>
                    <IconButton
                        id={build(
                            baseDebugId,
                            ids.QUICK_LAUNCH.shareQuickLaunch
                        )}
                        fontSize="small"
                        onClick={shareClickHandler}
                    >
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
        baseDebugId,
    } = props;

    const [embedCode, setEmbedCode] = useState("");
    const [qLaunchUrl, setQLaunchUrl] = useState("");
    const [embedDialogOpen, setEmbedDialogOpen] = useState(false);
    const [shareDialogOpen, setShareDialogOpen] = useState(false);
    const [anchorEl, setAnchorEl] = useState(null);
    const [deleteConfirmOpen, setDeleteConfirmOpen] = useState(false);

    const quickLaunchClickHandler = (event, qLaunch) => {
        onSelection(qLaunch);
        if (qLaunch.is_public) {
            setAnchorEl(event.currentTarget);
        } else {
            useQuickLaunch(qLaunch);
        }
    };

    const useQuickLaunchClickHandler = () => {
        useQuickLaunch(selected);
    };

    const embedCodeClickHandler = () => {
        const shareUrl = getShareUrl(selected.id);
        const host = window.location.host;
        const imgSrc = `https://${host}/${constants.QUICK_LAUNCH_EMBED_ICON}`;

        const embed = `<a href="${shareUrl}" target="_blank"><img src="${imgSrc}"></a>`;

        setEmbedCode(embed);
        setEmbedDialogOpen(true);
    };

    const shareClickHandler = () => {
        setQLaunchUrl(getShareUrl(selected.id));
        setShareDialogOpen(true);
    };

    const getShareUrl = () => {
        const host =
            window.location.protocol +
            "//" +
            window.location.host +
            window.location.pathname;
        const url = new URL(host);

        url.searchParams.set(constants.TYPE, constants.QUICK_LAUNCH);
        url.searchParams.set(constants.QUICK_LAUNCH_ID, selected.id);
        url.searchParams.set(constants.APP_ID, selected.app_id);

        return url.toString();
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
                    <DEHyperlink
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
                <Paper style={{ padding: 5 }} id={baseDebugId}>
                    <LoadingMask loading={loading}>
                        <Grid container spacing={24}>
                            {quickLaunches.map((qLaunch, index) => {
                                const id = build(baseDebugId, qLaunch.id);
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
                                    <Grid item key={index}>
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
                                    </Grid>
                                );
                            })}
                        </Grid>
                    </LoadingMask>
                </Paper>
                <ActionsPopper
                    anchorEl={anchorEl}
                    intl={intl}
                    baseDebugId={baseDebugId}
                    useQuickLaunchClickHandler={useQuickLaunchClickHandler}
                    embedCodeClickHandler={embedCodeClickHandler}
                    shareClickHandler={shareClickHandler}
                    onActionPopperClose={() => setAnchorEl(null)}
                />
                <Dialog open={embedDialogOpen} maxWidth="sm" fullWidth={true}>
                    <DEDialogHeader
                        heading={formatMessage(intl, "embedLbl")}
                        onClose={() => setEmbedDialogOpen(false)}
                    />
                    <DialogContent>
                        <CopyTextArea
                            debugIdPrefix={build(
                                baseDebugId,
                                ids.QUICK_LAUNCH.embedQuickLaunch
                            )}
                            text={embedCode}
                            multiline={true}
                        />
                    </DialogContent>
                </Dialog>
                <Dialog open={shareDialogOpen} maxWidth="sm" fullWidth={true}>
                    <DEDialogHeader
                        heading={formatMessage(intl, "shareLbl")}
                        onClose={() => setShareDialogOpen(false)}
                    />
                    <DialogContent>
                        <CopyTextArea
                            debugIdPrefix={build(
                                baseDebugId,
                                ids.QUICK_LAUNCH.shareQuickLaunch
                            )}
                            text={qLaunchUrl}
                            multiline={true}
                        />
                    </DialogContent>
                </Dialog>
                <DEConfirmationDialog
                    debugId={baseDebugId}
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

ListQuickLaunches.propTypes = {
    quickLaunches: PropTypes.array.isRequired,
    systemId: PropTypes.string.isRequired,
    userName: PropTypes.string.isRequired,
    onDelete: PropTypes.func,
    useQuickLaunch: PropTypes.func.isRequired,
    onCreate: PropTypes.func.isRequired,
    onSelection: PropTypes.func.isRequired,
    loading: PropTypes.bool.isRequired,
    selected: PropTypes.object,
    baseDebugId: PropTypes.string.isRequired,
};

export default withI18N(injectIntl(ListQuickLaunches), intlData);
