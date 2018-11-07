/**
 *
 * @author Sriram
 *
 */
import React from 'react';
import Radio from '@material-ui/core/Radio';
import FormControl from "@material-ui/core/FormControl/FormControl";
import FormLabel from "@material-ui/core/FormLabel/FormLabel";
import RadioGroup from "@material-ui/core/RadioGroup/RadioGroup";
import FormControlLabel from "@material-ui/core/FormControlLabel/FormControlLabel";
import withI18N, { formatHTMLMessage, formatMessage, getMessage } from "../../../util/I18NWrapper";
import analysisStatus from "../../model/analysisStatus";
import intlData from "../../messages";
import Grid from "@material-ui/core/Grid/Grid";
import Dialog from "@material-ui/core/Dialog/Dialog";
import DEDialogHeader from "../../../util/dialog/DEDialogHeader";
import DialogContent from "@material-ui/core/DialogContent/DialogContent";
import Button from "@material-ui/core/Button/Button";
import TextField from "@material-ui/core/TextField/TextField";
import Checkbox from "@material-ui/core/Checkbox/Checkbox";
import { injectIntl } from "react-intl";


function AnalysisInfo(props) {
    const {analysis, name, email} = props;
    if (analysis) {
        return (
            <Grid container spacing={24} style={{marginTop: 2, fontSize: 12}}>
                <Grid item xs={6}>
                    {getMessage("analysis")}: {analysis.name}
                </Grid>
                <Grid item xs={12}>
                    {getMessage("analysisId")} : {analysis.id}
                </Grid>
                <Grid item xs={12}>
                    {getMessage("app")} : {analysis.app_name}
                </Grid>
                <Grid item xs={12}>
                    {getMessage("currentStatus")} : {analysis.status}
                </Grid>
                <Grid item xs={12}>
                    {getMessage("outputFolder")} : {analysis.resultfolderid}
                </Grid>
                <Grid item xs={12}>
                    {getMessage("startDate")} : {analysis.startdate}
                </Grid>
                <Grid item xs={12}>
                    {getMessage("endDate")} : {analysis.enddate}
                </Grid>
                <Grid item xs={12}>
                    {getMessage("user")} : {analysis.username}
                </Grid>
                <Grid item xs={12}>
                    {getMessage("name")} : {name}
                </Grid>
                <Grid item xs={12}>
                    {getMessage("email")} : {email}
                </Grid>
            </Grid>

        );
    } else {
        return null;
    }
}

function CompletedStateCondition(props) {
    return (
        <FormControl component="fieldset">
            <FormLabel component="legend" style={{padding: 5, fontWeight: "bold"}}>{getMessage(
                "outputConditionHeader")}</FormLabel>
            <RadioGroup
                name="condition"
                value={props.outputCondition}
                onChange={props.handleConditionChange}
            >
                <FormControlLabel value="noOutput"
                                  control={<Radio/>}
                                  label={getMessage("noOutput")}/>
                <FormControlLabel value="unExpectedOutput"
                                  control={<Radio/>}
                                  label={getMessage("unExpectedOutput")}/>
            </RadioGroup>
        </FormControl>
    );
}


function SubmittedStateSupport(props) {
    const {analysis} = props;
    if (analysis.system_id === "de") {
        return (
            <div>
                {formatHTMLMessage("condorSubmitted")}
            </div>
        );
    } else {
        return (
            <div>
                {formatHTMLMessage("agaveSubmitted")}
            </div>
        );
    }
}

function FailedStateSupport(props) {
    return (
        <div>
            {formatHTMLMessage("failed")}
        </div>
    );
}

function RunningStateSupport(props) {
    const {analysis} = props;
    if (analysis.system_id === "de") {
        return (
            <div>
                {formatHTMLMessage("condorRunning")}
            </div>
        );
    } else {
        return (
            <div>
                {formatHTMLMessage("agaveRunning")}
            </div>
        );
    }
}

function CompletedNoOutputSupport() {
    return (
        <div>
            {formatHTMLMessage("completedNoOutput")}
        </div>
    );
}


function CompletedUnExpectedOutputSupport() {
    return (
        <div>
            {formatHTMLMessage("completedUnExpectedOutput")}
        </div>
    );
}

