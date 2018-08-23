import Color from "../util/CyVersePalette";

export default (theme) => ({
    table: {
        height: "70%",
        width: "80%",
    },
    tableHead: {
        backgroundColor: "#e2e2e2",
        position: "sticky",
        top: 0
    },
    loadingStyle: {
        position: 'absolute',
        top: 200,
        left: 400,
        color: '#DB6619',
    },
    container: {
        width: "100%",
        height: "100%",
        marginTop: 0,
        overflow: "auto",
    },
    toolbarButton: {
        marginRight: 20,
        textTransform: 'none',
    },
    toolbar: {
        backgroundColor: Color.lightGray,
        borderBottom: 'solid 2px',
        borderColor: Color.gray,
        paddingRight: 1,
    },
    toolbarMargins: {
        marginLeft: theme.spacing.unit,
        marginRight: theme.spacing.unit,
    },
    dialogCloseButton: {
        position: 'absolute',
        right: 0,
        top: 10,
        color: Color.white,
    },
});