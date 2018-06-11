import React from 'react';
import './CommentListItem.css';

const commentListItem = (props) => (
    <tr>
        <td className="comment-author" align="left">{props.author}</td>
        <td className="comment-text" align="left">{props.text}</td>
        <td className="comment-createdAt" align="left">{props.createdAt}</td>
    </tr>
);

export default commentListItem;