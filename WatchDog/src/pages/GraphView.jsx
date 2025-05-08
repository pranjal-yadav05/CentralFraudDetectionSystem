import React, { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import ForceGraph2D from 'react-force-graph-2d';
import './GraphView.css';

const API_BASE_URL = 'http://localhost:9000/api/graph';

function GraphView() {
  const { accountNumber, level: initialLevel } = useParams();
  const [graphData, setGraphData] = useState({ nodes: [], links: [] });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [level, setLevel] = useState(initialLevel || "2");
  
  // For controlling link distance
  const [linkDistance, setLinkDistance] = useState(150);

  // For showing tooltip on node click
  const [selectedNode, setSelectedNode] = useState(null);
  const [tooltipPosition, setTooltipPosition] = useState({ x: 0, y: 0 });
  
  // Track max transaction amount for link width scaling
  const [maxTransactionAmount, setMaxTransactionAmount] = useState(1);
  
  const graphRef = useRef();
  const containerRef = useRef();
  const navigate = useNavigate();

  useEffect(() => {
    fetchGraphData();
  }, [accountNumber, level]);

  // Update the link distance force when linkDistance state changes
  useEffect(() => {
    if (graphRef.current) {
      const linkForce = graphRef.current.d3Force('link');
      if (linkForce) {
        linkForce.distance(linkDistance);
        graphRef.current.d3ReheatSimulation();
      }
    }
  }, [linkDistance]);

  const fetchGraphData = async () => {
    try {
      setLoading(true);
      const response = await axios.get(`${API_BASE_URL}/${accountNumber}/${level}`);
      
      // Create nodes array
      const nodes = response.data.nodes.map(node => ({
        id: node.accountNumber,
        accountNumber: node.accountNumber,
        type: node.type || 'Unknown',
        balance: node.balance || 0,
        isSuspicious: node.suspicious || false
      }));
      
      // Build a lookup for nodes
      const nodeMap = {};
      nodes.forEach(node => {
        nodeMap[node.id] = node;
      });
      
      // Create links referencing node objects
      const links = [];
      let maxAmount = 0;
      response.data.links.forEach(link => {
        if (nodeMap[link.source] && nodeMap[link.target]) {
          const linkAmount = Math.abs(link.amount || 0);
          maxAmount = Math.max(maxAmount, linkAmount);
          
          links.push({
            source: nodeMap[link.source],
            target: nodeMap[link.target],
            amount: link.amount || 0,
            type: link.type || 'Unknown',
            date: link.date || new Date().toISOString()
          });
        } else {
          console.warn(`Skipping link: ${link.source} -> ${link.target} - One or both nodes not found`);
        }
      });

      // Set max transaction amount for link width scaling
      setMaxTransactionAmount(maxAmount);

      // Group links by source and target to handle overlapping links
      const linkGroups = {};
      links.forEach(link => {
        const key = `${link.source.id}-${link.target.id}`;
        if (!linkGroups[key]) {
          linkGroups[key] = [];
        }
        linkGroups[key].push(link);
      });

      // Increase spacing between overlapping links using a curvature factor
      const curvatureFactor = 2.0;
      Object.keys(linkGroups).forEach(key => {
        const group = linkGroups[key];
        if (group.length > 1) {
          group.forEach((link, index) => {
            link.curvature = ((index - (group.length - 1) / 2) / group.length) * curvatureFactor;
          });
        } else {
          group[0].curvature = 0;
        }
      });

      console.log(`Loaded ${nodes.length} nodes and ${links.length} links`);
      setGraphData({ nodes, links });
      setLoading(false);
    } catch (err) {
      console.error("Error fetching graph data:", err);
      setError('Failed to fetch graph data. Please try again later.');
      setLoading(false);
    }
  };

  const handleBackClick = () => {
    navigate('/');
  };

  const getNodeColor = (node) => {
    if (node.id === accountNumber) return '#E4D00A';
    if (node.isSuspicious) return '#f44336';
    return '#2196f3';
  };

  // Calculate link width based on transaction amount
  const getLinkWidth = (link) => {
    // Normalize link width between 1 and 10
    const maxWidth = 10;
    const minWidth = 1;
    
    // Use absolute value to handle negative transaction amounts
    const normalizedWidth = Math.abs(link.amount) / (maxTransactionAmount || 1);
    
    return minWidth + (normalizedWidth * (maxWidth - minWidth));
  };

  // Called when the engine stops; zoom to fit nodes
  const handleGraphReady = () => {
    if (graphRef.current) {
      setTimeout(() => {
        graphRef.current.zoomToFit(400, 50);
      }, 500);
    }
  };

  // Handle node click: show tooltip with node details
  const handleNodeClick = (node, event) => {
    if (containerRef.current) {
      const rect = containerRef.current.getBoundingClientRect();
      setTooltipPosition({
        x: event.clientX - rect.left,
        y: event.clientY - rect.top,
      });
    }
    setSelectedNode(node);
  };

  // Close tooltip
  const closeTooltip = () => {
    setSelectedNode(null);
  };

  const handleLevelChange = (newLevel) => {
    setLevel(newLevel);
    navigate(`/graph/${accountNumber}/${newLevel}`);
  };

  if (loading) return <div className="loading">Loading network graph...</div>;
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="graph-container">
      <div className="graph-header">
        <button className="back-btn" onClick={handleBackClick}>
          &larr; Back to Accounts
        </button>
        <h1>Account Network for {accountNumber}</h1>
        <div className="graph-info">
          <div className="level-selector">
            <label htmlFor="level">Network Depth Level:</label>
            <select 
              id="level" 
              value={level} 
              onChange={(e) => handleLevelChange(e.target.value)}
            >
              <option value="1">1</option>
              <option value="2">2</option>
              <option value="3">3</option>
              <option value="4">4</option>
              <option value="5">5</option>
            </select>
          </div>
          <p>Total Accounts: {graphData.nodes.length}</p>
          <p>Total Transactions: {graphData.links.length}</p>
        </div>
      </div>

      {/* Control panel for adjusting node distance */}
      <div className="control-panel">
        <label>
          Link Distance: 
          <input 
            type="range" 
            min="50" 
            max="300" 
            value={linkDistance} 
            onChange={(e) => setLinkDistance(Number(e.target.value))} 
          />
          {linkDistance}px
        </label>
      </div>
      
      <div className="graph-legend">
        <div className="legend-item">
          <span className="legend-color" style={{backgroundColor: '#E4D00A'}}></span>
          <span>Selected Account</span>
        </div>
        <div className="legend-item">
          <span className="legend-color" style={{backgroundColor: '#f44336'}}></span>
          <span>Suspicious Account</span>
        </div>
        <div className="legend-item">
          <span className="legend-color" style={{backgroundColor: '#2196f3'}}></span>
          <span>Normal Account</span>
        </div>
        <div className="legend-item">
          <span className="legend-thickness" style={{
            width: '10px', 
            height: '2px', 
            backgroundColor: '#999',
            marginRight: '5px'
          }}></span>
          <span>Link Thickness: Transaction Amount</span>
        </div>
      </div>
      
      <div className="graph-view" ref={containerRef}>
        {graphData.nodes.length > 0 ? (
          <ForceGraph2D
            ref={graphRef}
            graphData={graphData}
            nodeColor={getNodeColor}
            nodeLabel={node => `Account: ${node.accountNumber}\nType: ${node.type}\nBalance: $${node.balance.toFixed(2)}`}
            linkLabel={link => `Amount: $${link.amount.toFixed(2)}\nType: ${link.type}\nDate: ${new Date(link.date).toLocaleDateString()}`}
            linkDirectionalArrowLength={6}
            linkDirectionalArrowRelPos={1}
            linkWidth={getLinkWidth} // Use dynamic link width based on transaction amount
            nodeRelSize={6}
            linkCurvature={link => link.curvature || 0}
            onEngineStop={handleGraphReady}
            onNodeClick={handleNodeClick}
            linkColor={() => "#999"}
            cooldownTime={3000}
          />
        ) : (
          <div className="no-data">No network data found for this account at level {level}</div>
        )}

        {/* Tooltip showing selected node details */}
        {selectedNode && (
          <div 
            className="node-tooltip" 
            style={{ left: tooltipPosition.x, top: tooltipPosition.y }}
          >
            <button className="close-tooltip" onClick={closeTooltip}>x</button>
            <p><strong>Account:</strong> {selectedNode.accountNumber}</p>
            <p><strong>Type:</strong> {selectedNode.type}</p>
            <p><strong>Balance:</strong> ${selectedNode.balance.toFixed(2)}</p>
            <p><strong>Suspicious:</strong> {selectedNode.isSuspicious ? "Yes" : "No"}</p>
          </div>
        )}
      </div>
    </div>
  );
}

export default GraphView;