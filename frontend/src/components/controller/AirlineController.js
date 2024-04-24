import * as React from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import {Container, Paper, Button} from '@mui/material';
import Typography from "@mui/material/Typography";

export function FindAllAirlines() {
    const paperStyle = {padding: "5px 20px", width: 600, margin: "20px auto"}
    const [airlines, setAirlines] = React.useState([])

    const refreshAirlineList = () => {
        fetch("http://localhost:8080/api/v1/airlines", {
            method: "GET"
        })
            .then(res => res.json())
            .then(result => {
                setAirlines(result);
            })
            .catch(error => {
                console.error('Error refreshing list of airlines:', error);
            });
    };

    React.useEffect(() => {
        refreshAirlineList();
    }, []);

    return (
        <Paper elevation={3} style={paperStyle}>
            <h1>LIST ALL AIRLINES:</h1>
            {airlines.map(airline => (
                <div key={airline.id} style={{margin: "10px"}}>
                    <Paper elevation={6} style={{padding: "15px", textAlign: "left"}}>
                        <div>Id: {airline.id}</div>
                        <div>Name: {airline.name}</div>
                    </Paper>
                </div>
            ))}
            <Button variant="outlined" color="primary" style={{margin: "20px"}} onClick={refreshAirlineList}>
                View all airlines
            </Button>
        </Paper>
    )
}

// export function GetAirlineByName() {
//     const paperStyle = { padding: "5px 20px", width: 600, margin: "20px auto" };
//     const [airlineName, setAirlineName] = React.useState("");
//     const [airline, setAirline] = React.useState(null);
//
//     const getAirline = (name) => {
//         fetch(("http://localhost:8080/api/v1/airlines/" + name), {
//             method: "GET"
//         })
//             .then(res => {
//                 if (res.ok) {
//                     return res.json();
//                 } else {
//                     throw new Error("Airline not found");
//                 }
//             })
//             .then(result => {
//                 setAirline(result);
//             })
//             .catch(error => {
//                 setAirline(null);
//             });
//     };
//
//     return (
//         <Paper elevation={3} style={paperStyle}>
//             <h1>FIND AIRLINE BY NAME:</h1>
//             {airline && (
//                 <Paper elevation={6} style={{ margin: "50px", padding: "15px", textAlign: "left" }} key={airline.id}>
//                     Id: {airline.id} <br />
//                     Name: {airline.name} <br />
//                 </Paper>
//             )}
//             <TextField id="outlined-basic" label="Enter airline ID" variant="outlined"
//                        value={airlineName}
//                        onChange={(e) => setAirlineName(e.target.value)}
//             />
//             <br />
//             <Button variant="contained" style={{ margin: "20px" }} onClick={() => getAirline(airlineName)}>
//                 FIND
//             </Button>
//         </Paper>
//     );
// }

export function GetAirlineByName() {
    const containerStyle = { display: 'flex', flexDirection: 'column', alignItems: 'center' };
    const paperStyle = { padding: "5px 20px", width: 600, margin: "20px auto" };
    const buttonStyle = { margin: "10px" };
    const [airlineName, setAirlineName] = React.useState("");
    const [airline, setAirline] = React.useState(null);
    const [showButtons, setShowButtons] = React.useState(false);

    const getAirline = (name) => {
        fetch(("http://localhost:8080/api/v1/airlines/" + name), {
            method: "GET"
        })
            .then(res => {
                if (res.ok) {
                    return res.json();
                } else {
                    throw new Error("Airline not found");
                }
            })
            .then(result => {
                setAirline(result);
                setShowButtons(true); //
            })
            .catch(error => {
                setAirline(null);
                setShowButtons(false); //
            });
    };

    const handleSelect = () => {
        //
        console.log("Выбрана авиакомпания:", airline);
    };

    const handleUpdate = () => {
        //
        console.log("Обновить авиакомпанию:", airline);
    };

    const handleDelete = () => {
        //
        console.log("Удалить авиакомпанию:", airline);
    };

    return (
        <Container style={containerStyle}>
            <Paper elevation={3} style={paperStyle}>
                <h1>FIND AIRLINE BY NAME:</h1>
                <TextField id="outlined-basic" label="Enter airline ID" variant="outlined"
                           value={airlineName}
                           onChange={(e) => setAirlineName(e.target.value)}
                           style={{ marginBottom: '20px' }}
                />
                <Button variant="contained" style={{ margin: "20px" }} onClick={() => getAirline(airlineName)}>
                    FIND
                </Button>
                {airline && (
                    <>
                        <Paper elevation={6} style={{ margin: "50px", padding: "15px", textAlign: "left" }} key={airline.id}>
                            Id: {airline.id} <br />
                            Name: {airline.name} <br />
                        </Paper>
                        {showButtons && (
                            <>
                                <Button variant="contained" style={buttonStyle} onClick={handleSelect}>
                                    Выбрать
                                </Button>
                                <Button variant="contained" style={buttonStyle} onClick={handleUpdate}>
                                    Изменить
                                </Button>
                                <Button variant="contained" style={buttonStyle} onClick={handleDelete}>
                                    Удалить
                                </Button>
                            </>
                        )}
                    </>
                )}
            </Paper>
        </Container>
    );
}




