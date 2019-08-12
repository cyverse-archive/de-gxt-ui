/**
 *  @author sriram
 *
 **/

import React from "react";

import sanitizeHtml from "sanitize-html";
import showdown from "showdown";
import { injectIntl } from "react-intl";

import ids from "../ids";
import intlData from "../messages";
import { EDIT_MODE, VIEW_MODE } from "./AppInfoDialog";

import {
    build,
    DEHyperlink,
    formatMessage,
    getMessage,
    LoadingMask,
    withI18N,
} from "@cyverse-de/ui-lib";

import EditIcon from "@material-ui/icons/Edit";
import SaveIcon from "@material-ui/icons/Save";

import { Fab, TextField, Tooltip, Typography, Paper } from "@material-ui/core";

function References(props) {
    const { references } = props;
    if (references) {
        return (
            <React.Fragment>
                <Typography variant="subtitle1">
                    {getMessage("references")}
                </Typography>
                {references.map((ref) => {
                    return <div key={ref}>{ref}</div>;
                })}
            </React.Fragment>
        );
    }

    return null;
}

function WikiUrl(props) {
    const { id, wiki_url, name } = props;
    return (
        <div id={id} style={{ padding: 5 }}>
            <Typography variant="subtitle1">
                {getMessage("documentation")}
            </Typography>
            <DEHyperlink
                text={name}
                onClick={() => window.open(wiki_url, "_blank")}
            />
        </div>
    );
}

function AppDoc(props) {
    const {
        baseDebugId,
        appName,
        documentation,
        references,
        wiki_url,
        editable,
        saveDoc,
        onDocChange,
        loading,
        error,
        mode,
        onModeChange,
        intl,
    } = props;

    const markDownToHtml = () => {
        const converter = new showdown.Converter();
        converter.setFlavor("github");
        if (documentation) {
            return sanitizeHtml(converter.makeHtml(documentation));
        } else {
            return "";
        }
    };

    const docChange = (event) => {
        onDocChange(event.target.value);
    };

    if (wiki_url) {
        return <WikiUrl id={baseDebugId} wiki_url={wiki_url} name={appName} />;
    }

    if (error) {
        return <div id={baseDebugId}>{getMessage("docFetchError")}</div>;
    }

    return (
        <Paper id={baseDebugId} style={{ padding: 5, fontSize: 12 }}>
            <LoadingMask loading={loading}>
                {mode === VIEW_MODE && (
                    <React.Fragment>
                        <div
                            dangerouslySetInnerHTML={{
                                __html: markDownToHtml(),
                            }}
                        />
                        <References references={references} />
                    </React.Fragment>
                )}
                {mode === EDIT_MODE && (
                    <TextField
                        id={build(baseDebugId, ids.DETAILS.APP_DOC_EDITOR)}
                        multiline={true}
                        rows={20}
                        value={documentation}
                        fullWidth={true}
                        onChange={docChange}
                    />
                )}
                {editable && mode === VIEW_MODE && (
                    <Tooltip
                        title={formatMessage(intl, "edit")}
                        aria-label={formatMessage(intl, "edit")}
                    >
                        <Fab
                            color="primary"
                            aria-label={formatMessage(intl, "edit")}
                            style={{ float: "right" }}
                            size="medium"
                            onClick={() => onModeChange(EDIT_MODE)}
                        >
                            <EditIcon />
                        </Fab>
                    </Tooltip>
                )}
                {editable && mode === EDIT_MODE && (
                    <Tooltip
                        title={formatMessage(intl, "save")}
                        aria-label={formatMessage(intl, "save")}
                    >
                        <Fab
                            color="primary"
                            aria-label={formatMessage(intl, "save")}
                            style={{ float: "right" }}
                            size="medium"
                            onClick={saveDoc}
                        >
                            <SaveIcon />
                        </Fab>
                    </Tooltip>
                )}
            </LoadingMask>
        </Paper>
    );
}

export default withI18N(injectIntl(AppDoc), intlData);
