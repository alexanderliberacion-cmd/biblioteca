import * as React from "react";
import {useState} from "react";
import {useNavigate} from "react-router";
import {AuthService} from "../Services/AuthService.ts";
import {Boton} from "../component/Boton.tsx";
import type {auth} from "../Types.ts";
import {useAuth} from "../AuthProvider.tsx";

export function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const {resetSesionExpirada} = useAuth();

    async function handleLogin(e: React.ChangeEvent<HTMLFormElement>) {
        e.preventDefault();
        try {
            if(email === "" || password === "") {
                setError("No se puede iniciar sesion con campos vacios");
                return;
            }
            const data: auth = await AuthService.login(email, password);
            localStorage.setItem('token', data.token);
            localStorage.setItem('rol', data.rol);
            resetSesionExpirada();
            navigate("/");
        } catch(error) {
            setError("Error al iniciar sesion" + error);
        }

    }

    return (
        <div className={"w-full max-w-md mx-auto p-4 mt-8"}>
            <form onSubmit={handleLogin} autoComplete="off" className={"flex flex-col gap-4 bg-linear-to-r from-amber-300 to-orange-400 rounded-2xl p-6 shadow-xs"}>
                <input type={"email"} placeholder={"email"} value={email} onChange={(e) => setEmail(e.target.value)} className={"w-full bg-linear-to-r from-amber-300 to-orange-400 border border-slate-300 rounded-lg px-3 py-2 text-sm text-slate-900 shadow-xs"} />
                <input type={"password"} placeholder={"password"} value={password} onChange={(e) => setPassword(e.target.value)} className={"w-full bg-linear-to-r from-amber-300 to-orange-400 border border-slate-300 rounded-lg px-3 py-2 text-sm text-slate-900 shadow-xs"} />
                {error && <p className="text-red-500 font-semibold text-sm bg-red-50 p-2.5 rounded-lg border border-red-200">{error}</p>}
                <div className={"mt-2"}>
                <Boton type={"submit"}>Login</Boton>
                </div>
            </form>
        </div>
    )
}