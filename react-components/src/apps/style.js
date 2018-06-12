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
        height: "70%",
    },
    statTableHead: {
        backgroundColor: "#e2e2e2",
        position: "sticky",
        top: 0
    },
    statTablePager: {
        flexShrink: 0,
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
        height: "100%",
        marginTop: theme.spacing.unit * 3,
        overflow: "auto",
    },
    statSearchTextField: {
        marginLeft: theme.spacing.unit,
        marginRight: theme.spacing.unit,
        width: 200,
    },
});
