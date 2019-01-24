const styles = theme => ({
    column: {
        display: "flex",
        flexDirection: "column",
        width: "100%",
    },

    subjectSearch: {
        flexGrow: 1,
        flexBasis: '200px'
    },

    formItem: {
        margin: "5px",
    },

    toolbarItem: {
        marginRight: "15px",
    },

    textBlurb: {
        margin: '10px',
    },

    wrapper: {
        width: "100%",
        height: "100%",
    },

    loading: {
        position: 'absolute',
        top: '25%',
        left: '50%',
        color: '#DB6619',
    }
});

export default styles;