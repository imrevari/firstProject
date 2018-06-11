import React, {Component} from 'react';
import axios from 'axios';

import {Link} from 'react-router-dom';

class Login extends Component {
    constructor(props) {
        super(props);
        document.title = 'Login';
    }

    state = {
        email: {
            value: '',
            isValid: true,
            message: ''
        },
        password: {
            value: '',
            isValid: true,
            message: ''
        },
        registrationSaved:{
            value: false,
            isValid: true,
            message: ''
        }

    };

    componentDidMount() {

        const user = JSON.parse(localStorage.getItem('user'));

        if (user) {
            this.props.history.push('/mainPage');
            console.log(user);
        }
    }

    inputChangeHandler = (event) => {
        const updatedFromField = {};
        updatedFromField.value = event.target.value;
        updatedFromField.isValid = true;
        this.setState({
            [event.target.name]: updatedFromField
        })
    };

    validationHandler = (error) => {
        const updatedState = {
            ...this.state
        };

        for (let field in this.state) {
            const updatedFormElement = {
                ...updatedState[field]
            };
            updatedFormElement.isValid = true;
            updatedFormElement.message = '';
            updatedState[field] = updatedFormElement;
        }

        for (let fieldError of error.fieldErrors) {
            const updatedFormElement = {
                ...updatedState[fieldError.field]
            };
            updatedFormElement.isValid = false;
            updatedFormElement.message = fieldError.message;
            updatedState[fieldError.field] = updatedFormElement;
        }

        this.setState(updatedState);
    };

    postDataHandler = (event) => {
        event.preventDefault();

        const formData = {};
        for (let formElementIdentifier in this.state) {
            formData[formElementIdentifier] = this.state[formElementIdentifier].value;
        }

        axios.get('/api/users/me', {
            auth: {
                username: this.state.email.value,
                password: this.state.password.value
            }
        })
            .then(response => {
                // console.log(response);
                localStorage.setItem('user', JSON.stringify(response.data));
                this.props.history.push('/mainPage');
            })
            .catch(error => {
                console.log(error.response);
                if (error.response.status === 401) {
                    const errors = {
                        fieldErrors: [
                            {
                                field: 'email',
                                message: ''
                            },
                            {
                                field: 'password',
                                message: 'Invalid email - password combination'
                            }
                        ]
                    };
                    this.validationHandler(errors);
                } else {
                    this.setState(() => {
                        throw error;
                    })
                }
            });
    };


    render() {
        return (
            <div className="container">
                <h2>Please login</h2>
                <hr/>
                <br/>
                <form onSubmit={this.postDataHandler}>
                    <div>
                        <label
                            className={this.state.email.isValid ? "control-label input-label" : "control-label input-label invalid-label"}>
                            E-mail:
                        </label>
                        <input
                            className={this.state.email.isValid ? (this.state.registrationSaved.value ? "form-control my-input-field is-saved" : "form-control my-input-field") : "form-control my-input-field is-invalid"}
                            type="email"
                            name="email"
                            value={this.state.email.value}
                            onChange={this.inputChangeHandler}
                        />
                        <span className="form-text invalid-feedback">{this.state.email.message}</span>
                    </div>
                    <div>
                        <label
                            className={this.state.password.isValid ? "control-label input-label" : "control-label input-label invalid-label"}>
                            Password:
                        </label>
                        <input
                            className={this.state.password.isValid ? (this.state.registrationSaved.value ? "form-control my-input-field is-saved" : "form-control my-input-field") : "form-control my-input-field is-invalid"}
                            type="password"
                            name="password"
                            value={this.state.password.value}
                            onChange={this.inputChangeHandler}
                        />
                        <br/>
                        <div className="category-saved">{this.state.registrationSaved.value && "Registration was successful"}</div>
                        <span className="form-text invalid-feedback">{this.state.password.message}</span>
                    </div>
                    <br/>
                    <div>
                        <button className="btn btn-success my-button" type="submit">Login</button>
                        <Link to="newPassword">
                            <button className=" btn btn-primary">Forgot password</button>
                        </Link>
                    </div>


                    <br/>
                    <br/>

                    <Link to="/registration">
                        <button className="btn btn-primary">Register</button>
                    </Link>

                </form>
            </div>
        )
    }
}

export default Login;