export function AddAirline() {
    const paperStyle = {padding:"20px 20px", width:600, margin:"20px auto"}
    const [name,setName] = React.useState()

    const handleClick = (e) => {
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
        <Container>
            <Paper elevation={3} style={paperStyle}>
                <Box
                    component="form"
                    sx={{
                        '& > :not(style)': { m: 1 },
                    }}
                    noValidate
                    autoComplete="off"
                >
                    <h1>Create new airline</h1>
                    <TextField id="outlined-basic" label="City" variant="outlined"
                               value={name}
                               onChange={(e)=>setName(e.target.value)}
                    />
                    <h1>{name}</h1>
                    <Button variant="contained" onClick={handleClick}>
                        SAVE
                    </Button>
                </Box>
            </Paper>
        </Container>
    );
}

export function DeleteAirline(){
    const paperStyle = {padding:"5px 20px", width:600, margin:"20px auto"}
    const [airlineName,setAirlineName] = React.useState([])

    const Delete = (name) => {
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

    return (
        <Container>
            <Paper elevation={3} style={paperStyle}>
                <Box
                    component="form"
                    sx={{
                        '& > :not(style)': { m: 1 },
                    }}
                    noValidate
                    autoComplete="off"
                >
                    <h1>DELETE Airline BY NAME</h1>
                    <TextField id="outlined-basic" label="Enter airline name" variant="outlined"
                               value={airlineName}
                               onChange={(e)=>setAirlineName(e.target.value)}
                    />
                    <br />
                    <Button variant="contained" onClick={() => Delete(airlineName)}>
                        Delete
                    </Button>
                </Box>
            </Paper>
        </Container>
    );
}

export function UpdateAirline() {
    const paperStyle = { padding: "20px 20px", width: 600, margin: "20px auto" };
    const [id, setId] = React.useState('');
    const [name, setName] = React.useState('');

    const handleClick = (e) => {
        e.preventDefault();
        const updatedAirline = { id, name };
        console.log(updatedAirline);
        fetch(`http://localhost:8080/api/v1/airlines/update_airline`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(updatedAirline)
        }).then(() => console.log("Airline updated"));
    };

    return (
        <Container>
            <Paper elevation={3} style={paperStyle}>
                <Box
                    component="form"
                    sx={{
                        '& > :not(style)': { m: 1 },
                    }}
                    noValidate
                    autoComplete="off"
                >
                    <h1>Update airline</h1>
                    <TextField id="outlined-basic" label="Id" variant="outlined"
                               value={id}
                               onChange={(e) => setId(e.target.value)}
                    />
                    <TextField id="outlined-basic" label="Name" variant="outlined"
                               value={name}
                               onChange={(e) => setName(e.target.value)}
                    />
                    <Button variant="contained" onClick={handleClick}>
                        UPDATE
                    </Button>
                </Box>
            </Paper>
        </Container>
    );
}

