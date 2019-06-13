import { getMessage } from "@cyverse-de/ui-lib";

/**
 * An Operator is part of a Condition and allows the user to specify whether the condition
 * should be true or false, fuzzy or exact, etc.
 *
 * The options object is used to populate the dropdown menu(s) for each Condition
 *
 * The operatorMap object is used to map each option to different flags
 */
const options = {
    Is: {
        value: "Is",
        label: getMessage("is"),
    },
    IsNot: {
        value: "IsNot",
        label: getMessage("isNot"),
    },
    Contains: {
        value: "Contains",
        label: getMessage("contains"),
    },
    ContainsNot: {
        value: "ContainsNot",
        label: getMessage("containsNot"),
    },
    Between: {
        value: "Between",
        label: getMessage("between"),
    },
    BetweenNot: {
        value: "BetweenNot",
        label: getMessage("betweenNot"),
    },
    Begins: {
        value: "Begins",
        label: getMessage("begins"),
    },
    BeginsNot: {
        value: "BeginsNot",
        label: getMessage("beginsNot"),
    },
    Are: {
        value: "Are",
        label: getMessage("are"),
    },
    AreNot: {
        value: "AreNot",
        label: getMessage("areNot"),
    },
    AreAtLeast: {
        value: "AreAtLeast",
        label: getMessage("areAtLeast"),
    },
    AreNotAtLeast: {
        value: "AreNotAtLeast",
        label: getMessage("areNotAtLeast"),
    },
};

const operatorMap = {
    Is: {
        negated: false,
        exact: true,
        permission_recurse: undefined,
    },
    IsNot: {
        negated: true,
        exact: true,
        permission_recurse: undefined,
    },
    Contains: {
        negated: false,
        exact: false,
        permission_recurse: undefined,
    },
    ContainsNot: {
        negated: true,
        exact: false,
        permission_recurse: undefined,
    },
    Between: {
        negated: false,
        exact: undefined,
        permission_recurse: undefined,
    },
    BetweenNot: {
        negated: true,
        exact: undefined,
        permission_recurse: undefined,
    },
    Begins: {
        negated: false,
        exact: undefined,
        permission_recurse: undefined,
    },
    BeginsNot: {
        negated: true,
        exact: undefined,
        permission_recurse: undefined,
    },
    Are: {
        negated: false,
        exact: undefined,
        permission_recurse: undefined,
    },
    AreNot: {
        negated: true,
        exact: undefined,
        permission_recurse: undefined,
    },
    AreAtLeast: {
        negated: false,
        exact: undefined,
        permission_recurse: true,
    },
    AreNotAtLeast: {
        negated: true,
        exact: undefined,
        permission_recurse: true,
    },
};

export { options, operatorMap };
