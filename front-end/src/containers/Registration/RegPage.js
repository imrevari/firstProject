import React, {Component} from 'react';
import axios from 'axios';


class RegPage extends Component{

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
        name: {
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

    inputChangeHandler = (event) => {
        const updatedFromField = {};
        updatedFromField.value = event.target.value;
        updatedFromField.isValid = true;
        this.setState({
            [event.target.name]: updatedFromField
        });
        console.log(this.state);
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

    register = (event) => {
        event.preventDefault();

        const data = {};
        for (let formElementName in this.state) {
            data[formElementName] = this.state[formElementName].value;
        }
        console.log(data);

        axios.post('/api/users/register', data)
            .then(() => {
                this.setState({
                    email: {
                        value:'',
                        isValid: true,
                        message:''
                    },
                    password: {
                        value:'',
                        isValid: true,
                        message:''
                    },
                    name: {
                        value:'',
                        isValid: true,
                        message:''
                    },
                    registrationSaved: {
                        value: true,
                        isValid: true,
                        message: ''
                    }

                })
            })
            .then(() => {
                this.props.history.push('/verification');
                setTimeout(this.hideMessage, 2000);
            })
            .catch((error) => {
                console.log(error.response);

                const errors = {
                    fieldErrors: [
                        {
                            field: error.response.data.fieldErrors[0].field,
                            message: error.response.data.fieldErrors[0].message
                        }
                    ]
                };
                this.validationHandler(errors);
            })
    };

    hideMessage = () => {
        this.setState({
            ...this.state,
            registrationSaved: {
                value: false,
                isValid: true,
                message: ''
            }

        })
    };


    render() {
        return (
            <div className="container">
                <h2>Registration</h2>
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
                            className={this.state.name.isValid ? "control-label input-label" : "control-label input-label invalid-label"}>
                            Your name:
                        </label>
                        <input
                            className={this.state.name.isValid ? (this.state.registrationSaved.value ? "form-control my-input-field is-saved" : "form-control my-input-field") : "form-control my-input-field is-invalid"}
                            type="text"
                            name="name"
                            value={this.state.name.value}
                            onChange={this.inputChangeHandler}
                        />
                        <span className="form-text invalid-feedback">{this.state.name.message}</span>
                    </div>

                    <div>
                        <label
                            className={this.state.password.isValid ? "control-label input-label" : "control-label input-label invalid-label"}>
                            Password:
                        </label>
                        <input
                            className={this.state.password.isValid ? (this.state.registrationSaved.value ? "form-control my-input-field is-saved" : "form-control my-input-field") : "form-control my-input-field is-invalid"}
                            type="text"
                            name="password"
                            value={this.state.password.value}
                            onChange={this.inputChangeHandler}
                        />
                        <br/>
                        <div className="category-saved">{this.state.registrationSaved.value && "Registration was successful"}</div>
                        <span className="form-text invalid-feedback">{this.state.password.message}</span>
                    </div>
                    <br/>
                    <button onClick={this.register} className="btn btn-success my-button" type="submit">Register</button>


                </form>
            </div>
        )
    }







}

export default RegPage;