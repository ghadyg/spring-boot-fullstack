import React from 'react'
import ReactDOM from 'react-dom/client'
import Customer from './Customer.jsx'
import './index.css'
import { ChakraProvider } from '@chakra-ui/react'
import { createStandaloneToast } from '@chakra-ui/react'
import { createBrowserRouter,RouterProvider } from 'react-router-dom'
import Login from './Components/Login/Login.jsx'
import AuthProvider from './Components/context/AuthContext.jsx'
import ProtectedRoute from './Components/shared/ProtectedRoute.jsx'
import Signup from './Components/Signup/Signup.jsx'
import Home from './Home.jsx'



const { ToastContainer } = createStandaloneToast()

const router = createBrowserRouter([
  {
    path: "/",
    element: <Login/>
  },
  {
    path: "dashboard/customer",
    element: <ProtectedRoute><Customer /></ProtectedRoute> 
  },
  {
    path: "dashboard",
    element: <ProtectedRoute><Home /></ProtectedRoute>  
  },
  {
    path: "signup",
    element: <Signup></Signup>
  }
])

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <ChakraProvider>
        <AuthProvider>
        <RouterProvider router={router}/>
        </AuthProvider> 
      <ToastContainer />
    </ChakraProvider>
  </React.StrictMode>,
)
