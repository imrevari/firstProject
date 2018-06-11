import React from 'react';

const OrderedProductListItem = (props) => {

    return (
        <li><img src={props.imgURL} height="50" width="50"/> {props.name} - {props.quantity} pc</li>
    )
};

export default OrderedProductListItem;