import React, {Component} from 'react';
import axios from 'axios';
import {NotificationManager} from 'react-notifications';

import ProductListItem from '../../components/ProductListItem/ProductListItem';

class MainPage extends Component {
    constructor(props) {
        super(props);
        document.title = 'Home';
    }

    state = {
        products: [],
        unavailableProducts: []
    };

    getProducts = () => {

        axios.get('/api/products')
            .then(response => {
                console.log(response);
                this.setState({products: response.data});
            })
            .catch(error => {
                console.log(error);
                this.setState(() => {
                    throw error;
                })
            })
    };

    getUnavailableProducts = () => {

        axios.get('/api/products/unavailableItems')
            .then(response => {
                // console.log(response);
                this.setState({unavailableProducts: response.data});
            })
            .catch(error => {
                console.log(error);
                this.setState(() => {
                    throw error;
                })
            })

    };

    componentDidMount() {
        this.getProducts();
        if (JSON.parse(localStorage.getItem('user'))) {
            if (JSON.parse(localStorage.getItem('user')).role === 'ROLE_ADMIN') {
                this.getUnavailableProducts();
            }
        }
    }

    addToCart = (itemId) => {
        // console.log("item " + itemId + " added to the cart");
        axios.get('/api/products/putIntoCart/' + itemId, {withCredentials: true})
            .then(response => {
                // console.log(response.data);
                NotificationManager.success(response.data.name + ' added to the cart!');
            })
            .catch(error => {
                //console.log(error)
                this.setState(() => {
                    throw error;
                })
            })
    };

    fullDesc = (itemId) => {
        // console.log(itemId);
        this.props.history.push("/details/" + itemId);
    };

    deleteProduct = (itemId) => {
        axios.delete('/api/products/' + itemId, {withCredentials: true})
            .then((response) => {
                this.props.history.push("/")
            })
            .catch(error => {
                this.setState(() => {
                    throw error;
                })
            })
    };

    restoreProduct = (itemId) => {
        axios.put('/api/products/setToAvailable/' + itemId, {withCredentials: true})
            .then((response) => {
                this.props.history.push("/")
            })
            .catch(error => {
                this.setState(() => {
                    throw error;
                })
            })
    };

    editProduct = (itemId) => {
        this.props.history.push("/editProduct/" + itemId);
    };

    render() {

        const products = this.state.products.map(product => {

            if (product.description.length > 60) {
                product.description = product.description.substring(0, 55) + "...";
            }

            return (
                <ProductListItem
                    key={product.id}
                    picture={product.picture}
                    name={product.name}
                    shortDesc={product.description}
                    categoryName={product.categoryName}
                    price={product.price}
                    addToCart={() => this.addToCart(product.id)}
                    fullDesc={() => this.fullDesc(product.id)}
                    deleteProduct={() => this.deleteProduct(product.id)}
                    editProduct={() => this.editProduct(product.id)}
                    restoreProduct={() => this.restoreProduct(product.id)}
                    juser={JSON.parse(localStorage.getItem('user'))}
                    available={true}
                />
            )
        });

        const unavailableProducts = this.state.unavailableProducts.map(product => {

            if (product.description.length > 60) {
                product.description = product.description.substring(0, 55) + "...";
            }

            return (
                <ProductListItem
                    key={product.id}
                    picture={product.picture}
                    name={product.name}
                    shortDesc={product.description}
                    categoryName={product.categoryName}
                    price={product.price}
                    addToCart={() => this.addToCart(product.id)}
                    fullDesc={() => this.fullDesc(product.id)}
                    deleteProduct={() => this.deleteProduct(product.id)}
                    editProduct={() => this.editProduct(product.id)}
                    restoreProduct={() => this.restoreProduct(product.id)}
                    juser={JSON.parse(localStorage.getItem('user'))}
                    available={false}
                />
            )
        });

        return (
            <div className="table-container">
                <h2>Available Items</h2>
                <hr/>
                <table>
                    <tbody>
                    {products}
                    </tbody>
                </table>
                <div style={(!localStorage.user) ?
                    {display: 'none'} : (JSON.parse(localStorage.getItem('user')).role !== 'ROLE_ADMIN' ? {display: 'none'} : {})}>
                    <br/>
                    <h2>Unavailable Items</h2>
                    <hr/>
                    <table>
                        <tbody>
                        {unavailableProducts}
                        </tbody>
                    </table>
                </div>
            </div>
        )
    }
}

export default MainPage;