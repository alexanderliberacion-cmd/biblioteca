import { Navigate, Outlet } from "react-router";
import type {ProtectedRoutesProps, Rol} from "../Types.ts";


export default function ProtectedRoute({requiredRoles}:ProtectedRoutesProps) {
    const token = localStorage.getItem("token");
    const rol: Rol = localStorage.getItem("rol") as unknown as Rol;

    if (!token || !rol) {
        return <Navigate to="/login" replace />;
    }

    if( requiredRoles !== undefined && !requiredRoles.includes(rol)) {
        return <Navigate to={"/"} replace state={{mensaje: "No tienes permisos para acceder a esta pagina"}} />;

    }

    return <Outlet />;
}