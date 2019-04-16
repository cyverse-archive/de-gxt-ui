/**
 *
 * @author sriram
 *
 */

import React, { useState } from "react";
import Paper from "@material-ui/core/Paper";
import GridList from "@material-ui/core/GridList";
import GridListTile from "@material-ui/core/GridListTile";
import { QuickLaunch } from "@cyverse-de/de-components";
import Code from "@material-ui/icons/Code.js";
import Share from "@material-ui/icons/Share.js";
import IconButton from "@material-ui/core/IconButton";
import {
    Dialog,
    DialogContent,
    Divider,
    TableCell,
    TableRow,
    Tooltip,
} from "@material-ui/core";
import withI18N, { formatMessage } from "../../util/I18NWrapper";
import { injectIntl } from "react-intl";
import intlData from "../messages";
import constants from "../../constants";
import DEDialogHeader from "../../util/dialog/DEDialogHeader";
import CopyTextArea from "../../util/CopyTextArea";

function ListQuickLaunches(props) {
    const { quickLaunches, intl, userName } = props;
    const [embedCode, setEmbedCode] = useState("");
    const [qLaunchUrl, setQLaunchUrl] = useState("");
    const [embedDialogOpen, setEmbedDialogOpen] = useState(false);
    const [shareDialogOpen, setShareDialogOpen] = useState(false);

    const quickLaunchClickHandler = (id) => {
        console.log("Quick launch with id: " + id);
    };
    const embedCodeClickHandler = (id) => {
        console.log("Embed code with id: " + id);
        let img_src =
            "http//:" +
            window.location.host +
            "/" +
            constants.QUICK_LAUNCH_EMBED_ICON;
        const embed =
            "<a href='" +
            getShareUrl(id) +
            "' target='_blank'><img src='" +
            img_src +
            "'></a>";

        setEmbedCode(embed);
        setEmbedDialogOpen(true);
    };
    const shareClickHandler = (id) => {
        console.log("Share code with id: " + id);
        if (qLaunchUrl) {
            setShareDialogOpen(true);
        } else {
            setQLaunchUrl(getShareUrl(id));
            setShareDialogOpen(true);
        }
    };

    const getShareUrl = (id) => {
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
            id;
        return url;
    };

    const deleteQuickLaunchHandler = (id) => {
        console.log("Delete quick launch with id: " + id);
    };

    return (
        <React.Fragment>
            <Paper style={{ padding: 5 }}>
                <GridList cellHeight={50} cols={2}>
                    {quickLaunches.map((qLaunch) => {
                        const id = qLaunch.id;
                        const onDelete =
                            userName === qLaunch.creator
                                ? (id) => deleteQuickLaunchHandler(id)
                                : undefined;
                        return (
                            <GridListTile key={id} cols={2}>
                                <Tooltip
                                    title={formatMessage(
                                        intl,
                                        "qLaunchToolTip"
                                    )}
                                >
                                    <QuickLaunch
                                        label={qLaunch.name}
                                        isPublic={qLaunch.is_public}
                                        handleClick={() =>
                                            quickLaunchClickHandler(id)
                                        }
                                        handleDelete={onDelete}
                                    />
                                </Tooltip>
                                {qLaunch.is_public && (
                                    <Tooltip
                                        title={formatMessage(
                                            intl,
                                            "qLaunchEmbedToolTip"
                                        )}
                                    >
                                        <IconButton
                                            fontSize="small"
                                            onClick={() =>
                                                embedCodeClickHandler(id)
                                            }
                                        >
                                            <Code color="primary" />
                                        </IconButton>
                                    </Tooltip>
                                )}
                                {qLaunch.is_public && (
                                    <Tooltip
                                        title={formatMessage(
                                            intl,
                                            "qLaunchShareToolTip"
                                        )}
                                    >
                                        <IconButton
                                            fontSize="small"
                                            onClick={() =>
                                                shareClickHandler(id)
                                            }
                                        >
                                            <Share color="primary" />
                                        </IconButton>
                                    </Tooltip>
                                )}
                            </GridListTile>
                        );
                    })}
                </GridList>
            </Paper>
            <Dialog open={embedDialogOpen} maxWidth="sm" fullWidth={true}>
                <DEDialogHeader
                    heading={formatMessage(intl, "embed")}
                    onClose={() => setEmbedDialogOpen(false)}
                />
                <DialogContent>
                    <CopyTextArea text={embedCode} multiline={true} />
                </DialogContent>
            </Dialog>
            <Dialog open={shareDialogOpen} maxWidth="sm" fullWidth={true}>
                <DEDialogHeader
                    heading={formatMessage(intl, "share")}
                    onClose={() => setShareDialogOpen(false)}
                />
                <DialogContent>
                    <CopyTextArea text={qLaunchUrl} multiline={true} />
                </DialogContent>
            </Dialog>
        </React.Fragment>
    );
}

export default withI18N(injectIntl(ListQuickLaunches), intlData);
