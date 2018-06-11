import React, {Component} from 'react';
import axios from 'axios';

class NewProduct extends Component {
    constructor(props) {
        super(props);
        document.title = 'New product';
    }

    state = {
        categories: [],
        productForm: {
            name: {
                value: '',
                isValid: true,
                message: ''
            },
            description: {
                value: '',
                isValid: true,
                message: ''
            },
            price: {
                value: '',
                isValid: true,
                message: ''
            },
            picture: {
                value: '',
                isValid: true,
                message: ''
            },
            categoryName: {
                value: '',
                isValid: true,
                message: ''
            }
        }
    };

    fileInput;

    inputChangeHandler = (event) => {
        const target = event.target;
        const updatedProductForm = {
            ...this.state.productForm
        };
        const updatedFormElement = {
            ...updatedProductForm[target.name]
        };

        let value;
        value = target.value;

        updatedFormElement.value = value;
        updatedFormElement.isValid = true;
        updatedProductForm[target.name] = updatedFormElement;

        this.setState({...this.state, productForm: updatedProductForm});
    };

    postDataHandler = (event) => {
        event.preventDefault();

        const formData = {};
        for (let formElementIdentifier in this.state.productForm) {
            formData[formElementIdentifier] = this.state.productForm[formElementIdentifier].value;
        }

        let url = '/api/products';
        let method = 'post';
        const id = this.props.match.params.id;
        if (id) {
            url += '/' + id;
            method = 'put';
        }

        let file = new FormData();
        if (this.fileInput.files[0]) {
            file.append("file", this.fileInput.files[0]);
            this.uploadPicAndForm(file, formData, method, url);
        } else {
            if (this.state.productForm.picture.value) {
                formData.picture = this.state.productForm.picture.value;
            } else {
                formData.picture = "https://res.cloudinary.com/dw5zr3uob/image/upload/v1527152615/no-image.png";
            }
            this.uploadForm(formData, method, url);
        }
    };

