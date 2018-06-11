import React, {Component} from 'react';
import axios from 'axios';

class ProfileForm extends Component {

    state = {};


    postDataHandler = (event) => {
        event.preventDefault();
    };

    inputChangeHandler = (event) => {

    };

    render() {
        return (
            <form onSubmit={this.postDataHandler}>
                <div>
                    <input
                        className="form-control my-input-field my-input-field-disabled"
                        name="id"
                        value={this.props.id}
                        onChange={this.inputChangeHandler}
                        hidden="true"/>
                </div>
                <div>
                    <label className="control-label input-label">User Name:</label>
                    <input
                        className="form-control my-input-field my-input-field-disabled"
                        name="name"
                        value={this.props.name}
                        onChange={this.inputChangeHandler}
                        disabled="true"/>
                </div>
                <div>
                    <label className="control-label input-label">Email:</label>
                    <input
                        className="form-control my-input-field my-input-field-disabled"
                        name="email"
                        value={this.props.email}
                        onChange={this.inputChangeHandler}
                        disabled="true"/>
                </div>
                <div>
                    <label className="control-label input-label">Role:</label>
                    <input
                        className="form-control my-input-field my-input-field-disabled"
                        name="role"
                        value={this.props.role}
                        onChange={this.inputChangeHandler}
                    />
                </div>
                <br/>
                <button className="btn btn-info my-button" type="submit">Save</button>
            </form>
        );
    }
}

export default ProfileForm