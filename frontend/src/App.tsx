import {BrowserRouter, Routes, Route} from "react-router";
import {Catalogo} from "./pages/Catalogo.tsx";
import {Layout} from "./component/Layout.tsx";
import {Almacen} from "./pages/Almacen.tsx";
import {Socios} from "./pages/Socios.tsx";
import {Reportes} from "./pages/Reportes.tsx";
import ProtectedRoute from "./component/ProtectedRoute.tsx";
import {Login} from "./pages/Login.tsx";
import {Register} from "./pages/Register.tsx";
import React from "react";

function App() {

  return (
    <React.StrictMode>
    <BrowserRouter basename="/biblioteca">
      <Routes>
        <Route element={<Layout />}>
            <Route element={<ProtectedRoute requiredRoles={['ADMIN', 'SOCIO']}/>}>
              <Route path={"/socios"} element={<Socios />} />
            </Route>
            <Route element={<ProtectedRoute requiredRoles={["ADMIN"]}/>}>
             <Route path={"/reportes"} element={<Reportes />} />
             <Route path={"/almacen"} element={<Almacen />} />
            </Route>
          <Route path={"/login"} element={<Login />} />
          <Route path={"/register"} element={<Register />} />
          <Route path={"/"} element={<Catalogo />} />
          </Route>
      </Routes>
    </BrowserRouter>
    </React.StrictMode>
  )
}

export default App
