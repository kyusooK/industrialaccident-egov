// import React from 'react';
// import { BrowserRouter } from 'react-router-dom';
// import App from './App';
import reportWebVitals from './reportWebVitals';
// import { createRoot } from 'react-dom/client';
// import Keycloak from 'keycloak-js';

// const root = createRoot(document.getElementById("root"));

// const keycloak = new Keycloak({
//   url: 'keycloak-server',
//   realm: 'master',
//   clientId: 'cliend-id',
//   onLoad: 'login-required'
// });

// keycloak.init({ onLoad: 'login-required' }).then(authenticated => {
//   if (!authenticated) {
//     window.location.reload();
//   } else {
//     console.log("Authenticated");

//   localStorage.setItem("jwt",keycloak.idToken)
//   localStorage.setItem("username",keycloak.idTokenParsed.preferred_username)
//   localStorage.setItem("roles",keycloak.tokenParsed.realm_access.roles.join(","))

//     root.render(
//       <React.StrictMode>
//         <BrowserRouter>
//           <App keycloak={keycloak} />
//         </BrowserRouter>
//       </React.StrictMode>
//     );
//   }
// }).catch(error => {
//   console.error("Authentication Failed: ", error);
// });


// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

