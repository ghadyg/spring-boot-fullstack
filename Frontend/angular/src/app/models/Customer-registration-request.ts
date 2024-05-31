export interface CustomerRegistrationRequest{
    id?:number;
    name?: string;
    email? : string;
    age?:number;
    gender?: "male" | "female";
    password?:string;
}