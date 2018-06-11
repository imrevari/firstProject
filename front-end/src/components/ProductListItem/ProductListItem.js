import React from 'react';
import './ProductListItem.css';

const productListItem = (props) => (


    <tr>
        <td className="url" align="left"><img src={props.picture} alt="product pic" width="50" height="50"/></td>
        <td className="item-name" align="left">{props.name}</td>
        <td className="short-desc" align="left">{props.shortDesc}</td>
        <td className="categoryName" align="left">{props.categoryName}</td>
        <td className="price" align="left">${props.price}</td>
        <td className="table-buttons">

            <button className="td-button btn btn-success" style={(!localStorage.user) ?
                {display: 'none'} : (props.juser.role === 'ROLE_ADMIN' ? {display: 'none'} : {})}
                    onClick={props.addToCart}>
                <i className="fas fa-cart-arrow-down"></i>
            </button>

            <button className="td-button btn btn-info" onClick={props.fullDesc}>
                <i className="fas fa-info-circle"></i>
            </button>

            <button className="td-button btn btn-success" style={(!localStorage.user) ?
                {display: 'none'} : (props.juser.role === 'ROLE_USER' || props.available ? {display: 'none'} : {})}
                    onClick={props.restoreProduct}>
                <i className="fa fa-check"></i>
            </button>

            <button className="td-button btn btn-danger" style={(!localStorage.user) ?
                {display: 'none'} : (props.juser.role === 'ROLE_USER' || !props.available ? {display: 'none'} : {})}
                    onClick={props.deleteProduct}>
                <i className="fa fa-times"></i>
            </button>


            <button className="td-button btn btn-info" style={(!localStorage.user) ?
                {display: 'none'} : (props.juser.role === 'ROLE_USER' ? {display: 'none'} : {})}
                    onClick={props.editProduct}>
                <i className="far fa-edit"></i>
            </button>
        </td>
    </tr>
);

export default productListItem;