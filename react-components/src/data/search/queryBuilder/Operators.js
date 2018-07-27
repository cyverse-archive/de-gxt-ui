import { getMessage } from "../../../util/I18NWrapper";

const options = {
    Is: {
        value: 'Is',
        label: getMessage('is')
    },
    IsNot: {
        value: 'IsNot',
        label: getMessage('isNot')
    },
    Contains: {
        value: 'Contains',
        label: getMessage('contains')
    },
    ContainsNot: {
        value: 'ContainsNot',
        label: getMessage('containsNot')
    },
    Between: {
        value: 'Between',
        label: getMessage('between')
    },
    BetweenNot: {
        value: 'BetweenNot',
        label: getMessage('betweenNot')
    },
    Begins: {
        value: 'Begins',
        label: getMessage('begins')
    },
    BeginsNot: {
        value: 'BeginsNot',
        label: getMessage('beginsNot')
    },
    Are: {
        value: 'Are',
        label: getMessage('are')
    },
    AreNot: {
        value: 'AreNot',
        label: getMessage('areNot')
    },
    AreAtLeast: {
        value: 'AreAtLeast',
        label: getMessage('areAtLeast')
    },
    AreNotAtLeast: {
        value: 'AreNotAtLeast',
        label: getMessage('areNotAtLeast')
    }
};

const operatorMap = {
    Is: {
        negated: false,
        exact: true,
    },
    IsNot: {
        negated: true,
        exact: true,
    },
    Contains: {
        negated: false,
        exact: false,
    },
    ContainsNot: {
        negated: true,
        exact: false,
    },
    Between: {
        negated: false,
    },
    BetweenNot: {
        negated: true,
    },
    Begins: {
        negated: false,
    },
    BeginsNot: {
        negated: true,
    },
    Are: {
        negated: false,
    },
    AreNot: {
        negated: true,
    },
    AreAtLeast: {
        negated: false,
        permission_recurse: true,
    },
    AreNotAtLeast: {
        negated: true,
        permission_recurse: true,
    }
};

export {
    options,
    operatorMap
}