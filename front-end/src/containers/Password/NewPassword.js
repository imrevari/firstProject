import React, {Component} from 'react';
import axios from 'axios';
import './SavePassword.css'

class NewPassword extends Component {

    constructor(props) {
        super(props);
        document.title = 'New Password';
    }

    state = {
        email: {
            value: '',
            isValid: true,
            message: ''
        },
        emailSent : false
    };

    inputChangeHandler = (event) => {
        const updatedFromElement = {};
        updatedFromElement.value = event.target.value;
        updatedFromElement.isValid = true;
        this.setState({
            [event.target.name]: updatedFromElement
        })

    };

    validationHandler = (error) => {
        const updatedState = { ...this.state};

            const updatedFormElement = { ...updatedState.email}
            updatedFormElement.isValid = true;
            updatedFormElement.message = '';
            updatedState.email = updatedFormElement;

        for (let ferror of error.response.data.fieldErrors){
            const updatedFormElement = {...updatedState[ferror.field]}
            updatedFormElement.isValid = false;
            updatedFormElement.message = ferror.message;
            updatedState[ferror.field] = updatedFormElement;
        }
        this.setState(updatedState);
    };

    hideMessage = () => {
        this.setState({
            emailSent: false
        })
    };

    postDataHandler = (event) => {
        event.preventDefault();

        const pw = { email: this.state.email.value };

        axios.post('/api/users/newPassword', pw)
            .then((response)=>{
                    this.setState({
                        emailSent: true
                    })

            })
            .then(()=>{
                setTimeout( () => {
                    this.hideMessage;
                    this.props.history.push('/login');
                    }, 2500)
                })
            .catch(error => {
                console.log(error.response);
                if (error.response.data.hasOwnProperty("fieldErrors")) {
                    this.validationHandler(error);
                } else {
                    this.setState(() => {
                        throw error;
                    })
                }
            });
    };


    render() {
        console.log('in render beginning - email sent: ', this.state.emailSent);
        return (
            <form onSubmit={this.postDataHandler}>
            <div>
                <label
                    className={this.state.email.isValid ? "control-label input-label" : "control-label input-label invalid-label"}>
                    E-mail:
                </label>
                <input
                    className={this.state.email.isValid ? "form-control my-input-field" : "form-control my-input-field is-invalid"}
                    type="text"
                    name="email"
                    value={this.state.email.value}
                    onChange={this.inputChangeHandler}
                />
                <span className="form-text invalid-feedback">{this.state.email.message}</span>
            </div>
                <div
                    className="password-saved">{this.state.emailSent ? "An e-mail has been sent to the address above" : ""}
                    </div>
                <br/>
                <button className="btn btn-success my-button" type="submit">Send new password</button>
            </form>
        )
    }
}

export default NewPassword