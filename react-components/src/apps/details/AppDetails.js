/**
 *  @author sriram
 *
 **/

import React, { Component } from 'react';
import { Dialog, DialogContent, Paper, Typography } from "@material-ui/core";
import { withStyles } from "@material-ui/core/styles";
import withI18N, { formatMessage, getMessage } from "../../util/I18NWrapper";

import Grid from "@material-ui/core/Grid";
import Rating from "react-rating";
import goldstar from "../../../src/resources/images/star-gold.gif";
import whitestar from "../../../src/resources/images/star-white.gif";
import redstar from "../../../src/resources/images/star-red.gif";
import intlData from "../../apps/messages";
import CategoryTree from "./CategoryTree";
import formatDate from "../../util/DateFormatter";
import DEHyperLink from "../../util/hyperlink/DEHyperLink";
import { injectIntl } from "react-intl";
import CopyTextArea from "../../util/CopyTextArea";
import DEDialogHeader from "../../util/dialog/DEDialogHeader";
import constants from "../../constants";
import style from "../../apps/style";
import Delete from "@material-ui/icons/Delete";
import IconButton from "@material-ui/core/IconButton";


function Favorite(props) {
    const {is_favorite} = props.details;
    const {classes, isExternal, onFavoriteClick} = props;
    let className = classes.disableFavorite;

    if (!isExternal) {
        className = is_favorite ? classes.favorite : classes.notFavorite;
    }

    return (
        <div className={className} onClick={() => onFavoriteClick(isExternal)}></div>
    );
}

class AppDetails extends Component {

    constructor(props) {
        super(props);
        this.state = {
            dialogOpen: false,
            appURL: "",
        };
        this.onAppUrlClick = this.onAppUrlClick.bind(this);
        this.onUserManualClick = this.onUserManualClick.bind(this);
        this.onFavoriteClick = this.onFavoriteClick.bind(this);
    }

    onAppUrlClick() {
        const {id, system_id} = this.props.details;
        if (this.state.appURL) {
            this.setState({dialogOpen: true});
        } else {
            let host = window.location.protocol + '//' + window.location.host + window.location.pathname;
            const url = host + "?" + constants.TYPE + "=" + constants.APPS
                + "&" + constants.APP_ID + "="
                + id + "&" + constants.SYSTEM_ID
                + "=" + system_id;
            this.setState({dialogOpen: true, appURL: url});
        }
    }

    onUserManualClick() {
        const wiki_url = this.props.details.wiki_url;
        if (wiki_url) {
            window.open(wiki_url, "_blank");
        } else {
            //temp. hide this dialog to show app documentation
            this.setState({dialogOpen: false});
            this.props.presenter.onAppDetailsDocSelected();
        }
    }

    onFavoriteClick(isExternal) {
        const {presenter, details} = this.props;
        if (!isExternal) {
            presenter.onFavoriteClick(details);
        }
    }

    render() {
        const {details, intl, classes} = this.props;
        const isExternal = details.app_type.toUpperCase() === constants.EXTERNAL_APP.toUpperCase();
        const showAppURL = details.is_public || isExternal;

        if (details) {
            return (
                <React.Fragment>
                    <Paper style={{padding: 5, fontSize: 11}}>
                    <Grid container spacing={24} style={{paddingLeft: 5}}>
                        <Grid item xs={12}>
                            <Favorite details={details} isExternal={isExternal} classes={classes}/>
                        </Grid>
                        <Grid item xs={12}>
                            <b>{getMessage("descriptionLabel")}:</b> {details.description}
                        </Grid>
                        <Grid item xs={12}>
                            <Typography variant="h6">{getMessage("detailsLabel")}</Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <b>{getMessage("publishedOn")}</b> {formatDate(details.integration_date)}
                        </Grid>
                        <Grid item xs={12}>
                            <b>{getMessage("integratorName")}</b> {details.integrator_name}
                        </Grid>
                        <Grid item xs={12}>
                            <b>{getMessage("integratorEmail")}</b> {details.integrator_email}
                        </Grid>
                        <Grid item xs={12}>
                            <b>{getMessage("help")}</b>
                            <DEHyperLink onClick={this.onUserManualClick}
                                         text={formatMessage(intl,
                                             "userManual")}/>
                        </Grid>
                        <Grid item xs={12}>
                            <b>{getMessage("detailsRatingLbl")} </b>
                            <Rating
                                placeholderRating={details.rating.average}
                                emptySymbol={<img src={whitestar} className="icon" alt="white star"/>}
                                fullSymbol={<img src={goldstar} className="icon" alt="gold star"/>}
                                placeholderSymbol={<img src={redstar} className="icon"
                                                        alt="red star"/>}
                                fractions={2}
                                readonly={isExternal}
                            />
                            <span>
                                {
                                    details.rating.user &&
                                    <IconButton><Delete style={{height: 12, width: 12}}/> </IconButton>

                                }
                            </span>
                            <span>
                                    ({details.rating.total})
                            </span>

                        </Grid>
                        <Grid item xs={12}>
                            <b>{getMessage("analysesCompleted")}</b>
                            {details.job_stats.job_count_completed ?
                                details.job_stats.job_count_completed :
                                0}
                        </Grid>
                        <Grid item xs={12}>
                            <b>{getMessage("detailsLastCompleted")}</b> {formatDate(details.job_stats.job_last_completed)}
                        </Grid>
                        {showAppURL &&
                        <Grid item xs={12}>
                            <b>{getMessage("url")}:</b> <DEHyperLink onClick={this.onAppUrlClick}
                                                                     text={formatMessage(intl,
                                                                         "url")}/>
                        </Grid>
                        }
                        <Grid item xs={12}>
                            <b>{getMessage("category")}</b>
                        </Grid>
                        <Grid item xs={12}>
                            <CategoryTree hierarchies={details.hierarchies}/>
                        </Grid>
                    </Grid>
                </Paper>
                    <Dialog open={this.state.dialogOpen}
                            fullWidth>
                        <DEDialogHeader heading="Copy App URL"
                                        onClose={() => this.setState({dialogOpen: false})}/>
                        <DialogContent>
                            <CopyTextArea text={this.state.appURL}/>
                        </DialogContent>
                    </Dialog>
                </React.Fragment>
            );
        } else {
            return null;
        }
    }
}

AppDetails.propTypes = {};

export default withStyles(style)((withI18N(injectIntl(AppDetails), intlData)));
