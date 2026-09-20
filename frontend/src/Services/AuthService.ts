import type {auth} from "../Types.ts";
import {apiFetch} from "./apiFetch.ts";
import {API_ROUTES} from "./apiRoutes.ts";

export class AuthService {

    //Login
    static async login(email: string, password: string): Promise<auth> {
        return apiFetch(API_ROUTES.AUTH.LOGIN, {
            method: 'POST',
            body: JSON.stringify({email, password}),
        })
    }

    //register
    static async register(email: string, password: string): Promise<auth> {
        return apiFetch(API_ROUTES.AUTH.REGISTER, {
            method: 'POST',
            body: JSON.stringify({email, password}),
        })
    }
}