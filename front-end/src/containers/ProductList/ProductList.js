import React, {Component} from 'react';
import axios from 'axios';
import ProductListItem from '../../components/ProductListItem/ProductListItem';
import {NotificationManager} from "react-notifications";

class ProductList extends Component {
    constructor(props) {
        super(props);
        document.title = 'Product list';
    }

    state = {
        products: []
    };

    componentDidMount() {
        this.getCategoryProductList();
    }

    getCategoryProductList = () => {
        axios.get('/api/categories/list/' + this.props.match.params.id)
            .then(response => {
                console.log(response.data);

                this.setState({
                    products: response.data
                });
                console.log('category id is ' + this.props.match.params.id);
            })
            .catch(error => {
                console.log(error);
                this.setState(() => {
                    throw error;
                })
            });
    };

    addToCart = (itemId) => {
        // console.log("item " + itemId + " added to the cart");
        axios.get('/api/products/putIntoCart/' + itemId, {withCredentials: true})
            .then(response => {
                // console.log(response.data);
                NotificationManager.success(response.data.name + ' added to the cart!');
            })
            .catch(error => {
                console.log(error);
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
                this.getCategoryProductList();
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
        let products;
        let categoryName = '';

        products = this.state.products.map((product) => {
            categoryName = product.categoryName;
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
            );
        });

        return (
            <div className="table-container">
                <h2>Products in category: {categoryName}</h2>
                <hr/>
                <table>
                    <tbody>
                    {products}
                    </tbody>
                </table>
            </div>
        )
    }
}


export default ProductList;