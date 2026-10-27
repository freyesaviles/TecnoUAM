// frontend/src/components/EquiposList.jsx
import { useState, useEffect } from 'react';
import api from '../api/axios';

const EquiposList = () => {
    const [equipos, setEquipos] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchEquipos = async () => {
        try {
            setLoading(true);
            setError(null);

            // Descomentar cuando Max termine:
            // const response = await api.get('/equipos');
            // setEquipos(response.data);

            // Simulación temporal para maquetar:
            setTimeout(() => {
                setEquipos([
                    { id: 1, codigoPatrimonial: 'PC-001', nombre: 'Laptop Dell XPS', tipo: 'Computadora', estado: 'DISPONIBLE' },
                    { id: 2, codigoPatrimonial: 'PR-102', nombre: 'Proyector Epson', tipo: 'Audiovisual', estado: 'PRESTADO' }
                ]);
                setLoading(false);
            }, 1000);

        } catch (err) {
            setError('Error al cargar los equipos.');
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