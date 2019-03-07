/**
 *  @author sriram
 *
 **/

import React, { Component } from 'react';
import { Paper, Typography } from "@material-ui/core";
import withI18N, { getMessage } from "../../util/I18NWrapper";

import Grid from "@material-ui/core/Grid";
import Rating from "react-rating";
import goldstar from "../../../src/resources/images/star-gold.gif";
import whitestar from "../../../src/resources/images/star-white.gif";
import redstar from "../../../src/resources/images/star-red.gif";
import intlData from "../../apps/messages";

class AppDetails extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        const {details} = this.props;
        if (details) {
            return (
                <Paper style={{margin: 10}}>
                    <Grid container spacing={24}>
                        <Grid item xs={12}>
                            <b>{getMessage("descriptionLabel")}:</b> {details.description}
                        </Grid>
                        <Grid item xs={12}>
                            <Typography variant="h6">{getMessage("detailsLabel")}</Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <b>{getMessage("publishedOn")}</b> {details.integration_date}
                        </Grid>
                        <Grid item xs={12}>
                            <b>{getMessage("integratorName")}</b> {details.integrator_name}
                        </Grid>
                        <Grid item xs={12}>
                            <b>{getMessage("integratorEmail")}</b> {details.integrator_email}
                        </Grid>
                        <Grid item xs={12}>
                            <b>{getMessage("help")}</b> User Manual
                        </Grid>
                        <Grid item xs={12}>
                            <b>{getMessage("detailsRatingLbl")} </b>
                            <Rating
                                placeholderRating={3.5}
                                emptySymbol={<img src={whitestar} className="icon" alt="white star"/>}
                                fullSymbol={<img src={goldstar} className="icon" alt="gold star"/>}
                                placeholderSymbol={<img src={redstar} className="icon"
                                                        alt="red star"/>}
                                fractions={2}
                                readonly={true}
                            />
                            <span style={{padding: 3}}>
                              (3)
                        </span>
                        </Grid>
                        <Grid item xs={12}>
                            <b>{getMessage("analysesCompleted")}</b> {details.job_stats.job_count_completed}
                        </Grid>
                        <Grid item xs={12}>
                            <b>{getMessage("detailsLastCompleted")}</b> {details.job_stats.job_last_completed}
                        </Grid>
                        <Grid item xs={12}>
                            <b>{getMessage("url")}</b> URL
                        </Grid>
                    </Grid>
                </Paper>
            );
        } else {
            return null;
        }
    }
}

AppDetails.propTypes = {};

export default (withI18N(AppDetails, intlData));
