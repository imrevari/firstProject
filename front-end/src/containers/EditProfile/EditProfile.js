import React, {Component} from 'react';
import axios from 'axios';


class EditProfile extends Component{

    constructor(props) {
        super(props);
        document.title = 'Edit profile of user ' + this.props.location.state.name;
    }

    state = {
        roles: [],
        accountDetails: {}
    };

    getUserByParamsId = () => {
        axios.get('/api/users/user/' + this.props.match.params.id)
            .then((response) => {

                this.setState({accountDetails: response.data});
            })
            .catch(error => {
                this.setState(() => {
                    throw error;
                })
            })
    };

    componentDidMount(){

        axios.get('/api/users/roles')
            .then((response) => {
                this.setState({
                    roles: response.data

                })
            });



        this.getUserByParamsId();
    }

    inputChangedHandler = (event) => {
        const target = event.target;

        const updatedUser = {
            ...this.state.accountDetails
        };

        updatedUser[target.name] = target.value;

        this.setState({accountDetails: updatedUser});

    }

    postDataHandler = (event) => {
        event.preventDefault();

        const formData = {};
        for (let formElementIdentifier in this.state.accountDetails) {
            formData[formElementIdentifier] = this.state.accountDetails[formElementIdentifier];
        }

        let url = '/api/users/update';
        let method = 'post';


        axios({method: method, url: url, data: formData})
            .then(response => {
                // console.log(response);
                this.props.history.push('/listUsers')
            })
            .catch(error => {
                console.log(error.response);
                // this.validationHandler(error.response.data);
            });
    };

    render(){

        console.log(this.state);


        return(

            <div className="container">


                <h2>Profile of {this.state.accountDetails.name}</h2>
                <hr/>
                <br/>

                <form onSubmit={this.postDataHandler}>


                <label className="input-label">User Name:</label>
                <input className="my-input-field my-input-field-disabled" name="username" disabled="true"
                       placeholder={this.state.accountDetails.name}/>
                <br/>
                <label className="input-label">Email:</label>
                <input className="my-input-field my-input-field-disabled" name="email" disabled="true"
                       placeholder={this.state.accountDetails.email}/>

                <br/>
                <label className="input-label">Role:</label>
                <select name="role"
                        value={this.state.accountDetails.role}
                        onChange={this.inputChangedHandler}
                        className="my-input-field">

                    {Object.entries(this.state.roles).map(([key, value]) => {
                        return <option key={key} value={key}>{value}</option>
                    })}
                </select>

                    <br/>

                    <button type="submit" className="btn btn-primary">Submit</button>

                </form>
            </div>

        )



    }



}

export default EditProfile;