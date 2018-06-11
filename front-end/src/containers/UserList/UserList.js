import React, {Component} from 'react';
import axios from 'axios';

import UserListItem from '../../components/UserListItem/UserListItem'

class UserList extends Component {

    constructor(props) {
        super(props);
        document.title = 'List of users';
    }

    state = {
        users: []
    };

    updateUsers = () => {
        axios.get('/api/users/list')
            .then(response => {

                this.setState({
                    users: response.data
                })
            })
            .catch(error => {
                console.log(error);
                this.setState(() => {
                    throw error;
                })
            });

    };

    // editUser = (id) => {
    //
    //     this.props.history.push('/editProfile/' + id);
    // }

    componentDidMount() {
        this.updateUsers();
    }

    render() {



        let usersToDesplay;

        usersToDesplay = this.state.users.map((user) => {



            return (
                <UserListItem
                    key={user.id}
                    id={user.id}
                    name={user.name}
                    email={user.email}
                    role={user.role}
                    // editUser={() => this.editUser(user.id)}

                />
            );
        });

        return (

            <div>
                <h2>List of users</h2>

                <table className="table table-bordered table-striped">
                    <thead>
                    <tr className="success">
                        <th className="col-md-2">Name</th>
                        <th className="col-md-3">Email</th>
                        <th className="col-md-2">Role</th>

                    </tr>
                    </thead>
                    <tbody>
                    {usersToDesplay}
                    </tbody>
                </table>
                <br/>

            </div>
        )
    }


}

export default UserList;