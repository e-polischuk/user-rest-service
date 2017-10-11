class Save extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            user: {}
        };
        this.onSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();
        fetch('http://localhost:8080/user-rest-service/api/new', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: 0,
                name: document.getElementById('n_s').value,
                surname: document.getElementById('sn_s').value
            })
        }).then((resp) => {
            if(resp.ok) this.props.handler();
            document.getElementById('save').reset();
        })
    }

    render() {
        return (
        <form id="save" onSubmit={this.onSubmit}>
            <input id="n_s" type="text" placeholder="Name" maxlength="30"/>
            <input id="sn_s" type="text" placeholder="Surname" maxlength="30"/>
            <input type="submit" value="SAVE" />
        </form>
        );
    }
}

class Update extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            user: {}
        };
        this.onSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();
        var self = this;
        fetch('http://localhost:8080/user-rest-service/api/update', {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: document.getElementById('i_u').value,
                name: document.getElementById('n_u').value,
                surname: document.getElementById('sn_u').value
            })
        }).then((resp) => {
            if(resp.ok) this.props.handler();
            document.getElementById('update').reset();
        });
    }

    render() {
        return (
        <form id="update" onSubmit={this.onSubmit}>
            <input id="n_u" type="text" placeholder="Name" maxlength="30"/>
            <input id="sn_u" type="text" placeholder="Surname" maxlength="30"/>
            <input id="i_u" type="number" placeholder="ID" min="1" max="2000000000"/>
            <input type="submit" value="UPDATE" />
        </form>
        );
    }
}

class Delete extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            user: {}
        };
        this.onSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();
        var self = this;
        fetch('http://localhost:8080/user-rest-service/api/delete', {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: document.getElementById('i_d').value,
                name: '',
                surname: ''
            })
        }).then((resp) => {
            if(resp.ok) this.props.handler();
            document.getElementById('delete').reset();
        });
    }

    render() {
        return (
        <form id="delete" onSubmit={this.onSubmit}>
            <input id="i_d" type="number" placeholder="ID" min="1" max="2000000000"/>
            <input type="submit" value="DELETE" />
        </form>
        );
    }
}

function Users(prop) {
    return prop.list.map((user) =>
        <tr key={user.id}>
            <td>{user.id}</td>
            <td>{user.name}</td> 
            <td>{user.surname}</td>
        </tr>
    );
}

class App extends React.Component {
    constructor(prop) {
        super(prop);
        this.state = {
            list: []
        };
        this.refresh();
        this.handler = this.refresh.bind(this);
    }

    refresh() {
        fetch('http://localhost:8080/user-rest-service/api/all', {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then((resp) => {
            if(resp.ok) return resp.json();
        }).then((json) => {
            console.log(json);
            this.setState({list: json});
        });
    }

    render() {
        return (
        <div>
            <Save handler={this.handler} />
            <Update handler={this.handler} />
            <Delete handler={this.handler} />
            <table id="tab">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>NAME</th> 
                    <th>SURNAME</th>
                </tr>
                </thead>
                <tbody>
                    <Users list={this.state.list} />
                </tbody>
            </table>
        </div>
        );
    }
}

ReactDOM.render(
  <App />,
  document.getElementById('root')
);