import {Boton} from "./Boton.tsx";
import {Link, useLocation} from "react-router";
import {type locationsItem, locationsObject} from "../Types.ts";
import type {JSX} from "react";

export function Header(): JSX.Element {
    const location = useLocation();
    const filterLocation: locationsItem[] = locationsObject.filter(loc => loc.ruta  !== location.pathname);


    return (
        <>
            <header className="flex h-20 min-w-full items-center justify-between bg-linear-to-r from-amber-400 to-orange-500 px-6 shadow-md md:w-full">
                <Link to={"/"}>
                    <h1 className="text-2xl font-semibold underline decoration-2">Biblioteca Personal</h1>
                </Link>

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
