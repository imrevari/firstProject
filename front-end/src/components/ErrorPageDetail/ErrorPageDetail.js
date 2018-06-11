import React from 'react';

const ErrorPageDetail = (props) => {
    return(
        <div>
            <h2>Oops! Something happened!</h2>
            <hr/><br/>
            <div>The error must be :</div>
            <div>{props.text}</div>
        </div>
    )
};

export default ErrorPageDetail;