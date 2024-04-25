import * as React from 'react';
import TextField from '@mui/material/TextField';
import {Paper, Button} from '@mui/material';


const paperStyle = {
    padding: "5px 0px",
    width: "100%",
    marginTop: "30px",
    backgroundColor: "inherit",
};

const elementStyle = {
    margin: "20px",
    padding: "15px",
    textAlign: "left"
}

const headerStyle = {
    marginBottom: "20px",
    marginTop: "20px"
}

const buttonStyle = {
    margin: "20px"
}

export function FindAllAirlines() {
    const [airlines, setAirlines] = React.useState([]);

    const refreshAirlineList = () => {
        fetch("http://localhost:8080/api/v1/airlines", {
            method: "GET"
        })
            .then(response => response.json())
            .then(result => {
                setAirlines(result);
                console.log("Airline list get success");
            })
            .catch(error => {
                console.error("Error refreshing list of airlines:", error);
            });
    };

    return (
        <Paper elevation={3} style={paperStyle}>
            <h1 style={headerStyle}>All airlines</h1>
            {airlines.map(airline => (
                <div>
                    <Paper elevation={6} style={elementStyle}>
                        <div>Id: {airline.id}</div>
                        <div>Name: {airline.name}</div>
                    </Paper>
                </div>
            ))}
            <Button variant="outlined" style={buttonStyle} onClick={refreshAirlineList}>
                View all airlines
            </Button>
        </Paper>
    )
}

export function GetAirlineByNameAndProcessIt() {
    const [airlineName, setAirlineName] = React.useState("");
    const [newAirlineName, setNewAirlineName] = React.useState("");
    const [airline, setAirline] = React.useState(null);
    const [showSelectButton, setShowSelectButton] = React.useState(false);
    const [showDeleteAndUpdateButtons, setShowDeleteAndUpdateButtons] = React.useState(false);
    const [showUpdateField, setShowUpdateField] = React.useState(false);

    const handleFind = (name) => {
        fetch(("http://localhost:8080/api/v1/airlines/" + name), {
            method: "GET"
        })
            .then(response => {
                if (response.ok) {
                    console.log("Airline get success");
                    return response.json();
                } else {
                    throw new Error("Airline not found");
                }
            })
                .then(result => {
                    setAirline(result);
                    setShowSelectButton(true);
                })
                .catch(error => {
                    setAirline(null);
                });
    };

    const handleUpdate = () => {
        console.log("Update airline:", airline);
        setShowDeleteAndUpdateButtons(false);
        setShowUpdateField(true);
    };

    const handleDelete = (name) => {
        console.log("Delete airline:", airline);
        fetch(`http://localhost:8080/api/v1/airlines/delete_airline/` + name, {
            method: "DELETE"
        })
            .then(response => {
                if (response.ok) {
                    console.log("Airline deleted successfully");
                } else {
                    console.error("Failed to delete airline");
                }
            })
            .catch(error => {
                console.error("Error deleting airline:", error);
            });
    };

    const update = (id, name) => {
        const updatedAirline = { id, name };
        console.log("Update airline:", updatedAirline);
        fetch(`http://localhost:8080/api/v1/airlines/update_airline`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(updatedAirline)
        })
            .then(() => console.log("Airline updated"))
            .catch(error => {
                console.error("Error updating airline:", error);
            });
        setShowUpdateField(false);
    }

    const handleSelect = () => {
        setShowDeleteAndUpdateButtons(true);
        setShowSelectButton(false);
        console.log("Airline selected:", airline);
    };

    return (
        <Paper elevation={3} style={paperStyle}>
            <h1 style={headerStyle}>Find airline</h1>
            <TextField name="outlined" label="Name" variant="outlined"
                       value={airlineName}
                       onChange={(e) => setAirlineName(e.target.value)}
            />
            {showUpdateField && (
                <>
                    <h1></h1>
                    <TextField name="outlined" label="New name" variant="outlined" style={{marginTop: "20px"}}
                               value={newAirlineName}
                               onChange={(e) => setNewAirlineName(e.target.value)}
                    />
                    <h1></h1>
                    <Button variant="outlined" style={buttonStyle} onClick={() => update(airline.id, newAirlineName)}>
                        Save
                    </Button>
                </>
            )}
            {airline && (
                <>
                    <Paper elevation={6} style={elementStyle}>
                        Id: {airline.id} <br/>
                        Name: {airline.name} <br/>
                    </Paper>
                    {showSelectButton && (
                        <>
                            <Button variant="outlined" style={buttonStyle} onClick={handleSelect}>
                                Select
                            </Button>
                        </>
                    )}
                    {showDeleteAndUpdateButtons && (
                        <>
                            <Button variant="outlined" style={buttonStyle} onClick={() => handleUpdate()}>
                                Update
                            </Button>
                            <Button variant="outlined" style={buttonStyle} onClick={() => handleDelete(airline.name)}>
                                Delete
                            </Button>
                        </>
                    )}
                </>
            )}
            <h1></h1>
            <Button variant="outlined" style={buttonStyle} onClick={() => handleFind(airlineName)}>
                FIND
            </Button>
        </Paper>

    );
}

export function AddAirline() {
    const [name, setName] = React.useState()

    const handleSaveButton = (e) => {
        e.preventDefault()
        const newAirline = {name}
        console.log(newAirline)
        fetch("http://localhost:8080/api/v1/airlines/save_airline",{
            method:"POST",
            headers:{"Content-Type":"application/json"},
            body:JSON.stringify(newAirline)
        }).then(()=>console.log("New airline added")
        )}

    return (
        <Paper elevation={3} style={paperStyle}>
            <h1 style={headerStyle}>Create new airline</h1>
            <TextField name="outlined" label="Name" variant="outlined"
                       value={name}
                       onChange={(e) => setName(e.target.value)}
            />
            <h1></h1>
            <Button variant="outlined" style={buttonStyle} onClick={handleSaveButton}>
                SAVE
            </Button>
        </Paper>
    )
}

