import logo from './logo.svg';
import './App.css';
import axios from 'axios';
import { Routes, Route, BrowserRouter, useParams } from 'react-router-dom';
import pkceChallenge from 'pkce-challenge'
import { useEffect, useState } from 'react';

const styleCenter = { display: "flex", justifyContent: "center" }

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/authorized" element={<FirstExchange />} />
      </Routes>
    </BrowserRouter>
  );
}

function Home() {

  // Simple button to log in
  return <div style={styleCenter}> <button onClick={login}> Log in </button> </div>
}

function login() {

  // Generate PKCE and store in local storage
  const pkce = pkceChallenge();
  window.localStorage.setItem('code_verifier', pkce.code_verifier)

  // Redirect to oauth endpoint
  window.location.replace("http://127.0.0.1:8000/oauth2/authorize" +
    "?response_type=code" +
    "&client_id=client1" +
    "&redirect_uri=http://127.0.0.1:3000/authorized" +
    "&scope=openid" +
    "&code_challenge=" + pkce.code_challenge +
    "&code_challenge_method=S256" +
    "&state=sweller")
}

function FirstExchange(props) {
  const urlParams = new URLSearchParams(window.location.search);
  const [showDocs, setShowDocs] = useState(false)
  const [accessToken, setAccessToken] = useState(0)

  useEffect(() => {

    // Upon mouting exchange code for access token and store in state
    let code_verifier = window.localStorage.getItem('code_verifier')
    var axios = require('axios');
    var qs = require('qs');
    var data = qs.stringify({
      'code': urlParams.get('code'),
      'redirect_uri': 'http://127.0.0.1:3000/authorized',
      'grant_type': 'authorization_code',
      'client_secret': 'myClientSecretValue',
      'code_verifier': code_verifier
    });
    var config = {
      method: 'post',
      url: 'http://127.0.0.1:8000/oauth2/token',
      headers: {
        'Authorization': 'Basic Y2xpZW50MTpteUNsaWVudFNlY3JldFZhbHVl',
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      data: data
    };
    axios(config)
      .then(function (response) {
        setAccessToken(response.data.access_token)
      })
      .catch(function (error) {
        console.log(error);
      })

  }, [])

  return (
    <div>
      <div style={styleCenter}>You are logged in</div>
      <div style={styleCenter}>
        <button onClick={() => setShowDocs(true)}>Access documents</button>
      </div>
      {showDocs ? <AccessDocs AT={accessToken} /> : <div />}
    </div>
  )
}

function AccessDocs(props) {

  const [docs, setDocs] = useState("No docs read yet")

  // If button has been clicked then hit endpoint with access token (passed through props) to retrieve docs
  var axios = require('axios');
  var config = {
    method: 'get',
    url: 'http://localhost:8081/secret',
    headers: {
      'Authorization': 'Bearer ' + props.AT
    }
  };
  if (docs === "No docs read yet") {
    axios(config)
      .then(function (response) {
        setDocs(response.data)
      })
      .catch(function (error) {
        console.log(error);
      });
  }

  return (docs === "No docs read yet" ? <div>{docs.map}</div> :
    <div>
      {docs.map((doc) => (
        <div style={styleCenter}>Name: {doc.name}  {' '} Date: {doc.date} {' '} Security Level: {doc.security} {' '} Owner: {doc.owner} {' '}</div>
      ))}
    </div>
  )
}

export default App;
