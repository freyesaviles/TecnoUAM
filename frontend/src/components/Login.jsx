// frontend/src/components/Login.jsx
import { useState } from 'react';
import api from '../api/axios';

const Login = ({ onLoginSuccess }) => {
    const [credenciales, setCredenciales] = useState({ username: '', password: '' });
    const [error, setError] = useState(null);

    const handleChange = (e) => {
        setCredenciales({ ...credenciales, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // Descomentar cuando Emilio termine:
            // const res = await api.post('/auth/login', credenciales);
            // const token = res.data.token;

            // Simulación temporal:
            const token = "token_simulado_temporal_123";

            localStorage.setItem('jwt_token', token);
            onLoginSuccess();
        } catch (err) {
            setError('Credenciales inválidas');
        }
    };

    return (
        <div style={{ padding: '20px', maxWidth: '300px', margin: 'auto' }}>
            <h2>Iniciar Sesión</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
                <input name="username" placeholder="Usuario" onChange={handleChange} required />
                <input name="password" type="password" placeholder="Contraseña" onChange={handleChange} required />
                <button type="submit">Ingresar</button>
            </form>
        </div>
    );
};

export default Login;