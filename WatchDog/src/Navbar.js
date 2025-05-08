import React, { useState } from "react";
import { Link, useLocation } from "react-router-dom";
import "./Navbar.css";

function Navbar() {
  const [isOpen, setIsOpen] = useState(false);
  const location = useLocation();

  const toggleMenu = () => {
    setIsOpen(!isOpen);
  };

  const isActive = (path) => {
    return location.pathname === path;
  };

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <Link to="/" className="navbar-brand">
          <span className="brand-text">WatchDog</span>
        </Link>

        <button className="menu-toggle" onClick={toggleMenu}>
          <span className={`hamburger ${isOpen ? "open" : ""}`}></span>
        </button>

        <div className={`navbar-menu ${isOpen ? "active" : ""}`}>
          <ul className="navbar-nav">
            <li className="nav-item">
              <Link
                to="/overview"
                className={`nav-link ${isActive("/overview") ? "active" : ""}`}>
                Overview
              </Link>
            </li>
            <li className="nav-item">
              <Link
                to="/trackaccount"
                className={`nav-link ${
                  isActive("/trackaccount") ? "active" : ""
                }`}>
                Track Accounts
              </Link>
            </li>
            <li className="nav-item">
              <Link
                to="/transactions"
                className={`nav-link ${
                  isActive("/transactions") ? "active" : ""
                }`}>
                Transactions
              </Link>
            </li>
            <li className="nav-item">
              <Link
                to="/accounts"
                className={`nav-link ${isActive("/accounts") ? "active" : ""}`}>
                Suspicious Accounts
              </Link>
            </li>
            <li className="nav-item">
              <Link
                to="/add-bank"
                className={`nav-link ${isActive("/add-bank") ? "active" : ""}`}>
                Add Bank
              </Link>
            </li>
            <li className="nav-item">
              <Link
                to="/add-accounts"
                className={`nav-link ${
                  isActive("/add-accounts") ? "active" : ""
                }`}>
                Add Accounts
              </Link>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
