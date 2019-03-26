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
import CircularProgress from "@material-ui/core/CircularProgress";
import Book from "../../resources/images/book.png";
import build from "../../util/DebugIDUtil";
import ids from "../ids";


const AGAVE = "agave";
function Favorite(props) {
    const {is_favorite, id} = props.details;
    const {classes, isExternal, onFavoriteClick} = props;
    let className = classes.disableFavorite;
    const debugId = build(props.baseDebugId, id);
    if (!isExternal) {
        className = is_favorite ? classes.favorite : classes.notFavorite;
    }

    return (
        <div id={build(debugId, ids.DETAILS.APP_FAVORITE_CELL)} className={className}
             onClick={() => onFavoriteClick(isExternal)}></div>
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
        this.onFavoriteClick = this.onFavoriteClick.bind(this);
        this.onRatingChange = this.onRatingChange.bind(this);
        this.onDeleteRatingClick = this.onDeleteRatingClick.bind(this);
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

    onDeleteRatingClick() {
        const {presenter, details} = this.props;
        this.setState({loading: true});
        presenter.onAppRatingDeSelected(details, () => {
            this.setState({loading: false});
        }, (httpCode, message) => {
            this.setState({loading: false});
        })
    }

    onRatingChange(value) {
        const {presenter, details} = this.props;
        this.setState({loading: true});
        //service accepts only long
        presenter.onAppRatingSelected(details, Math.ceil(value), () => {
            this.setState({loading: false});
        }, (httpCode, message) => {
            this.setState({loading: false});
        })
    }

    onFavoriteClick(isExternal) {
        const {presenter, details} = this.props;
        this.setState({loading: true});
        if (!isExternal) {
            presenter.onAppFavoriteSelected(details, () => {
                this.setState({loading: false});
            }, (httpCode, message) => {
                this.setState({loading: false});
            });
        }
    }

    render() {
        const {details, baseDebugId, intl, classes} = this.props;
        const {loading, dialogOpen} = this.state;
        const isExternal = details.app_type.toUpperCase() === constants.EXTERNAL_APP.toUpperCase();
        const showAppURL = details.is_public || isExternal;
        const {average, user, total} = details.rating;

        if (details) {
            return (
                <React.Fragment>
                    <Paper id={baseDebugId} style={{padding: 5, fontSize: 11}}>
                        {loading &&
                        <CircularProgress size={30} className={classes.loadingStyle} thickness={7}/>
                        }
                    <Grid container spacing={24} style={{paddingLeft: 5}}>
                        <Grid item xs={12}>
                            <Favorite baseDebugId={baseDebugId}
                                      details={details}
                                      isExternal={isExternal}
                                      classes={classes}
                                      onFavoriteClick={this.onFavoriteClick}/>
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
                            <b>{getMessage("detailsRatingLbl")} </b>
                            <Rating
                                placeholderRating={average}
                                emptySymbol={<img src={whitestar} className="icon" alt="white star"/>}
                                fullSymbol={<img src={goldstar} className="icon" alt="gold star"/>}
                                placeholderSymbol={<img src={redstar} className="icon"
                                                        alt="red star"/>}
                                fractions={2}
                                readonly={isExternal}
                                onChange={this.onRatingChange}
                            />
                            <span>
                                {
                                    user &&
                                    <IconButton onClick={this.onDeleteRatingClick}
                                                className={classes.ratingDelete}>
                                        <Delete fontSize="small"/>
                                    </IconButton>
                                }
                            </span>
                            <span>
                                    ({total ? total : 0})
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
                        {details.hierarchies &&
                        <React.Fragment>
                            <Grid id={build(baseDebugId, ids.DETAILS.CATEGORIES_TREE)} item xs={12}>
                                <b>{getMessage("category")}</b>
                                <CategoryTree hierarchies={details.hierarchies}/>
                            </Grid>
                        </React.Fragment>
                        }
                        {
                            details.system_id === AGAVE &&
                            <React.Fragment>
                                <Grid id={build(baseDebugId, ids.DETAILS.CATEGORIES_TREE)} item xs={12}>
                                    <b>{getMessage("category")}</b>
                                    <br/>
                                    <img src={Book} alt={AGAVE}/> {getMessage("hpc")}
                                </Grid>
                            </React.Fragment>
                        }
                    </Grid>
                </Paper>
                    <Dialog open={dialogOpen}
                            fullWidth>
                        <DEDialogHeader heading={formatMessage(intl, "copyAppUrl")}
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
