import { StyleSheet } from 'aphrodite';
export default StyleSheet.create( {
    normal: {
        color: '#0971AB',
        cursor: 'pointer',
        textAlign: 'left',
        fontSize: '11px',
        margin: 2,
        ':hover': {
            textDecoration: 'underline',
            backgroundColor: '#ededed',
        },
    },
})