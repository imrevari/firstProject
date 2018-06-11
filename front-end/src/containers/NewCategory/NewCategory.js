import React, {Component} from 'react';
import axios from 'axios';
import './NewCategory.css';

class NewCategory extends Component {
    constructor(props) {
        super(props);
        document.title = 'New category';
    }

    state = {
        categoryForm: {
            name: {
                value: '',
                isValid: true,
                message: ''
            }
        },
        categorySaved: false
    };

    inputChangeHandler = (event) => {
        const target = event.target;
        const updatedCategoryForm = {
            ...this.state.categoryForm
        };
        const updatedFormElement = {
            ...updatedCategoryForm[target.name]
        };

        let value;
        value = target.value;

        updatedFormElement.value = value;
        updatedFormElement.isValid = true;
        updatedCategoryForm[target.name] = updatedFormElement;

        this.setState({...this.state, categoryForm: updatedCategoryForm});
    };

    postDataHandler = (event) => {
        event.preventDefault();

        const formData = {};
        for (let formElementIdentifier in this.state.categoryForm) {
            formData[formElementIdentifier] = this.state.categoryForm[formElementIdentifier].value;
        }

        let url = '/api/categories';
        let method = 'post';
        const id = this.props.match.params.id;
        if (id) {
            url += '/' + id;
            method = 'put';
        }

        axios({method: method, url: url, data: formData})
            .then(() => {
                this.setState({
                    categoryForm: {
                        name: {
                            value: '',
                            isValid: true,
                            message: ''
                        }
                    },
                    emailSent: true
                })
            })
            .then(() => {
                this.props.history.push('/newCategory');
                setTimeout(this.hideMessage, 1500);
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

    hideMessage = () => {
        this.setState({
            ...this.state,
            emailSent: false
        })
    };

    validationHandler = (error) => {
        const updatedCategoryForm = {
            ...this.state.categoryForm
        };

        for (let field in this.state.categoryForm) {
            const updatedFormElement = {
                ...updatedCategoryForm[field]
            };
            updatedFormElement.isValid = true;
            updatedFormElement.message = '';
            updatedCategoryForm[field] = updatedFormElement;
        }

        if (error.response.data.hasOwnProperty('fieldErrors')) {
            for (let fieldError of error.response.data.fieldErrors) {
                const updatedFormElement = {
                    ...updatedCategoryForm[fieldError.field]
                };
                updatedFormElement.isValid = false;
                updatedFormElement.message = fieldError.message;
                updatedCategoryForm[fieldError.field] = updatedFormElement;
            }

            this.setState({...this.state, categoryForm: updatedCategoryForm});
        } else {
            this.setState({
                ...this.state,
                categoryForm: {
                    name: {
                        value: '',
                        isValid: false,
                        message: 'Please don\'t mess with my input fields'
                    }
                }
            })
        }
    };

    render() {
        return (
            <div className="container">
                <h2>New Category</h2>
                <hr/>
                <br/>
                <form onSubmit={this.postDataHandler}>
                    <div>
                        <label
                            className={this.state.categoryForm.name.isValid ? "control-label input-label" : "control-label input-label invalid-label"}>
                            Category Name:</label>
                        <input
                            className={this.state.categoryForm.name.isValid ? (this.state.emailSent ? "form-control my-input-field is-saved" : "form-control my-input-field") : "form-control my-input-field is-invalid"}
                            name="name"
                            value={this.state.categoryForm.name.value}
                            onChange={this.inputChangeHandler}
                        />
                        <span className="form-text invalid-feedback">{this.state.categoryForm.name.message}</span>
                    </div>
                    <div
                        className="category-saved">{this.state.emailSent && "Category has been saved successfully."}</div>
                    <br/>
                    <button className="btn btn-info my-button" type="submit">Save</button>
                </form>
            </div>
        )
    }
}

export default NewCategory;