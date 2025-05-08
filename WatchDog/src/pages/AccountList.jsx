import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./AccountList.css";

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL + "/api";

function AccountList() {
  const [accounts, setAccounts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isRefreshing, setIsRefreshing] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [filter, setFilter] = useState("all"); // 'all', 'suspicious', 'normal'

  const navigate = useNavigate();

  useEffect(() => {
    fetchAccounts();
  }, []);

  const fetchAccounts = async () => {
    try {
      setIsRefreshing(true);
      const response = await axios.get(`${API_BASE_URL}/accounts`);
      console.log("accounts", response.data);
      setAccounts(response.data);
      setLoading(false);
      setIsRefreshing(false);
    } catch (err) {
      setError("Failed to fetch accounts. Please try again later.");
      setLoading(false);
      setIsRefreshing(false);
    }
  };

  const handleTest = async () => {
    try {
      setIsRefreshing(true);
      await axios.get("https://frauddetection-r211.onrender.com/api/test");
      await fetchAccounts(); // Refresh the page data
    } catch (err) {
      setError("Failed to run test. Please try again later.");
    } finally {
      setIsRefreshing(false);
    }
  };

  const handleGetData = async () => {
    try {
      setIsRefreshing(true);
      await axios.get(`${API_BASE_URL}/transactions`);
      await fetchAccounts(); // Refresh the page data
    } catch (err) {
      setError("Failed to get data. Please try again later.");
    } finally {
      setIsRefreshing(false);
    }
  };

  const handleViewGraph = (accountNumber) => {
    navigate(`/graph/${accountNumber}/2`);
  };

  const isSuspicious = (account) => account.suspiciousScore > 50;

  const filteredAccounts = accounts.filter((account) => {
    const matchesSearch = account.accountNumber
      .toLowerCase()
      .includes(searchTerm.toLowerCase());
    const matchesFilter =
      filter === "all" ||
      (filter === "suspicious" && isSuspicious(account)) ||
      (filter === "normal" && !isSuspicious(account));
    return matchesSearch && matchesFilter;
  });

  if (loading)
    return (
      <div className="loading">
        <i className="fas fa-circle-notch fa-spin"></i>
        <p>Loading Accounts...</p>
      </div>
    );

  if (error)
    return (
      <div className="error">
        <i className="fas fa-exclamation-circle"></i>
        <p>{error}</p>
      </div>
    );

  return (
    <div className="account-list-container">
      <div className="dashboard-header">
        <div className="header-content">
          <div className="header-title">
            <h1>Account List</h1>
            <p className="header-subtitle">
              Monitor and manage all bank accounts for fraud detection and
              suspicious activity tracking
            </p>
          </div>
          <div className="header-actions">
            <button
              className="action-button test-button"
              onClick={handleTest}
              disabled={isRefreshing}>
              <i className="fas fa-vial"></i>
              Test
            </button>
            <button
              className="action-button get-data-button"
              onClick={handleGetData}
              disabled={isRefreshing}>
              <i className="fas fa-download"></i>
              Get Data
            </button>
          </div>
        </div>
      </div>

      <div className="filters-section">
        <div className="search-box">
          <i className="fas fa-search"></i>
          <input
            type="text"
            placeholder="Search by account number..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
        <div className="filter-buttons">
          <button
            className={`filter-btn ${filter === "all" ? "active" : ""}`}
            onClick={() => setFilter("all")}>
            All Accounts
          </button>
          <button
            className={`filter-btn ${filter === "suspicious" ? "active" : ""}`}
            onClick={() => setFilter("suspicious")}>
            Suspicious
          </button>
          <button
            className={`filter-btn ${filter === "normal" ? "active" : ""}`}
            onClick={() => setFilter("normal")}>
            Normal
          </button>
        </div>
      </div>

      <div className="accounts-grid">
        {filteredAccounts.map((account) => (
          <div
            key={account.accountNumber}
            className={`account-card ${
              isSuspicious(account) ? "suspicious" : ""
            }`}>
            <div className="account-header">
              <div className="account-title">
                <h3>
                  Account: {account.accountNumber}
                  {isSuspicious(account) && (
                    <span className="suspicious-indicator">
                      <i className="fas fa-exclamation-triangle"></i>
                    </span>
                  )}
                </h3>
              </div>
              {isSuspicious(account) && (
                <span className="suspicious-badge">
                  <i className="fas fa-shield-alt"></i> Suspicious
                </span>
              )}
            </div>
            <div className="account-details">
              <div className="detail-item">
                <span className="detail-label">Balance</span>
                <span className="detail-value">${account.balance}</span>
              </div>
              <div className="detail-item">
                <span className="detail-label">Transaction Frequency</span>
                <span className="detail-value">{account.frequency}</span>
              </div>
              <div className="detail-item">
                <span className="detail-label">Last Transaction</span>
                <span className="detail-value">
                  {new Date(account.lastTransaction).toLocaleString()}
                </span>
              </div>
              <div className="detail-item">
                <span className="detail-label">Suspicious Score</span>
                <span
                  className={`detail-value ${
                    isSuspicious(account)
                      ? "status-suspicious"
                      : "status-normal"
                  }`}>
                  {account.suspiciousScore}
                </span>
              </div>
            </div>
            <button
              className="view-graph-btn"
              onClick={() => handleViewGraph(account.accountNumber)}>
              <i className="fas fa-project-diagram"></i>
              View Network
            </button>
          </div>
        ))}
      </div>

      {filteredAccounts.length === 0 && (
        <div className="no-results">
          <i className="fas fa-search"></i>
          <p>No accounts found matching your criteria</p>
        </div>
      )}
    </div>
  );
}

export default AccountList;
