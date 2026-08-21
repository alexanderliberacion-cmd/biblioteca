import type {JSX} from "react";
import type {ListaProps} from "../Types.ts";

export function Lista({children}: ListaProps): JSX.Element {

    return (
        <ol className="flex flex-col items-center bg-amber-700 h-40 w-60 py-2 border-3 border-amber-900">
            <li className="flex items-center justify-center font-bold text-2xl bg-amber-400 h-full w-full text-center overflow-auto p-2">
                {children}
            </li>
        </ol>
    );
}
