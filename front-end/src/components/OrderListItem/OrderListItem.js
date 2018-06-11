import React from 'react';
import OrderedProductListItem from "../OrderedProductListItem/OrderedProductListItem";

const OrderListItem = (props) => {
    let orderedProducts;
    if (props.orderedProducts) {
        orderedProducts = props.orderedProducts.map((orderedProduct) => {
            return (<OrderedProductListItem
                key={orderedProduct.productId}
                imgURL={orderedProduct.imgURL}
                name={orderedProduct.name}
                quantity={orderedProduct.quantity}/>)
        });
    }

    return (
        <tr>
            <td>
                <ul>
                    {orderedProducts}
                </ul>
            </td>
            <td>{props.orderTime}</td>
            <td>{"$" + props.amount}</td>
        </tr>
    )
};

export default OrderListItem;