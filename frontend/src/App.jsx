function App() {
  return (
    <main className="app-shell">
      <section className="intro-panel" aria-labelledby="app-title">
        <p className="eyebrow">Centro de Capacitación TecnoUAM</p>
        <h1 id="app-title">Préstamos claros. Equipos listos.</h1>
        <p className="intro-copy">
          La mesa de operaciones para saber qué equipo está disponible y quién
          lo tiene en uso.
        </p>
        <div className="status-chip" role="status">
          <span className="status-dot" aria-hidden="true" />
          Base de interfaz preparada para conectar la API
        </div>
      </section>

      <section className="workspace-panel" aria-labelledby="workspace-title">
        <div className="panel-heading">
          <div>
            <p className="section-kicker">Inventario</p>
            <h2 id="workspace-title">Equipos tecnológicos</h2>
          </div>
          <span className="panel-count">API pendiente</span>
        </div>
        <div className="empty-state">
          <div className="empty-mark" aria-hidden="true">+</div>
          <h3>La lista aparecerá aquí</h3>
          <p>
            Esta vista se conectará a <code>GET /api/equipos</code> con un
            token válido.
          </p>
        </div>
      </section>
    </main>
  )
}

export default App

