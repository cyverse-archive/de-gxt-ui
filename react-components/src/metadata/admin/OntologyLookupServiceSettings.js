/**
 * @author psarando
 */
import React, { Component } from "react";
import { FastField, FieldArray } from "formik";

import intlData from "../messages";
import styles from "../style";
import ids from "./ids";
import StringListEditor from "./StringListEditor";

import {
    build,
    FormSelectField,
    getMessage,
    withI18N,
} from "@cyverse-de/ui-lib";

import { Grid, MenuItem, withStyles } from "@material-ui/core";

const OLSEntityTypes = ["CLASS", "PROPERTY", "INDIVIDUAL", "ONTOLOGY"];

const OLSEntityTypeMenuItems = OLSEntityTypes.map((type, index) => (
    <MenuItem key={index} value={type}>
        {type.toLowerCase()}
    </MenuItem>
));

class OntologyLookupServiceSettings extends Component {
    render() {
        const { field, parentID } = this.props;
        const formID = build(parentID, ids.OLS_PARAMS_EDIT_DIALOG);

        return (
            <Grid
                container
                spacing={16}
                direction="column"
                justify="flex-start"
                alignItems="stretch"
            >
                <Grid item>
                    <fieldset>
                        <legend>{getMessage("olsSettingTypeTitle")}</legend>

                        <FastField
                            name={`${field}.type`}
                            id={build(formID, ids.ONTOLOGY_ENTITY_TYPE)}
                            label={getMessage("olsSettingTypeLabel")}
                            component={FormSelectField}
                        >
                            {OLSEntityTypeMenuItems}
                        </FastField>
                    </fieldset>
                </Grid>

                <Grid item>
                    <FieldArray
                        name={`${field}.ontology`}
                        render={(arrayHelpers) => (
                            <StringListEditor
                                {...arrayHelpers}
                                parentID={build(formID, ids.ONTOLOGIES)}
                                title={getMessage("olsSettingOntologyTitle")}
                                helpLabel={getMessage(
                                    "olsSettingOntologyHelpLabel"
                                )}
                                columnLabel={getMessage(
                                    "olsSettingOntologyColumnLabel"
                                )}
                            />
                        )}
                    />
                </Grid>

                <Grid item>
                    <FieldArray
                        name={`${field}.childrenOf`}
                        render={(arrayHelpers) => (
                            <StringListEditor
                                {...arrayHelpers}
                                parentID={build(formID, ids.ONTOLOGY_CHILDREN)}
                                title={getMessage("olsSettingChildrenOfTitle")}
                                helpLabel={getMessage(
                                    "olsSettingChildrenOfHelpLabel"
                                )}
                                columnLabel={getMessage(
                                    "olsSettingIRIColumnLabel"
                                )}
                            />
                        )}
                    />
                </Grid>

                <Grid item>
                    <FieldArray
                        name={`${field}.allChildrenOf`}
                        render={(arrayHelpers) => (
                            <StringListEditor
                                {...arrayHelpers}
                                parentID={build(
                                    formID,
                                    ids.ONTOLOGY_ALL_CHILDREN
                                )}
                                title={getMessage(
                                    "olsSettingAllChildrenOfTitle"
                                )}
                                helpLabel={getMessage(
                                    "olsSettingAllChildrenOfHelpLabel"
                                )}
                                columnLabel={getMessage(
                                    "olsSettingIRIColumnLabel"
                                )}
                            />
                        )}
                    />
                </Grid>
            </Grid>
        );
    }
}

export default withStyles(styles)(
    withI18N(OntologyLookupServiceSettings, intlData)
);
