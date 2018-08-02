import cyverse from "../../util/CyVersePalette";

export default {
    searchButton: {
        display: 'flex',
        flexDirection: 'row-reverse',
        margin: '5px',
    },

    buttonBar: {
        display: 'flex',
        flexDirection: 'row',
        justifyContent: 'flex-end'
    },

    form: {
        padding: '10px 5px 10px 15px',
        borderStyle: 'solid',
        borderWidth: '1px',
        borderColor: cyverse.gray,
        minWidth: '500px',
        minHeight: '300px',
        maxHeight: '300px',
        overflow: 'scroll'
    },

    condition: {
        paddingLeft: '20px',
    },

    permissionUsers: {
        marginTop: '5px',
        padding: '5px',
    },

    userChip: {
        margin: '2px',
    },
};