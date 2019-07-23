import { palette } from "@cyverse-de/ui-lib";

export default (theme) => ({
    table: {
        overflow: "auto",
        height: "80%",
        width: "100%",
    },
    tableHead: {
        backgroundColor: palette.blue,
        position: "sticky",
        top: 0,
    },
    container: {
        width: "100%",
        height: "100%",
        marginTop: 0,
        overflow: "auto",
    },
    toolbarButton: {
        marginRight: 10,
        marginLeft: 10,
        textTransform: "none",
        fontSize: 10,
    },
    toolbar: {
        backgroundColor: palette.lightGray,
        borderBottom: "solid 2px",
        borderColor: palette.gray,
        height: 55,
    },
    dialogCloseButton: {
        position: "absolute",
        right: 0,
        top: 10,
        color: palette.white,
    },
    analysisName: {
        fontSize: 12,
        "&:hover": {
            textDecoration: "underline",
            cursor: "pointer",
        },
    },
    interactiveButton: {
        color: palette.darkBlue,
        "&:hover": {
            cursor: "pointer",
        },
    },
    menuItem: {
        fontSize: 10,
        padding: 5,
    },
    cellText: {
        fontSize: 12,
    },
    toolbarItemColor: {
        color: palette.darkBlue,
    },
    inputType: {
        cursor: "pointer",
        textDecoration: "underline",
        textOverflow: "ellipsis",
    },
    otherType: {
        whiteSpace: "pre-wrap",
    },
    dropDown: {
        margin: 3,
        height: 40,
        flexDirection: "unset",
    },
    dropDownLabel: {
        paddingLeft: 5,
        fontSize: 10,
    },
    heading: {
        fontSize: theme.typography.pxToRem(15),
        flexBasis: "33.33%",
        flexShrink: 0,
    },
    secondaryHeading: {
        fontSize: theme.typography.pxToRem(15),
        color: theme.palette.text.secondary,
    },
    analysisInfoFont: {
        fontSize: 11,
    },
});
