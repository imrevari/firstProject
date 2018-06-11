import React from 'react';
import {Link} from 'react-router-dom';



const userListItem = (props) =>(


    <tr className="table-cell">

        <td >{props.name}</td>
        <td >{props.email}</td>
        <td >{props.role === 'ROLE_ADMIN' ? 'ADMIN' : 'USER'}</td>

        <td >
            <Link to={{
                pathname: '/editProfile/' + props.id,
                state: { name: props.name }
            }}>
                <button  className="btn btn-primary">
                    EDIT
                </button>
            </Link>

        </td>
    </tr>



)

export default userListItem;