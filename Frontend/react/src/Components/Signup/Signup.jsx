import React,{ useEffect }  from 'react'
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import {
    Button,
    Checkbox,
    Flex,
    FormControl,
    FormLabel,
    Heading,
    Input,
    Link,
    Stack,
    Image,
    Text,
    Box,
    Alert,
    AlertIcon
  } from '@chakra-ui/react';
import CreateCustomerForm from '../customer/CreateCustomerForm';


export default function Signup() {
    const {customer,setCustomerFromToken} = useAuth();
    const navigate = useNavigate();
    useEffect(()=>{
      if(customer){
        navigate("/dashboard");
      }
    })
    return (
      <Stack minH={'100vh'} direction={{ base: 'column', md: 'row' }}>
        <Flex p={8} flex={1} align={'center'} justify={'center'}>
          <Stack spacing={4} w={'full'} maxW={'md'}>
            <Heading fontSize={'2xl'} mb={15}>Register for an account</Heading>
            <CreateCustomerForm onSuccess={(token)=>{
                localStorage.setItem("access_token",token);
                setCustomerFromToken();
                navigate("/dashboard/customer");
                }}/>
            <Link color={"blue.500"} href='/'>Have an account? Login now.</Link>
          </Stack>
        </Flex>
        <Flex flex={1} p={10} flexDirection={"column"} alignItems={"center"} justifyContent={"center"} bgGradient={{sm:"linear(to-r,blue.600, purple.600)"}}>
          <Stack margin={"auto"}>
          <Text fontSize={"5xl"} color={'white'} fontWeight={"bold"} mb={5} m={"auto"}>
              <Link href='https://github.com/ghadyg'>
                Checkout my github
              </Link>
          </Text>
          <Image
            alt={'Login Image'}
            objectFit={'cover'}
            src={
              'https://images.unsplash.com/photo-1486312338219-ce68d2c6f44d?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1352&q=80'
            }
          />
          </Stack>
        </Flex>
      </Stack>
    );
}
