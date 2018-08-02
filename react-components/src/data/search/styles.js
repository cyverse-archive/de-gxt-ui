import cyverse from "../../util/CyVersePalette";

export default {
    searchButton: {
        display: 'flex',
        flexDirection: 'row-reverse',
    },

    buttonBar: {
        display: 'flex',
        flexDirection: 'row',
        justifyContent: 'space-between',
    },

    fullWidth: {
        width: '100%'
    },

    form: {
        width: '100%',
        padding: '0 10px 10px 10px',
        tableLayout: 'fixed'
    },

    group: {
        padding: '10px',
        borderStyle: 'solid',
        borderWidth: '1px',
        borderColor: cyverse.gray,
        paddingLeft: '10px'
    },

    condition: {
        paddingLeft: '8px',
    },

    permissionUsers: {
        marginTop: '5px',
        padding: '5px',
    },

    userChip: {
        margin: '2px',
    },
};