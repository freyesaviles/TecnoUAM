// frontend/src/App.jsx
import { useState, useEffect } from 'react';
import Login from './components/Login';
import EquiposList from './components/EquiposList';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem('jwt_token');
    if (token) {
      setIsAuthenticated(true);
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('jwt_token');
    setIsAuthenticated(false);
  };

  return (
      <div>
        <header style={{ padding: '10px', backgroundColor: '#333', color: 'white', display: 'flex', justifyContent: 'space-between' }}>
          <h1>TecnoUAM - Panel</h1>
          {isAuthenticated && <button onClick={handleLogout}>Cerrar Sesión</button>}
        </header>

        <main>
          {!isAuthenticated ? (
              <Login onLoginSuccess={() => setIsAuthenticated(true)} />
          ) : (
              <EquiposList />
          )}
        </main>
      </div>
  );
}

export default App;

