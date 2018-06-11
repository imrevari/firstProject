import React, {Component} from 'react';
import axios from 'axios';

class Confirmation extends Component {

    state = {
        confirmationCode: {
            value: '',
            isValid: true,
            message: ''
        }
    };

    inputChangeHandler = (event) => {
        const updatedConfirmationCode = {...this.state};
        const updatedFormElement = {...updatedConfirmationCode[event.target.name]};
        updatedFormElement.value = event.target.value;
        updatedFormElement.isValid = 'true';
        updatedConfirmationCode[event.target.name] = updatedFormElement;
        this.setState(updatedConfirmationCode);
    };

    validationHandler = (error) => {
        const updatedConfirmationCode = {...this.state};

        for (let field in this.state) {
            const updatedFormElement = {
                ...updatedConfirmationCode[field]
            };
            updatedFormElement.isValid = true;
            updatedFormElement.message = '';
            updatedConfirmationCode[field] = updatedFormElement;
        }

        for (let fieldError of error.fieldErrors) {
            const updatedFormElement = {
                ...updatedConfirmationCode[fieldError.field]
            };
            updatedFormElement.isValid = false;
            updatedFormElement.message = fieldError.message;
            updatedConfirmationCode[fieldError.field] = updatedFormElement;
        }

        this.setState(updatedConfirmationCode);
    };

    confirmSubmit = (event) => {
        event.preventDefault();

        const value = this.state.confirmationCode.value;
        const formData = {confirmationCode: value};

        axios({
            method: 'POST',
            url: '/api/users/verification',
            data: formData
        }).then(() => {
            this.props.history.push('/')
        })
        // catch ág hozzáadása, eddig ezek nem voltak, error handling miatt
            .catch(error => {
                console.log(error.response);
                if (error.response.data.hasOwnProperty("fieldErrors")) {
                    this.validationHandler(error.response.data);
                } else {
                    this.setState(() => {
                        throw error;
                    })
                }
            })
    };

    render() {
        return (
            <div className="container">
                <h2>Confirmation Page</h2>
                <hr/>
                <br/>
                <h4>We sent you an e-mail with a verification code.</h4>
                <form onSubmit={this.confirmSubmit}>
                    <div className={this.state.confirmationCode.isValid ? null : "has-error"}>
                        <label className="control-label input-label">Confirmation code</label>
                        <input className="form-control my-input-field "
                               name="confirmationCode"
                               onChange={this.inputChangeHandler}
                               value={this.state.confirmationCode.value}/>
                        <span className="help-block">{this.state.confirmationCode.message}</span>
                    </div>
                    <button className="btn btn-primary my-buttons" type="submit">Confirm</button>
                </form>
            </div>
        )

    }

}

export default Confirmation;