import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';

ReactDOM.render(
    <App text={
`The Dark Arts better be worried,
oh boy!`
    }/>,
    document.getElementById('root')
);
registerServiceWorker();
