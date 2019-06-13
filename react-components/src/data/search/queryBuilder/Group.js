import React, { Fragment } from "react";

import AddBtn from "./AddBtn";
import Clause from "./Clause";
import DeleteBtn from "./DeleteBtn";
import ids from "../../search/ids";
import styles from "../styles";

import { build } from "@cyverse-de/ui-lib";
import { Field, getIn } from "formik";
import { withStyles } from "@material-ui/core/styles";

/**
 * A component which allows users to create different groupings in QueryBuilder
 * such as Any and All
 */
const DEFAULT_CLAUSE = { type: "label", args: { label: "" } };

function Group(props) {
    const {
        push,
        remove,
        form: { values },
        name,
        classes,
        collaboratorsUtil,
        presenter,
        parentId,
    } = props;

    let args = getIn(values, name);
    let baseId = build(parentId, name);

    return (
        <Fragment>
            <AddBtn
                onClick={() => push(DEFAULT_CLAUSE)}
                id={build(baseId, ids.addConditionBtn)}
            />
            {args &&
                args.map((field, index) => {
                    return (
                        <div className={classes.condition} key={index}>
                            <DeleteBtn
                                onClick={() => remove(index)}
                                id={build(parentId, ids.deleteConditionBtn)}
                            />
                            <Field
                                name={`${name}.${index}`}
                                collaboratorsUtil={collaboratorsUtil}
                                presenter={presenter}
                                parentId={build(baseId, index)}
                                component={Clause}
                            />
                        </div>
                    );
                })}
        </Fragment>
    );
}

export default withStyles(styles)(Group);
