import palette from "../util/CyVersePalette";

const styles = theme => ({
    loadingStyle: {
        color: palette.orange,
        position: "relative",
        top: "50%",
        left: "50%",
    },
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
        backgroundColor: palette.lightGray,
        position: "sticky",
        top: 0
    },
    deleteIcon: {
        margin: 5,
        "&:hover": {
            backgroundColor: theme.palette.error.dark,
        },
    },
    toolbar: {
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
        maxWidth: "25rem",
    },
    errorSubTitle: {
        color: theme.palette.error.dark,
    },
    childAVUsContainer: {
        paddingLeft: theme.typography.pxToRem(4),
        paddingRight: theme.typography.pxToRem(4),
    },
    helpText: {
        margin: theme.spacing.unit * 2,
    },
});

export default styles;
