import React, { useEffect, useState } from 'react'
import { getPageOfCustomer } from './services/client';
import axios from 'axios';

export default function useCustomer({pageSize,offset}) {
    
    const [customers,setCustomer] = useState([]);
    const [isLoading,setIsLoading] = useState(false);
    const [hasMore,setHasMore] = useState(true);
    const [error,seterror] = useState("");

    

    useEffect(()=>{
        if(offset===0) setCustomer([]);
        setIsLoading(true);
        const cancelToken = axios.CancelToken.source();
        getPageOfCustomer(pageSize,offset,cancelToken).then((res)=>{
            setCustomer((prev)=> [...prev,...res.data]);
            setIsLoading(false);
            Object.keys(res.data).length === 0? setHasMore(false) :setHasMore(true) 
        }).catch((err)=>{
            seterror(error);
        });
        
        return () => {
            cancelToken.cancel();
          };

    },[offset])

    return {customers,isLoading,hasMore,error}
}
