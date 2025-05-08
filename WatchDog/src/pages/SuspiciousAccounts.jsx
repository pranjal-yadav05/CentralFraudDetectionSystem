import React, { useState, useEffect } from "react";
import "./SuspiciousAccounts.css";

function SuspiciousAccounts() {
  const [accounts, setAccounts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [timestamp, setTimestamp] = useState(new Date());

  useEffect(() => {
    fetchData();
  }, []);

  const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

  const fetchData = async () => {
    try {
      setLoading(true);
      const response = await fetch(`${API_BASE_URL}/api/accounts`);
      const data = await response.json();
      console.log(data);
      setAccounts(data);
      setTimestamp(new Date());
      setLoading(false);
    } catch (err) {
      setError("Error loading suspicious accounts. Please try again later.");
      setLoading(false);
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return "N/A";
    const date = new Date(dateString);
    return date.toLocaleString();
  };

  if (loading)
    return (
      <div className="loading">
        <i className="fas fa-circle-notch fa-spin"></i>
        <p>Loading suspicious accounts...</p>
      </div>
    );
  if (error)
    return (
      <div className="error">
        <i className="fas fa-exclamation-circle"></i>
        <p>{error}</p>
      </div>
    );

  const suspiciousCount = accounts.filter(
    (account) => account.suspicious
  ).length;

  return (
    <div className="suspicious-accounts-container">
      <header className="dashboard-header">
        <div className="header-content">
          <div className="header-title">
            <h1>Account Monitoring</h1>
            <p className="header-subtitle">
              Track and analyze bank accounts for suspicious activities
            </p>
          </div>
          <div className="header-stats">
            <div className="stat-item">
              <span className="stat-label">Total Accounts</span>
              <span className="stat-value">{accounts.length}</span>
            </div>
            <div className="stat-item">
              <span className="stat-label">Suspicious</span>
              <span className="stat-value suspicious">{suspiciousCount}</span>
            </div>
          </div>
        </div>
      </header>

      <div className="accounts-table-container">
        <table className="accounts-table">
          <thead>
            <tr>
              <th>Account Number</th>
              <th>Frequency</th>
              <th>Last Transaction</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {accounts.map((account) => (
              <tr
                key={account.accountNumber}
                className={account.suspicious ? "suspicious-row" : ""}>
                <td className="account-number">{account.accountNumber}</td>
                <td>{account.frequency}</td>
                <td>{formatDate(account.lastTransaction)}</td>
                <td className="status-cell">
                  {account.suspicious ? (
                    <div className="suspicious-indicator">
                      <span className="status-text">Suspicious</span>
                    </div>
                  ) : (
                    <div className="normal-indicator">
                      <span className="status-text">Normal</span>
                    </div>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <footer className="dashboard-footer">
        <p>Data updated: {timestamp.toLocaleString()}</p>
      </footer>
    </div>
  );
}

export default SuspiciousAccounts;