    uploadForm = (formData, method, url) => {
        axios({method: method, url: url, data: formData})
            .then((response) => {
                // console.log(response);
            })
            .then(() => {
                this.props.history.push('/')
            })
            .catch((error) => {
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

    uploadPicAndForm = (file, formData, method, url) => {
        axios.post('/api/products/uploadPic', file, {withCredentials: true})
            .then(response => {
                // console.log(response);
                formData.picture = response.data;
                this.uploadForm(formData, method, url);
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

    validationHandler = (error) => {
        const updatedProductForm = {
            ...this.state.productForm
        };

        for (let field in this.state.productForm) {
            const updatedFormElement = {
                ...updatedProductForm[field]
            };
            updatedFormElement.isValid = true;
            updatedFormElement.message = '';
            updatedProductForm[field] = updatedFormElement;
        }

        if (error.response.data.hasOwnProperty('fieldErrors')) {
            for (let fieldError of error.response.data.fieldErrors) {
                const updatedFormElement = {
                    ...updatedProductForm[fieldError.field]
                };
                updatedFormElement.isValid = false;
                updatedFormElement.message = fieldError.message;
                updatedProductForm[fieldError.field] = updatedFormElement;
            }

            this.setState({...this.state, productForm: updatedProductForm});
        } else {
            this.setState({
                ...this.state,
                productForm: {
                    categoryName: {
                        value: '',
                        isValid: false,
                        message: 'Please don\'t mess with my input fields'
                    }
                }
            })
        }
    };

    getCategoryNames = () => {
        axios.get('/api/categories/names')
            .then((response) => {
                this.setState({
                    ...this.state,
                    categories: response.data
                })
            })
            .catch(error => {
                this.setState(() => {
                    throw error;
                })
            })
        ;
    };

    getProductByParamsId = () => {
        axios.get('/api/products/' + this.props.match.params.id)
            .then((response) => {
                const updatedProductForm = {
                    ...this.state.productForm
                };

                for (let field in response.data) {
                    const updatedFormElement = {
                        ...updatedProductForm[field]
                    };
                    updatedFormElement.value = response.data[field];
                    updatedProductForm[field] = updatedFormElement;
                }

                this.setState({...this.state, productForm: updatedProductForm});
            })
            .catch(error => {
                this.setState(() => {
                    throw error;
                })
            })
    };

    componentWillReceiveProps() {

        const clearState = {...this.state.productForm};

        var updatedFormElement = {};

        updatedFormElement.value = '';

        updatedFormElement.isValid = true;

        updatedFormElement.message = '';

        for (let formElementIdentifier in clearState) {
            clearState[formElementIdentifier] = updatedFormElement;
        }

        console.log(clearState);

        this.setState({
            ...this.state,
            productForm: clearState
        })


    }

    componentDidMount() {
        this.getCategoryNames();

        if (this.props.match.params.id) {
            this.getProductByParamsId();
        }
    }

    render() {
        return (
            <div className="container">
                <h2>{this.props.match.params.id != null ? "Edit" : "New"} Product</h2>
                <hr/>
                <br/>
                <form onSubmit={this.postDataHandler}>
                    <div className="form-group">
                        <label
                            className={this.state.productForm.name.isValid ? "control-label input-label" : "control-label input-label invalid-label"}>
                            Product Name:</label>
                        <input
                            className={this.state.productForm.name.isValid ? "form-control my-input-field" : "form-control my-input-field is-invalid"}
                            name="name"
                            value={this.state.productForm.name.value}
                            onChange={this.inputChangeHandler}
                        />
                        <span className="form-text invalid-feedback">{this.state.productForm.name.message}</span>
                    </div>
                    <div className="form-group">
                        <label
                            className={this.state.productForm.description.isValid ? "control-label input-label" : "control-label input-label invalid-label"}>Description:</label>
                        <input
                            className={this.state.productForm.description.isValid ? "form-control my-input-field" : "form-control my-input-field is-invalid"}
                            name="description"
                            value={this.state.productForm.description.value}
                            onChange={this.inputChangeHandler}
                        />
                        <span className="form-text invalid-feedback">{this.state.productForm.description.message}</span>
                    </div>
                    <div className="form-group">
                        <label
                            className={this.state.productForm.price.isValid ? "control-label input-label" : "control-label input-label invalid-label"}>Price:</label>
                        <input
                            className={this.state.productForm.price.isValid ? "form-control my-input-field" : "form-control my-input-field is-invalid"}
                            type="number"
                            step="0.01"
                            min="0.01"
                            id="price"
                            name="price"
                            value={this.state.productForm.price.value}
                            onChange={this.inputChangeHandler}
                        />
                        <span className="form-text invalid-feedback">{this.state.productForm.price.message}</span>
                    </div>
                    <div className="form-group">
                        <label
                            className={this.state.productForm.categoryName.isValid ? "control-label input-label" : "control-label input-label invalid-label"}>Category:</label>
                        <select
                            className={this.state.productForm.categoryName.isValid ? "form-control my-input-field" : "form-control my-input-field is-invalid"}
                            id="categoryName"
                            name="categoryName"
                            value={this.state.productForm.categoryName.value}
                            onChange={this.inputChangeHandler}>
                            <option key="" value=""></option>
                            {this.state.categories.map((value, idx) => {
                                return <option key={idx} value={value}>{value}</option>
                            })}
                        </select>
                        <span
                            className="form-text invalid-feedback">{this.state.productForm.categoryName.message}</span>
                    </div>
                    <div className="form-group">
                        <label htmlFor="input-file" className="control-label input-label">Image File:</label>
                        <input
                            id="input-file"
                            type="file"
                            ref={input => {
                                this.fileInput = input;
                            }}/>
                        <p className="help-block">Please, don't upload file if don't want to modify pic!</p>
                    </div>
                    <br/>
                    <button className="btn btn-info my-button" type="submit">Save</button>
                </form>
            </div>
        )
    }
}

export default NewProduct;