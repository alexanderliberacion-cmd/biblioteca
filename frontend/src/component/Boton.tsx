import type {BotonProps} from "../Types.ts";
import type {JSX} from "react";

export function Boton({children, as: Component = 'button', ...props}: BotonProps): JSX.Element {
    return (
        <>
            <Component
                className="w-fit px-3 rounded-2xl border-3 bg-linear-to-br from-blue-300 to-emerald-400 font-semibold transition-transform hover:scale-105" {...props}>{children}
            </Component>
        </>
    );
}
