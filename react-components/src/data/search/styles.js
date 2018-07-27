import cyverse from "../../util/CyVersePalette";

export default {
    searchButton: {
        display: 'flex',
        flexDirection: 'row-reverse',
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
        borderColor: cyverse.gray
    },

    condition: {
        display: 'grid',
        gridTemplateColumns: '180px auto auto auto auto auto',
        gridColumnGap: '5px',
        gridRowGap: '5px',
        padding: '5px 10px 5px 10px',
    },

    selectField: {
        display: 'inline-table'
    },

    collabTableHead: {
        backgroundColor: cyverse.gray,
        position: "sticky",
        top: 0,
        padding: '0 0 0 20px',
    },

    collabTable: {
        overflow: "auto",
        height: "200px",
        width: "300px"
    },

    collabCell: {
        padding: '0 0 0 20px',
    },
};