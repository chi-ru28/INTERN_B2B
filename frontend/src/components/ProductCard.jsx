import React, { useContext, useState } from 'react';
import { CartContext } from '../context/CartContext';
import { AuthContext } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

const ProductCard = ({ product }) => {
  const { addToCart } = useContext(CartContext);
  const { user } = useContext(AuthContext);
  const navigate = useNavigate();
  const [added, setAdded] = useState(false);

  const handleAddToCart = () => {
    if (!user) {
      navigate('/login');
      return;
    }
    
    addToCart(product);
    setAdded(true);
    setTimeout(() => setAdded(false), 1500); // Visual feedback duration
  };

  return (
    <div className="card animate-fade-in" style={{ display: 'flex', flexDirection: 'column', height: '100%', position: 'relative' }}>
      <div style={{ backgroundColor: 'var(--color-bg)', height: '200px', borderRadius: 'var(--radius-md)', marginBottom: 'var(--spacing-md)', display: 'flex', alignItems: 'center', justifyContent: 'center', overflow: 'hidden' }}>
        {product.imageUrl ? (
          <img src={product.imageUrl} alt={product.name} style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
        ) : (
          <span style={{ color: 'var(--color-text-muted)', fontSize: '3rem' }}>🛍️</span>
        )}
      </div>
      <h3 style={{ marginBottom: '0.25rem' }}>{product.name}</h3>
      <p style={{ fontSize: '0.875rem', flexGrow: 1 }}>{product.description}</p>
      
      <div className="flex-between" style={{ marginTop: 'var(--spacing-md)' }}>
        <span style={{ fontSize: '1.25rem', fontWeight: '700', color: 'var(--color-text-main)' }}>
          ${product.price}
        </span>
        <button 
          className={`btn ${added ? 'btn-success' : 'btn-primary'}`} 
          onClick={handleAddToCart}
          style={{ 
            padding: '0.5rem 1rem', 
            fontSize: '0.875rem',
            backgroundColor: added ? '#10b981' : undefined,
            color: added ? '#ffffff' : undefined,
            transition: 'all 0.3s ease'
          }}
        >
          {added ? 'Added!' : 'Add to Cart'}
        </button>
      </div>
    </div>
  );
};

export default ProductCard;
