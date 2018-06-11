import React, {Component} from 'react';
import axios from 'axios';
import OrderListItem from "../../components/OrderListItem/OrderListItem";
import {Link} from 'react-router-dom';

class MyProfile extends Component {
    constructor(props) {
        super(props);
        document.title = 'My Profile';
    }

    state = {
        myAccountDetails: {},
        myOrdersDetails: []
    };

    componentDidMount() {
        this.getMyAccount();
        this.getMyOrders();
    }

    getMyOrders = () => {
        axios.get('/api/orders/myOrders', {withCredentials: true})
            .then(response => {
                console.log(response.data);
                this.setState({
                    myOrdersDetails: [...response.data]
                });
            })
            .catch(error => {
                console.log("error when getting myAccount content: ", error);
                this.setState(() => {
                    throw error;
                })
            })
    };

    getMyAccount = () => {
        axios.get('/api/users/myAccount', {withCredentials: true})
            .then(response => {
                // console.log(response.data);
                this.setState({
                    myAccountDetails: {...response.data}
                })
            })
            .catch(error => {
                console.log("error when getting myAccount content: ", error);
                this.setState(() => {
                    throw error;
                })
            })
    };

    render() {
        let orders;
        if (this.state.myOrdersDetails) {
            orders = this.state.myOrdersDetails.map((order, index) => {
                return (<OrderListItem
                    key={index}
                    orderedProducts={order.orderedProducts}
                    orderTime={order.orderTime}
                    amount={order.amount}/>)
            });
        }
        return (
            <div className="container">
                <h2>My Profile</h2>
                <hr/>
                <br/>
                <label className="input-label">User Name:</label>
                <input className="my-input-field my-input-field-disabled" name="username" disabled="true"
                       placeholder={this.state.myAccountDetails.name}/>
                <br/>
                <label className="input-label">Email:</label>
                <input className="my-input-field my-input-field-disabled" name="goal" disabled="true"
                       placeholder={this.state.myAccountDetails.email}/>
                <br/>
                <Link to="/changePassword">
                    <button className="btn btn-primary">Change password</button>
                </Link>
                <br/>
                <br/>
                <br/>
                <div style={(this.state.myOrdersDetails.length < 1) ?
                    {display: 'none'} : {}}>
                    <h3>My Orders</h3>
                    <table className="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th className="col-md-6">Item</th>
                            <th className="col-md-4">Date</th>
                            <th className="col-md-2">Amount</th>
                        </tr>
                        </thead>
                        <tbody>
                        {orders}
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}

export default MyProfile