import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import AccountList from "./pages/AccountList";
import GraphView from "./pages/GraphView";
import Navbar from "./Navbar";
import "./App.css";
import FraudOverview from "./pages/FraudOverview";
import SuspiciousAccounts from "./pages/SuspiciousAccounts";
import SuspiciousTransactions from "./pages/SuspiciousTransactions";
import UserDetails from "./pages/UserDetails";
import LandingPage from "./pages/LandingPage";
import AddBank from "./pages/AddBank";
import AddAccounts from "./pages/AddAccounts";

function App() {
  return (
    <Router>
      <div className="App">
        <Navbar />
        <div className="container">
          <Routes>
            <Route path="/" element={<LandingPage />} />
            <Route path="/trackaccount" element={<AccountList />} />
            <Route path="/overview" element={<FraudOverview />} />
            <Route path="/accounts" element={<SuspiciousAccounts />}></Route>
            <Route path="/transactions" element={<SuspiciousTransactions />} />
            <Route path="/users" element={<UserDetails />}></Route>
            <Route
              path="/graph/:accountNumber/:level"
              element={<GraphView />}
            />
            <Route path="/add-bank" element={<AddBank />} />
            <Route path="/add-accounts" element={<AddAccounts />} />
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;
