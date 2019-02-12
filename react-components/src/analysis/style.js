import Color from "../util/CyVersePalette";

export default (theme) => ({
    table: {
        overflow: "auto",
        height: "80%",
        width: "100%"
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
        marginRight: 10,
        marginLeft: 10,
        textTransform: 'none',
        fontSize: 10,
    },
    toolbar: {
        backgroundColor: Color.lightGray,
        borderBottom: 'solid 2px',
        borderColor: Color.gray,
        height: 55,
    },
    dialogCloseButton: {
        position: 'absolute',
        right: 0,
        top: 10,
        color: Color.white,
    },
    analysisName: {
        fontSize: 12,
        '&:hover': {
            textDecoration: 'underline',
            cursor: 'pointer',
        },
    },
    menuItem: {
        fontSize: 10,
        padding:5,
    },
    cellText: {
        fontSize: 12,
    },
    toolbarItemColor: {
        color: Color.darkBlue,
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
        flexDirection: 'unset',
    },
    dropDownLabel: {
        paddingLeft: 5,
        fontSize:10
    }
});