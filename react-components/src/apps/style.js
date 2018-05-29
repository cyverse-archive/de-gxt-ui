import {StyleSheet} from "aphrodite";
export default StyleSheet.create({
    loadingStyle: {
        position: 'absolute',
        top: 400,
        left: 400,
        color: '#DB6619',
    },
    filterButton: {
        margin: 1,
    },
    statTable: {
        overflow: "auto",
    },
    tableHead: {
        backgroundColor: "#e2e2e2",
        position: "sticky",
        top: 0
    },
    label: {
        fontWeight: 'bold',
        fontSize: 10,
        width: 90,
        paddingBottom: '0.5em'
    },
    value: {
        fontSize: 10,
        paddingLeft: 2,
        paddingBottom: '0.5em'
    },
});