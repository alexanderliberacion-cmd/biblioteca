import {BrowserRouter, Routes, Route} from "react-router";
import {Catalogo} from "./pages/Catalogo.tsx";
import {Layout} from "./component/Layout.tsx";
import {Almacen} from "./pages/Almacen.tsx";
import {Socios} from "./pages/Socios.tsx";
import {Reportes} from "./pages/Reportes.tsx";

function App() {

  return (
    <>
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route path={"/"} element={<Catalogo /> } />
          <Route path={"/almacen"} element={<Almacen /> } />
          <Route path={"/socios"} element={<Socios />} />
          <Route path={"/reportes"} element={<Reportes />} />
        </Route>
      </Routes>
    </BrowserRouter>
    </>
  )
}

export default App
