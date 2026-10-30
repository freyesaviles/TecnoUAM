import React, { useState } from 'react';
import api from '../api/axios';

const Login = ({ onLoginSuccess }) => {
    const [credenciales, setCredenciales] = useState({ username: '', password: '' });
    const [error, setError] = useState('');

    const handleChange = (e) => {
        setCredenciales({
            ...credenciales,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const res = await api.post('/auth/login', credenciales);
            const token = res.data.token;

            localStorage.setItem('token', token);
            localStorage.setItem('username', res.data.username || credenciales.username);
            localStorage.setItem('roles', JSON.stringify(res.data.roles || []));

            onLoginSuccess({
                token,
                username: res.data.username || credenciales.username,
                roles: res.data.roles || [],
            });
        } catch (err) {
            setError('Credenciales inválidas. Inténtalo de nuevo.');
        }
    };

    return (
        <div style={styles.container}>
            <div style={styles.card}>
                <div style={styles.header}>
                    <div style={styles.uamBadge}>UAM</div>
                    <h2 style={styles.title}>Iniciar Sesión</h2>
                    <p style={styles.subtitle}>Ingresa tus credenciales para acceder al sistema</p>
                </div>

                {error && <div style={styles.errorMessage}>{error}</div>}

                <form onSubmit={handleSubmit} style={styles.form}>
                    <div style={styles.inputGroup}>
                        <label htmlFor="username" style={styles.label}>Usuario</label>
                        <input
                            id="username"
                            type="text"
                            name="username"
                            placeholder="Ej. admin"
                            value={credenciales.username}
                            onChange={handleChange}
                            style={styles.input}
                            required
                        />
                    </div>

                    <div style={styles.inputGroup}>
                        <label htmlFor="password" style={styles.label}>Contraseña</label>
                        <input
                            id="password"
                            type="password"
                            name="password"
                            placeholder="••••••••"
                            value={credenciales.password}
                            onChange={handleChange}
                            style={styles.input}
                            required
                        />
                    </div>

                    <button type="submit" style={styles.button}>
                        Ingresar
                    </button>
                </form>
            </div>
        </div>
    );
};

const styles = {
    container: {
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        minHeight: '70vh',
        padding: '20px',
    },
    card: {
        backgroundColor: '#ffffff',
        padding: '42px 34px',
        borderRadius: '12px',
        boxShadow: '0 12px 30px rgba(29, 36, 46, 0.12)',
        width: '100%',
        maxWidth: '380px',
        borderTop: '5px solid #00a3ad',
    },
    header: {
        textAlign: 'center',
        marginBottom: '28px',
    },
    uamBadge: {
        display: 'inline-block',
        fontSize: '22px',
        fontWeight: '900',
        color: '#00a3ad',
        marginBottom: '8px',
        letterSpacing: '1px',
        fontFamily: "'Trebuchet MS', sans-serif",
    },
    title: {
        margin: '0 0 6px 0',
        color: '#0e507d', // Azul oscuro de los títulos de la web
        fontSize: '24px',
        fontWeight: '700',
    },
    subtitle: {
        margin: 0,
        color: '#64748b',
        fontSize: '13.5px',
    },
    form: {
        display: 'flex',
        flexDirection: 'column',
        gap: '18px',
    },
    inputGroup: {
        display: 'flex',
        flexDirection: 'column',
        gap: '6px',
        textAlign: 'left',
    },
    label: {
        fontSize: '13px',
        fontWeight: '600',
        color: '#1d242e',
    },
    input: {
        padding: '11px 14px',
        borderRadius: '6px',
        border: '1px solid #cbd5e1',
        fontSize: '14px',
        outline: 'none',
        boxSizing: 'border-box',
        width: '100%',
    },
    button: {
        marginTop: '10px',
        padding: '12px',
        backgroundColor: '#0e507d', // Azul de acción institucional
        color: '#ffffff',
        border: 'none',
        borderRadius: '6px',
        fontSize: '15px',
        fontWeight: '600',
        cursor: 'pointer',
        boxShadow: '0 4px 12px rgba(14, 80, 125, 0.25)',
    },
    errorMessage: {
        backgroundColor: '#fef2f2',
        color: '#dc2626',
        padding: '10px',
        borderRadius: '6px',
        fontSize: '13px',
        textAlign: 'center',
        marginBottom: '15px',
        border: '1px solid #fecaca',
    },
};

export default Login;
