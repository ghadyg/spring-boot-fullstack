import {
    createContext,
    useContext,
    useEffect,
    useState
} from 'react'
import {perfomrLogin} from '../../services/client.js'
import { jwtDecode } from 'jwt-decode';

const AuthContext = createContext({});

const AuthProvider =({children})=>{
    const [customer,setCustomer] = useState(null)
    const setCustomerFromToken = ()=>{
        let token = localStorage.getItem('access_token');
        if(token)
            {
                token = jwtDecode(token);
                setCustomer({
                    username: token.sub,
                    roles: token.scopes
                })
            }
    }
    useEffect(()=>{
        setCustomerFromToken();
    },[])

    const login = async (usernameAndPassword)=>{
       return new Promise((resolve,reject)=>{
            perfomrLogin(usernameAndPassword).then(res=>{
                const jwtToken = res.headers["authorization"];
                localStorage.setItem('access_token',jwtToken);
                const decodedToken = jwtDecode(jwtToken);
                setCustomer({
                    username: decodedToken.sub,
                    roles: decodedToken.scopes
                })
                resolve(res);
            }).catch(err=>reject(err))
        })
    }
    const logout = async()=>{
        localStorage.removeItem('access_token');
        setCustomer(null);
    }

    const isCustomerAuth = () =>{
        const token = localStorage.getItem("access_token");
        if(!token)
        {
            return false
        }
        const decodedToken = jwtDecode(token);
        if(Date.now() > decodedToken.exp*1000){
            logout();
            return false;
        }
        return true;
    }

    return (
        <AuthContext.Provider value={{
            customer,
            login,
            logout,
            isCustomerAuth,
            setCustomerFromToken
        }}>
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => useContext(AuthContext);

export default AuthProvider;


