export default (theme) => ({
    loadingStyle: {
        position: 'absolute',
        top: 400,
        left: 400,
        color: '#DB6619',
    },
    statFilterButton: {
        margin: 1,
    },
    statTable: {
        overflow: "auto",
    },
    statTableHead: {
        backgroundColor: "#e2e2e2",
        position: "sticky",
        top: 0
    },
    toolDetailsLabel: {
        fontWeight: 'bold',
        fontSize: 10,
        width: 90,
        paddingBottom: '0.5em'
    },
    toolDetailsValue: {
        fontSize: 10,
        paddingLeft: 2,
        paddingBottom: '0.5em'
    },
    statContainer: {
        width: "100%",
        marginTop: theme.spacing.unit * 3,
        overflow: "auto",
        height: 800,
    },
    statSearchTextField: {
        marginLeft: theme.spacing.unit,
        marginRight: theme.spacing.unit,
        width: 200,
    },
});