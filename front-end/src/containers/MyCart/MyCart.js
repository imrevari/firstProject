import React, {Component} from 'react';
import axios from 'axios';
import {Link} from 'react-router-dom';
import './myCart.css';

class MyCart extends Component {
    constructor(props) {
        super(props);
        document.title = 'My cart';
    }

    state = {
        cartContent: []
    };

    componentDidMount() {
        axios.get('/api/products/myCart', {withCredentials: true})
            .then(response => {
                // console.log(response.data);
                this.setState({
                    cartContent: response.data
                })
            })
            .catch(error => {
                console.log("error when getting cart content: ", error)
                this.setState(() => {
                    throw error;
                })
            })
    }

    increaseQuantity = (itemId) => {
        const cart = [...this.state.cartContent];
        const newItem = cart.filter((item) => item.id === itemId)[0];
        if (newItem) {
            newItem.amount = newItem.amount + 1;
        }
        axios.put('/api/products/increaseQuantity/' + itemId, newItem, {withCredentials: true})
            .then(response => {
                // console.log(response);
                this.setState({cartContent: response.data});
            })
            .catch(error => {
                this.setState(() => {
                    throw error;
                })
            })
    };

    decreaseQuantity = (itemId) => {

        const cart = [...this.state.cartContent];
        const newItem = cart.filter((item) => item.id === itemId)[0];
        if (newItem) {
            newItem.amount = newItem.amount - 1;
        }
        axios.put('/api/products/decreaseQuantity/' + itemId, newItem, {withCredentials: true})
            .then(response => {
                // console.log(response);
                this.setState({cartContent: response.data});
            })
            .catch(error => {
                this.setState(() => {
                    throw error;
                })
            })
    };

    doEmptyCart = (redirect) => {
        axios.delete('/api/products/emptyCart', {withCredentials: true})
            .then(() => {
                this.setState({cartContent: []})
            })
            .then(() => {
                if (redirect) {
                    redirect();
                }
            })
            .catch(error => {
                console.log(error);
                this.setState(() => {
                    throw error;
                })
            })
    };


    emptyCart = () => {
        this.doEmptyCart();
    };

    checkOut = () => {
        axios.post('/api/orders', {withCredentials: true})
            .then(() => {
                this.setState({cartContent: []});
                this.props.history.push('/checkout');
            })
            .catch(error => {
                console.log(error);
            });
    };


    render() {

        let itemsInCart = [];
        let total = 0;

        if (this.state.cartContent) {
            itemsInCart = this.state.cartContent.map((item) => {
                // console.log(item);
                return (
                    <tr key={item.productId}>
                        <td>{item.name}</td>
                        <td align="center">${item.price}</td>
                        <td align="center">{item.amount}</td>
                        <td align="center">
                            <button className="btn btn-success my-small-button"
                                    onClick={() => this.increaseQuantity(item.productId)}>+
                            </button>
                            <button className="btn btn-warning my-small-button"
                                    onClick={() => this.decreaseQuantity(item.productId)}>-
                            </button>
                        </td>
                    </tr>
                )
            });

            this.state.cartContent.forEach(item => {
                total += item.price * item.amount;
            });
        }

        return (
            <div>
                <h2>My Cart</h2>
                <br/>
                {itemsInCart.length > 0 ?

                    <div align="center">
                        <table className="table my-table">
                            <thead>
                            <tr>
                                <th className="name-column">Product Name</th>
                                <th className="price-column">Price</th>
                                <th className="amount-column">Amount</th>
                                <th className="button-column"></th>
                            </tr>
                            </thead>
                            <tbody>
                            {itemsInCart}
                            </tbody>
                        </table>
                        <br/>
                        <div><b>Total: ${total}</b></div>
                        <br/>
                        <Link to="/">
                            <button className="btn btn-info my-button">Continue shopping</button>
                        </Link>
                        <button className="btn btn-danger my-button" onClick={this.emptyCart}>Empty cart</button>
                        <button className="btn btn-success my-button" onClick={this.checkOut}>Check out</button>
                    </div>

                    :

                    <div>
                        <div className="alert alert-light"><strong>Your cart is empty.</strong></div>
                        <br/>
                        <Link to="/">
                            <button className="btn btn-info">Go shopping!</button>
                        </Link>
                    </div>
                }
            </div>
        )
    }
}

export default MyCart;