import React, { useState, useEffect } from 'react';
import './Orders.css';

import api from '../api/axiosConfig';

const Orders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        setLoading(true);
        // Hit the API Gateway for orders
        const response = await api.get('/api/orders');

        const data = response.data;
        setOrders(data);
        setError(null);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
  }, []);

  return (
    <div className="orders-container">
      <div className="orders-header">
        <h2 className="glow-text">Orders History</h2>
        <div className="orders-count">{orders.length} Orders</div>
      </div>

      {loading ? (
        <div className="loading-container">
          <div className="spinner"></div>
          <h3 className="animate-pulse">Fetching Secure Records...</h3>
        </div>
      ) : error ? (
        <div className="error-card">
          <div className="error-icon">⚠</div>
          <div className="error-content">
            <h3>System Error</h3>
            <p>{error}</p>
          </div>
        </div>
      ) : orders.length === 0 ? (
        <div className="empty-state">
          <p>No orders found in the database.</p>
        </div>
      ) : (
        <div className="orders-list">
          {orders.map((order) => {
            const totalItems = order.orderLineItemsList?.reduce((acc, item) => acc + item.quantity, 0) || 0;
            const totalPrice = order.orderLineItemsList?.reduce((acc, item) => acc + (item.quantity * item.price), 0) || 0;
            
            return (
              <div key={order.id} className="order-card">
                <div className="order-card-header">
                  <div>
                    <div className="order-id-label">ORDER ID</div>
                    <div className="order-number">{order.orderNumber}</div>
                  </div>
                  <div className="status-badge success">Placed</div>
                </div>
                
                <div className="order-details-divider"></div>
                
                <div className="order-items-list">
                  {order.orderLineItemsList?.map(item => (
                    <div key={item.id} className="order-item-row">
                      <div className="item-sku">{item.skuCode}</div>
                      <div className="item-meta">
                        <span className="item-qty">x{item.quantity}</span>
                        <span className="item-price">${item.price.toFixed(2)}</span>
                      </div>
                    </div>
                  ))}
                </div>
                
                <div className="order-card-footer">
                  <div className="footer-label">Total ({totalItems} items)</div>
                  <div className="footer-total">${totalPrice.toFixed(2)}</div>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};

export default Orders;
