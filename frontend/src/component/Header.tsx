import {Boton} from "./Boton.tsx";
import {Link, useLocation, useNavigate} from "react-router";
import {type locationsItem, locationsObject, type Rol} from "../Types.ts";
import React, {type JSX} from "react";

export function Header(): JSX.Element {
    const location = useLocation();
    const rolActual: Rol = localStorage.getItem("rol") as unknown as Rol;
    const filterLocation: locationsItem[] = locationsObject.filter(loc => loc.ruta  !== location.pathname && (loc.requiredRoles?.includes(rolActual) || loc.requiredRoles === undefined));
    const navigate = useNavigate();

    //funcion para cerrar la sesion
    function handleLogout(e: React.MouseEvent<HTMLButtonElement>)  {
        e.preventDefault();
        try {
            localStorage.removeItem("rol");
            localStorage.removeItem("rolActual");
            navigate("/login");
        } catch (error) {
            console.log(error);
        }
    }
    return (
        <>
            <header className="flex flex-wrap h-20 w-full overflow-x-auto items-center justify-between bg-linear-to-r from-amber-400 to-orange-500 px-6 shadow-md md:w-full">
                <Link to={"/"}>
                    <h1 className="text-2xl font-semibold underline decoration-2">Biblioteca Personal</h1>
                </Link>

                {rolActual === null && (
                    <>
                        <Boton as={Link} to={"/login"}>Login</Boton>
                        <Boton as={Link} to={"/register"}>Register</Boton>
                    </>

                )}
                {rolActual !== null && (
                    <>
                        <Boton type={"submit"} onClick={handleLogout}>Cerrar Sesion </Boton>
                    </>
                )}
                {
                    filterLocation.map((loc: locationsItem) => {
                        return (
                            <Boton
                                key={loc.ruta}
                                as={Link}
                                to={loc.ruta}
                            >
                                {loc.nombre}
                            </Boton>
                        )
                    })
                }
            </header></>
    );

}
