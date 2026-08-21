import type {SelectorSocioProps} from "../Types.ts";
import type {JSX} from "react";

export function SelectorSocio({socios, idSocio, onChangeSocio}: SelectorSocioProps): JSX.Element {
    return (
        <select value={idSocio} onChange={onChangeSocio} className="border-2  bg-linear-to-r  from-amber-300 to-orange-400 rounded-md transition-transform hover:scale-105">
            <option value={0}>Selecciona un socio</option>
            {socios.map(socio => <option value={socio.idSocio} key={socio.idSocio}>{socio.nombre}</option>)}
        </select>
    );
}
