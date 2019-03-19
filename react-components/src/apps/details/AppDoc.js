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
import withI18N, { formatMessage } from "../../util/I18NWrapper";
import { injectIntl } from "react-intl";
import intlData from "../messages";


function AppDoc(props) {
    const {presenter, app, classes, intl} = props;
    const wiki_url = app.wiki_url;
    const [doc, setDoc] = useState(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        function handleSuccess(doc) {
            setLoading(false);
            setDoc(doc.documentation);
        }

        function handleFailure(statusCode, message) {
            setLoading(false);
        }

        if (!wiki_url) {
            setLoading(true);
            presenter.getAppDoc(app, handleSuccess, handleFailure);
        }
    }, [doc]);

    if (wiki_url) {
        return <iframe src={wiki_url} style={{minHeight: 600, width: '100%'}}></iframe>
    }

    const converter = new showdown.Converter();
    converter.setFlavor('github');
    const html = doc ? sanitizeHtml(converter.makeHtml(doc)) : formatMessage(intl, "docFetchError");
    return (
        <Paper style={{padding: 5, fontSize: 12}}>
            {loading &&
            <CircularProgress size={30} className={classes.loadingStyle} thickness={7}/>
            }
            <div dangerouslySetInnerHTML={{__html: html}}/>
        </Paper>
    );
}

export default withStyles(style)((withI18N(injectIntl(AppDoc), intlData)));
