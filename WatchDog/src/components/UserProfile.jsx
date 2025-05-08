import React from "react";
import "../pages/UserDetails.css";

function UserProfile({ user }) {
  const formatDate = (dateString) => {
    if (!dateString) return "N/A";
    const options = {
      year: "numeric",
      month: "long",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    };
    return new Date(dateString).toLocaleDateString("en-US", options);
  };

  const formatBalance = (balance) => {
    if (balance === undefined || balance === null) return "N/A";
    return `₹${balance.toLocaleString()}`;
  };

  // Extract unique accounts from transactions
  const getAccountsFromTransactions = () => {
    if (!user || !user.transections) return [];

    const accounts = new Map();

    user.transections.forEach((transaction) => {
      // Check sender account
      if (transaction.sender?.account) {
        const account = transaction.sender.account;
        if (!accounts.has(account.accountNumber)) {
          accounts.set(account.accountNumber, account);
        }
      }

      // Check receiver account
      if (transaction.receiver?.account) {
        const account = transaction.receiver.account;
        if (!accounts.has(account.accountNumber)) {
          accounts.set(account.accountNumber, account);
        }
      }
    });

    return Array.from(accounts.values());
  };

  if (!user || !user.user) {
    return (
      <div className="user-profile-card">
        <div className="profile-header">
          <h2>
            <i className="fas fa-user-circle"></i>
            User Profile
          </h2>
        </div>
        <div className="profile-details">
          <div className="user-info-card">
            <div className="no-results">
              <i className="fas fa-user-slash"></i>
              <p>No user data available</p>
            </div>
          </div>
        </div>
      </div>
    );
  }

  const accounts = getAccountsFromTransactions();

  return (
    <div className="user-profile-card">
      <div className="profile-header">
        <h2>
          <i className="fas fa-user-circle text-white"></i>
          User Profile
        </h2>
        {user.user.suspicious && (
          <div className="suspicious-badge">
            <i className="fas fa-exclamation-triangle"></i>
            Suspicious Account
            <span className="blinking-dot"></span>
          </div>
        )}
      </div>

      <div className="profile-details">
        <div className="user-info-card">
          <h3 className="user-info-title">
            <i className="fas fa-id-card"></i>
            User Information
          </h3>
          <div className="user-info-content">
            <div className="detail-row">
              <div className="detail-label">
                <i className="fas fa-user"></i>
                Name
              </div>
              <div className="detail-value">{user.user.name || "N/A"}</div>
            </div>
            <div className="detail-row">
              <div className="detail-label">
                <i className="fas fa-fingerprint"></i>
                User ID
              </div>
              <div className="detail-value">{user.user.id || "N/A"}</div>
            </div>
            <div className="detail-row">
              <div className="detail-label">
                <i className="fas fa-id-badge"></i>
                Government ID
              </div>
              <div className="detail-value">
                {user.user.govIdNum || "N/A"} ({user.user.idType || "N/A"})
              </div>
            </div>
            <div className="detail-row">
              <div className="detail-label">
                <i className="fas fa-envelope"></i>
                Email
              </div>
              <div className="detail-value">{user.user.email || "N/A"}</div>
            </div>
            <div className="detail-row">
              <div className="detail-label">
                <i className="fas fa-phone"></i>
                Phone
              </div>
              <div className="detail-value">
                {user.user.mobileNumber || "N/A"}
              </div>
            </div>
            <div className="detail-row">
              <div className="detail-label">
                <i className="fas fa-map-marker-alt"></i>
                Address
              </div>
              <div className="detail-value">{user.user.address || "N/A"}</div>
            </div>
          </div>
        </div>

        <div className="user-info-card">
          <h3 className="user-info-title">
            <i className="fas fa-chart-line"></i>
            Account Statistics
          </h3>
          <div className="accounts-grid">
            {accounts.length > 0 ? (
              accounts.map((account, index) => (
                <div
                  key={account.id || index}
                  className={`account-card ${
                    account.suspicious ? "suspicious" : "normal"
                  }`}>
                  <div className="account-header">
                    <div className="account-number">
                      <i className="fas fa-wallet"></i>
                      {account.accountNumber || "N/A"}
                    </div>
                    <div
                      className={`account-status ${
                        account.suspicious ? "suspicious" : "normal"
                      }`}>
                      <i
                        className={`fas ${
                          account.suspicious
                            ? "fa-exclamation-triangle"
                            : "fa-check-circle"
                        }`}></i>
                      {account.suspicious ? "Suspicious" : "Normal"}
                    </div>
                  </div>

                  <div className="account-balance">
                    <div className="balance-label">Current Balance</div>
                    <div className="balance-amount">
                      {formatBalance(account.balance)}
                    </div>
                  </div>

                  <div className="account-details">
                    <div className="detail-item">
                      <div className="detail-icon">
                        <i className="fas fa-exchange-alt"></i>
                      </div>
                      <div className="detail-content">
                        <div className="detail-label">
                          Transaction Frequency
                        </div>
                        <div className="detail-value">
                          {account.freq ? `${account.freq} per month` : "N/A"}
                        </div>
                      </div>
                    </div>

                    <div className="detail-item">
                      <div className="detail-icon">
                        <i className="fas fa-clock"></i>
                      </div>
                      <div className="detail-content">
                        <div className="detail-label">Last Transaction</div>
                        <div className="detail-value">
                          {formatDate(account.lastTransaction)}
                        </div>
                      </div>
                    </div>

                    <div className="detail-item">
                      <div className="detail-icon">
                        <i className="fas fa-university"></i>
                      </div>
                      <div className="detail-content">
                        <div className="detail-label">Account Type</div>
                        <div className="detail-value">
                          {account.type || "N/A"}
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              ))
            ) : (
              <div className="no-results">
                <i className="fas fa-info-circle"></i>
                <p>No account information available</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default UserProfile;
