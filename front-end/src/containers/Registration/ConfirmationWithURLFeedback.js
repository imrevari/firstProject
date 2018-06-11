import React, {Component} from 'react';
import axios from 'axios';

class ConfirmationWithURLFeedback extends Component {
    state = {
        message: '',
        isValid: true
    };

    componentDidMount() {
        axios.get('/api/users/verification?confirmationCode=' + this.props.match.params.confirmationCode)
            .then((response) => {
                this.setState({
                    message: 'Thank you, the registration is successful!'
                });
            })
            .catch((error) => {
                if (error.response.data.hasOwnProperty("fieldErrors")) {
                this.setState({
                    message: error.response.data.fieldErrors[0].message,
                    isValid: false
                })
                } else {
                    this.setState(()=> {
                        throw  error;
                    })
                }
            })
    }

    render() {
        return (
            <p className={this.state.isValid ? "text-success" : "text-danger"}>{this.state.message}</p>
        );
    };
}

export default ConfirmationWithURLFeedback