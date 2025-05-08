import React, { useState, useEffect } from "react";
import "./SuspiciousTransactions.css";

function SuspiciousTransactions() {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [timestamp, setTimestamp] = useState(new Date());

  const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      setLoading(true);
      const response = await fetch(`${API_BASE_URL}/api/transactions/getall`);
      const data = await response.json();
      console.log("all trans: ", data);
      setTransactions(data);
      setTimestamp(new Date());
      setLoading(false);
    } catch (err) {
      setError(
        "Error loading suspicious transactions. Please try again later."
      );
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
        <p>Loading suspicious transactions...</p>
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
    <div className="suspicious-transactions-container">
      <header className="dashboard-header">
        <div className="header-content">
          <div className="header-title">
            <h1>Transaction Monitoring</h1>
            <p className="header-subtitle">
              Track and analyze all bank transactions for suspicious activities
            </p>
          </div>
          <div className="header-stats">
            <div className="stat-item">
              <span className="stat-label">Total Transactions</span>
              <span className="stat-value">{transactions.length}</span>
            </div>
            <div className="stat-item">
              <span className="stat-label">Suspicious</span>
              <span className="stat-value suspicious">
                {transactions.filter((t) => t.suspicious).length}
              </span>
            </div>
          </div>
        </div>
      </header>

      <div className="transactions-table-container">
        <table className="transactions-table">
          <thead>
            <tr>
              <th>Transaction ID</th>
              <th>Sender</th>
              <th>Receiver</th>
              <th>Amount</th>
              <th>Date</th>
              <th>Description</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {transactions.map((transaction) => (
              <tr
                key={transaction.id}
                className={transaction.suspicious ? "suspicious-row" : ""}>
                <td className="id-cell">{transaction.id}</td>
                <td>
                  <span className="sender">
                    {transaction.sender.accountNumber}
                  </span>
                </td>
                <td>
                  <span className="receiver">
                    {transaction.receiver.accountNumber}
                  </span>
                </td>
                <td className="amount-cell">
                  {transaction.currency} {transaction.amt.toLocaleString()}
                </td>
                <td>{formatDate(transaction.createdDate)}</td>
                <td className="description-cell">
                  {transaction.description || "N/A"}
                </td>
                <td className="status-cell">
                  {transaction.suspicious ? (
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
        <p>
          Showing {transactions.length} transactions | Last updated:{" "}
          {timestamp.toLocaleString()}
        </p>
      </footer>
    </div>
  );
}

export default SuspiciousTransactions;
