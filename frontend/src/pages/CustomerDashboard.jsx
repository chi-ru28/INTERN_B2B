import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Package, TrendingUp, Clock, ShoppingCart, ChevronRight, FileText } from 'lucide-react';

const CustomerDashboard = () => {
  const [recentOrders, setRecentOrders] = useState([]);
  const [stats, setStats] = useState({ totalOrders: 0, activeOrders: 0, totalSpent: 0 });

  useEffect(() => {
    // Fetch real orders to populate the dashboard stats
    fetch('/api/orders')
      .then(res => {
        if (!res.ok) throw new Error('Failed to fetch');
        return res.json();
      })
      .then(data => {
        // Sort by id descending for latest
        const sorted = data.sort((a, b) => b.id - a.id);
        setRecentOrders(sorted.slice(0, 3)); // Top 3
        
        // Calculate basic stats
        const active = sorted.filter(o => o.status === 'PROCESSING' || o.status === 'SHIPPED').length;
        const total = sorted.reduce((sum, order) => {
          return sum + order.orderLineItemsList.reduce((itemSum, item) => itemSum + (item.price * item.quantity), 0);
        }, 0);

        setStats({
          totalOrders: sorted.length,
          activeOrders: active,
          totalSpent: total
        });
      })
      .catch(err => console.error("Error fetching orders:", err));
  }, []);

  return (
    <div className="container animate-fade-in" style={{ paddingTop: '2rem', paddingBottom: '4rem' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <div>
          <h1 className="glow-text" style={{ margin: '0 0 0.5rem 0', fontSize: '2.5rem' }}>Welcome Back, Demo User</h1>
          <p style={{ color: 'var(--color-text-muted)', margin: 0, fontSize: '1.1rem' }}>Here is an overview of your enterprise account activity.</p>
        </div>
        <Link to="/catalog" className="btn btn-primary" style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', textDecoration: 'none' }}>
          <ShoppingCart size={18} />
          New Order
        </Link>
      </div>

      {/* Stats Grid */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', gap: '1.5rem', marginBottom: '3rem' }}>
        <div className="card" style={{ padding: '1.5rem', display: 'flex', alignItems: 'flex-start', gap: '1rem', background: 'linear-gradient(145deg, rgba(30, 41, 59, 0.7), rgba(15, 23, 42, 0.9))' }}>
          <div style={{ padding: '1rem', borderRadius: '12px', background: 'rgba(99, 102, 241, 0.1)', color: '#818cf8' }}>
            <Package size={24} />
          </div>
          <div>
            <h3 style={{ margin: '0 0 0.25rem 0', color: 'var(--color-text-muted)', fontSize: '0.9rem', fontWeight: '500', textTransform: 'uppercase', letterSpacing: '0.5px' }}>Total Orders</h3>
            <div style={{ fontSize: '2rem', fontWeight: 'bold', color: '#f8fafc' }}>{stats.totalOrders}</div>
          </div>
        </div>
        
        <div className="card" style={{ padding: '1.5rem', display: 'flex', alignItems: 'flex-start', gap: '1rem', background: 'linear-gradient(145deg, rgba(30, 41, 59, 0.7), rgba(15, 23, 42, 0.9))' }}>
          <div style={{ padding: '1rem', borderRadius: '12px', background: 'rgba(16, 185, 129, 0.1)', color: '#34d399' }}>
            <Clock size={24} />
          </div>
          <div>
            <h3 style={{ margin: '0 0 0.25rem 0', color: 'var(--color-text-muted)', fontSize: '0.9rem', fontWeight: '500', textTransform: 'uppercase', letterSpacing: '0.5px' }}>Active Orders</h3>
            <div style={{ fontSize: '2rem', fontWeight: 'bold', color: '#f8fafc' }}>{stats.activeOrders}</div>
          </div>
        </div>
        
        <div className="card" style={{ padding: '1.5rem', display: 'flex', alignItems: 'flex-start', gap: '1rem', background: 'linear-gradient(145deg, rgba(30, 41, 59, 0.7), rgba(15, 23, 42, 0.9))' }}>
          <div style={{ padding: '1rem', borderRadius: '12px', background: 'rgba(245, 158, 11, 0.1)', color: '#fbbf24' }}>
            <TrendingUp size={24} />
          </div>
          <div>
            <h3 style={{ margin: '0 0 0.25rem 0', color: 'var(--color-text-muted)', fontSize: '0.9rem', fontWeight: '500', textTransform: 'uppercase', letterSpacing: '0.5px' }}>Total Spend</h3>
            <div style={{ fontSize: '2rem', fontWeight: 'bold', color: '#f8fafc' }}>
              ${stats.totalSpent.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2})}
            </div>
          </div>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '2fr 1fr', gap: '2rem' }}>
        {/* Recent Orders List */}
        <div className="card" style={{ padding: '2rem' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
            <h2 style={{ margin: 0, fontSize: '1.5rem' }}>Recent Orders</h2>
            <Link to="/orders" style={{ color: 'var(--color-primary)', textDecoration: 'none', display: 'flex', alignItems: 'center', gap: '0.25rem', fontSize: '0.9rem', fontWeight: '500' }}>
              View All <ChevronRight size={16} />
            </Link>
          </div>
          
          {recentOrders.length === 0 ? (
            <div style={{ textAlign: 'center', padding: '3rem 1rem', background: 'rgba(15, 23, 42, 0.5)', borderRadius: '12px', border: '1px dashed rgba(255,255,255,0.1)' }}>
              <FileText size={48} style={{ color: '#475569', marginBottom: '1rem' }} />
              <h3 style={{ color: '#cbd5e1', marginBottom: '0.5rem' }}>No recent orders</h3>
              <p style={{ color: '#94a3b8', fontSize: '0.9rem', maxWidth: '300px', margin: '0 auto 1.5rem' }}>You haven't placed any orders yet. Check out our catalog to get started.</p>
              <Link to="/catalog" className="btn btn-primary" style={{ textDecoration: 'none' }}>Browse Catalog</Link>
            </div>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
              {recentOrders.map(order => {
                const total = order.orderLineItemsList.reduce((sum, item) => sum + (item.price * item.quantity), 0);
                return (
                  <div key={order.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '1.25rem', background: 'rgba(15, 23, 42, 0.4)', borderRadius: '12px', border: '1px solid rgba(255,255,255,0.05)', transition: 'transform 0.2s, background 0.2s' }} className="hover-lift">
                    <div>
                      <div style={{ fontWeight: 'bold', color: '#f8fafc', marginBottom: '0.25rem' }}>Order #{order.orderNumber}</div>
                      <div style={{ fontSize: '0.85rem', color: '#94a3b8' }}>
                        {order.orderLineItemsList.length} items • ${total.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2})}
                      </div>
                    </div>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '1.5rem' }}>
                      <span style={{ 
                        padding: '0.25rem 0.75rem', 
                        borderRadius: '999px', 
                        fontSize: '0.75rem', 
                        fontWeight: 'bold',
                        backgroundColor: order.status === 'SHIPPED' ? 'rgba(16, 185, 129, 0.15)' : 'rgba(99, 102, 241, 0.15)',
                        color: order.status === 'SHIPPED' ? '#34d399' : '#818cf8',
                        textTransform: 'uppercase',
                        letterSpacing: '0.5px'
                      }}>
                        {order.status || 'PROCESSING'}
                      </span>
                      <Link to={`/orders`} className="btn btn-secondary" style={{ padding: '0.4rem 0.75rem', fontSize: '0.85rem', textDecoration: 'none' }}>
                        Details
                      </Link>
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </div>

        {/* Quick Actions / Help */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
          <div className="card" style={{ padding: '2rem', background: 'linear-gradient(145deg, #1e293b, #0f172a)' }}>
            <h3 style={{ margin: '0 0 1rem 0', fontSize: '1.2rem' }}>Quick Actions</h3>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
              <Link to="/profile" className="btn btn-secondary" style={{ textDecoration: 'none', textAlign: 'center', padding: '0.75rem' }}>Edit Profile</Link>
              <Link to="/settings" className="btn btn-secondary" style={{ textDecoration: 'none', textAlign: 'center', padding: '0.75rem' }}>Account Settings</Link>
              <Link to="/admin" className="btn" style={{ textDecoration: 'none', textAlign: 'center', padding: '0.75rem', background: 'rgba(245, 158, 11, 0.1)', color: '#fbbf24', border: '1px solid rgba(245, 158, 11, 0.2)' }}>Go to Admin Panel</Link>
            </div>
          </div>

          <div className="card" style={{ padding: '2rem', background: 'radial-gradient(circle at top right, rgba(99, 102, 241, 0.15), transparent 70%), #1e293b' }}>
            <h3 style={{ margin: '0 0 0.5rem 0', fontSize: '1.2rem', color: '#f8fafc' }}>Need Support?</h3>
            <p style={{ color: '#94a3b8', fontSize: '0.9rem', marginBottom: '1.5rem', lineHeight: '1.5' }}>Our enterprise support team is available 24/7 to assist with your B2B purchasing needs.</p>
            <button className="btn btn-primary" style={{ width: '100%' }}>Contact Support</button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CustomerDashboard;
