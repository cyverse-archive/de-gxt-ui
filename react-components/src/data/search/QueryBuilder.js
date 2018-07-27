import ids from "./ids";
import messages from './messages';
import SaveSearchButton from "./SaveSearchButton";
import styles from "./styles";
import withI18N, { getMessage } from "../../util/I18NWrapper";
import withStoreProvider from "../../util/StoreProvider";

import Button from '@material-ui/core/Button';
import injectSheet from "react-jss";
import React, { Component } from 'react';
import { reduxForm, Field } from "redux-form";
import Condition from "./queryBuilder/Condition";


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
        let {handleSubmit, classes, initialValues} = this.props;
        let originalName = initialValues ? initialValues : null;

        return (
            <div>
                <Condition root={true} {...this.props}/>
                <Field name='label'
                       originalName={originalName}
                       handleSave={handleSubmit(this.handleSaveSearch)}
                       component={renderSaveSearchBtn}/>
                <div className={classes.searchButton}>
                    <Button variant="raised"
                            className={classes.searchButton}
                            onClick={handleSubmit(this.handleSubmitForm)}>
                        {getMessage('searchBtn')}
                    </Button>
                </div>
            </div>
        );
    }
}

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
    )(injectSheet(styles)(withI18N(QueryBuilder, messages))));