class ShareWithSupportDialog extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            outputCondition: "noOutput",
            shareWithSupport: false,
            enableSubmit: false,
            comment: "",
            analysis: props.analysis,
        };
        this.handleConditionChange = this.handleConditionChange.bind(this);
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.props.analysis !== prevProps.analysis) {
            this.setState({analysis: this.props.analysis});
        }
    }

    handleConditionChange(event) {
        console.log("radio changed..." + event.target.value);
        this.setState({outputCondition: event.target.value})
    }

    render() {
        const {analysis, intl, name, email, onShareWithSupport} = this.props;
        const {outputCondition, shareWithSupport, enableSubmit} = this.state;

        const status = analysis ? analysis.status : "";
        return (
            <React.Fragment>
                <Dialog open={this.props.dialogOpen}>
                    <DEDialogHeader
                        heading={analysis.name}
                        onClose={
                            () => {
                                this.setState({shareWithSupport: false});
                                onShareWithSupport(analysis, this.state.comment, false)
                            }
                        }/>
                    <DialogContent>
                        <div style={{
                            display: shareWithSupport ?
                                "none" :
                                "block"
                        }}>
                            <div style={{
                                display: status === analysisStatus.COMPLETED ?
                                    "block" :
                                    "none"
                            }}>
                                <CompletedStateCondition analysis={analysis}
                                                         handleConditionChange={this.handleConditionChange}
                                                         outputCondition={outputCondition}/>
                            </div>
                            <div style={{
                                display: status === analysisStatus.SUBMITTED ?
                                    "block" :
                                    "none"
                            }}>
                                <SubmittedStateSupport analysis={analysis}/>
                            </div>
                            <div style={{
                                display: status === analysisStatus.RUNNING ?
                                    "block" :
                                    "none"
                            }}>
                                <RunningStateSupport analysis={analysis}/>
                            </div>
                            <div style={{
                                display: status === analysisStatus.FAILED ?
                                    "block" :
                                    "none"
                            }}>
                                <FailedStateSupport analysis={analysis}/>
                            </div>
                            <div style={{
                                display: (outputCondition === "noOutput" && status ===
                                    analysisStatus.COMPLETED) ?
                                    "block" :
                                    "none"
                            }}>
                                <CompletedNoOutputSupport/>
                            </div>

                            <div style={{
                                display: (outputCondition === "unExpectedOutput" && status ===
                                    analysisStatus.COMPLETED) ?
                                    "block" :
                                    "none"
                            }}>
                                <CompletedUnExpectedOutputSupport/>
                            </div>
                            <Button variant="contained"
                                    color="primary"
                                    style={{textTransform: "none", float: "right"}}
                                    onClick={() => {
                                        this.setState({shareWithSupport: true})
                                    }}>
                                I still need help!
                            </Button>
                        </div>
                        <div style={{
                            display: shareWithSupport ?
                                "block" :
                                "none",
                            flexGrow: 1
                        }}>
                            <AnalysisInfo analysis={analysis} name={name} email={email}/>
                            <TextField
                                id="comments"
                                placeholder={formatMessage(intl, "comments")}
                                style={{margin: 8}}
                                fullWidth
                                multiline
                                margin="normal"
                                variant="outlined"
                                onChange={(event) => {
                                    this.setState({comment: event.target.value})
                                }}
                                InputLabelProps={{
                                    shrink: true
                                }}
                            />
                            <FormControlLabel
                                control={
                                    <Checkbox
                                        checked={enableSubmit}
                                        onChange={(event) => this.setState({enableSubmit: event.target.checked})}
                                        value={enableSubmit}

                                    />
                                }
                                label={formatHTMLMessage("shareDisclaimer")}
                            />
                            <Button variant="contained"
                                    color="primary"
                                    style={{textTransform: "none", float: "right"}}
                                    disabled={!enableSubmit}
                                    onClick={() => {
                                        this.setState({shareWithSupport: false});
                                        onShareWithSupport(analysis,
                                            this.state.comment,
                                            true)
                                    }
                                    }
                            >
                                {getMessage("submit")}
                            </Button>
                        </div>
                    </DialogContent>
                </Dialog>
            </React.Fragment>
        );
    }
}

ShareWithSupportDialog.propTypes = {};
export default withI18N(injectIntl(ShareWithSupportDialog), intlData);