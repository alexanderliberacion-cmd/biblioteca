import type { InputProps} from "../Types.ts";
import type {JSX} from "react";

export function Input({value, type = "text", name, placeholder, onChange, id}:InputProps): JSX.Element {
    return (
        <>
        <input value={value}
               onChange={onChange}
               name={name}
               placeholder={placeholder}
               type={type}
               className="border-2 bg-linear-to-r from-amber-300 to-orange-400 font-bold text-center rounded-md text-black"
               id={id}
        />
        </>
    );
}
