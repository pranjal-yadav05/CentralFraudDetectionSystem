import React, { useState } from 'react';
import '../pages/UserDetails.css';

function TransactionHistory({ transactions }) {
  const [searchTerm, setSearchTerm] = useState('');
  
  // Filter transactions based on search term
  const filteredTransactions = transactions.filter(transaction =>
    transaction.id.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString();
  };

  return (
    <div className="transaction-history-card">
      <div className="transaction-header">
        <h2>Transaction History</h2>
        <div className="search-container">
          <input
            type="text"
            placeholder="Search transaction ID..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="search-input"
          />
          <i className="search-icon">🔍</i>
        </div>
      </div>

      <div className="transaction-table-container">
        {filteredTransactions.length > 0 ? (
          <table className="transaction-table">
            <thead>
              <tr>
                <th>Transaction ID</th>
                <th>Date</th>
                <th>Amount</th>
                <th>Type</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {filteredTransactions.map((transaction) => (
                <tr key={transaction.id} className={transaction.suspicious ? 'suspicious-row' : ''}>
                  <td>{transaction.id}</td>
                  <td>{formatDate(transaction.createdDate)}</td>
                  <td className={`amount-cell ${transaction.type === 'Credit' ? 'credit-amount' : 'debit-amount'}`}>
                  ₹{transaction.amt}
                  </td>
                  <td>{transaction.type}</td>
                  <td className="status-cell">
                    {transaction.suspicious ? (
                      <div className="suspicious-indicator">
                        <span className="flag-icon">🚩</span>
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
        ) : (
          <div className="no-results">No transactions found</div>
        )}
      </div>
    </div>
  );
}

export default TransactionHistory; 