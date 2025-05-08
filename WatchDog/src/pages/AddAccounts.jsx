import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./AddAccounts.css";

function AddAccounts() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    num_accounts: 0,
    num_transactions: 0,
    num_fraud_accounts: 0,
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    const numValue = parseInt(value) || 0;
    setFormData((prev) => ({
      ...prev,
      [name]: numValue,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSuccess(false);

    const { num_accounts, num_transactions, num_fraud_accounts } = formData;

    // Validate minimum values
    if (num_accounts < 1) {
      setError("Number of accounts must be at least 1");
      setLoading(false);
      return;
    }
    if (num_transactions < 1) {
      setError("Number of transactions must be at least 1");
      setLoading(false);
      return;
    }
    if (num_fraud_accounts < 0) {
      setError("Number of fraud accounts cannot be negative");
      setLoading(false);
      return;
    }

    // Validate maximum values
    if (num_accounts > 50) {
      setError("Maximum number of accounts is 50");
      setLoading(false);
      return;
    }
    if (num_transactions > 150) {
      setError("Maximum number of transactions is 150");
      setLoading(false);
      return;
    }
    if (num_fraud_accounts > 30) {
      setError("Maximum number of fraud accounts is 30");
      setLoading(false);
      return;
    }
    if (num_fraud_accounts > num_accounts) {
      setError(
        "Number of fraud accounts cannot be greater than total accounts"
      );
      setLoading(false);
      return;
    }

    try {
      const response = await fetch(
        "https://frauddetection-r211.onrender.com/api/bank/create-graph",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(formData),
        }
      );

      if (!response.ok) {
        throw new Error("Failed to generate accounts");
      }

      setSuccess(true);
      // Navigate to account list after successful submission
      navigate("/trackaccount");
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="add-accounts-container">
      <div className="dashboard-header">
        <div className="header-content">
          <div className="header-title">
            <h1>Generate Accounts</h1>
            <p className="header-subtitle">
              Generate a set of accounts with transactions and fraud patterns
              for testing
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
            Accounts generated successfully!
          </div>
        )}
        <form onSubmit={handleSubmit} className="add-accounts-form">
          <div className="form-section">
            <h2>Account Generation Parameters</h2>
            <div className="form-grid">
              <div className="form-group">
                <label htmlFor="num_accounts">
                  Number of Accounts (Max: 50)
                </label>
                <input
                  type="number"
                  id="num_accounts"
                  name="num_accounts"
                  value={formData.num_accounts}
                  onChange={handleChange}
                  required
                  placeholder="Enter number of accounts"
                />
              </div>

              <div className="form-group">
                <label htmlFor="num_transactions">
                  Number of Transactions (Max: 150)
                </label>
                <input
                  type="number"
                  id="num_transactions"
                  name="num_transactions"
                  value={formData.num_transactions}
                  onChange={handleChange}
                  required
                  placeholder="Enter number of transactions"
                />
              </div>

              <div className="form-group">
                <label htmlFor="num_fraud_accounts">
                  Number of Fraud Accounts (Max: 30)
                </label>
                <input
                  type="number"
                  id="num_fraud_accounts"
                  name="num_fraud_accounts"
                  value={formData.num_fraud_accounts}
                  onChange={handleChange}
                  required
                  placeholder="Enter number of fraud accounts"
                />
              </div>
            </div>
          </div>

          <div className="form-actions">
            <button type="submit" className="submit-button" disabled={loading}>
              {loading ? "Generating..." : "Generate Accounts"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default AddAccounts;
