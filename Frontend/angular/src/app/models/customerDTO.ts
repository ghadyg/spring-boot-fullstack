export interface CustomerDTO{
    id?: number;
    name?: string;
    email?: string;
    username?: string;
    password?:string;
    gender?: 'male'|'female';
    age?:number;
    roles?: string[];
}