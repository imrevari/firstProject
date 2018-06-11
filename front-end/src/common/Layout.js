import React from 'react';
import {Route, Switch} from 'react-router-dom';
import NavbarAdmin from './NavbarAdmin';
import NavbarUser from './NavbarUser';
import Navbar from './Navbar'
import MainPage from '../containers/MainPage/MainPage';
import ProductDetails from '../containers/ProductDetails/ProductDetails';
import NewProduct from './../containers/NewProduct/NewProduct';
import MyCart from './../containers/MyCart/MyCart';
import CheckOutPage from '../components/CheckOutPage/CheckOutPage';
import NewCategory from './../containers/NewCategory/NewCategory';
import Login from './../containers/Login/Login';
import ProductList from './../containers/ProductList/ProductList';
import PrivateRoute from "./../common/PrivateRoute";
import PrivateRouteAdmin from './../common/PrivateRouteAdmin'
import Logout from '../containers/Logout/Logout'
import RegPage from "../containers/Registration/RegPage";
import Confirmation from "../containers/Registration/Confirmation";
import ErrorPage from '../components/ErrorPageDetail/ErrorPageDetail'
import UserList from "../containers/UserList/UserList";
import MyProfile from "../containers/MyProfile/MyProfile";
import ConfirmationWithURLFeedback from "../containers/Registration/ConfirmationWithURLFeedback";
import EditProfile from "../containers/EditProfile/EditProfile";
import NewPassword from "../containers/Password/NewPassword"
import ChangePassword from "../containers/Password/ChangePassword"

const layout = () => {

    const user = JSON.parse(localStorage.getItem('user'));
    return (
        <div>

            {localStorage.user ? ((user.role === 'ROLE_ADMIN') ? <NavbarAdmin user={user.name}/> :
                <NavbarUser user={user.name}/>) :
                <Navbar/>}

            <div className="page_content container">
                <Switch>
                    <Route path="/" exact component={Login}/>
                    <Route path="/login" exact component={Login}/>
                    <PrivateRoute path="/myCart" exact component={MyCart}/>
                    <PrivateRoute path="/myProfile" exact component={MyProfile}/>
                    <Route path="/mainPage" exact component={MainPage}/>
                    <Route path="/details/:id" exact component={ProductDetails}/>
                    <PrivateRouteAdmin path="/newProduct" exact component={NewProduct}/>
                    <PrivateRouteAdmin path="/listUsers" exact component={UserList}/>
                    <PrivateRouteAdmin path="/editProduct/:id" exact component={NewProduct}/>
                    <PrivateRouteAdmin path="/editProfile/:id" exact state component={EditProfile}/>
                    <PrivateRoute path="/checkout" exact component={CheckOutPage}/>
                    <PrivateRouteAdmin path="/newCategory" exact component={NewCategory}/>
                    <Route path="/products/:id" exact component={ProductList}/>
                    <Route path="/logout" exact component={Logout}/>
                    <Route path="/registration" exact component={RegPage}/>
                    <Route path="/verification" exact component={Confirmation}/>
                    <Route path="/verification/:confirmationCode" exact component={ConfirmationWithURLFeedback}/>
                    <Route path="/newPassword" exact component={NewPassword}/>
                    <PrivateRoute path="/changePassword" exact component={ChangePassword}/>

                    <Route render={() => (<ErrorPage text="There is no page for this URL"/>)}/>
                </Switch>
            </div>
        </div>
    );
};


export default layout;
