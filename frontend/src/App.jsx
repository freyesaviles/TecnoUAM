import React, { useState } from 'react';
import Login from './components/Login';
import EquiposList from './components/EquiposList';

const TOKEN_KEY = 'token';

function loadSession() {
    const token = localStorage.getItem(TOKEN_KEY);

    if (!token) {
        return null;
    }

    let roles = [];
    try {
        roles = JSON.parse(localStorage.getItem('roles') || '[]');
    } catch {
        roles = [];
    }

    return {
        token,
        username: localStorage.getItem('username') || '',
        roles,
    };
}

function App() {
    const [session, setSession] = useState(loadSession);

    const handleLogout = () => {
        localStorage.removeItem(TOKEN_KEY);
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('username');
        localStorage.removeItem('roles');
        setSession(null);
    };

    const roleLabel = session?.roles?.[0]?.replace('ROLE_', '') || 'USUARIO';

    return (
        <div style={styles.appContainer}>
            {/* Navbar con fondo oscuro oficial (#1d242e) y acentos turquesa */}
            <header style={styles.header}>
                <div style={styles.headerContent}>
                    <div style={styles.brandGroup}>
                        <span style={styles.logoText}>UAM</span>
                        <span style={styles.divider}>|</span>
                        <span style={styles.systemName}>TecnoUAM — Panel</span>
                    </div>

                    {session && (
                        <div style={styles.sessionGroup}>
                            <span style={styles.sessionLabel}>
                                {session.username} · {roleLabel}
                            </span>
                            <button onClick={handleLogout} style={styles.logoutButton}>
                                Cerrar Sesión
                            </button>
                        </div>
                    )}
                </div>
            </header>

            {/* Contenido principal */}
            <main style={styles.mainContent}>
                {!session ? (
                    <Login onLoginSuccess={setSession} />
                ) : (
                    <EquiposList role={roleLabel} />
                )}
            </main>
        </div>
    );
}

const styles = {
    appContainer: {
        minHeight: '100vh',
        backgroundColor: '#f4f6f8',
        fontFamily: "'Segoe UI', Roboto, Helvetica, Arial, sans-serif",
    },
    header: {
        backgroundColor: '#1d242e', // Azul marino oscuro exacto de la web UAM
        color: '#ffffff',
        padding: '20px 36px',
        boxShadow: '0 4px 12px rgba(0, 0, 0, 0.15)',
        borderBottom: '4px solid #00a3ad', // Franja turquesa UAM
    },
    headerContent: {
        width: '100%',
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
    },
    sessionGroup: {
        display: 'flex',
        alignItems: 'center',
        gap: '16px',
    },
    sessionLabel: {
        color: '#cbd5e1',
        fontSize: '14px',
        fontWeight: '600',
    },
    brandGroup: {
        display: 'flex',
        alignItems: 'center',
        gap: '14px',
    },
    logoText: {
        fontSize: '28px',
        fontWeight: '900',
        color: '#00a3ad', // Turquesa característico del logo UAM
        letterSpacing: '1px',
        fontFamily: "'Trebuchet MS', sans-serif",
    },
    divider: {
        color: '#4a5568',
        fontSize: '22px',
        fontWeight: '300',
    },
    systemName: {
        fontSize: '20px',
        fontWeight: '600',
        color: '#ffffff',
        whiteSpace: 'nowrap',
        letterSpacing: '-0.2px',
    },
    logoutButton: {
        backgroundColor: '#0e507d', // Azul oscuro complementario
        color: '#ffffff',
        border: 'none',
        padding: '10px 20px',
        borderRadius: '6px',
        cursor: 'pointer',
        fontSize: '14px',
        fontWeight: '600',
        transition: 'background-color 0.2s',
    },
    mainContent: {
        padding: '40px 20px',
    },
};

export default App;
