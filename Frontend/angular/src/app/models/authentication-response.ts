import { CustomerDTO } from "./customerDTO";

export interface AuthenticationResponse{
    token?:string;
    customerDTO?:CustomerDTO;
}