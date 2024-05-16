import React from 'react'
import { useEffect } from 'react'
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function ProtectedRoute({children}) {
 
  const navigate = useNavigate();
  const {isCustomerAuth} = useAuth()
    useEffect(()=>{
        if(!isCustomerAuth()){
            navigate("/");
        }
  },[])

  return isCustomerAuth() ? children : "";
}
