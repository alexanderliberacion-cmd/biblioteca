import type {SelectorLibroProps} from "../Types.ts";
import type {JSX} from "react";

export function SelectorLibro({libros, isbn, onChangeLibros}: SelectorLibroProps): JSX.Element {
    return (
        <>
            <select value={isbn} onChange={onChangeLibros} className="border-2 bg-linear-to-r from-amber-300 to-orange-400 rounded-md transition-transform hover:scale-105">
                <option value={""}>Selecciona un libro</option>
                {libros.map(libro => <option value={libro.isbn} key={libro.isbn}>{libro.titulo}</option>)}
            </select>
        </>
    );
}
