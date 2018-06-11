import React, {Component} from 'react';
import axios from 'axios';
import './SavePassword.css';

class ChangePassword extends Component {
    constructor(props) {
        super(props);
        document.title = "Change password"
    }

    state = {
        formdata: {
            currentPassword: {
                value: '',
                isValid: true,
                message: ''
            },
            newPassword: {
                value: '',
                isValid: true,
                message: ''
            },
            newPasswordAgain: {
                value: '',
                isValid: true,
                message: ''
            }
        },
        passwordAltered: false
    };

    inputChangeHandler = (event) => {
        const target = event.target;
        const updatedForm = {
            ...this.state.formdata
        };
        const updatedFormElement = {
            ...updatedForm[target.name]
        };

        let value = target.value;


        updatedFormElement.value = value;
        updatedForm[target.name] = updatedFormElement;

        this.setState({formdata: updatedForm});
    };

    validationHandler = (error) => {
        const updatedForm = {
            ...this.state.formdata
        };

        for (let field in this.state.formdata) {
            const updatedFormElement = {
                ...updatedForm[field]
            };
            updatedFormElement.isValid = true;
            updatedFormElement.message = '';
            updatedForm[field] = updatedFormElement;
        }

        for (let fieldError of error.fieldErrors) {
            const updatedFormElement = {
                ...updatedForm[fieldError.field]
            };
            updatedFormElement.isValid = false;
            updatedFormElement.message = fieldError.message;
            updatedForm[fieldError.field] = updatedFormElement;
        }

        this.setState({formdata: updatedForm});
    };

    hideMessage = () => {
        this.setState({

            passwordAltered: false
        })
    };

    postDataHandler = (event) => {
        event.preventDefault();

        const pw = {
            currentPassword: this.state.formdata.currentPassword.value,
            newPassword: this.state.formdata.newPassword.value,
            newPasswordAgain: this.state.formdata.newPasswordAgain.value
        };
        console.log(pw);

        axios.post('/api/users/changePassword', pw)
            .then((response) => {

                this.setState({
                    formdata: {
                        currentPassword: {

                            isValid: true,
                            message: ''
                        },
                        newPassword: {

                            isValid: true,
                            message: ''
                        },
                        newPasswordAgain: {

                            isValid: true,
                            message: ''
                        }
                    },
                    passwordAltered: true
                })

            })
            .then(() => {
                setTimeout(() => {
                    this.hideMessage;
                    this.props.history.push('/myProfile');
                }, 2500)
            })
            .catch(error => {
                if (error.response.data.hasOwnProperty("fieldErrors")) {
                    this.validationHandler(error.response.data);
                } else {
                    this.setState(() => {
                        throw error;
                    })
                }
            });
    };


    render() {
        return (
            <form onSubmit={this.postDataHandler}>
                <div>
                    <label
                        className={this.state.formdata.currentPassword.isValid ? "control-label input-label" : "control-label input-label invalid-label"}>
                        Current Password:
                    </label>
                    <input
                        className={this.state.formdata.currentPassword.isValid ? "form-control my-input-field" : "form-control my-input-field is-invalid"}
                        type="text"
                        name="currentPassword"
                        value={this.state.formdata.currentPassword.value}
                        onChange={this.inputChangeHandler}
                    />
                    <span className="form-text invalid-feedback">{this.state.formdata.currentPassword.message}</span>
                </div>
                <div>
                    <label
                        className={this.state.formdata.newPassword.isValid ? "control-label input-label" : "control-label input-label invalid-label"}>
                        New Password:
                    </label>
                    <input
                        className={this.state.formdata.newPassword.isValid ? "form-control my-input-field" : "form-control my-input-field is-invalid"}
                        type="text"
                        name="newPassword"
                        value={this.state.formdata.newPassword.value}
                        onChange={this.inputChangeHandler}
                    />
                    <span className="form-text invalid-feedback">{this.state.formdata.newPassword.message}</span>
                </div>

                <div>
                    <label
                        className={this.state.formdata.newPasswordAgain.isValid ? "control-label input-label" : "control-label input-label invalid-label"}>
                        New Password Again:
                    </label>
                    <input
                        className={this.state.formdata.newPasswordAgain.isValid ? "form-control my-input-field" : "form-control my-input-field is-invalid"}
                        type="text"
                        name="newPasswordAgain"
                        value={this.state.formdata.newPasswordAgain.value}
                        onChange={this.inputChangeHandler}
                    />
                    <span className="form-text invalid-feedback">{this.state.formdata.newPasswordAgain.message}</span>
                </div>
                <div
                    className="password-saved">{this.state.passwordAltered ? "The password has been altered" : ""}
                </div>
                <br/>
                <button className="btn btn-success my-button" type="submit">Save new password</button>
            </form>
        )
    }


}

export default ChangePassword