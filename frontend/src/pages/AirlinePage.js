import * as React from 'react';
import { FindAllAirlines, GetAirlineByNameAndProcessIt, AddAirline} from '../components/controller/AirlineController';
import MenuAppBar from "../components/MenuAppBar";

const leftComponentStyle = {
    width: '50%',
    paddingLeft:'50px',
    paddingRight:'15px'
}

const rightComponentStyle = {
    width: '50%',
    paddingLeft:'15px',
    paddingRight:'50px'
}

export function AirlinePage() {
    return (
        <div className='App'>
            <MenuAppBar showHomeButton={true} title="Airlines" />
            <div style={{ display: 'flex', width: '100%' }}>
                <div style={leftComponentStyle}>
                    <FindAllAirlines />
                </div>
                <div style={rightComponentStyle}>
                    <AddAirline />
                    <GetAirlineByNameAndProcessIt />
                </div>
            </div>
        </div>
    );
}
