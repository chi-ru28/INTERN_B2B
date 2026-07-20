import React, { useState, useEffect } from 'react';
import ProductCard from '../components/ProductCard';

const Catalog = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    // In a real environment, this fetches from the API Gateway e.g., /api/products
    // For scaffolding visualization, we will simulate a network delay and mock data
    const fetchProducts = async () => {
      try {
        setLoading(true);
        // Simulate network call
        await new Promise(resolve => setTimeout(resolve, 800));
        
        const mockProducts = [
          { id: 1, name: 'Enterprise Server Blade', description: 'High-density compute for the modern data center.', price: '4,999.00' },
          { id: 2, name: 'Core Switch 9000', description: 'Layer 3 routing with terabit backplane.', price: '12,500.00' },
          { id: 3, name: 'NVMe Storage Array', description: '100TB raw flash storage with sub-millisecond latency.', price: '25,000.00' },
          { id: 4, name: 'Secure Firewall Pro', description: 'Next-gen packet inspection and VPN termination.', price: '3,200.00' },
          { id: 5, name: 'Load Balancer Appliance', description: 'L7 load balancing with hardware SSL offloading.', price: '8,400.00' },
          { id: 6, name: 'AI Accelerator Card', description: 'Tensor cores for enterprise ML workloads.', price: '15,000.00' },
        ];
        
        setProducts(mockProducts);
        setError(null);
      } catch (err) {
        setError('Failed to fetch catalog data.');
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  return (
    <div className="container" style={{ paddingTop: 'var(--spacing-xl)', paddingBottom: 'var(--spacing-xl)' }}>
      <div className="flex-between" style={{ marginBottom: 'var(--spacing-lg)' }}>
        <h2>Product Catalog</h2>
        <div style={{ color: 'var(--color-text-muted)' }}>{products.length} Items Found</div>
      </div>

      {loading ? (
        <div className="flex-center" style={{ height: '400px' }}>
          <h3 className="animate-fade-in" style={{ color: 'var(--color-primary)' }}>Loading Catalog...</h3>
        </div>
      ) : error ? (
        <div className="card" style={{ borderLeft: '4px solid var(--color-error)' }}>
          <h3 style={{ color: 'var(--color-error)' }}>Error</h3>
          <p>{error}</p>
        </div>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 'var(--spacing-lg)' }}>
          {products.map((product) => (
            <ProductCard key={product.id} product={product} />
          ))}
        </div>
      )}
    </div>
  );
};

export default Catalog;
