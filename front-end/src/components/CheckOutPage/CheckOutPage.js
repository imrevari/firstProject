import React from 'react';
import {Link} from 'react-router-dom';
import './checkOutPage.css'

const checkOutPage = (props) => {
    return(
        <div>
            <div className="alert-light thankyou-message">Thank you for choosing us!</div>
            <Link to="/"><button className="btn btn-info">Back to the shop</button></Link>
        </div>
    )
};

export default checkOutPage;