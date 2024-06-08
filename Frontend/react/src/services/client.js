/* eslint-disable no-useless-catch */
import axios from "axios";

const getAuthConfig= () =>({
    headers: {
        Authorization: `Bearer ${localStorage.getItem('access_token')}`
    }
})

export const getCustomers = async () =>{
    try
    {
        return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`
            ,getAuthConfig()
        )
    }catch(e){
        throw e
    }
}

export const saveCustomer = async(customer)=>{
    // eslint-disable-next-line no-useless-catch
    try{
        return await axios
        .post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`,
            customer
        )

    }catch(e){
        throw e
    }
}

export const perfomrLogin = async(usernameAndPassword)=>{
    try{
        return await axios
        .post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/auth/login`,
            usernameAndPassword
        )
    }catch(e){
        throw e
    }
}

export const deleteCustomer = async(id)=>{
    try{
        return await axios
        .delete(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${id}`
        ,getAuthConfig()
        )
    }catch(err){
        throw err
    }
}

export const UpdateCustomer = async(customer,id)=>{
    try{
        return await axios
        .put(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${id}`,
            customer,
            getAuthConfig()
        )

    }catch(e){
        throw e
    }
}

export const uploadCustomerProfile = async(id,formData)=>{
    try{
        return axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${id}/profile-image`,
            formData,{
                ...getAuthConfig(),
                'Content-Type': 'multipart/form-data'
            }
        )
            
    }catch(e){
        throw e
    }
}

export const getProfilePicture = (id)=>{
    return `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${id}/profile-image`
}

