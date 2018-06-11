import React, {Component} from 'react';
import axios from 'axios';
import './productDetails.css';
import {Link} from 'react-router-dom';
import CommentListItem from '../../components/CommentListItem/CommentListItem';

class ProductDetails extends Component {
    constructor(props) {
        super(props);
        document.title = 'Product details';
    }


    state = {
        comments: [],
        product: {},
        productId: '',
        commentForm: {
            author: {
                value: localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')).name : ''
                ,
                isValid: true,
                message: ''
            },
            text: {
                value: '',
                isValid: true,
                message: ''
            }
        }
    };

    inputChangeHandler = (event) => {
        const target = event.target;
        const updatedCommentForm = {
            ...this.state.commentForm
        };
        const updatedFormElement = {
            ...updatedCommentForm[target.name]
        };

        let value;
        value = target.value;

        updatedFormElement.value = value;
        updatedFormElement.isValid = true;
        updatedCommentForm[target.name] = updatedFormElement;

        this.setState({...this.state, commentForm: updatedCommentForm});
    };

    postDataHandler = (event) => {
        event.preventDefault();

        const commentData = {};
        for (let formElementIdentifier in this.state.commentForm) {
            commentData[formElementIdentifier] = this.state.commentForm[formElementIdentifier].value;
        }

        commentData.productId = this.props.match.params.id;

        axios.post('/api/comments/', commentData)
            .then((response) => {
                console.log(response);
                this.setState({
                    ...this.state,
                    commentForm: {
                        author: {
                            value: JSON.parse(localStorage.getItem('user')).name,
                            isValid: true,
                            message: ''
                        },
                        text: {
                            value: '',
                            isValid: true,
                            message: ''
                        }
                    }
                });
            })
            .then(() => {
                this.getProductComments();
            })
            .catch((error) => {
                console.log(error.response);
                if (error.response.data.hasOwnProperty("fieldErrors")) {
                    this.validationHandler(error);
                } else {
                    this.setState(()=>{
                        throw error;
                    })
                }
            });
    };

    validationHandler = (error) => {
        const updatedCommentForm = {
            ...this.state.commentForm
        };

        for (let field in this.state.productForm) {
            const updatedFormElement = {
                ...updatedCommentForm[field]
            };
            updatedFormElement.isValid = true;
            updatedFormElement.message = '';
            updatedCommentForm[field] = updatedFormElement;
        }

        if (error.response.data.hasOwnProperty('fieldErrors')) {
            for (let fieldError of error.response.data.fieldErrors) {
                const updatedFormElement = {
                    ...updatedCommentForm[fieldError.field]
                };
                updatedFormElement.isValid = false;
                updatedFormElement.message = fieldError.message;
                updatedCommentForm[fieldError.field] = updatedFormElement;
            }

            this.setState({...this.state, commentForm: updatedCommentForm});
        } else {
            this.setState({
                ...this.state,
                commentForm: {
                    text: {
                        value: '',
                        isValid: false,
                        message: 'Please don\'t mess with my input fields'
                    }
                }
            })
        }
    };

    getProductDetail = () => {
        axios.get('/api/products/' + this.props.match.params.id)
            .then(response => {
                console.log(response.data);
                this.setState({
                    ...this.state,
                    product: response.data,
                    productId: this.props.match.params.id
                })
            })
            .then(() => {
                this.getProductComments();
                console.log(this.state.product)
            })
            .catch(error => {
                console.log(error);
                this.setState(()=> {
                    throw error;
                })
            });


    };

    getProductComments = () => {
        axios.get('/api/comments/list/' + this.props.match.params.id)
            .then(response => {
                console.log(response.data);
                this.setState({
                    ...this.state,
                    comments: response.data
                })
            })
            .catch(error => {
                console.log(error);
                this.setState(()=> {
                    throw error;
                })
            });
    };

    componentDidMount() {
        this.getProductDetail();

    }


    render() {
        let comments;

        comments = this.state.comments.map((comment) => {
            return (
                <CommentListItem
                    key={comment.id}
                    author={comment.author}
                    text={comment.text}
                    createdAt={comment.createdAt}
                />
            );
        });


        return (
            <div>
                <div className="product-description-container">
                    <h3><Link to={"/products/" + this.state.product.categoryId}>{this.state.product.categoryName}</Link>
                        : {this.state.product.name}</h3>
                    <p>{this.state.product.description}</p>
                    <span>${this.state.product.price}</span>
                    <br/>
                    <img src={this.state.product.picture} alt="product pic" width="50%" height="50%"/>
                </div>





                <div className="table-container" style={localStorage.user ?  {} : {display: 'none'}}>
                    <hr/>
                    <h4>Leave review:</h4>
                    <br/>
                    <form onSubmit={this.postDataHandler}>
                        <div>
                            <label
                                className={this.state.commentForm.author.isValid ? "control-label input-label" : "control-label input-label invalid-label"}>
                                Author:</label>
                            <input
                                className={this.state.commentForm.author.isValid ? "form-control my-input-field" : "form-control my-input-field is-invalid"}
                                name="author"
                                value={this.state.commentForm.author.value}
                                onChange={this.inputChangeHandler}
                                disabled
                            />
                            <span className="form-text invalid-feedback">{this.state.commentForm.author.message}</span>
                        </div>
                        <div>
                            <label
                                className={this.state.commentForm.text.isValid ? "control-label input-label" : "control-label input-label invalid-label"}>
                                Text:</label>
                            <input
                                className={this.state.commentForm.text.isValid ? "form-control my-input-field" : "form-control my-input-field is-invalid"}
                                name="text"
                                value={this.state.commentForm.text.value}
                                onChange={this.inputChangeHandler}
                            />
                            <span className="form-text invalid-feedback">{this.state.commentForm.text.message}</span>
                        </div>
                        <br/>
                        <button className="btn btn-info my-button" type="submit">Add review</button>
                    </form>
                </div>





                <div className="table-container">
                    <h4>Review:</h4>
                    <hr/>
                    <table>
                        <tbody>
                        {comments}
                        </tbody>
                    </table>
                </div>
            </div>
        )
    }
}

export default ProductDetails;