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

    permissionUsers: {
        marginTop: '5px',
        padding: '5px',
    },

    userChip: {
        margin: '2px',
    },

    condition: {
        display: 'grid',
        gridTemplateColumns: '180px auto auto auto auto auto',
        gridColumnGap: '5px',
        gridRowGap: '5px',
        padding: '5px 10px 5px 10px',
    },
};