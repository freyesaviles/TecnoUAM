// frontend/src/components/EquiposList.jsx
import { useState, useEffect } from 'react';
import api from '../api/axios';

const EquiposList = ({ role }) => {
    const [equipos, setEquipos] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchEquipos = async () => {
        try {
            setLoading(true);
            setError(null);

            // El interceptor de Axios adjunta el token almacenado.
            const response = await api.get('/equipos');

            setEquipos(response.data);
            setLoading(false);
        } catch (err) {
            console.error(err);
            setError('Error al cargar los equipos. Verifica tus permisos o si tu sesión expiró.');
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchEquipos();
    }, []);

    if (loading) return <div>Cargando inventario...</div>;
    if (error) return <div style={{ color: 'red' }}>{error}</div>;

    return (
        <div style={{ padding: '20px' }}>
            <h2>Gestión de Equipos</h2>
            <p>Sesión activa como <strong>{role}</strong>. Esta vista consulta datos reales de la API.</p>
            <button onClick={fetchEquipos} style={{ marginBottom: '15px' }}>Recargar Lista</button>

            {equipos.length === 0 ? (
                <p>No hay equipos registrados en el sistema.</p>
            ) : (
                <table border="1" cellPadding="10" style={{ borderCollapse: 'collapse', width: '100%' }}>
                    <thead>
                    <tr style={{ backgroundColor: '#f4f4f4' }}>
                        <th>Código Patrimonial</th>
                        <th>Nombre</th>
                        <th>Tipo</th>
                        <th>Estado</th>
                    </tr>
                    </thead>
                    <tbody>
                    {equipos.map((equipo) => (
                        <tr key={equipo.id}>
                            <td>{equipo.codigoPatrimonial}</td>
                            <td>{equipo.nombre}</td>
                            <td>{equipo.tipo}</td>
                            <td>{equipo.estado}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
};

export default EquiposList;
