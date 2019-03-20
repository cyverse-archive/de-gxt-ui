/**
 *  @author sriram
 *
 **/

import React, { useEffect, useState } from 'react';
import showdown from "showdown";
import sanitizeHtml from "sanitize-html";
import { Paper, withStyles } from "@material-ui/core";
import CircularProgress from "@material-ui/core/CircularProgress";
import style from "../style";
import withI18N, { getMessage } from "../../util/I18NWrapper";
import { injectIntl } from "react-intl";
import intlData from "../messages";
import DEHyperLink from "../../util/hyperlink/DEHyperLink";
import Typography from "@material-ui/core/Typography";
import Fab from "@material-ui/core/Fab";
import EditIcon from "@material-ui/icons/Edit";
import SaveIcon from "@material-ui/icons/Save";
import TextField from "@material-ui/core/TextField";

const EDIT_MODE = "edit";
const VIEW_MODE = "view";

function References(props) {
    const {doc} = props;
    if (doc && doc.references) {
        return (
            <React.Fragment>
                <Typography variant="subtitle1">{getMessage("references")}</Typography>
                {
                    doc.references.map((ref) => {
                        return <div>{ref}</div>
                    })
                }
            </React.Fragment>

        );
    }

    return null;
}

function WikiUrl(props) {
    const {wiki_url, name} = props;
    return (
        <div style={{padding: 5}}>
            <Typography variant="subtitle1">{getMessage("documentation")}</Typography>
            <DEHyperLink text={name} onClick={() => window.open(wiki_url, "_blank")}/>
        </div>
    );
}

function AppDoc(props) {
    const {presenter, app, editable, classes, intl} = props;
    const wiki_url = app.wiki_url;

    const [doc, setDoc] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(false);
    const [mode, setMode] = useState(VIEW_MODE);

    const documentation = (doc, mode) => {
        const converter = new showdown.Converter();
        converter.setFlavor('github');
        if (doc) {
            if (mode === VIEW_MODE) {
                return sanitizeHtml(converter.makeHtml(doc.documentation));
            } else {
                return doc.documentation;
            }
        } else {
            return "";
        }
    };

    const saveDoc = () => {
        const md = document.getElementById("docEditor").innerHTML;
        console.log("Inner html ->" + md);
        setLoading(true);
        setMode(VIEW_MODE);
        presenter.onSaveMarkdownSelected(app.id, app.system_id, md, (doc) => {
            setLoading(false);
            setDoc(doc);
        }, (statusCode, errorMessage) => {
            setLoading(false);
            setError(true);
        })
    };

    useEffect(() => {
        function handleSuccess(doc) {
            setLoading(false);
            setDoc(doc);
        }

        function handleFailure(statusCode, message) {
            setLoading(false);
            setError(true);
        }

        if (!wiki_url) {
            setLoading(true);
            presenter.getAppDoc(app, handleSuccess, handleFailure);
        }
    }, []);


    if (wiki_url) {
        return (<WikiUrl wiki_url={wiki_url} name={app.name}/>);
    }

    if (error) {
        return (<div>{getMessage("docFetchError")}</div>);
    }

    return (
        <Paper style={{padding: 5, fontSize:12}}>
            {loading &&
            <CircularProgress size={30} className={classes.loadingStyle} thickness={7}/>
            }
            {mode === VIEW_MODE &&
            <div id="docEditor" dangerouslySetInnerHTML={{__html: documentation(doc, mode)}}/>
            }
            {mode === EDIT_MODE &&
              <TextField multiline={true} rows={20} value={documentation(doc, mode)} fullWidth={true}/>
            }
            {mode === VIEW_MODE && <References doc={doc}/>}
            {(editable && mode === VIEW_MODE) &&
            <Fab color="primary"
                 aria-label="Edit"
                 style={{float: 'right'}}
                 size="medium"
                 onClick={() => setMode(EDIT_MODE)}>
                <EditIcon/>
            </Fab>
            }
            {(editable && mode === EDIT_MODE) &&
            <Fab color="primary"
                 aria-label="Save"
                 style={{float: 'right'}}
                 size="medium"
                 onClick={saveDoc}>
                <SaveIcon/>
            </Fab>
            }
        </Paper>
    );
}

export default withStyles(style)((withI18N(injectIntl(AppDoc), intlData)));
