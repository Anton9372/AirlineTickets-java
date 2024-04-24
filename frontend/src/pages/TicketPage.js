import * as React from 'react';
import Box from '@mui/material/Box';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import { Link } from 'react-router-dom';
import MenuAppBar from "../components/MenuAppBar";

export function TicketPage () {
    return(
        <div className='App'>
            <MenuAppBar showHomeButton={true} title="Tickets" />
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '20vh' }}>
                <div style={{textAlign:'center'}}>
                    <h1>ticket page<br/></h1>
                </div>
            </div>
        </div>
    );
}