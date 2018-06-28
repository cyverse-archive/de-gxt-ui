const styles = theme => ({
    attributeTableContainer: {
        width: "100%",
        height: "100%",
    },
    attributeTableWrapper: {
        overflow: "auto",
        height: "80%",
    },
    appBar: {
        position: "relative",
    },
    flex: {
        flex: 1,
    },
    tableHead: {
        backgroundColor: "#e2e2e2",
        position: "sticky",
        top: 0
    },
    deleteIcon: {
        "&:hover": {
            backgroundColor: theme.palette.error.dark,
        },
    },
});

const toolbarStyles = theme => ({
    root: {
        paddingLeft: theme.spacing.unit,
        paddingRight: theme.spacing.unit,
    },
    spacer: {
        flex: '1 1 100%',
    },
    actions: {
        color: theme.palette.text.secondary,
    },
    title: {
        paddingLeft: theme.spacing.unit,
        flex: '0 0 auto',
    },
});

export default styles;
export { toolbarStyles };
