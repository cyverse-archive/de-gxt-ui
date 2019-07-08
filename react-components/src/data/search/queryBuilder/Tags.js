import React, { Fragment } from "react";

import { options } from "./Operators";
import SearchFormTagPanel from "../SearchFormTagPanel";
import SelectOperator from "./SelectOperator";
import styles from "../styles";

import { getFormError } from "@cyverse-de/ui-lib";
import { FieldArray, getIn } from "formik";
import FormHelperText from "@material-ui/core/FormHelperText";
import { withStyles } from "@material-ui/core/styles";

/**
 * A component which allows users to specify tags in QueryBuilder
 */
const TAGS_DEFAULT = { tags: [] };
function Tags(props) {
    const operators = [options.Are, options.AreNot];

    const {
        parentId,
        field: { name },
        presenter,
    } = props;

    return (
        <Fragment>
            <SelectOperator
                operators={operators}
                parentId={parentId}
                name={name}
            />
            <FieldArray
                name={`${name}.tags`}
                render={(arrayHelpers) => (
                    <TagSearchField
                        parentId={parentId}
                        presenter={presenter}
                        arrayHelpers={arrayHelpers}
                    />
                )}
            />
        </Fragment>
    );
}

const TagSearchField = withStyles(styles)(renderTagSearchField);

function renderTagSearchField(props) {
    const {
        parentId,
        presenter,
        classes,
        arrayHelpers: {
            name,
            form: { values, touched, errors },
            ...arrayHelpers
        },
    } = props;

    let input = getIn(values, name);
    const errorMsg = getFormError(name, touched, errors);

    return (
        <div className={classes.autocompleteField}>
            <SearchFormTagPanel
                parentId={parentId}
                presenter={presenter}
                array={arrayHelpers}
                tagQuery={input}
            />
            <FormHelperText error>{errorMsg}</FormHelperText>
        </div>
    );
}

export { Tags, TAGS_DEFAULT };
