import * as React from 'react';
import Box from '@mui/material/Box';
import { Link } from 'react-router-dom';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import MenuAppBar from "../components/MenuAppBar";

export function MainPage () {
    return(
        <div className='App'>
            <MenuAppBar showHomeButton={false} title="Airline Tickets" />
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '20vh' }}>
                <div style={{textAlign:'center'}}>
                    <h1>main page<br/></h1>
                </div>
            </div>
        </div>
    );
}