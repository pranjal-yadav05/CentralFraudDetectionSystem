import React, { useState, useEffect } from "react";
import UserProfile from "../components/UserProfile";
import TransactionHistory from "../components/TransactionHistory";
import "./UserDetails.css";

function UserDetails() {
  const [users, setUsers] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchAllUsers();
  }, []);

  const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

  const fetchAllUsers = async () => {
    try {
      setLoading(true);
      const response = await fetch(`${API_BASE_URL}/api/users`);
      const data = await response.json();
      console.log(data);
      setUsers(data);

      // Set the first user as selected by default if available
      if (data.length > 0) {
        setSelectedUser(data[0]);
      }

      setLoading(false);
    } catch (err) {
      setError(
        "उपयोगकर्ता डेटा लोड करने में त्रुटि हुई। कृपया बाद में पुन: प्रयास करें।"
      );
      setLoading(false);
    }
  };

  const handleUserSelect = (user) => {
    setSelectedUser(user);
  };

  if (loading)
    return (
      <div className="loading-overlay">
        <div className="loading-container">
          <div className="loading-spinner"></div>
          <p className="loading-text">Loading user data...</p>
        </div>
      </div>
    );

  if (error)
    return (
      <div className="error-overlay">
        <div className="error-container">
          <div className="error-icon">
            <i className="fas fa-exclamation-circle"></i>
          </div>
          <p className="error-text">{error}</p>
        </div>
      </div>
    );

  if (!users.length)
    return (
      <div className="error-overlay">
        <div className="error-container">
          <div className="error-icon warning">
            <i className="fas fa-user-slash"></i>
          </div>
          <p className="error-text">No users found</p>
        </div>
      </div>
    );

  return (
    <div className="user-details-container">
      <header className="dashboard-header">
        <div className="header-content">
          <div className="header-title">
            <h1>User Details</h1>
            <p className="header-subtitle">
              View and manage user information and transactions
            </p>
          </div>
          <div className="header-actions">
            <button onClick={fetchAllUsers} className="refresh-button">
              <i className="fas fa-sync-alt"></i>
              Refresh
            </button>
          </div>
        </div>
      </header>

      <main className="main-content">
        <div className="user-selector">
          <div className="selector-content">
            <div className="selector-group">
              <label htmlFor="user-select" className="selector-label">
                <i className="fas fa-user"></i>
                Select User:
              </label>
              <select
                id="user-select"
                value={selectedUser?.user?.id || ""}
                onChange={(e) => {
                  const userId = e.target.value;
                  const selected = users.find(
                    (user) => user.user.id === userId
                  );
                  if (selected) handleUserSelect(selected);
                }}
                className="user-select">
                {users.map((user) => (
                  <option key={user.user.id} value={user.user.id}>
                    {user.user.name} ({user.user.id})
                  </option>
                ))}
              </select>
            </div>
          </div>
        </div>

        {selectedUser && (
          <div className="user-details-content">
            <UserProfile user={selectedUser} />
            <TransactionHistory transactions={selectedUser.transections} />
          </div>
        )}
      </main>
    </div>
  );
}

export default UserDetails;
