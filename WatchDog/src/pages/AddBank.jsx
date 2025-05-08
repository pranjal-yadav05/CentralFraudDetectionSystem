import React, { useState } from "react";
import "./AddBank.css";

function AddBank() {
  const [formData, setFormData] = useState({
    bankId: "",
    bankName: "",
    bankEmail: "",
    transactionURI: "",
    databaseStructure: "NOSQL",
    transectionConfig: {
      id: "",
      sender: "",
      receiver: "",
      amt: "",
      description: "",
      createdDate: "",
      currency: "",
    },
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name.startsWith("config.")) {
      const configField = name.split(".")[1];
      setFormData((prev) => ({
        ...prev,
        transectionConfig: {
          ...prev.transectionConfig,
          [configField]: value,
        },
      }));
    } else {
      setFormData((prev) => ({
        ...prev,
        [name]: value,
      }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSuccess(false);

    try {
      const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;
      const response = await fetch(`${API_BASE_URL}/config/bank`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        throw new Error("Failed to add bank");
      }

      setSuccess(true);
      setFormData({
        bankId: "",
        bankName: "",
        bankEmail: "",
        transactionURI: "",
        databaseStructure: "NOSQL",
        transectionConfig: {
          id: "",
          sender: "",
          receiver: "",
          amt: "",
          description: "",
          createdDate: "",
          currency: "",
        },
      });
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="add-bank-container">
      <div className="dashboard-header">
        <div className="header-content">
          <div className="header-title">
            <h1>Add New Bank</h1>
            <p className="header-subtitle">
              Connect a new bank account to monitor for suspicious activities
              and fraud detection
            </p>
          </div>
        </div>
      </div>

      <div className="form-container">
        {error && (
          <div className="error-message">
            <i className="fas fa-exclamation-circle"></i>
            {error}
          </div>
        )}
        {success && (
          <div className="success-message">
            <i className="fas fa-check-circle"></i>
            {success}
          </div>
        )}
        <form onSubmit={handleSubmit} className="add-bank-form">
          <div className="form-section">
            <h2>Bank Information</h2>
            <div className="form-grid">
              <div className="form-group">
                <label htmlFor="bankId">Bank ID</label>
                <input
                  type="text"
                  id="bankId"
                  name="bankId"
                  value={formData.bankId}
                  onChange={handleChange}
                  required
                  placeholder="Enter bank ID"
                />
              </div>

              <div className="form-group">
                <label htmlFor="bankName">Bank Name</label>
                <input
                  type="text"
                  id="bankName"
                  name="bankName"
                  value={formData.bankName}
                  onChange={handleChange}
                  required
                  placeholder="Enter bank name"
                />
              </div>

              <div className="form-group">
                <label htmlFor="bankEmail">Bank Email</label>
                <input
                  type="email"
                  id="bankEmail"
                  name="bankEmail"
                  value={formData.bankEmail}
                  onChange={handleChange}
                  required
                  placeholder="Enter bank email"
                />
              </div>

              <div className="form-group">
                <label htmlFor="transactionURI">Transaction URI</label>
                <input
                  type="url"
                  id="transactionURI"
                  name="transactionURI"
                  value={formData.transactionURI}
                  onChange={handleChange}
                  required
                  placeholder="Enter transaction API endpoint"
                />
              </div>

              <div className="form-group">
                <label htmlFor="databaseStructure">Database Structure</label>
                <select
                  id="databaseStructure"
                  name="databaseStructure"
                  value={formData.databaseStructure}
                  onChange={handleChange}
                  required>
                  <option value="NOSQL">NoSQL</option>
                  <option value="SQL">SQL</option>
                </select>
              </div>
            </div>
          </div>

          <div className="form-section">
            <h2>Transaction Configuration</h2>
            <div className="form-grid">
              <div className="form-group">
                <label htmlFor="config.id">Transaction ID Field</label>
                <input
                  type="text"
                  id="config.id"
                  name="config.id"
                  value={formData.transectionConfig.id}
                  onChange={handleChange}
                  required
                  placeholder="e.g., txn_id"
                />
              </div>

              <div className="form-group">
                <label htmlFor="config.sender">Sender Field</label>
                <input
                  type="text"
                  id="config.sender"
                  name="config.sender"
                  value={formData.transectionConfig.sender}
                  onChange={handleChange}
                  required
                  placeholder="e.g., from"
                />
              </div>

              <div className="form-group">
                <label htmlFor="config.receiver">Receiver Field</label>
                <input
                  type="text"
                  id="config.receiver"
                  name="config.receiver"
                  value={formData.transectionConfig.receiver}
                  onChange={handleChange}
                  required
                  placeholder="e.g., to"
                />
              </div>

              <div className="form-group">
                <label htmlFor="config.amt">Amount Field</label>
                <input
                  type="text"
                  id="config.amt"
                  name="config.amt"
                  value={formData.transectionConfig.amt}
                  onChange={handleChange}
                  required
                  placeholder="e.g., amt"
                />
              </div>

              <div className="form-group">
                <label htmlFor="config.description">Description Field</label>
                <input
                  type="text"
                  id="config.description"
                  name="config.description"
                  value={formData.transectionConfig.description}
                  onChange={handleChange}
                  required
                  placeholder="e.g., description"
                />
              </div>

              <div className="form-group">
                <label htmlFor="config.createdDate">Date Field</label>
                <input
                  type="text"
                  id="config.createdDate"
                  name="config.createdDate"
                  value={formData.transectionConfig.createdDate}
                  onChange={handleChange}
                  required
                  placeholder="e.g., createdDate"
                />
              </div>

              <div className="form-group">
                <label htmlFor="config.currency">Currency Field</label>
                <input
                  type="text"
                  id="config.currency"
                  name="config.currency"
                  value={formData.transectionConfig.currency}
                  onChange={handleChange}
                  required
                  placeholder="e.g., currency"
                />
              </div>
            </div>
          </div>

          <div className="form-actions">
            <button type="submit" className="submit-button" disabled={loading}>
              {loading ? (
                <>
                  <i className="fas fa-circle-notch fa-spin"></i>
                  Adding Bank...
                </>
              ) : (
                <>
                  <i className="fas fa-plus"></i>
                  Add Bank
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default AddBank;
