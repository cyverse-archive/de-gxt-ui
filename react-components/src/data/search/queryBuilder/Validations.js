import { getIn, setIn } from "formik";

const minValue = (value) =>
    value && value < 0 ? `Must be at least 0` : undefined;

const validate = (values) => {
    let errors = {};

    errors = validateClause(values, null, errors);
    return errors;
};

function validateClause(values, path, errors) {
    let typeField = path ? `${path}.type` : "type";
    let typeVal = getIn(values, typeField);
    let argsField = path ? `${path}.args` : "args";
    let argsVal = getIn(values, argsField);

    switch (typeVal) {
        case "all":
        case "any":
            if (!argsVal || !argsVal.length) {
                errors = setIn(errors, typeField, "Empty grouping");
            } else {
                argsVal.forEach((item, index) => {
                    errors = validateClause(
                        values,
                        `${argsField}.${index}`,
                        errors
                    );
                });
            }
            break;
        case "label":
            if (!argsVal.label || !argsVal.label.length) {
                errors = setIn(errors, `${argsField}.label`, "Empty value");
            }
            break;
        case "owner":
            if (!argsVal.owner) {
                errors = setIn(errors, `${argsField}.owner`, "Empty value");
            }
            break;
        case "path":
            if (!argsVal.prefix || !argsVal.prefix.length) {
                errors = setIn(errors, `${argsField}.prefix`, "Empty value");
            }
            break;
        case "size":
            if (
                isNaN(Number.parseFloat(argsVal.from.value)) &&
                isNaN(Number.parseFloat(argsVal.to.value))
            ) {
                errors = setIn(errors, typeField, "Empty file sizes");
            }
            break;
        case "created":
        case "modified":
            if (!argsVal.from && !argsVal.to) {
                errors = setIn(errors, typeField, "Empty Start and End date");
            }
            break;
        case "permissions":
            if (!argsVal.users || !argsVal.users.length) {
                errors = setIn(errors, `${argsField}.users`, "Empty user list");
            }
            break;
        case "metadata":
            if (!argsVal.attribute && !argsVal.value) {
                errors = setIn(errors, typeField, "Empty attribute and value");
            }
            break;
        case "tag":
            if (!argsVal.tags || !argsVal.tags.length) {
                errors = setIn(errors, `${argsField}.tags`, "Empty tags list");
            }
            break;
        default:
            return errors;
    }
    return errors;
}

export { minValue, validate };
