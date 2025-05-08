import React, { useEffect } from "react";
import { Link } from "react-router-dom";
import "./LandingPage.css";

function LandingPage() {
  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add("animate");
          }
        });
      },
      { threshold: 0.1 }
    );

    document
      .querySelectorAll(".animate-on-scroll")
      .forEach((el) => observer.observe(el));

    return () => observer.disconnect();
  }, []);

  return (
    <div className="landing-page">
      {/* Hero Section */}
      <section className="hero">
        <div className="container">
          <div className="hero-content animate-on-scroll">
            <h1>
              <span className="gradient-text">WatchDog</span>
              <span className="subtitle">Intelligent Fraud Prevention</span>
            </h1>
            <p className="hero-description">
              Advanced AI-powered fraud detection system that safeguards your
              banking operations with real-time monitoring and instant alerts.
            </p>
            <div className="hero-cta">
              <Link to="/overview" className="btn btn-primary">
                Get Started
                <span className="arrow">→</span>
              </Link>
              <Link to="/trackaccount" className="btn btn-secondary">
                View Demo
                <span className="play-icon">▶</span>
              </Link>
            </div>
          </div>
          <div className="hero-visual animate-on-scroll">
            <div className="floating-elements">
              <div className="floating-card security">
                <i className="fas fa-shield-alt"></i>
                <span>Real-time Security</span>
              </div>
              <div className="floating-card analytics">
                <i className="fas fa-chart-line"></i>
                <span>Smart Analytics</span>
              </div>
              <div className="floating-card alerts">
                <i className="fas fa-bell"></i>
                <span>Instant Alerts</span>
              </div>
            </div>
            <div className="hero-image">
              <div className="gradient-overlay"></div>
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="features">
        <div className="container">
          <div className="section-header animate-on-scroll">
            <h2>
              Why Choose <span>WatchDog</span>?
            </h2>
            <p>Advanced features that set us apart in fraud detection</p>
          </div>
          <div className="features-grid">
            <div className="feature-card animate-on-scroll">
              <div className="feature-icon">
                <i className="fas fa-robot"></i>
              </div>
              <h3>AI-Powered Detection</h3>
              <p>
                Advanced machine learning algorithms that adapt and learn from
                new fraud patterns.
              </p>
            </div>
            <div className="feature-card animate-on-scroll">
              <div className="feature-icon">
                <i className="fas fa-bolt"></i>
              </div>
              <h3>Real-time Monitoring</h3>
              <p>
                Instant detection and response to suspicious activities across
                all transactions.
              </p>
            </div>
            <div className="feature-card animate-on-scroll">
              <div className="feature-icon">
                <i className="fas fa-chart-pie"></i>
              </div>
              <h3>Smart Analytics</h3>
              <p>
                Comprehensive insights and reporting for better fraud prevention
                strategies.
              </p>
            </div>
            <div className="feature-card animate-on-scroll">
              <div className="feature-icon">
                <i className="fas fa-shield-virus"></i>
              </div>
              <h3>Multi-layer Security</h3>
              <p>
                Multiple security layers to protect against various types of
                fraud attempts.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Stats Section */}
      <section className="stats">
        <div className="container">
          <div className="stats-container">
            <div className="stat-card animate-on-scroll">
              <div className="stat-number">99.9%</div>
              <div className="stat-label">Detection Accuracy</div>
            </div>
            <div className="stat-card animate-on-scroll">
              <div className="stat-number">24/7</div>
              <div className="stat-label">Active Monitoring</div>
            </div>
            <div className="stat-card animate-on-scroll">
              <div className="stat-number">50ms</div>
              <div className="stat-label">Response Time</div>
            </div>
            <div className="stat-card animate-on-scroll">
              <div className="stat-number">1M+</div>
              <div className="stat-label">Transactions Protected</div>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="cta">
        <div className="container">
          <div className="cta-content animate-on-scroll">
            <h2>Ready to Secure Your Banking System?</h2>
            <p>
              Join leading financial institutions in protecting their assets
              with WatchDog.
            </p>
            <Link to="/add-bank" className="btn btn-cta">
              Start Protecting Now
              <span className="arrow">→</span>
            </Link>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="footer">
        <div className="container">
          <div className="footer-content">
            <div className="footer-brand">
              <h3>WatchDog</h3>
              <p>Advanced fraud detection for modern banking.</p>
            </div>
            <div className="footer-links">
              <div className="footer-section">
                <h4>Product</h4>
                <ul>
                  <li>
                    <Link to="/overview">Overview</Link>
                  </li>
                  <li>
                    <Link to="/trackaccount">Track Accounts</Link>
                  </li>
                  <li>
                    <Link to="/transactions">Transactions</Link>
                  </li>
                  <li>
                    <Link to="/accounts">Suspicious Accounts</Link>
                  </li>
                </ul>
              </div>
              <div className="footer-section">
                <h4>Company</h4>
                <ul>
                  <li>
                    <a href="#about">About Us</a>
                  </li>
                  <li>
                    <a href="#contact">Contact</a>
                  </li>
                  <li>
                    <a href="#careers">Careers</a>
                  </li>
                  <li>
                    <a href="#blog">Blog</a>
                  </li>
                </ul>
              </div>
              <div className="footer-section">
                <h4>Connect</h4>
                <ul>
                  <li>
                    <a href="#twitter">Twitter</a>
                  </li>
                  <li>
                    <a href="#linkedin">LinkedIn</a>
                  </li>
                  <li>
                    <a href="#github">GitHub</a>
                  </li>
                  <li>
                    <a href="#discord">Discord</a>
                  </li>
                </ul>
              </div>
            </div>
          </div>
          <div className="footer-bottom">
            <p>&copy; 2024 WatchDog. All rights reserved.</p>
          </div>
        </div>
      </footer>
    </div>
  );
}

export default LandingPage;
