import { useState } from 'react'
import './App.css'

import { getUsuario, isAuthenticated, logout as authLogout } from './services/AuthService'

import Login from './pages/Login'
import Dashboard from './pages/Dashboard'
import Empleado from './pages/Empleado'
import Contrato from './pages/Contrato'
import Usuario from './pages/Usuario'
import Nomina from './pages/Nomina'
import ReporteNomina from './pages/ReporteNomina'
import DetalleNomina from './pages/DetalleNomina'
import Navbar from './components/Navbar'
import Sidebar from './components/Sidebar'

function App() {
  const [autenticado, setAutenticado] = useState(isAuthenticated())
  const [usuario, setUsuario] = useState(getUsuario())
  const [moduloActivo, setModuloActivo] = useState('dashboard')

  const handleLoginSuccess = (data) => {
    setAutenticado(true)
    setUsuario({ token: data.token, correo: data.correo, rol: data.rol })
    setModuloActivo('dashboard')
  }

  const handleLogout = () => {
    authLogout()
    setAutenticado(false)
    setUsuario(null)
  }

  if (!autenticado) {
    return <Login onLoginSuccess={handleLoginSuccess} />
  }

  return (
    <div className="app-layout">
      <Navbar onLogout={handleLogout} />
      <div className="app-body">
        <Sidebar
          rol={usuario?.rol}
          moduloActivo={moduloActivo}
          onModuloChange={setModuloActivo}
        />
        <main className="app-content">
          {moduloActivo === 'dashboard' && <Dashboard />}
          {moduloActivo === 'empleados' && <Empleado />}
          {moduloActivo === 'contratos' && <Contrato />}
          {moduloActivo === 'usuarios' && <Usuario />}
          {moduloActivo === 'nominas' && <Nomina />}
          {moduloActivo === 'reporte-nomina' && <ReporteNomina />}
          {moduloActivo === 'detalle-nomina' && <DetalleNomina />}
        </main>
      </div>
    </div>
  )
}

export default App
