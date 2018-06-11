import React from 'react';
import 'react-notifications/lib/notifications.css';
import './App.css';
import Layout from './common/Layout';
import {BrowserRouter} from 'react-router-dom';
import ErrorBoundry from "./common/ErrorBoundry";
import {NotificationContainer} from "react-notifications";

const app = () => (
    <BrowserRouter>
        <div className="App">
            <ErrorBoundry>
                <Layout/>
                <NotificationContainer/>
            </ErrorBoundry>
        </div>
    </BrowserRouter>
);

export default app;
