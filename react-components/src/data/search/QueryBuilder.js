import build from "../../util/DebugIDUtil";
import Condition from "./queryBuilder/Condition";
import ids from "./ids";
import messages from './messages';
import SaveSearchButton from "./SaveSearchButton";
import styles from "./styles";
import withI18N, { getMessage } from "../../util/I18NWrapper";
import withStoreProvider from "../../util/StoreProvider";

import Button from '@material-ui/core/Button';
import PropTypes from "prop-types";
import React, { Component } from 'react';
import { reduxForm, Field } from "redux-form";
import { withStyles } from "@material-ui/core/styles";

/**
 * A form which allows users to build their own custom data search queries
 */
class QueryBuilder extends Component {
    constructor(props) {
        super(props);

        this.handleSubmitForm = this.handleSubmitForm.bind(this);
        this.handleSaveSearch = this.handleSaveSearch.bind(this);
    }

    handleSubmitForm(values) {
        this.props.presenter.onSearchBtnClicked(values);
    }

    handleSaveSearch(values) {
        let initialValues = this.props.initialValues;
        let originalName = initialValues ? initialValues.label : null;
        this.props.presenter.onSaveSearch(values, originalName);
    }

    render() {
        let {
            handleSubmit,
            classes,
            initialValues,
            parentId
        } = this.props;
        let originalName = initialValues ? initialValues : null;

        return (
            <div>
                <Condition root={true}
                           parentId={build(parentId, ids.form)}
                           helperProps={this.props}/>
                <div className={classes.buttonBar}>
                    <Field name='label'
                           originalName={originalName}
                           id={ids.saveBtn}
                           handleSave={handleSubmit(this.handleSaveSearch)}
                           component={renderSaveSearchBtn}/>
                    <Button variant="raised"
                            id={build(parentId, ids.searchBtn)}
                            className={classes.searchButton}
                            onClick={handleSubmit(this.handleSubmitForm)}>
                        {getMessage('searchBtn')}
                    </Button>
                </div>
            </div>
        );
    }
}

QueryBuilder.propTypes = {
    presenter: PropTypes.shape({
        onAddTagSelected: PropTypes.func.isRequired,
        onEditTagSelected: PropTypes.func.isRequired,
        fetchTagSuggestions: PropTypes.func.isRequired,
        onSaveSearch: PropTypes.func.isRequired,
        onSearchBtnClicked: PropTypes.func.isRequired,
        searchCollaborators: PropTypes.func.isRequired,
    }),
    parentId: PropTypes.string.isRequired,
};

function renderSaveSearchBtn(props) {
    let {
        input,
        disabled,
        ...custom
    } = props;
    return (
        <SaveSearchButton disabled={disabled}
                          {...input}
                          {...custom}
        />
    )
}

export default withStoreProvider(
    reduxForm(
        {
            form: 'dataQueryBuilder',
            enableReinitialize: true
        }
    )(withStyles(styles)(withI18N(QueryBuilder, messages))));