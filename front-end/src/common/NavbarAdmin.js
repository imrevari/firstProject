import React from 'react';
import {Link} from 'react-router-dom';

const navbarAdmin = (props) => (


    <nav className="navbar navbar-expand-lg navbar-light bg-light">
        <div className="container">
            <Link className="navbar-brand" to="/">WEBSHOP - ADMIN</Link>
            {console.log(props)}
            <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarColor03"
                    aria-controls="navbarColor03" aria-expanded="false" aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"></span>
            </button>

                <div className="collapse navbar-collapse" id="navbarColor03">
                    <ul className="navbar-nav mr-auto">
                        <li className="nav-item">
                            <Link className="nav-link" to="/">Products</Link>
                            <Link className="nav-link" to="/myProfile">My Profile</Link>
                            <Link className="nav-link" to="/newProduct">New Product</Link>
                            <Link className="nav-link" to="/newCategory">New Category</Link>
                            <Link className="nav-link" to="/listUsers">Users</Link>
                            <Link className="nav-link" to="/logout">Logout</Link>
                        </li>
                    </ul>
                    {props.user}
                    {/*<ul className="navbar-nav ml-auto">*/}
                        {/*<li><Link className="nav-link" to="/myCart"><i className="fas fa-shopping-cart"></i> My*/}
                            {/*Cart</Link></li>*/}
                    {/*</ul>*/}
                </div>
            </div>
        </nav>
);

export default navbarAdmin;